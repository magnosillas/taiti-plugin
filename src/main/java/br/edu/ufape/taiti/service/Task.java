package br.edu.ufape.taiti.service;


import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.project.Project;
import kong.unirest.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<String[]> scenarios = new ArrayList<>();



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
                    String[] arr = new String[] { absolutePath, linha[1] };
                    scenarios.add(arr);
                }
                files.delete();


        }

        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String[]> getScenarios() {
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


