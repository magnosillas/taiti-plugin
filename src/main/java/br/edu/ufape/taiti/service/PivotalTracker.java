package br.edu.ufape.taiti.service;

import br.edu.ufape.taiti.exceptions.HttpException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;



import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.io.FileOutputStream;


/**
 * Classe responsável por fazer todas as requisições ao Pivotal Tracker. Para fazer as requisições é utilizado a
 * biblioteca UniRest (<a href="http://kong.github.io/unirest-java/">...</a>).
 */
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



    public void saveScenarios(File scenarios, String taskID) throws HttpException, IOException, InterruptedException {
        JSONArray comments = null;
        try {
            comments = getComments(taskID);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        JSONObject taitiComment = getTaitiComment(comments);
        if (taitiComment != null) {
            deleteComment(getID(taitiComment), taskID);
        }

        postCommentWithFile(scenarios, taskID);

    }
    public int checkStatus()  {
        if(projectID.isBlank()){
            return 400;
        }
        String request = PIVOTAL_URL + API_PATH + "/projects/" + projectID;
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(request))
                .header(TOKEN_HEADER, token)
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return 400;
        }

        String resposta = response.body();

        return response.statusCode();
    }
    public File downloadFiles(String storyId) throws HttpException, IOException, InterruptedException {
        File tempTaitiDirectory = new File(getProjectPath() + File.separator + "temp_taiti");

        if (!(tempTaitiDirectory.exists() && tempTaitiDirectory.isDirectory())) {
            tempTaitiDirectory.mkdir();
        }

        JSONArray taitiFiles = getTaitiFiles();

        for (Object obj :  taitiFiles) {
            if (obj instanceof JSONObject) {
                JSONObject taitiFile = (JSONObject) obj;
                int fileStoryID = taitiFile.getInt("story_id");

                if (fileStoryID == Integer.parseInt(storyId)) {
                    String downloadUrl = PIVOTAL_URL + taitiFile.getString("download_url");

                    HttpClient httpClient = HttpClient.newHttpClient();
                    HttpRequest downloadRequest = HttpRequest.newBuilder()
                            .uri(URI.create(downloadUrl))
                            .header(TOKEN_HEADER, token)
                            .GET()
                            .build();

                    HttpResponse<byte[]> response = httpClient.send(downloadRequest, HttpResponse.BodyHandlers.ofByteArray());

                    int statusCode = response.statusCode();

                    if (statusCode >= 300 && statusCode < 400) {
                        String redirectUrl = response.headers().firstValue("Location").orElse(null);

                        if (redirectUrl != null) {
                            HttpRequest redirectRequest = HttpRequest.newBuilder()
                                    .uri(URI.create(redirectUrl))
                                    .header(TOKEN_HEADER, token)
                                    .GET()
                                    .build();

                            HttpResponse<byte[]> redirectResponse = httpClient.send(redirectRequest, HttpResponse.BodyHandlers.ofByteArray());
                            byte[] fileBytes = redirectResponse.body();
                            // Restante do seu código para salvar o arquivo
                            File downloadedFile = new File(getProjectPath() + File.separator + "temp_taiti" + File.separator + "file-" + fileStoryID + ".csv");

                            FileOutputStream fileOutputStream = new FileOutputStream(downloadedFile);
                            fileOutputStream.write(fileBytes);
                            fileOutputStream.close();

                            return downloadedFile;
                        }
                    } else {
                        byte[] fileBytes = response.body();
                        // Restante do seu código para salvar o arquivo
                        File downloadedFile = new File(getProjectPath() + File.separator + "temp_taiti" + File.separator + "file-" + fileStoryID + ".csv");

                        FileOutputStream fileOutputStream = new FileOutputStream(downloadedFile);
                        fileOutputStream.write(fileBytes);
                        fileOutputStream.close();

                        return downloadedFile;
                    }



                }
            }
        }

        return null;
    }

