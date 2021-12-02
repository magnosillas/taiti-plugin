package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.gui.configuretask.TaskConfigurePanel;
import br.edu.ufape.taiti.gui.configuretask.table.TablePanel;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPanel {
    private JPanel rootPanel;
    private JSplitPane mainSplit;
    private JPanel contentPanel;
    private JPanel leftPanel;
    private JPanel listPanel;
    private JBList<String> optionsList;

    private TablePanel tablePanelDialog;
    private TaskConfigurePanel taskConfigurePanel;

    private final Project project;

    public MainPanel(Project project) {
        this.project = project;
        tablePanelDialog = new TablePanel();
        taskConfigurePanel = new TaskConfigurePanel(project, tablePanelDialog);

        configurePanels();
        configureList();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public TaskConfigurePanel getTaskConfigurePanel() {
        return taskConfigurePanel;
    }

    public TablePanel getTablePanelDialog() {
        return tablePanelDialog;
    }

    public void updateContent() {
        tablePanelDialog = new TablePanel();
        taskConfigurePanel = new TaskConfigurePanel(project, tablePanelDialog);

        setContentPanel(taskConfigurePanel.getRootPanel());
    }

    private void setContentPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.validate();
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
        optionsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int index = optionsList.locationToIndex(e.getPoint());
                    if (index == 0) {
                        setContentPanel(taskConfigurePanel.getRootPanel()); // criar nova tela ou continuar de onde parou
                    } else if (index == 1) {
                        setContentPanel(new JPanel());
                    }
                }
            }
        });

        listPanel.add(optionsList, BorderLayout.CENTER);
    }

    private void configurePanels() {
        mainSplit.setDividerLocation(180);
        mainSplit.setDividerSize(2);
        mainSplit.setContinuousLayout(true);

        contentPanel.setLayout(new BorderLayout());
        listPanel.setLayout(new BorderLayout());
    }
}
