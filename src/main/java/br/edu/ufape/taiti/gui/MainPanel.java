package br.edu.ufape.taiti.gui;

import javax.swing.*;

public class MainPanel {

    private JPanel mainPanel;
    private JPanel rightPanel;
    private JPanel centerPanel;
    private JPanel leftPanel;
    private JPanel tablePanel;
    private JPanel inputPanel;

    private JLabel labelGithubURL;
    private JLabel labelPivotalURL;
    private JLabel labelTaskID;

    private JTextField githubURL;
    private JTextField PivotalURL;
    private JTextField TaskID;

    private JTree tree;
    private JScrollPane scrollPanel;

    public JComponent getMainPanel() {
        return mainPanel;
    }
}
