package br.edu.ufape.taiti.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.table.JBTable;
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
    private final JBTable table;

    public TaitiDialog(Project project) {
        super(true);

        mainPanel = new MainPanel(project);
        textGithubURL = mainPanel.getTextGithubURL();
        textPivotalTrackerURL = mainPanel.getTextPivotalURL();
        textTaskID = mainPanel.getTextTaskID();
        table = mainPanel.getTable();

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
    protected @Nullable ValidationInfo doValidate() {
        ValidationInfo validationInfo;

        // check if the fields is empty
        if (StringUtil.isBlank(textGithubURL.getText())) {
            validationInfo = new ValidationInfo("The GitHub URL can not be empty.", textGithubURL);
            return validationInfo;
        }
        if (StringUtil.isBlank(textPivotalTrackerURL.getText())) {
            validationInfo = new ValidationInfo("The PivotalTracker URL can not be empty", textPivotalTrackerURL);
            return validationInfo;
        }
        if (StringUtil.isBlank(textTaskID.getText())) {
            validationInfo = new ValidationInfo("The Task ID can not be empty", textTaskID);
            return validationInfo;
        }
        if (table.getRowCount() == 1) {
            validationInfo = new ValidationInfo("Select at least one scenario.", table);
            return validationInfo;
        }

        // check if the input data is valid
        try {
            new URL(textGithubURL.getText()).toURI();
            String regex = "https://([w]{3}\\.)?github\\.com/.[^/]+/.[^/]+";
            if (!textGithubURL.getText().matches(regex)) {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            validationInfo = new ValidationInfo("Enter a valid URL.", textGithubURL);
            return validationInfo;
        }
        try {
            new URL(textPivotalTrackerURL.getText()).toURI();
            String regex = "https://[w]{3}\\.pivotaltracker\\.com/n/projects/\\d+";
            if (!textPivotalTrackerURL.getText().matches(regex)) {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            validationInfo = new ValidationInfo("Enter a valid URL.", textPivotalTrackerURL);
            return validationInfo;
        }
        try {
            Integer.parseInt(textTaskID.getText());
        } catch (NumberFormatException e) {
            validationInfo = new ValidationInfo("Insert only numbers.", textTaskID);
            return validationInfo;
        }

        return null;
    }
}
