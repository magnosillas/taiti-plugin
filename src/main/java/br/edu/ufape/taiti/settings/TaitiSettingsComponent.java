package br.edu.ufape.taiti.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TaitiSettingsComponent {

    private final JPanel mainPanel;
    private final JBTextField pivotalURLText;
    private final JBPasswordField pivotalToken;

    public TaitiSettingsComponent() {
        pivotalURLText = new JBTextField();
        pivotalToken = new JBPasswordField();

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("PivotalTracker URL: "), pivotalURLText, 1, false)
                .addLabeledComponent(new JBLabel("PivotalTracker token: "), pivotalToken, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JComponent getPreferredFocusedComponent() {
        return pivotalURLText;
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @NotNull
    public String getPivotalURLText() {
        return pivotalURLText.getText();
    }

    public void setPivotalURLText(@NotNull String text) {
        pivotalURLText.setText(text);
    }

    @NotNull
    public String getPivotalToken() {
        return String.valueOf(pivotalToken.getPassword());
    }

    public void setPivotalToken(@NotNull String text) {
        pivotalToken.setText(text);
    }
}
