package br.edu.ufape.taiti.gui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TaitiDialog extends DialogWrapper {

    private final MainPanel mainPanel;

    public TaitiDialog() {
        super(true);
        init();
        setTitle("TAITI");

        mainPanel = new MainPanel();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel.getMainPanel();
    }
}
