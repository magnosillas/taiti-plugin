package br.edu.ufape.taiti.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

public class TaitiDialog extends DialogWrapper {

    private final Project project;

    public TaitiDialog(Project project) {
        super(true);
        init();
        setTitle("TAITI");

        this.project = project;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        MainPanel mainPanel = new MainPanel(project);

        return mainPanel.getMainPanel();
    }
}