//curl -X GET -H "X-TrackerToken: ce2a6540e0be3574c871f403fb12ef0f" "https://www.pivotaltracker.com/file_attachments/118790662/download" -o "myfile.csv"

    public JSONArray getTaitiFiles() throws HttpException, IOException, InterruptedException {
        JSONArray plannedStories = null;

        plannedStories = getPlannedStories();

        JSONArray taitiFiles = new JSONArray();

        for (Object obj : plannedStories) {
            if (obj instanceof JSONObject) {
                JSONObject plannedStory = (JSONObject) obj;
                JSONObject taitiComment = null;

                taitiComment = getTaitiComment(getComments(getID(plannedStory)));


                if (taitiComment != null) {
                    JSONArray files = null;

                    files = getFiles(getID(plannedStory));

                    for (Object o : files) {
                        if (o instanceof JSONObject) {
                            JSONObject fileInfo = (JSONObject) o;

                            // esse getID do fileInfo pega o ID do comentário a qual o arquivo está associado
                            if (getID(fileInfo).equals(getID(taitiComment))) {
                                JSONArray fileAttachments =  fileInfo.getJSONArray("file_attachments");
                                JSONObject taitiFile =  fileAttachments.getJSONObject(0);
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

//    private JSONArray getFiles(String taskID) throws HttpException {
//        String request = "/projects/" + projectID + "/stories/" + taskID + "/comments/?fields=file_attachments";
//
//        HttpResponse<JsonNode> response = Unirest.get(PIVOTAL_URL + API_PATH + request)
//                .header(TOKEN_HEADER, token)
//                .asJson();
//
//        if (!response.isSuccess()) {
//            throw new HttpException(response.getStatusText(), response.getStatus());
//        }
//
//        return response.getBody().getArray();
//    }

    public JSONArray getFiles(String taskID) throws HttpException, IOException, InterruptedException {
        String request = "/projects/" + projectID + "/stories/" + taskID + "/comments/?fields=file_attachments";

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(PIVOTAL_URL + API_PATH + request))
                .header(TOKEN_HEADER, token)
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new HttpException(response.body(), response.statusCode());
        }

        return new JSONArray(response.body());
    }


    public JSONObject getTaitiComment(JSONArray comments)  {
        JSONObject taitiComment = null;

        for (Object obj : comments) {
            if (obj instanceof JSONObject) {
                JSONObject comment = (JSONObject) obj;
                if (!comment.isNull("text") && comment.getString("text").equals(TAITI_MSG)) {
                    taitiComment = comment;
                }
            }
        }

        return taitiComment;
    }

//    public JSONArray getComments(String taskID) throws HttpException {
//        String request = "/projects/" + projectID + "/stories/" + taskID + "/comments";
//
//        HttpResponse<JsonNode> response = Unirest.get(PIVOTAL_URL + API_PATH + request)
//                .header(TOKEN_HEADER, token)
//                .asJson();
//
//        if (!response.isSuccess()) {
//            throw new HttpException(response.getStatusText(), response.getStatus());
//        }
//
//        return response.getBody().getArray();
//    }

    public JSONArray getComments(String taskID) throws HttpException, IOException, InterruptedException {
        String request = "/projects/" + projectID + "/stories/" + taskID + "/comments";

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(PIVOTAL_URL + API_PATH + request))
                .header(TOKEN_HEADER, token)
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new HttpException(response.body(), response.statusCode());
        }

        return new JSONArray(response.body());
    }

    public int getPersonId() throws IOException, InterruptedException {

        String request = "/me?fields=%3Adefault";
        String pivotalUrl = PIVOTAL_URL + API_PATH + request;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(pivotalUrl))
                .header(TOKEN_HEADER, token)
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // O request foi bem-sucedido, você pode processar o JSON da resposta aqui
            JSONObject obj = new JSONObject(response.body());
            return obj.getInt("id");
        } else {
            // Caso haja um erro na chamada, você pode tratar aqui de acordo com o status code retornado
            System.out.println("Erro na chamada da API: " + response.statusCode());
            return -1; // Ou algum valor de erro específico, dependendo do contexto do seu projeto
        }
    }


    public List<Person> getMembers() throws HttpException, IOException, InterruptedException {
        String request = "/projects/" + projectID + "/memberships";

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(PIVOTAL_URL + API_PATH + request))
                .header(TOKEN_HEADER, token)
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new HttpException(response.body(), response.statusCode());
        }

        JSONArray memberships = new JSONArray(response.body());
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < memberships.length(); i++) {
            JSONObject membership = memberships.getJSONObject(i);
            JSONObject person = membership.getJSONObject("person");
            int id = person.getInt("id");
            String name = person.getString("name");
            people.add(new Person(id, name));
        }

        return people;
    }



    public JSONArray getPlannedStories() throws HttpException, IOException, InterruptedException {
        JSONArray stories = new JSONArray();

        HttpClient httpClient = HttpClient.newHttpClient();

        // Primeira chamada com with_state=started
        String request = "/projects/" + projectID + "/stories?with_state=started";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(PIVOTAL_URL + API_PATH + request))
                .header(TOKEN_HEADER, token)
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new HttpException(response.body(), response.statusCode());
        }

        JSONArray jsonArray = new JSONArray(response.body());
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject) {
                JSONObject story = (JSONObject) obj;
                if (!story.isNull("estimate") && !story.isNull("owner_ids")) {
                    JSONArray ownerID = story.getJSONArray("owner_ids");
                    if (ownerID.length() > 0) {
                        stories.put(obj);
                    }
                }
            }
        }

        // Segunda chamada com with_state=unstarted
        request = "/projects/" + projectID + "/stories?with_state=unstarted";
        httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(PIVOTAL_URL + API_PATH + request))
                .header(TOKEN_HEADER, token)
                .GET()
                .build();

        response = httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new HttpException(response.body(), response.statusCode());
        }

        jsonArray = new JSONArray(response.body());
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject) {
                JSONObject story = (JSONObject) obj;
                if (!story.isNull("estimate") && !story.isNull("owner_ids")) {
                    JSONArray ownerID = story.getJSONArray("owner_ids");
                    if (ownerID.length() > 0) {
                        stories.put(obj);
                    }
                }
            }
        }

        return stories;
    }

