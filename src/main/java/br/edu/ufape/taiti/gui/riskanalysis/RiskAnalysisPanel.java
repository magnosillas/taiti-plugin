package br.edu.ufape.taiti.gui.riskanalysis;

import br.edu.ufape.taiti.gui.riskanalysis.result.ResultPanel;
import br.edu.ufape.taiti.gui.riskanalysis.run.RunPanel;
import br.edu.ufape.taiti.service.PivotalTracker;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Esta classe é responsável por criar a interface gráfica relacionada a análise de risco de conflito, essa etapa
 * em duas partes, a parte de rodar a análise, na qual a interface gráfica é criada por RunPanel, e a parte onde mostra os resultados,
 * na qual a interface gráfica é criada por ResultPanel.
 */
public class RiskAnalysisPanel {
    private JPanel rootPanel;
    private JPanel optionsPanel;
    private JPanel contentPanel;
    private JLabel runLabel;
    private JLabel resultLabel;

    private RunPanel runPanel;
    private ResultPanel resultPanel;

    public RiskAnalysisPanel(PivotalTracker pivotalTracker, JPanel btnP, JButton runButton) {
        runPanel = new RunPanel(pivotalTracker);
        resultPanel = new ResultPanel();

        configurePanels();
        configureLabels(btnP, runButton);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public RunPanel getRunPanel() {
        return runPanel;
    }

    public void changePanel() {
        runLabel.setBorder(null);
        resultLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, JBColor.blue));
        resultPanel.setMessageText("Loading results...");
        setContentPanel(resultPanel.getRootPanel());
    }

    public void updateResultPanel() {
        resultPanel.loadResults();
    }

    private void setContentPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.validate();
    }

    private void configureLabels(JPanel btnP, JButton runButton) {
        runLabel.setFont(JBFont.regular().biggerOn(3).asBold());
        runLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    resultLabel.setBorder(null);
                    runLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, JBColor.blue));
                    setContentPanel(runPanel.getRootPanel());
                    runPanel.isShowing = true;
                    btnP.removeAll();
                    btnP.add(runButton, BorderLayout.CENTER);
                    btnP.validate();
                }
            }
        });

        resultLabel.setFont(JBFont.regular().biggerOn(3).asBold());
        resultLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    runLabel.setBorder(null);
                    resultLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, JBColor.blue));
                    setContentPanel(resultPanel.getRootPanel());
                    runPanel.isShowing = false;
                    btnP.removeAll();
                    btnP.validate();
                }
            }
        });
    }

    private void configurePanels() {
        optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, JBColor.border()),
                BorderFactory.createEmptyBorder(20, 0, 20, 0)
                ));
        contentPanel.setLayout(new BorderLayout());
    }
}
