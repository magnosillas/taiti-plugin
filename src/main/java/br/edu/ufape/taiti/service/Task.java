package br.edu.ufape.taiti.service;


import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.project.Project;
import kong.unirest.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private final String name;
    private final int id;
    private final String type;
    private final String priority;
    private final String state;
    private  final int ownerID;
    private final String url;
    private String personName;
    private List<Object[]> scenarios = new ArrayList<>();
    ArrayList<Task> conflictTasks = new ArrayList<>();
    ArrayList<ArrayList<Object[]>> conflictScenarios = new ArrayList<>();
    private int conflictNum;



   // curl -X GET -H "X-TrackerToken: ce2a6540e0be3574c871f403fb12ef0f" "https://www.pivotaltracker.com/services/v5/projects/2590203/memberships"

    public Task(JSONObject task, PivotalTracker pivotalTracker, Project project) {
        this.name = task.getString("name");
        this.id = task.getInt("id");
        this.type = task.getString("story_type");
        this.priority = task.getString("story_priority");
        this.state = task.getString("current_state");
        this.ownerID = task.getInt("owned_by_id");
        this.url = task.getString("url");

        List<Person> members;
        try {
            members = pivotalTracker.getMembers();
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }

        setName(members);
        setScenarios(pivotalTracker, project);
    }

    public void setScenarios(PivotalTracker pivotalTracker, Project project) {
        try {
            File files = pivotalTracker.downloadFiles(String.valueOf(id));
            if (files != null) {
                TaitiTool taiti = new TaitiTool(project);
                List<String[]> arquivo = taiti.readTaitiFile(files);
                for (String[] linha : arquivo) {
                    String absolutePath = project.getBasePath() + "/" + linha[0];
                    String[] numbersString = linha[1].replaceAll("[\\[\\]]", "").split(", ");

                    int[] numbersInt = new int[numbersString.length];
                    for (int i = 0; i < numbersString.length; i++) {
                        numbersInt[i] = Integer.parseInt(numbersString[i]);
                    }
                    Object[] arr = new Object[] {absolutePath, numbersInt};
                    scenarios.add(arr);
                }
                files.delete();


        }

        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkConflictRisk(List<Task> listTask) {

        conflictNum = 0;




        for (Object[] lines : scenarios) { // percorro scenario por scenario
            String absolutePath = (String)lines[0];
            int[] numbers = (int[]) lines[1];

            for (Task auxTask : listTask) {
                ArrayList<Object[]> conflictScenariosAux = new ArrayList<>();
                List<Object[]> auxScenarios = auxTask.getScenarios(); //pego os scenarios da task que vou verificar
                for (Object[] auxLines : auxScenarios) { // percoso scenario por scenario
                    String auxAbsolutePath = (String)auxLines[0];
                    int[] auxNumbers = (int[]) auxLines[1];

                    if(absolutePath.equals(auxAbsolutePath)){
                        ArrayList<Integer> conflictRows = new ArrayList<>();


                        for (int num: numbers) {
                            for (int auxNum:auxNumbers) {
                                if(num == auxNum){
                                    conflictRows.add(num);
                                    conflictNum++;

                                }
                            }

                        }
                        if(!conflictRows.isEmpty()){
                            conflictScenariosAux.add( new Object[] {absolutePath, conflictRows});
                        }
                    }
                }
                if(!conflictScenariosAux.isEmpty()){
                    conflictTasks.add(auxTask);
                    conflictScenarios.add(conflictScenariosAux);
                }
            }

        }
    }

    public List<Object[]> getScenarios() {
        return scenarios;
    }

    public String getPersonName() {
        return personName;
    }

    public String getName() {
        return name;
    }

    public void setName(List<Person> members){
        for (Person person : members){
            if (person.getId() == this.ownerID) this.personName = person.getName();
        }

    }

    public String getStoryName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPriority() {
        return priority;
    }

    public String getState() {
        return state;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getUrl() {
        return url;
    }
}


