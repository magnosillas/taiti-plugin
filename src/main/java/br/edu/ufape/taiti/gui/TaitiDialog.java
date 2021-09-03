package br.edu.ufape.taiti.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;
import org.jsoup.internal.StringUtil;

import javax.swing.*;

public class TaitiDialog extends DialogWrapper {

    private final MainPanel mainPanel;
    private final JTextField textTaskID;
    private final JBTable table;

    public TaitiDialog(Project project) {
        super(true);

        mainPanel = new MainPanel(project);
        textTaskID = mainPanel.getTextTaskID();
        table = mainPanel.getTable();

        setTitle("TAITI");
        setSize(1000,810);
        init();
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel.getRootPanel();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return mainPanel.getTextTaskID();
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        ValidationInfo validationInfo;

        // check if the fields is empty
        if (StringUtil.isBlank(textTaskID.getText())) {
            validationInfo = new ValidationInfo("The Task ID can not be empty.", textTaskID);
            return validationInfo;
        }
        if (table.getRowCount() == 1) {
            validationInfo = new ValidationInfo("Select at least one scenario.", table);
            return validationInfo;
        }

        // check if the input data is valid
        String regex = "#?\\d+$";
        if (!textTaskID.getText().matches(regex)) {
            validationInfo = new ValidationInfo("The task ID is wrong.", textTaskID);
            return validationInfo;
        }

        return null;
    }
}
