package br.edu.ufape.taiti.service;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.gui.taskbar.Task;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Stories {
    private List<Task> unstartedStories;
    private List<Task> startedStories;
    private JSONArray plannedStories;
    private PivotalTracker pivotalTracker;
    private int ownerID;

    public Stories(PivotalTracker pivotalTracker){

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
            plannedStories = pivotalTracker.getPlannedStories();

            for(int i = 0; i < plannedStories.length(); i++){
                JSONObject obj = plannedStories.getJSONObject(i);
                Task plannedStory = new Task(obj);
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

    public int getOwnerID() {
        return ownerID;
    }
}
