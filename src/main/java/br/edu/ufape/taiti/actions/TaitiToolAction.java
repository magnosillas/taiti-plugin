package br.edu.ufape.taiti.actions;

import br.edu.ufape.taiti.gui.ScenarioTestInformation;
import br.edu.ufape.taiti.gui.TaitiDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TaitiToolAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        TaitiDialog taitiDialog = new TaitiDialog();

        if (taitiDialog.showAndGet()) {
            for (ScenarioTestInformation s : taitiDialog.getMainPanel().getScenarios()) {
                System.out.println(s.getFilePath() + " - " + s.getLineNumber());
            }
        }

    }
}
