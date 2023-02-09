package br.edu.ufape.taiti.service;


import kong.unirest.json.JSONObject;

public class Task {

    private String name;
    private int id;
    private String type;
    private String priority;
    private String state;
    private int ownerID;
    private String url;


    public Task(JSONObject task) {
        this.name = task.getString("name");
        this.id = task.getInt("id");
        this.type = task.getString("story_type");
        this.priority = task.getString("story_priority");
        this.state = task.getString("current_state");
        this.ownerID = task.getInt("owned_by_id");
        this.url = task.getString("url");
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


