package br.edu.ufape.taiti.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TaitiSettingsComponent {

    private final JPanel mainPanel;
    private final JBTextField githubURLText;
    private final JBTextField pivotalURLText;
    private final JBPasswordField pivotalToken;

    public TaitiSettingsComponent() {
        githubURLText = new JBTextField();
        pivotalURLText = new JBTextField();
        pivotalToken = new JBPasswordField();

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("GitHub URL: "), githubURLText, 1, false)
                .addLabeledComponent(new JBLabel("PivotalTracker URL: "), pivotalURLText, 1, false)
                .addLabeledComponent(new JBLabel("PivotalTracker token: "), pivotalToken, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JComponent getPreferredFocusedComponent() {
        return githubURLText;
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @NotNull
    public String getGithubURLText() {
        return githubURLText.getText();
    }

    public void setGithubURLText(@NotNull String text) {
        githubURLText.setText(text);
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
