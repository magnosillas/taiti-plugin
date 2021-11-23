package br.edu.ufape.taiti.gui.configuretask.table;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
                table.getColumnModel().getColumn(0).setPreferredWidth(e.getComponent().getWidth() - 5);
            }
        });
    }

    private void initTable() {
        table = new JBTable();
        table.setShowGrid(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);

        JBScrollPane scrollPane = new JBScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.CENTER);

//        removeScenarioBtn.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                ArrayList<TestRow> testRowsChecked = new ArrayList<>();
//
//                // catch all rows checked
//                for (int r = 1; r < tableModel.getRowCount(); r++) {
//                    if ((boolean) tableModel.getValueAt(r, 0)) {
//                        String test = (String) tableModel.getValueAt(r, 1);
//                        TestRow testRow = tableModel.findTestRow(test);
//                        testRowsChecked.add(testRow);
//                    }
//                }
//                // remove all rows checked
//                tableModel.getRow(0).setCheckbox(false);
//                for (TestRow t : testRowsChecked) {
//                    tableModel.removeRow(t);
//                    OpenFeatureFile openFeatureFile = repositoryOpenFeatureFile.getFeatureFile(t.getFile());
//                    int deselectedLine = openFeatureFile.deselectLine(t.getTest());
//                    featureFileViewModel.fireTableDataChanged();
//
//                    scenarios.remove(new ScenarioTestInformation(t.getFile().getAbsolutePath(), deselectedLine));
//                }
//            }
//        });

        tableModel = new TestsTableModel();
        table.setModel(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(345);
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                TestRow t = ((TestsTableModel) table.getModel()).getRow(row);
                label.setToolTipText(t.getFile().getName());

                return label;
            }
        });
    }
}
