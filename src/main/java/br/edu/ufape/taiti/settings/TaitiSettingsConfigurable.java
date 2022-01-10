package br.edu.ufape.taiti.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.jsoup.internal.StringUtil;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Esta classe é um controller para as configurações do plugin e está definida no arquivo plugin.xml.
 *
 * Mais informaçoes em: https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html e
 * https://plugins.jetbrains.com/docs/intellij/settings-tutorial.html
 */
public class TaitiSettingsConfigurable implements Configurable {

    private TaitiSettingsComponent component;
    private final Project project;

    public TaitiSettingsConfigurable(Project project) {
        this.project = project;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "TAITI";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return component.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        component = new TaitiSettingsComponent();
        return component.getPanel();
    }

    @Override
    public boolean isModified() {
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        boolean modified = !component.getPivotalURLText().equals(settings.pivotalURL);
        modified |= !component.getPivotalToken().equals(settings.token);
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        validate();
        settings.pivotalURL = component.getPivotalURLText();
        settings.token = component.getPivotalToken();
        settings.storeCredentials(project);
    }

    @Override
    public void reset() {
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.retrieveStoredCredentials(project);
        component.setPivotalURLText(settings.pivotalURL);
        component.setPivotalToken(settings.token);
    }

    @Override
    public void disposeUIResources() {
        component = null;
    }

    private void validate() throws ConfigurationException {
        // check if the fields is empty
        if (StringUtil.isBlank(component.getPivotalURLText())) {
            throw new ConfigurationException("The PivotalTracker URL field is empty", "Cannot Save Settings");
        }
        if (StringUtil.isBlank(component.getPivotalToken())) {
            throw new ConfigurationException("The PivotalTracker token field is empty", "Cannot Save Settings");
        }

        // check if the input data is valid
        try {
            new URL(component.getPivotalURLText()).toURI();
            String regex = "https://[w]{3}\\.pivotaltracker\\.com/n/projects/\\d+";
            if (!component.getPivotalURLText().matches(regex)) {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            throw new ConfigurationException(
                    "The PivotalTracker URL is malformed, example of URL: https://www.pivotaltracker.com/n/projects/{project_id}",
                    "Cannot Save Settings");
        }
    }

}
