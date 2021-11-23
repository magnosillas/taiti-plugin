package br.edu.ufape.taiti.gui.configuretask.table;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TableDialog extends DialogWrapper {

    private final TablePanel tablePanel;

    public TableDialog(TablePanel tablePanel) {
        super(true);

        this.tablePanel = tablePanel;

        setTitle("Review");
        setSize(400,400);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return tablePanel.getPanel();
    }
}
