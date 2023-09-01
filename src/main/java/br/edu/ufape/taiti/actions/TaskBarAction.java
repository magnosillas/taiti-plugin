package br.edu.ufape.taiti.actions;


import br.edu.ufape.taiti.gui.taskbar.TaskBarGUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class TaskBarAction implements ToolWindowFactory {



    /**
        Classe responsável por iniciar a TaskBar através do ToolWindow
   **/

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        String classpath = System.getProperty("java.class.path");
        TaskBarGUI taskBarGUI = new TaskBarGUI(toolWindow, project);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(taskBarGUI.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);

    }



}
