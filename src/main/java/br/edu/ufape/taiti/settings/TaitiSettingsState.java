package br.edu.ufape.taiti.settings;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Esta classe é responsável por armazenar os estados das configurações do plugin.
 */
public class TaitiSettingsState {
    protected String pivotalURL = "";
    protected String token = "";
    protected String githubURL = "";
    protected String scenariosFolder = "features";
    protected String stepDefinitionsFolder = "features/step_definitions";
    protected String unityTestFolder = "spec";

    public String getScenariosFolder() {
        return scenariosFolder;
    }

    public String getStepDefinitionsFolder() {
        return stepDefinitionsFolder;
    }

    public String getUnityTestFolder() {
        return unityTestFolder;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public String getPivotalURL() {
        return pivotalURL;
    }

    public String getToken() {
        return token;
    }

    public static TaitiSettingsState getInstance(Project project) {
        return project.getService(TaitiSettingsState.class);
    }

    public void retrieveStoredCredentials(Project project) {
        String keyPivotalURL = "pivotalURL" + getProjectName(project);
        String keyPivotalToken = "pivotalToken" + getProjectName(project);
        String keyGithubURL = "githubURL" + getProjectName(project);

        String keyScenariosFolder = "scenariosFolder" + getProjectName(project);
        String keyStepDefinitionsFolder = "stepDefinitionsFolder" + getProjectName(project);
        String keyUnityTestFolder = "unityTestFolder" + getProjectName(project);

        CredentialAttributes credentialAttributes = createCredentialAttributes(keyPivotalURL);
        Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.pivotalURL = credentials.getPasswordAsString();
        }

        credentialAttributes = createCredentialAttributes(keyPivotalToken);
        credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.token = credentials.getPasswordAsString();
        }

        credentialAttributes = createCredentialAttributes(keyGithubURL);
        credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.githubURL = credentials.getPasswordAsString();
        }

        credentialAttributes = createCredentialAttributes(keyStepDefinitionsFolder);
        credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.stepDefinitionsFolder = credentials.getPasswordAsString();
        }

        credentialAttributes = createCredentialAttributes(keyUnityTestFolder);
        credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.unityTestFolder = credentials.getPasswordAsString();
        }

        credentialAttributes = createCredentialAttributes(keyScenariosFolder);
        credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.scenariosFolder = credentials.getPasswordAsString();
        }
    }

    public void storeCredentials(Project project) {
        String keyPivotalURL = "pivotalURL" + getProjectName(project);
        String keyPivotalToken = "pivotalToken" + getProjectName(project);
        String keyGithubURL = "githubURL" + getProjectName(project);

        String keyScenariosFolder = "scenariosFolder" + getProjectName(project);
        String keyStepDefinitionsFolder = "stepDefinitionsFolder" + getProjectName(project);
        String keyUnityTestFolder = "unityTestFolder" + getProjectName(project);

        CredentialAttributes credentialAttributes = createCredentialAttributes(keyPivotalURL);
        Credentials credentials = new Credentials(keyPivotalURL, this.pivotalURL);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);

        credentialAttributes = createCredentialAttributes(keyPivotalToken);
        credentials = new Credentials(keyPivotalToken, this.token);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);

        credentialAttributes = createCredentialAttributes(keyGithubURL);
        credentials = new Credentials(keyGithubURL, this.githubURL);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);

        credentialAttributes = createCredentialAttributes(keyScenariosFolder);
        credentials = new Credentials(keyScenariosFolder, this.scenariosFolder);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);

        credentialAttributes = createCredentialAttributes(keyUnityTestFolder);
        credentials = new Credentials(keyUnityTestFolder, this.unityTestFolder);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);

        credentialAttributes = createCredentialAttributes(keyStepDefinitionsFolder);
        credentials = new Credentials(keyStepDefinitionsFolder, this.stepDefinitionsFolder);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    private String getProjectName(Project project) {
        String projectName = "";

        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir != null) {
            projectName = projectDir.getName();
        }
        return projectName;
    }

    private CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("TaitiSystem", key));
    }
}
