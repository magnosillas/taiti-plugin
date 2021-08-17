package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.gui.fileview.*;
import br.edu.ufape.taiti.gui.tree.TaitiTree;
import br.edu.ufape.taiti.gui.tree.TaitiTreeFileNode;
import br.edu.ufape.taiti.gui.table.TestRow;
import br.edu.ufape.taiti.gui.table.TestsTableModel;
import br.edu.ufape.taiti.gui.table.TestsTableRenderer;
import br.edu.ufape.taiti.tool.ScenarioTestInformation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;

import javax.swing.*;
import javax.swing.border.Border;
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
    private JPanel rootPanel;
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

    private final Project project;

    public MainPanel(Project project) {
        this.project = project;
        scenarios = new ArrayList<>();
        repositoryOpenFeatureFile = new RepositoryOpenFeatureFile();

        configurePanels();
        configureTree();
        configureInputPanel();
        initTable();
        initCenterPanel();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public ArrayList<ScenarioTestInformation> getScenarios() {
        return scenarios;
    }

    public JTextField getTextGithubURL() {
        return textGithubURL;
    }

    public JTextField getTextPivotalURL() {
        return textPivotalURL;
    }

    public JTextField getTextTaskID() {
        return textTaskID;
    }

    public JBTable getTable() {
        return table;
    }

    private void updateCenterPanel(File file) {
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
        featureFileView.setRowHeight(0, 30);

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

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new JScrollPane(featureFileView), BorderLayout.CENTER);
    }

    private void initTable() {
        table = new JBTable();
        table.setShowGrid(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(true);

        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
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
        table.setRowHeight(0, 30);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(0).setCellRenderer(new TestsTableRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new TestsTableRenderer());
        table.getTableHeader().setUI(null);
    }

    private void configureTree() {
        String projectPath = "";
        String projectName = "";

        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir != null) {
            projectPath = projectDir.getPath();
            projectName = projectDir.getName();
        }

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(projectName);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

        tree = new TaitiTree(treeModel);
        treePanel.setLayout(new BorderLayout());
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
        if (featureDirectory != null) {

            // get every parent directory of features directory
            File parent = featureDirectory.getParentFile();
            ArrayList<DefaultMutableTreeNode> parentsNodes = new ArrayList<>();
            while (!parent.getName().equals(projectName)) {
                parentsNodes.add(new DefaultMutableTreeNode(parent.getName()));
                parent = parent.getParentFile();
            }

            // add every parent directory of feature directory to the tree
            tree.addParentsNodeToTree(parentsNodes, rootNode, parentsNodes.size() - 1);

            // add the feature directory to the tree
            DefaultMutableTreeNode featureNode = new DefaultMutableTreeNode(featureDirectory.getName());
            if (parentsNodes.size() > 0) {
                parentsNodes.get(0).add(featureNode);
            } else {
                rootNode.add(featureNode);
            }

            // populating the tree with the files into feature directory
            tree.addNodesToTree(featureDirectory.getAbsolutePath(), featureNode);
            treePanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        } else {
            JLabel label = new JLabel("Could not find feature directory");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setForeground(JBColor.gray);
            treePanel.add(label, BorderLayout.CENTER);
        }
    }

    private void configureInputPanel() {
        JLabel title = new JLabel("Task Identification");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font(null, Font.PLAIN, 15));

        labelGithubURL = new JLabel("GitHub Project URL:");
        textGithubURL = new JTextField();

        labelPivotalURL = new JLabel("PivotalTracker Project URL:");
        textPivotalURL = new JTextField();

        labelTaskID = new JLabel("Task ID:");
        textTaskID = new JTextField();

        JPanel panel = new JPanel(new GridBagLayout());
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(title, BorderLayout.NORTH);
        inputPanel.add(panel, BorderLayout.CENTER);

        GridBag gb = new GridBag()
                .setDefaultInsets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultPaddingX(0, -25);

        panel.add(labelGithubURL, gb.nextLine().next().weightx(0.2));
        panel.add(textGithubURL, gb.next().weightx(0.8));

        panel.add(labelPivotalURL, gb.nextLine().next().weightx(0.2));
        panel.add(textPivotalURL, gb.next().weightx(0.8));

        panel.add(labelTaskID, gb.nextLine().next().weightx(0.2));
        panel.add(textTaskID, gb.next().weightx(0.6));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        Border border = BorderFactory.createCompoundBorder
                (BorderFactory.createMatteBorder(0, 0, 1, 0, JBColor.border()),
                BorderFactory.createEmptyBorder(0, 10, 30, 0));

        inputPanel.setBorder(border);
    }

    private void configurePanels() {
        rootPanel = new JPanel();
        treePanel = new JPanel();
        centerPanel = new JPanel();
        rightPanel = new JPanel();
        inputPanel = new JPanel();
        tablePanel = new JPanel();

        rootPanel.setLayout(new FlowLayout());
        rootPanel.setMinimumSize(new Dimension(1300, 720));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treePanel, centerPanel);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(4);
        splitPane.setPreferredSize(new Dimension(900, 700));
        splitPane.setMinimumSize(new Dimension(900, 700));

        rightPanel.setLayout(new BorderLayout(0, 50));
        rightPanel.setPreferredSize(new Dimension(390, 700));
        rightPanel.setMinimumSize(new Dimension(390, 700));

        rootPanel.add(splitPane);
        rootPanel.add(rightPanel);
        rightPanel.add(inputPanel, BorderLayout.NORTH);
        rightPanel.add(tablePanel, BorderLayout.CENTER);
    }
}