//    private void postCommentWithFile(File file, String taskID) throws HttpException {
//        // add file
//        String requestFile = "/projects/" + projectID + "/uploads";
//
//        HttpResponse<JsonNode> responseFile = Unirest.post(PIVOTAL_URL + API_PATH + requestFile)
//                .header(TOKEN_HEADER, token)
//                .field("file", file)
//                .asJson();
//
//        if (!responseFile.isSuccess()) {
//            throw new HttpException(responseFile.getStatusText(), responseFile.getStatus());
//        }
//
//        // add comment
//        String requestComment = "/projects/" + projectID + "/stories/" + taskID + "/comments?fields=%3Adefault%2Cfile_attachment_ids";
//
//        JSONObject json = new JSONObject();
//        json.put("file_attachments", List.of(responseFile.getBody().getObject()));
//        json.put("text", TAITI_MSG);
//
//        HttpResponse<JsonNode> responseComment = Unirest.post(PIVOTAL_URL + API_PATH + requestComment)
//                .header(TOKEN_HEADER, token)
//                .header("Content-Type", "application/json")
//                .body(json)
//                .asJson();
//
//        if (!responseComment.isSuccess()) {
//            throw new HttpException(responseComment.getStatusText(), responseComment.getStatus());
//        }
//    }

//        export TOKEN='your Pivotal Tracker API token'
//
//        export FILE_PATH='/home/vader/art-projects/new-imperial-logo-6.jpg'
//
//        export PROJECT_ID=99

//curl -X POST -H "X-TrackerToken: ce2a6540e0be3574c871f403fb12ef0f" -F file=@"/media/HDD/Faculdade/PROJETO TAITIR BIA/diaspora/scenarios.csv" "https://www.pivotaltracker.com/services/v5/projects/2590203/uploads"
//curl -X POST -H "X-TrackerToken: ce2a6540e0be3574c871f403fb12ef0f" -H "Content-Type: application/json" -d '{"file_attachments":[{"kind":"file_attachment","id":120989160,"filename":"scenarios.csv","created_at":"2023-08-17T14:55:16Z","uploader_id":3457781,"thumbnailable":false,"size":54,"download_url":"/file_attachments/120989160/download","content_type":"text/csv","uploaded":false,"big_url":"#","thumbnail_url":"#"}] , "text":"[TAITI] Scenarios"}' "https://www.pivotaltracker.com/services/v5/projects/2590203/stories/185842559/comments?fields=%3Adefault%2Cfile_attachment_ids"


    public void postCommentWithFile(File file, String taskID) throws IOException {
        String requestFil = "/projects/" + projectID + "/uploads";
        String requestComment = "/projects/" + projectID + "/stories/" + taskID + "/comments?fields=%3Adefault%2Cfile_attachment_ids";

        OkHttpClient client = new OkHttpClient();

        try {
            // Upload file
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));

            Request requestFile = new Request.Builder()
                    .url(PIVOTAL_URL + API_PATH + requestFil)
                    .addHeader(TOKEN_HEADER, token)
                    .post(requestBodyBuilder.build())
                    .build();

            Response responseFile = client.newCall(requestFile).execute();

            try {
                if (!responseFile.isSuccessful()) {
                    throw new IOException("Error uploading file: " + responseFile.message());
                }

                JSONObject responseFileBody = new JSONObject(responseFile.body().string());

                // Add comment
                JSONObject json = new JSONObject();
                json.put("file_attachments", new JSONObject[]{responseFileBody});
                json.put("text", TAITI_MSG);

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody commentRequestBody = RequestBody.create(mediaType, json.toString());

                Request requestCommentPost = new Request.Builder()
                        .url(PIVOTAL_URL + API_PATH + requestComment)
                        .addHeader(TOKEN_HEADER, token)
                        .addHeader("Content-Type", "application/json")
                        .post(commentRequestBody)
                        .build();

                Response responseComment = client.newCall(requestCommentPost).execute();

                try {
                    if (!responseComment.isSuccessful()) {
                        throw new IOException("Error adding comment: " + responseComment.message());
                    }

                    // Process responseComment if needed

                } finally {
                    // Certifique-se de fechar a resposta após usá-la
                    if (responseComment.body() != null) {
                        responseComment.body().close();
                    }
                }

            } finally {
                // Certifique-se de fechar a resposta após usá-la
                if (responseFile.body() != null) {
                    responseFile.body().close();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void deleteComment(String commentID, String taskID) throws HttpException, IOException, InterruptedException {
        String request = "/projects/" + projectID + "/stories/" + taskID + "/comments/" + commentID;
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(PIVOTAL_URL + API_PATH + request))
                .header(TOKEN_HEADER, token)
                .DELETE()
                .build();

        java.net.http.HttpResponse<String> response = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >  210) {
            throw new HttpException(response.body(), response.statusCode());
        }
    }


    private String getID(JSONObject json)  {
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
