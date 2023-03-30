package br.edu.ufape.taiti.service;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.project.Project;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Stories {
    private final List<Task> unstartedStories;
    private final List<Task> startedStories;
    private final PivotalTracker pivotalTracker;
    private final int ownerID;
    private Project project;
    public Stories(PivotalTracker pivotalTracker, Project project){

        this.project = project;
        this.pivotalTracker = pivotalTracker;
        unstartedStories = new ArrayList<>();
        startedStories = new ArrayList<>();
        try {
            ownerID = pivotalTracker.getPersonId();
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }


    }



    public void clearLists(){
        unstartedStories.clear();
        startedStories.clear();
    }

    public void startList(){
        try {
            JSONArray plannedStories = pivotalTracker.getPlannedStories();

            for(int i = 0; i < plannedStories.length(); i++){
                JSONObject obj = plannedStories.getJSONObject(i);
                Task plannedStory = new Task(obj, pivotalTracker, project);
                JSONObject taitiComment = pivotalTracker.getTaitiComment(pivotalTracker.getComments(String.valueOf(plannedStory.getId())));
                //Seleciono apenas as tasks que contem o arquivo [TAITI] Scenarios, ou seja, que já foram adicionados
                if ( (taitiComment != null && taitiComment.getString("text").equals("[TAITI] Scenarios"))) {
                    //Adiciono a uma lista as minhas tasks que ainda não começaram
                    if (plannedStory.getState().equals("unstarted") && plannedStory.getOwnerID() == ownerID) {
                        unstartedStories.add(plannedStory);
                    }
                    //Adiciono a uma lista as tasks que já começaram de outros membros
                    else if (plannedStory.getState().equals("started") && plannedStory.getOwnerID() != ownerID) {
                        startedStories.add(plannedStory);
                    }
                }
            }

        } catch (HttpException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Task> getUnstartedStories() {
        return unstartedStories;
    }

    public List<Task> getStartedStories() {
        return startedStories;
    }


}
