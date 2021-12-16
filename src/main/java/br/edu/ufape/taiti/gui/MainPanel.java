package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.gui.configuretask.TaskConfigurePanel;
import br.edu.ufape.taiti.gui.configuretask.table.TableDialog;
import br.edu.ufape.taiti.gui.configuretask.table.TablePanel;
import br.edu.ufape.taiti.gui.riskanalysis.RiskAnalysisPanel;
import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class MainPanel {
    private JPanel rootPanel;
    private JSplitPane mainSplit;
    private JPanel contentPanel;
    private JPanel leftPanel;
    private JPanel listPanel;
    private JPanel buttonsPanel;
    private JPanel btnP;
    private JBList<String> optionsList;

    private TablePanel tablePanelDialog;
    private TaskConfigurePanel taskConfigurePanel;
    private RiskAnalysisPanel riskAnalysisPanel;

    private final Project project;

    private final TaitiTool taiti;
    private final PivotalTracker pivotalTracker;

    private JButton saveButton;
    private JButton runButton;

    public MainPanel(Project project, TaitiTool taiti, PivotalTracker pivotalTracker) {
        this.project = project;
        this.taiti = taiti;
        this.pivotalTracker = pivotalTracker;

        configurePanels();
        configureList();
        configureActions();

        tablePanelDialog = new TablePanel();
        taskConfigurePanel = new TaskConfigurePanel(project, tablePanelDialog, taiti, pivotalTracker);
        riskAnalysisPanel = new RiskAnalysisPanel(pivotalTracker, btnP, runButton);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void updateFirstPanelContent() {
        tablePanelDialog = new TablePanel();
        taskConfigurePanel = new TaskConfigurePanel(project, tablePanelDialog, taiti, pivotalTracker);

        setContentPanel(taskConfigurePanel.getRootPanel());
    }

    private void setContentPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.validate();
    }

    private void configureActions() {
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String taskID = taskConfigurePanel.getTextTaskID().getText().replace("#", "");
            TableDialog tableDialog = new TableDialog(taskConfigurePanel, tablePanelDialog, taiti, pivotalTracker, taskConfigurePanel.getScenarios(), taskID);

            if (tableDialog.showAndGet()) {
                updateFirstPanelContent();
            }
        });

        runButton = new JButton("Run");
        runButton.addActionListener(e -> {
            Integer intersectionSize = (Integer) riskAnalysisPanel.getRunPanel().getIntersectionSizeComboBox().getSelectedItem();
            boolean filtering = riskAnalysisPanel.getRunPanel().getFilteringCheckbox().isSelected();

            btnP.removeAll();
            btnP.validate();

            riskAnalysisPanel.changePanel();
            riskAnalysisPanel.getRunPanel().isShowing = false;
            try {
                ArrayList<File> files = pivotalTracker.downloadFiles();
                 taiti.createTestI(files);
                // TODO: rodar análise de conflito

            } catch (HttpException ex) {
                ex.printStackTrace();
            }

            riskAnalysisPanel.updateResultPanel();
        });
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
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 1) {
                    int index = optionsList.locationToIndex(mouseEvent.getPoint());
                    if (index == 0) {
                        btnP.removeAll();
                        btnP.add(saveButton, BorderLayout.CENTER);
                        btnP.validate();
                        setContentPanel(taskConfigurePanel.getRootPanel());
                    } else if (index == 1) {
                        if (!riskAnalysisPanel.getRunPanel().isShowing) {
                            btnP.removeAll();
                            btnP.validate();
                        } else {
                            btnP.removeAll();
                            btnP.add(runButton, BorderLayout.CENTER);
                            btnP.validate();
                        }
                        setContentPanel(riskAnalysisPanel.getRootPanel());
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
        btnP.setLayout(new BorderLayout());
    }
}
