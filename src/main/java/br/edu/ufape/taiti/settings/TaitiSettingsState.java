package br.edu.ufape.taiti.settings;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;

public class TaitiSettingsState {
    protected String githubURL = "";
    protected String pivotalURL = "";
    protected String token = "";

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
        String keyGithubURL = "githubURL" + getProjectName(project);
        String keyPivotalURL = "pivotalURL" + getProjectName(project);
        String keyPivotalToken = "pivotalToken" + getProjectName(project);

        CredentialAttributes credentialAttributes = createCredentialAttributes(keyGithubURL);
        Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.githubURL = credentials.getPasswordAsString();
        }

        credentialAttributes = createCredentialAttributes(keyPivotalURL);
        credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.pivotalURL = credentials.getPasswordAsString();
        }

        credentialAttributes = createCredentialAttributes(keyPivotalToken);
        credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            this.token = credentials.getPasswordAsString();
        }
    }

    public void storeCredentials(Project project) {
        String keyGithubURL = "githubURL" + getProjectName(project);
        String keyPivotalURL = "pivotalURL" + getProjectName(project);
        String keyPivotalToken = "pivotalToken" + getProjectName(project);

        CredentialAttributes credentialAttributes = createCredentialAttributes(keyGithubURL);
        Credentials credentials = new Credentials(keyGithubURL, this.githubURL);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);

        credentialAttributes = createCredentialAttributes(keyPivotalURL);
        credentials = new Credentials(keyPivotalURL, this.pivotalURL);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);

        credentialAttributes = createCredentialAttributes(keyPivotalToken);
        credentials = new Credentials(keyPivotalToken, this.token);
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
