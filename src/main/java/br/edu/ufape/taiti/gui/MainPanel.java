package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.gui.fileview.*;
import br.edu.ufape.taiti.gui.tree.TaitiTree;
import br.edu.ufape.taiti.gui.tree.TaitiTreeFileNode;
import br.edu.ufape.taiti.gui.table.TestRow;
import br.edu.ufape.taiti.gui.table.TestsTableModel;
import br.edu.ufape.taiti.gui.table.TestsTableRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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

import static br.edu.ufape.taiti.gui.Constants.FIRST_ROW_HEIGHT;

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

    private final ArrayList<ScenarioTestInformation> scenarios;
    private final RepositoryOpenFeatureFile repositoryOpenFeatureFile;

    public MainPanel() {
        scenarios = new ArrayList<>();
        repositoryOpenFeatureFile = new RepositoryOpenFeatureFile();

        configurePanels();
        configureTree();
        configureInputPanel();
        initTable();
        initCenterPanel();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ArrayList<ScenarioTestInformation> getScenarios() {
        return scenarios;
    }

    public void updateCenterPanel(File file) {
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();
        OpenFeatureFile openFeatureFile;

        if (repositoryOpenFeatureFile.exists(file)) {
            openFeatureFile = repositoryOpenFeatureFile.getFeatureFile(file);
        } else {
            ArrayList<FileLine> fileLines = new ArrayList<>();
            Scanner scanner;
            try {
                scanner = new Scanner(new FileReader(filePath));
                int countLine = 1;
                fileLines.add(new FileLine(false, fileName, -1));
                fileLines.add(new FileLine(false, "", -1));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    fileLines.add(new FileLine(false, line, countLine));
                    countLine++;
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
        featureFileView.setRowHeight(0, FIRST_ROW_HEIGHT);

        featureFileView.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxCellRenderer(file));
        featureFileView.getColumnModel().getColumn(0).setCellEditor(new CheckBoxEditor(new JCheckBox(), file));
        featureFileView.getColumnModel().getColumn(1).setCellRenderer(new FileLineRenderer(file));
        featureFileView.getTableHeader().setUI(null);
    }

    private void initCenterPanel() {
        featureFileView = new FeatureFileView();
        featureFileView.setShowGrid(false);
        featureFileView.getTableHeader().setResizingAllowed(false);
        featureFileView.getTableHeader().setReorderingAllowed(false);
        featureFileView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        featureFileView.setDragEnabled(false);
        centerPanel.add(new JScrollPane(featureFileView), BorderLayout.CENTER);
    }

    private void initTable() {
        table = new JBTable();
        table.setShowGrid(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(true);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton removeScenarioBtn = new JButton("Remove");
        removeScenarioBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ArrayList<TestRow> testRowsChecked = new ArrayList<>();

                // catch all rows checked
                for (int r = 1; r < tableModel.getRowCount(); r++) {
                    if ((boolean) tableModel.getValueAt(r, 0)) {
                        String test = (String) tableModel.getValueAt(r, 1);
                        TestRow testRow = tableModel.findTestRow(test);
                        testRowsChecked.add(testRow);
                    }
                }
                // remove all rows checked
                for (TestRow t : testRowsChecked) {
                    tableModel.removeRow(t);
                    tableModel.getRow(0).setCheckbox(false);
                    OpenFeatureFile openFeatureFile = repositoryOpenFeatureFile.getFeatureFile(t.getFile());
                    int deselectedLine = openFeatureFile.deselectLine(t.getTest());
                    featureFileViewModel.fireTableDataChanged();

                    scenarios.remove(new ScenarioTestInformation(t.getFile().getAbsolutePath(), deselectedLine));
                }
            }
        });

        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.add(removeScenarioBtn, BorderLayout.EAST);
        tablePanel.add(btnPanel, BorderLayout.NORTH);

        tableModel = new TestsTableModel();
        table.setModel(tableModel);

        tableModel.addRow(new TestRow(null, false, "Tests"));
        table.setRowHeight(0, FIRST_ROW_HEIGHT);
        // TODO: deixar tamanho da tabela dinâmica
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(0).setCellRenderer(new TestsTableRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new TestsTableRenderer());
        table.getTableHeader().setUI(null);
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

    //TODO: aumentar tamanho dos textfields
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

//        Border blackline = BorderFactory.createLineBorder(JBColor.border());
//        TitledBorder border = BorderFactory.createTitledBorder(blackline, "Task Identification");
//        border.setTitleJustification(TitledBorder.CENTER);
//
//        this.inputPanel.setBorder(border);
    }

    //TODO: deixar mais responsivo
    private void configurePanels() {
        mainPanel = new JPanel();
        treePanel = new JPanel();
        centerPanel = new JPanel();
        rightPanel = new JPanel();
        inputPanel = new JPanel();
        tablePanel = new JPanel();

        mainPanel.setLayout(null);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treePanel, centerPanel);
        splitPane.setDividerLocation(300);
        rightPanel.setLayout(new BorderLayout(0, 50));

        treePanel.setLayout(new BorderLayout());
        centerPanel.setLayout(new BorderLayout());

        inputPanel.setLayout(new GridLayout(3, 2, 5, 50));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        splitPane.setBounds(0, 0, 900, 700);
        rightPanel.setBounds(900, 0, 300, 700);

        mainPanel.add(splitPane);
        mainPanel.add(rightPanel);
        rightPanel.add(inputPanel, BorderLayout.NORTH);
        rightPanel.add(tablePanel, BorderLayout.CENTER);
    }
}
