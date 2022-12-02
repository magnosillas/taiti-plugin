package br.edu.ufape.taiti.gui.taskbar;


import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyToolWindow {
    private JPanel mainPanel;
    private JPanel myToolWindowContent;
    private JButton refreshButton;
    private JButton addButton;
    private JTextField searchBar;
    private JButton search;
    private JList tasksList;
    private ArrayList<String> people;
    private DefaultListModel listPeopleModel;

    public MyToolWindow(ToolWindow toolWindow) {


    }
    public JPanel getContent() {
        return myToolWindowContent;
    }
}
