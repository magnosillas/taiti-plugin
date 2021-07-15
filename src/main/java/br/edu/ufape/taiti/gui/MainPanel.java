package br.edu.ufape.taiti.gui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MainPanel {
    // panels
    private JPanel mainPanel;
    private JPanel treePanel;
    private JPanel rightPanel;
    private JPanel centerPanel;
    private JPanel inputPanel;
    private JPanel tablePanel;

    private JSplitPane splitPane;

    // components
    private TaitiTree tree;

    private JLabel labelGithubURL;
    private JLabel labelPivotalURL;
    private JLabel labelTaskID;

    private JTextField textGithubURL;
    private JTextField textPivotalURL;
    private JTextField textTaskID;

    private JBTable table;
    private TestsTableModel tableModel;

    private FeatureFileView featureFileView;
    private FeatureFileViewModel featureFileViewModel;

    // -------------
    private ArrayList<String> scenarios;
    private RepositoryOpenFeatureFile repositoryOpenFeatureFile;

    private Project project;

    public MainPanel(Project project) {
        scenarios = new ArrayList<>();
        repositoryOpenFeatureFile = new RepositoryOpenFeatureFile();
        this.project = project;

        configurePanels();
        configureTree();
        configureInputPanel();
        initTable();
        initCenterPanel();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateCenterPanel(File file) {
        String filePath = file.getAbsolutePath();
        OpenFeatureFile openFeatureFile;

        if (repositoryOpenFeatureFile.exists(file)) {
            openFeatureFile = repositoryOpenFeatureFile.getFeatureFile(file);
        } else {
            ArrayList<FileLine> fileLines = new ArrayList<>();
            Scanner scanner;
            try {
                scanner = new Scanner(new FileReader(filePath));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    fileLines.add(new FileLine(false, line));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            openFeatureFile = new OpenFeatureFile(file, fileLines);
            repositoryOpenFeatureFile.addFeatureFile(openFeatureFile);
        }

        featureFileViewModel = new FeatureFileViewModel(file, openFeatureFile.getFileLines(), scenarios, tableModel);
        featureFileView.setModel(featureFileViewModel);
        featureFileView.setTableWidth();
        featureFileView.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxCellRenderer());
    }

    private void initCenterPanel() {
        featureFileView = new FeatureFileView();
        featureFileView.setShowGrid(false);
        featureFileView.getTableHeader().setResizingAllowed(false);
        featureFileView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        centerPanel.add(new JScrollPane(featureFileView), BorderLayout.CENTER);
    }

    private void initTable() {
        table = new JBTable();
        tableModel = new TestsTableModel();
        table.setModel(tableModel);
        table.getTableHeader().setResizingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER); // scroll não está funcionando

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(270);
    }

    private void configureTree() {
        // TODO: pegar caminho e nome do projeto dinamicamente
        String projectPath = "C:\\Users\\usuario\\Projects\\diaspora";
        String projectName = "diaspora";

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(projectName);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

        tree = new TaitiTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                    if (node == null) return;

                    Object nodeInfo = node.getUserObject();
                    if (node.isLeaf() && !node.getAllowsChildren()) {
                        TaitiTreeFileNode taitiTreeFileNode = (TaitiTreeFileNode) nodeInfo;
                        updateCenterPanel(taitiTreeFileNode.getFile());
                    }
                }
            }
        });

        File featureDirectory = tree.findFeatureDirectory(projectPath);
        // TODO: se não existir o diretório features fazer um tratamento de erro
        if (featureDirectory != null) {
            DefaultMutableTreeNode featureNode = new DefaultMutableTreeNode(featureDirectory.getName());
            rootNode.add(featureNode);
            tree.addNodesToTree(featureDirectory.getAbsolutePath(), featureNode);
        }

        treePanel.add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    private void configureInputPanel() {
        labelGithubURL = new JLabel("GitHub Project URL:");
        textGithubURL = new JTextField();

        labelPivotalURL = new JLabel("PivotalTracker Project URL:");
        textPivotalURL = new JTextField();

        labelTaskID = new JLabel("Task ID:");
        textTaskID = new JTextField();

        this.inputPanel.add(labelGithubURL);
        this.inputPanel.add(textGithubURL);

        this.inputPanel.add(labelPivotalURL);
        this.inputPanel.add(textPivotalURL);

        this.inputPanel.add(labelTaskID);
        this.inputPanel.add(textTaskID);
    }

    private void configurePanels() {
        mainPanel = new JPanel();
        treePanel = new JPanel();
        centerPanel = new JPanel();
        rightPanel = new JPanel();
        inputPanel = new JPanel();
        tablePanel = new JPanel();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treePanel, centerPanel);
        splitPane.setDividerLocation(300);

        mainPanel.setLayout(null);
        rightPanel.setLayout(null);

        treePanel.setLayout(new BorderLayout());
        tablePanel.setLayout(new BorderLayout());
        centerPanel.setLayout(new BorderLayout());

        inputPanel.setLayout(new GridLayout(3, 2, 5, 50));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        splitPane.setBounds(0, 0, 900, 700);
        rightPanel.setBounds(900, 0, 300, 700);
        inputPanel.setBounds(0, 0, 300, 200);
        tablePanel.setBounds(0, 300, 300, 500);

        mainPanel.add(splitPane);
        mainPanel.add(rightPanel);
        rightPanel.add(inputPanel);
        rightPanel.add(tablePanel);
    }
}
