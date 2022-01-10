package br.edu.ufape.taiti.gui.riskanalysis.result;

import javax.swing.*;

/**
 * Classe responsável por mostrar o resultado da análise de conflito. (Interface ainda não está feita)
 */
public class ResultPanel {
    private JPanel rootPanel;
    private JLabel messageLabel;

    public ResultPanel() {
        messageLabel.setText("No results.");
    }

    public void setMessageText(String text) {
        this.messageLabel.setText(text);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void loadResults() {
        messageLabel.setText("Showing results");
    }
}
