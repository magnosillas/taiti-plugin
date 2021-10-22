package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.settings.TaitiSettingsState;
import br.edu.ufape.taiti.tool.ScenarioTestInformation;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.internal.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TaitiDialog extends DialogWrapper {

    private final MainPanel mainPanel;
    private final JTextField textTaskID;
    private final JBTable table;

    private PivotalTracker pivotalTracker;
    private TaitiTool taiti;

    private final Project project;

    public TaitiDialog(Project project) {
        super(true);

        this.mainPanel = new MainPanel(project);
        this.textTaskID = mainPanel.getTextTaskID();
        this.table = mainPanel.getTable();
        this.project = project;

        setTitle("TAITI");
        setSize(1000,810);
        init();
    }

    private void prepareServices() {
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.retrieveStoredCredentials(project);

        String taskID = textTaskID.getText().replace("#", "");
        ArrayList<ScenarioTestInformation> scenarios = mainPanel.getScenarios();

        taiti = new TaitiTool(settings.getGithubURL(), Integer.parseInt(taskID), scenarios, project);
        pivotalTracker = new PivotalTracker(settings.getToken(), settings.getPivotalURL(), project);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel.getRootPanel();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return textTaskID;
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        ValidationInfo validationInfo;

        // check if the fields is empty
        if (StringUtil.isBlank(textTaskID.getText())) {
            validationInfo = new ValidationInfo("The Task ID can not be empty.", textTaskID);
            return validationInfo;
        }
        if (table.getRowCount() == 1) {
            validationInfo = new ValidationInfo("Select at least one scenario.", table);
            return validationInfo;
        }

        // check if the input data is valid
        String regex = "#?\\d+$";
        if (!textTaskID.getText().matches(regex)) {
            validationInfo = new ValidationInfo("The task ID is wrong.", textTaskID);
            return validationInfo;
        }

        return null;
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{getOKAction(), new RunConflictAnalysisAction(), getCancelAction()};
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();

        prepareServices();

        String taskID = textTaskID.getText().replace("#", "");

        try {
            File file = taiti.createScenariosFile();
            pivotalTracker.saveScenarios(file, taskID);
            taiti.deleteScenariosFile();
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo!");
        } catch (HttpException e) {
            System.out.println(e.getStatusText() + " - " + e.getStatusNumber());
            taiti.deleteScenariosFile();
        }
    }

    protected class RunConflictAnalysisAction extends DialogWrapperAction {
        protected RunConflictAnalysisAction() {
            super("Run Conflict Analysis");
            putValue(Action.NAME, "Run Conflict Analysis");
        }

        @Override
        protected void doAction(ActionEvent e) {
            if (doValidate() == null) {
                getOKAction().setEnabled(isOkEnabled());
                doOKAction();

                try {
                    ArrayList<File> files = pivotalTracker.downloadFiles();
                    taiti.createTestI(files);

                } catch (HttpException exception) {
                    System.out.println(exception.getStatusText() + " - " + exception.getStatusNumber());
                }
            }
        }

        boolean isOkEnabled() {
            return true;
        }
    }
}
