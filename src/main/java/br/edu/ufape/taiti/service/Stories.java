package br.edu.ufape.taiti.service;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.ufpe.cin.tan.analysis.task.TodoTask;
import br.ufpe.cin.tan.conflict.PlannedTask;
import br.ufpe.cin.tan.exception.CloningRepositoryException;
import com.intellij.openapi.project.Project;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Stories {
    private final List<Task> unstartedStories;
    private final List<Task> startedStories;
    private final PivotalTracker pivotalTracker;
    private final int ownerID;
    private Project project;
    private String githubURL;
    public Stories(PivotalTracker pivotalTracker, Project project, String githubURL){
        this.githubURL = githubURL;
        this.project = project;
        this.pivotalTracker = pivotalTracker;
        unstartedStories = new ArrayList<>();
        startedStories = new ArrayList<>();
        try {
            ownerID = pivotalTracker.getPersonId();
        } catch (IOException | InterruptedException e) {
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
                int taskId = obj.getInt("id");

                JSONObject taitiComment = pivotalTracker.getTaitiComment(pivotalTracker.getComments(String.valueOf(taskId)));
                //Seleciono apenas as tasks que contem o arquivo [TAITI] Scenarios, ou seja, que já foram adicionados
                if ( (taitiComment != null && taitiComment.getString("text").equals("[TAITI] Scenarios"))) {
                    Task plannedStory = new Task(obj, pivotalTracker, project);

                    String versaoJava = System.getProperty("java.version");
                    System.out.println("Versão do Java: " + versaoJava);
//
//                    String url = "https://github.com/diaspora/diaspora";
//                    final Integer id1 = 1;
////
//                    LinkedHashMap<String, Serializable> map = new LinkedHashMap<String, Serializable>(2);
//                    map.put("path", "features/desktop/help.feature");
//                    map.put("lines", new ArrayList<Integer>(Arrays.asList(4)));
//                    ArrayList<LinkedHashMap<String, Serializable>> tests = new ArrayList<LinkedHashMap<String, Serializable>>(Arrays.asList(map));
////
//
//                    TodoTask task1;
//                    PlannedTask plannedTask1 = null;
////
//                    task1 = new TodoTask(url, id1, tests);
//
////
                    ArrayList<LinkedHashMap<String, Serializable>> testss = plannedStory.getScenarios();
                    int idTeste = plannedStory.getId();
                    TodoTask todoTask = new TodoTask( githubURL, idTeste , testss);
//
//
                    PlannedTask plannedTask = todoTask.generateTaskForConflictAnalysis();
                    plannedStory.setiTesk(plannedTask);


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

        } catch (HttpException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        catch (CloningRepositoryException e) {
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
