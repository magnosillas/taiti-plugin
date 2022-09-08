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
    }

    public void storeCredentials(Project project) {
        String keyPivotalURL = "pivotalURL" + getProjectName(project);
        String keyPivotalToken = "pivotalToken" + getProjectName(project);

        CredentialAttributes credentialAttributes = createCredentialAttributes(keyPivotalURL);
        Credentials credentials = new Credentials(keyPivotalURL, this.pivotalURL);
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
