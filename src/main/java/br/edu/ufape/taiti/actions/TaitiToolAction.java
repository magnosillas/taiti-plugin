package br.edu.ufape.taiti.actions;

import br.edu.ufape.taiti.gui.TaitiDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TaitiToolAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        new TaitiDialog().show();
    }
}
