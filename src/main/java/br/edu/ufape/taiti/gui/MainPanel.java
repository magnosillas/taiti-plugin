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

/**
 * Esta é a classe principal responsável pela interface gráfica. O layout da interface é configurado no arquivo MainPanel.form.
 * O plugin tem duas etapas, a primeira parte é selecionar as tarefas e salvá-las no Pivotal Tracker, as classe que implementam
 * a interface gráfica responsável por isso são a TablePanel e TaskConfigurePanel, e a segunda parte é rodar a análise de conflito
 * e a interface gráfica responsável por essa parte é feita pela classe RiskAnalysisPanel.
 */
public class MainPanel {
    private JPanel rootPanel;
    private JPanel contentPanel;
    private JPanel buttonsPanel;
    private JPanel btnP; // esse objeto representa o painel onde é colocado os botões das ações.

    private TablePanel tablePanelDialog;
    private TaskConfigurePanel taskConfigurePanel;

    private final Project project;

    private final TaitiTool taiti;
    private final PivotalTracker pivotalTracker;

    private JButton saveButton;

    public MainPanel(Project project, TaitiTool taiti, PivotalTracker pivotalTracker) {
        this.project = project;
        this.taiti = taiti;
        this.pivotalTracker = pivotalTracker;


        this.tablePanelDialog = new TablePanel();
        this.taskConfigurePanel = new TaskConfigurePanel(project, tablePanelDialog, taiti, pivotalTracker);

        configureActions();
        configurePanels();
        updateFirstPanelContent();
        btnP.add(saveButton);
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

    /**
     * Aqui é configurado as ações dos botões da interface.
     */
    private void configureActions() {
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String taskID = taskConfigurePanel.getTextTaskID().getText().replace("#", "");
            // Ao clicar em salvar é mostrado a tabela de scenarios selecionados.
            TableDialog tableDialog = new TableDialog(this.taskConfigurePanel, tablePanelDialog, taiti, pivotalTracker, taskConfigurePanel.getScenarios(), taskID);

            if (tableDialog.showAndGet()) {
                updateFirstPanelContent();
            }

            btnP.add(saveButton);
            btnP.validate();
        });
    }


    private void configurePanels() {



        contentPanel.setLayout(new BorderLayout());

        btnP.setLayout(new BorderLayout());
    }
}