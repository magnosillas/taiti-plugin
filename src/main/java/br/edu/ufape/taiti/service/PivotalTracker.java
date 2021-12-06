package br.edu.ufape.taiti.service;

import br.edu.ufape.taiti.exceptions.HttpException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PivotalTracker {
    private static final String PIVOTAL_URL = "https://www.pivotaltracker.com";
    private static final String PROJECT_PATH = "/n/projects/";
    private static final String API_PATH = "/services/v5";
    private static final String TOKEN_HEADER = "X-TrackerToken";
    private static final String TAITI_MSG = "[TAITI] Scenarios";

    private final String token;
    private final String projectID;
    private final Project project;

    public PivotalTracker(String token, String pivotalProjectURL, Project project) {
        this.token = token;
        this.projectID = getProjectIDFromProjectURL(pivotalProjectURL);
        this.project = project;
    }

    public void saveScenarios(File scenarios, String taskID) throws HttpException {
        JSONArray comments = getComments(taskID);
        JSONObject taitiComment = getTaitiComment(comments);
        if (taitiComment != null) {
            deleteComment(getID(taitiComment), taskID);
        }
        postCommentWithFile(scenarios, taskID);
    }

    public ArrayList<File> downloadFiles() throws HttpException {
        File tempTaitiDirectory = new File(getProjectPath() + File.separator + "temp_taiti");

        if (!(tempTaitiDirectory.exists() && tempTaitiDirectory.isDirectory())) {
            tempTaitiDirectory.mkdir();
        }

        ArrayList<File> files = new ArrayList<>();

        for (Object obj : getTaitiFiles()) {
            if (obj instanceof JSONObject) {
                JSONObject taitiFile = (JSONObject) obj;

                File result = Unirest.get(PIVOTAL_URL + taitiFile.get("download_url"))
                        .header(TOKEN_HEADER, token)
                        .asFile(getProjectPath() + File.separator + "temp_taiti" + File.separator + "file-" + taitiFile.get("story_id") + ".csv")
                        .getBody();

                files.add(result);
            }
        }

        return files;
    }

    private JSONArray getTaitiFiles() throws HttpException {
        JSONArray plannedStories = getPlannedStories();
        JSONArray taitiFiles = new JSONArray();

        for (Object obj : plannedStories) {
            if (obj instanceof JSONObject) {
                JSONObject plannedStory = (JSONObject) obj;
                JSONObject taitiComment = getTaitiComment(getComments(getID(plannedStory)));

                if (taitiComment != null) {
                    JSONArray files = getFiles(getID(plannedStory));
                    for (Object o : files) {
                        if (o instanceof JSONObject) {
                            JSONObject fileInfo = (JSONObject) o;

                            // esse getID do fileInfo pega o ID do comentário a qual o arquivo está associado
                            if (getID(fileInfo).equals(getID(taitiComment))) {
                                JSONArray fileAttachments = (JSONArray) fileInfo.get("file_attachments");
                                JSONObject taitiFile = fileAttachments.getJSONObject(0);
                                taitiFile.put("story_id", plannedStory.get("id"));

                                taitiFiles.put(taitiFile);
                            }
                        }
                    }
                }
            }
        }

        return taitiFiles;
    }

    private JSONArray getFiles(String taskID) throws HttpException {
        String request = "/projects/" + projectID + "/stories/" + taskID + "/comments/?fields=file_attachments";

        HttpResponse<JsonNode> response = Unirest.get(PIVOTAL_URL + API_PATH + request)
                .header(TOKEN_HEADER, token)
                .asJson();

        if (!response.isSuccess()) {
            throw new HttpException(response.getStatusText(), response.getStatus());
        }

        return response.getBody().getArray();
    }

    private JSONArray getPlannedStories() throws HttpException {
        JSONArray stories = new JSONArray();

        String request = "/projects/" + projectID + "/stories?with_state=started";
        HttpResponse<JsonNode> response = Unirest.get(PIVOTAL_URL + API_PATH + request)
                .header(TOKEN_HEADER, token)
                .asJson();

        if (!response.isSuccess()) {
            throw new HttpException(response.getStatusText(), response.getStatus());
        }
        for (Object obj : response.getBody().getArray()) {
            if (obj instanceof JSONObject) {
                JSONObject story = (JSONObject) obj;
                if (!story.isNull("estimate") && !story.isNull("owner_ids")) {
                    JSONArray ownerID = (JSONArray) story.get("owner_ids");
                    if (ownerID.length() > 0) {
                        stories.put(obj);
                    }
                }
            }
        }

        request = "/projects/" + projectID + "/stories?with_state=unstarted";
        response = Unirest.get(PIVOTAL_URL + API_PATH + request)
                .header(TOKEN_HEADER, token)
                .asJson();

        if (!response.isSuccess()) {
            throw new HttpException(response.getStatusText(), response.getStatus());
        }
        for (Object obj : response.getBody().getArray()) {
            if (obj instanceof JSONObject) {
                JSONObject story = (JSONObject) obj;
                if (!story.isNull("estimate") && !story.isNull("owner_ids")) {
                    JSONArray ownerID = (JSONArray) story.get("owner_ids");
                    if (ownerID.length() > 0) {
                        stories.put(obj);
                    }
                }
            }
        }

        return stories;
    }

    private void postCommentWithFile(File file, String taskID) throws HttpException {
        // add file
        String requestFile = "/projects/" + projectID + "/uploads";

        HttpResponse<JsonNode> responseFile = Unirest.post(PIVOTAL_URL + API_PATH + requestFile)
                .header(TOKEN_HEADER, token)
                .field("file", file)
                .asJson();

        if (!responseFile.isSuccess()) {
            throw new HttpException(responseFile.getStatusText(), responseFile.getStatus());
        }

        // add comment
        String requestComment = "/projects/" + projectID + "/stories/" + taskID + "/comments?fields=%3Adefault%2Cfile_attachment_ids";

        JSONObject json = new JSONObject();
        json.put("file_attachments", List.of(responseFile.getBody().getObject()));
        json.put("text", TAITI_MSG);

        HttpResponse<JsonNode> responseComment = Unirest.post(PIVOTAL_URL + API_PATH + requestComment)
                .header(TOKEN_HEADER, token)
                .header("Content-Type", "application/json")
                .body(json)
                .asJson();

        if (!responseComment.isSuccess()) {
            throw new HttpException(responseComment.getStatusText(), responseComment.getStatus());
        }
    }

    private void deleteComment(String commentID,  String taskID) throws HttpException {
        String request = "/projects/" + projectID + "/stories/" + taskID + "/comments/" + commentID;

        HttpResponse<JsonNode> response = Unirest.delete(PIVOTAL_URL + API_PATH + request)
                .header(TOKEN_HEADER, token)
                .header("Content-Type", "application/json")
                .asJson();

        if (!response.isSuccess()) {
            throw new HttpException(response.getStatusText(), response.getStatus());
        }
    }

    private JSONObject getTaitiComment(JSONArray comments) {
        JSONObject taitiComment = null;

        for (Object obj : comments) {
            if (obj instanceof JSONObject) {
                JSONObject comment = (JSONObject) obj;
                if (!comment.isNull("text") && comment.get("text").equals(TAITI_MSG)) {
                    taitiComment = comment;
                }
            }
        }

        return taitiComment;
    }

    private JSONArray getComments(String taskID) throws HttpException {
        String request = "/projects/" + projectID + "/stories/" + taskID + "/comments";

        HttpResponse<JsonNode> response = Unirest.get(PIVOTAL_URL + API_PATH + request)
                .header(TOKEN_HEADER, token)
                .asJson();

        if (!response.isSuccess()) {
            throw new HttpException(response.getStatusText(), response.getStatus());
        }

        return response.getBody().getArray();
    }

    private String getID(JSONObject json) {
        return String.valueOf(json.get("id"));
    }

    private String getProjectIDFromProjectURL(String pivotalProjectURL) {
        return pivotalProjectURL.replace(PIVOTAL_URL + PROJECT_PATH, "");
    }

    private String getProjectPath() {
        String projectPath = "";

        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir != null) {
            projectPath = projectDir.getPath();
        }

        return projectPath;
    }
}
