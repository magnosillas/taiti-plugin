package br.edu.ufape.taiti.actions;

import br.edu.ufape.taiti.gui.MainPanel;
import br.edu.ufape.taiti.gui.TaitiDialog;
import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.tool.ScenarioTestInformation;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class TaitiAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();

        TaitiDialog taitiDialog = new TaitiDialog(project);

        if (taitiDialog.showAndGet()) {
            MainPanel mainPanel = taitiDialog.getMainPanel();

            String githubURL = mainPanel.getTextGithubURL().getText();
            String pivotalTrackerURL = mainPanel.getTextPivotalURL().getText();
            String taskID = mainPanel.getTextTaskID().getText();

            ArrayList<ScenarioTestInformation> scenarios = mainPanel.getScenarios();

            TaitiTool taiti = new TaitiTool(githubURL, Integer.parseInt(taskID), scenarios, project);
            PivotalTracker pivotalTracker = new PivotalTracker("your_token", pivotalTrackerURL, taskID);

            File file = taiti.createScenariosFile();
            pivotalTracker.saveScenarios(file);
        }
    }

}
