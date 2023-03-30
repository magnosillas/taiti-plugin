package br.edu.ufape.taiti.actions;

import br.edu.ufape.taiti.gui.conflicts.ConflictsGUI;
import br.edu.ufape.taiti.gui.conflicts.ConflictsTable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ConflictsAction implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ConflictsGUI conflictsGUI = new ConflictsGUI(toolWindow, project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(conflictsGUI.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
