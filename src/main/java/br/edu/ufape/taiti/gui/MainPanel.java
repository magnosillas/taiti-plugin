package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.gui.configuretask.TaskConfigurePanel;
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

public class MainPanel {
    private JPanel rootPanel;
    private JSplitPane mainSplit;
    private JPanel contentPanel;
    private JPanel leftPanel;
    private JPanel listPanel;
    private JBList<String> optionsList;

    private TablePanel tablePanelDialog;
    private TaskConfigurePanel taskConfigurePanel;
    private RiskAnalysisPanel riskAnalysisPanel;

    private final Project project;

    private final TaitiTool taiti;
    private final PivotalTracker pivotalTracker;

    public MainPanel(Project project, TaitiTool taiti, PivotalTracker pivotalTracker) {
        this.project = project;
        this.taiti = taiti;
        this.pivotalTracker = pivotalTracker;

        tablePanelDialog = new TablePanel();
        taskConfigurePanel = new TaskConfigurePanel(project, tablePanelDialog, taiti, pivotalTracker, this);
        riskAnalysisPanel = new RiskAnalysisPanel();

        configurePanels();
        configureList();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void updateContent() {
        tablePanelDialog = new TablePanel();
        taskConfigurePanel = new TaskConfigurePanel(project, tablePanelDialog, taiti, pivotalTracker, this);

        setContentPanel(taskConfigurePanel.getRootPanel());
    }

    private void setContentPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.validate();
    }

    // TODO: trocar lista para o stripe
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
                        setContentPanel(taskConfigurePanel.getRootPanel());
                    } else if (index == 1) {
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
    }
}
