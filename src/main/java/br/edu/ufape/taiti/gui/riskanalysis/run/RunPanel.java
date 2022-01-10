package br.edu.ufape.taiti.gui.riskanalysis.run;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.service.PivotalTracker;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBFont;

import javax.swing.*;

/**
 * Responsável por mostrar a interface gráfica para configuração da análise de conflito.
 */
public class RunPanel {
    private JPanel rootPanel;
    private JPanel messagePanel;
    private JPanel configurePanel;
    private JLabel messageLabel;
    private JLabel configureLabel;
    private JComboBox<Integer> intersectionSizeComboBox;
    private JCheckBox filteringCheckbox;
    private JLabel sizeLabel;
    private JLabel filteringLabel;

    private final PivotalTracker pivotalTracker;

    public boolean isShowing;

    public RunPanel(PivotalTracker pivotalTracker) {
        this.pivotalTracker = pivotalTracker;

        configurePanels();
        configureMessageLabel();
        initConfigurePanel();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JComboBox<Integer> getIntersectionSizeComboBox() {
        return intersectionSizeComboBox;
    }

    public JCheckBox getFilteringCheckbox() {
        return filteringCheckbox;
    }

    private void configureMessageLabel() {
        try {
            int totalTask = pivotalTracker.getPlannedStories().length();
            int taskWithScenarios = pivotalTracker.getTaitiFiles().length();
            String message = "<html><body>There are " + totalTask + " task running in PivotalTracker," +
                    " but only " + taskWithScenarios + " have scenarios selected, <br>" +
                    "do you want do run  conflict analysis?</body></html>";

            messageLabel.setText(message);

        } catch (HttpException e) {
            e.printStackTrace();
        }
    }

    private void initConfigurePanel() {
        for (int i = 1; i <=10; i++) {
            intersectionSizeComboBox.addItem(i);
        }
        // Default intersection size: 4
        intersectionSizeComboBox.setSelectedItem(4);
    }

    private void configurePanels() {
        messagePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, JBColor.border()));
        configurePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        configureLabel.setFont(JBFont.regular().biggerOn(2));
    }
}
