package br.edu.ufape.taiti.gui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

public class TaitiDialog extends DialogWrapper {

    public TaitiDialog() {
        super(true);
        init();
        setTitle("TAITI");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        ContentPanel mainPanel = new ContentPanel();

        return mainPanel.getMainPanel();
    }
}
