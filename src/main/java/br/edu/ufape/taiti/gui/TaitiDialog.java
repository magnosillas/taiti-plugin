package br.edu.ufape.taiti.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.internal.StringUtil;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class TaitiDialog extends DialogWrapper {

    private final MainPanel mainPanel;
    private final JTextField textGithubURL;
    private final JTextField textPivotalTrackerURL;
    private final JTextField textTaskID;

    public TaitiDialog(Project project) {
        super(true);
        mainPanel = new MainPanel(project);
        textGithubURL = mainPanel.getTextGithubURL();
        textPivotalTrackerURL = mainPanel.getTextPivotalURL();
        textTaskID = mainPanel.getTextTaskID();

        setTitle("TAITI");
        setSize(1340,800);
        init();
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel.getRootPanel();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        ValidationInfo validationInfo;

        try {
            new URL(textGithubURL.getText()).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            validationInfo = new ValidationInfo("Insert an URL valid.", textGithubURL);
            return validationInfo;
        }

        try {
            new URL(textPivotalTrackerURL.getText()).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            validationInfo = new ValidationInfo("Insert an URL valid.", textPivotalTrackerURL);
            return validationInfo;
        }

        try {
            Integer.parseInt(textTaskID.getText());
        } catch (NumberFormatException e) {
            validationInfo = new ValidationInfo("Insert only numbers.", textTaskID);
            return validationInfo;
        }

        if (StringUtil.isBlank(textGithubURL.getText())) {
            validationInfo = new ValidationInfo("The GitHub URL can not be empty.", textGithubURL);
            return validationInfo;
        } else if (StringUtil.isBlank(textPivotalTrackerURL.getText())) {
            validationInfo = new ValidationInfo("The PivotalTracker URL can not be empty", textPivotalTrackerURL);
            return validationInfo;
        } else if (StringUtil.isBlank(textTaskID.getText())) {
            validationInfo = new ValidationInfo("The Task ID can not be empty", textTaskID);
            return validationInfo;
        }

        return null;
    }

    @Override
    protected Action @NotNull [] createActions() {
        return super.createActions();
    }
}
