package br.edu.ufape.taiti.gui.configuretask.table;

import br.edu.ufape.taiti.gui.configuretask.fileview.OpenFeatureFile;
import br.edu.ufape.taiti.gui.configuretask.fileview.RepositoryOpenFeatureFile;
import br.edu.ufape.taiti.tool.ScenarioTestInformation;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

/**
 * Classe responsável por mostrar a tabela com todos os scenarios selecionados.
 */
public class TablePanel {

    private JPanel panel;
    private JBTable table;
    private TestsTableModel tableModel;

    public TablePanel() {
        initPanel();
        initTable();
    }

    public JPanel getPanel() {
        return panel;
    }

    public TestsTableModel getTableModel() {
        return tableModel;
    }

    public JBTable getTable() {
        return table;
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMinimumSize(new Dimension(350, 300));

        JLabel label = new JLabel("Review selected scenarios.");
        label.setHorizontalAlignment(JLabel.CENTER);
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        textPanel.add(label);
        panel.add(textPanel, BorderLayout.NORTH);

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                table.getColumnModel().getColumn(0).setPreferredWidth(e.getComponent().getWidth() - 15);
            }
        });
    }

    public void initToolbar(RepositoryOpenFeatureFile repositoryOpenFeatureFile, ArrayList<ScenarioTestInformation> scenarios) {
        ToolbarDecorator toolbar = ToolbarDecorator.createDecorator(table);
        toolbar = toolbar.disableAddAction();
        toolbar = toolbar.setRemoveAction(new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                int[] selectedRows = table.getSelectedRows();
                ArrayList<TestRow> selectedTestRows = new ArrayList<>();

                for (int row : selectedRows) {
                    selectedTestRows.add((TestRow) table.getValueAt(row, 0));
                }
                for (TestRow testRow : selectedTestRows) {
                    OpenFeatureFile openFeatureFile = repositoryOpenFeatureFile.getFeatureFile(testRow.getFile());
                    int deselectedLine = openFeatureFile.deselectLine(testRow.getTest());
                    scenarios.remove(new ScenarioTestInformation(testRow.getFile().getAbsolutePath(), deselectedLine));

                    tableModel.removeRow(testRow);
                    tableModel.fireTableDataChanged();
                }
            }
        });
        JPanel toolbarPanel = toolbar.createPanel();

        panel.add(toolbarPanel, BorderLayout.CENTER);
    }

    private void initTable() {
        table = new JBTable();
        table.setShowGrid(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);

        tableModel = new TestsTableModel();
        table.setModel(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(345);
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                TestRow t = (TestRow) table.getModel().getValueAt(row, 0);
                label.setToolTipText(t.getFile().getName());

                return label;
            }
        });
    }
}
