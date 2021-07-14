package br.edu.ufape.taiti.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ContentPanel {
    // panels
    private JPanel mainPanel;
    private JPanel treePanel;
    private JPanel rightPanel;
    private JPanel centerPanel;
    private JPanel inputPanel;
    private JPanel tablePanel;

    private JSplitPane splitPane;

    // components
    private JTree tree;

    private JLabel labelGithubURL;
    private JLabel labelPivotalURL;
    private JLabel labelTaskID;

    private JTextField textGithubURL;
    private JTextField textPivotalURL;
    private JTextField textTaskID;

    private JTable table;
    private TestsTableModel tableModel;

    private JTable featureFileTable;
    private FeatureFileTableModel featureFileTableModel;

    // -------------
    private ArrayList<String> scenarios;
    private FeatureFileRepository featureFileRepository;

    public ContentPanel() {
        scenarios = new ArrayList<>();
        featureFileRepository = new FeatureFileRepository();

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

        ArrayList<RowFile> fileLines = new ArrayList<>();
        Scanner scanner;
        try {
            scanner = new Scanner(new FileReader(filePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileLines.add(new RowFile(false, line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FeatureFile featureFile;
        if (featureFileRepository.exists(file)) {
            featureFile = featureFileRepository.findFeatureFile(file);
        } else {
            featureFile = new FeatureFile(file, fileLines);
            featureFileRepository.addFeatureFile(featureFile);
        }

        fileLines = featureFile.getFileLines();

        featureFileTableModel = new FeatureFileTableModel(file, fileLines, scenarios, tableModel);
        featureFileTable.setModel(featureFileTableModel);
        setTableWidth(featureFileTable);
        featureFileTable.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxCellRenderer());
    }

    private void setTableWidth(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth + 20);
        }
    }

    private void initCenterPanel() {
        featureFileTable = new JTable();
        featureFileTable.setShowGrid(false);
        featureFileTable.getTableHeader().setResizingAllowed(false);
        featureFileTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        centerPanel.add(new JScrollPane(featureFileTable), BorderLayout.CENTER); // scroll não está funcionando
    }

    private void initTable() {
        table = new JTable();
        tableModel = new TestsTableModel();
        table.setModel(tableModel);
        table.getTableHeader().setResizingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER); // scroll não está funcionando

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(270);
    }

    private void configureTree() {
        String projectPath = "C:\\Users\\usuario\\Projects\\diaspora";
        String projectFileName = "diaspora";

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(projectFileName);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

        tree = new JTree(treeModel);
        FileTree fileTree = new FileTree(tree, this); // TODO: arrumar isso aqui que tá muito feio
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addMouseListener(fileTree);

        File featureDir = fileTree.findFeatureDir(projectPath);
        // TODO: se não existir o diretório features fazer um tratamento de erro
        if (featureDir != null) {
            DefaultMutableTreeNode featureNode = new DefaultMutableTreeNode(featureDir.getName());
            rootNode.add(featureNode);
            fileTree.addNodesToTree(featureDir.getAbsolutePath(), featureNode);
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
