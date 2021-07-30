package br.edu.ufape.taiti.gui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

public class TaitiDialog extends DialogWrapper {

    private final MainPanel mainPanel;

    public TaitiDialog() {
        super(true);
        mainPanel = new MainPanel();

        setTitle("TAITI");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel.getMainPanel();
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }
}
