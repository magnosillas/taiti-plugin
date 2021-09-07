package br.edu.ufape.taiti.actions;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.gui.MainPanel;
import br.edu.ufape.taiti.gui.TaitiDialog;
import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.settings.TaitiSettingsState;
import br.edu.ufape.taiti.tool.ScenarioTestInformation;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TaitiAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        TaitiDialog taitiDialog = new TaitiDialog(project);
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);

        MainPanel mainPanel = taitiDialog.getMainPanel();
        settings.retrieveStoredCredentials(project);

        taitiDialog.show();

        if (taitiDialog.isOK()) {
            String taskID = mainPanel.getTextTaskID().getText().replace("#", "");
            ArrayList<ScenarioTestInformation> scenarios = mainPanel.getScenarios();

            TaitiTool taiti = new TaitiTool(settings.getGithubURL(), Integer.parseInt(taskID), scenarios, project);
            PivotalTracker pivotalTracker = new PivotalTracker(settings.getToken(), settings.getPivotalURL(), taskID);

            try {
                File file = taiti.createScenariosFile();
                pivotalTracker.saveScenarios(file);
                taiti.deleteScenariosFile();
            } catch (IOException e) {
                System.out.println("Erro ao criar o arquivo!");
            } catch (HttpException e) {
                System.out.println(e.getStatusText() + " - " + e.getStatusNumber());
                taiti.deleteScenariosFile();
            }
        }
    }
}
