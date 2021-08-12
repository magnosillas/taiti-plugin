package br.edu.ufape.taiti.actions;

import br.edu.ufape.taiti.gui.MainPanel;
import br.edu.ufape.taiti.gui.TaitiDialog;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class TaitiToolAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();

        TaitiDialog taitiDialog = new TaitiDialog(project);
        taitiDialog.show();

        MainPanel mainPanel = taitiDialog.getMainPanel();

        String githubURL = mainPanel.getTextGithubURL().getText();
        String pivotalTrackerURL = mainPanel.getTextPivotalURL().getText();
        int taskID = Integer.parseInt(mainPanel.getTextTaskID().getText());

        if (taitiDialog.isOK()) {
            System.out.println(githubURL);
            System.out.println(pivotalTrackerURL);
            System.out.println(taskID);

            TaitiTool taiti = new TaitiTool(githubURL, taskID);
//            taiti.run();
        }

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }
}
