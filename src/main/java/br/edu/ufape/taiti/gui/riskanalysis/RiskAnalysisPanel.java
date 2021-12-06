package br.edu.ufape.taiti.gui.riskanalysis;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBFont;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RiskAnalysisPanel {
    private JPanel rootPanel;
    private JPanel optionsPanel;
    private JPanel contentPanel;
    private JLabel runLabel;
    private JLabel resultLabel;

    public RiskAnalysisPanel() {
        configurePanels();
        configureLabels();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void configureLabels() {
        runLabel.setFont(JBFont.regular().biggerOn(3).asBold());
        runLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    resultLabel.setBorder(null);
                    runLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, JBColor.blue));
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
                }
            }
        });
    }

    private void configurePanels() {
        optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, JBColor.border()),
                BorderFactory.createEmptyBorder(20, 0, 20, 0)
                ));
    }
}
