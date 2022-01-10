package br.edu.ufape.taiti.actions;

import br.edu.ufape.taiti.gui.TaitiDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Esta classe é o início da aplicação, nela é definida uma ação e essa ação é configurada no arquivo resources/META-INF/plugin.xml.
 * Ao acionar a ação, o método actionPerformed é chamado.
 */
public class TaitiAction extends AnAction {
    /**
     * Este método pega a referência ao projeto atualmente aberto no IntelliJ
     * e cria um objeto TaitiDialog, responsável por mostrar a janela da aplicação.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        TaitiDialog taitiDialog = new TaitiDialog(project);

        taitiDialog.show();
    }
}
