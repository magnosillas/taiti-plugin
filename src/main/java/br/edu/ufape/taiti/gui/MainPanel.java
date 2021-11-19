package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.gui.configuretask.TaskConfigurePanel;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBFont;

import javax.swing.*;
import java.awt.*;

public class MainPanel {
    private JPanel rootPanel;
    private JSplitPane mainSplit;
    private JPanel contentPanel;
    private JPanel leftPanel;
    private JPanel listPanel;
    private JBList<String> optionsList;

    private final TaskConfigurePanel taskConfigurePanel;

    public MainPanel(Project project) {
        taskConfigurePanel = new TaskConfigurePanel(project);

        configurePanels();
        configureList();
        setContentPanel(taskConfigurePanel.getRootPanel());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public TaskConfigurePanel getTaskConfigurePanel() {
        return taskConfigurePanel;
    }

    private void setContentPanel(JPanel panel) {
        this.contentPanel.setLayout(new BorderLayout());

        this.contentPanel.add(panel, BorderLayout.CENTER);
    }

    private void configureList() {
        String[] options = {"Configure task", "Run conflict risk analysis"};
        optionsList = new JBList<>(options);

        optionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        optionsList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JBLabel label = new JBLabel(value);

            label.setFont(JBFont.regular().biggerOn(1));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);

            return label;
        });

        listPanel.add(optionsList, BorderLayout.CENTER);
    }

    private void configurePanels() {
        mainSplit.setDividerLocation(180);
        mainSplit.setDividerSize(2);
        mainSplit.setContinuousLayout(true);

        listPanel.setLayout(new BorderLayout());
    }
}
