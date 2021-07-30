package br.edu.ufape.taiti.actions;

import br.edu.ufape.taiti.gui.ScenarioTestInformation;
import br.edu.ufape.taiti.gui.TaitiDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class TaitiToolAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
//        Project project = event.getData(CommonDataKeys.PROJECT);

        TaitiDialog taitiDialog = new TaitiDialog();
        taitiDialog.setSize(1240,800);
//        taitiDialog.setResizable(false);
        if (taitiDialog.showAndGet()) {
            for (ScenarioTestInformation s : taitiDialog.getMainPanel().getScenarios()) {
                System.out.println(s.getFilePath() + " - " + s.getLineNumber());
            }
        }

    }
}
