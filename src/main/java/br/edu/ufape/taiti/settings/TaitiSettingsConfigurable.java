package br.edu.ufape.taiti.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
        boolean modified = !component.getGithubURLText().equals(settings.githubURL);
        modified |= !component.getPivotalURLText().equals(settings.pivotalURL);
        modified |= !component.getPivotalToken().equals(settings.token);
        return modified;
    }

    @Override
    public void apply() {
        // TODO: tratar os dados
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.githubURL = component.getGithubURLText();
        settings.pivotalURL = component.getPivotalURLText();
        settings.token = component.getPivotalToken();
    }

    @Override
    public void reset() {
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        component.setGithubURLText(settings.githubURL);
        component.setPivotalURLText(settings.pivotalURL);
        component.setPivotalToken(settings.token);
    }

    @Override
    public void disposeUIResources() {
        component = null;
    }
}
