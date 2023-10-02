package br.edu.ufape.taiti.service;


import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.tool.TaitiTool;
import br.ufpe.cin.tan.conflict.PlannedTask;
import com.intellij.openapi.project.Project;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONObject;

public class Task {

    private final String name;
    private final int id;
    private final String type;
    private final String priority;
    private final String state;
    private  final int ownerID;
    private final String url;
    private String personName;
    private ArrayList<LinkedHashMap<String, Serializable>> scenarios = new ArrayList<>();

    private PlannedTask iTesk ;



    private ArrayList<Task> conflictTasks = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, Serializable>> conflictScenarios = new ArrayList<>();
    private double conflictRate;



    // curl -X GET -H "X-TrackerToken: ce2a6540e0be3574c871f403fb12ef0f" "https://www.pivotaltracker.com/services/v5/projects/2590203/memberships"

    public Task(JSONObject task, PivotalTracker pivotalTracker, Project project) throws IOException, InterruptedException {
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
        } catch (HttpException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        setName(members);
        setScenarios(pivotalTracker, project);
    }

    public void setScenarios(PivotalTracker pivotalTracker, Project project) throws IOException, InterruptedException {
        try {
            File files = pivotalTracker.downloadFiles(String.valueOf(id));
            if (files != null) {
                TaitiTool taiti = new TaitiTool(project);
                List<String[]> arquivo = taiti.readTaitiFile(files);
                for (String[] linha : arquivo) {
                    String absolutePath =  linha[0];
                    String[] numbersString = linha[1].replaceAll("[\\[\\]]", "").split(", ");

                    ArrayList<Integer> numbersInt = new ArrayList<>();
                    for (String s : numbersString) {
                        numbersInt.add(Integer.parseInt(s));
                    }
                    LinkedHashMap<String, Serializable> map = new LinkedHashMap<String, Serializable>(2);

                    map.put("path", absolutePath);
                    map.put("lines", numbersInt);
                    scenarios.add(map);

                }
                files.delete();


            }

        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkConflictRisk(List<Task> listTask) {
        conflictTasks.clear();
        conflictRate = 0;

        for (LinkedHashMap<String, Serializable> lines : scenarios) { // percorro scenario por scenario
            String absolutePath = (String)lines.get("path");
            ArrayList<Integer> numbers = (ArrayList<Integer>) lines.get("lines");

            for (Task auxTask : listTask) {
                LinkedHashMap<String, Serializable> conflictScenariosAux = new LinkedHashMap<String, Serializable>(2);
                ArrayList<LinkedHashMap<String, Serializable>> auxScenarios = auxTask.getScenarios(); //pego os scenarios da task que vou verificar
                for (LinkedHashMap<String, Serializable> auxLines : auxScenarios) { // percoso scenario por scenario
                    String auxAbsolutePath = (String) auxLines.get("path");

                    ArrayList<Integer> auxNumbers = (ArrayList<Integer>) auxLines.get("lines");

                    if(absolutePath.equals(auxAbsolutePath)){
                        ArrayList<Integer> conflictRows = new ArrayList<>();


                        for (int num: numbers) {
                            for (int auxNum:auxNumbers) {
                                if(num == auxNum){
                                    conflictRows.add(num);
                                    conflictRate++;

                                }
                            }

                        }
                        if(!conflictRows.isEmpty()){
                            conflictScenariosAux.put("path", absolutePath);
                            conflictScenariosAux.put("lines", conflictRows);

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

    public ArrayList<Task> getConflictTasks() {
        return conflictTasks;
    }

    public ArrayList<LinkedHashMap<String, Serializable>>  getConflictScenarios() {
        return conflictScenarios;
    }

    public double getConflictRate() {
        return conflictRate;
    }

    public ArrayList<LinkedHashMap<String, Serializable>> getScenarios() {
        return scenarios;
    }

    public String getPersonName() {
        return personName;
    }

    public void setConflictRate(double conflictRate) {
        this.conflictRate = conflictRate;
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

    public PlannedTask getiTesk() {
        return iTesk;
    }

    public void setiTesk(PlannedTask iTesk) {
        this.iTesk = iTesk;
    }
}


