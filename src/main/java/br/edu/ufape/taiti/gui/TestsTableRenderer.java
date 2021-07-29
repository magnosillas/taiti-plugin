package br.edu.ufape.taiti.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TestsTableHeaderRenderer extends DefaultTableCellRenderer {

    private JCheckBox checkBox;
    private ActionListener actionListener;
    private JPanel btnPanel;

    public TestsTableHeaderRenderer(ActionListener actionListener, JPanel p) {
        this.checkBox = new JCheckBox();
        this.actionListener = actionListener;
        this.btnPanel = p;
        this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JPanel panel = new JPanel(new BorderLayout());
        if (row == 0 && column == 0) {
            Color bg = table.getTableHeader().getBackground();
            checkBox.setBackground(bg);
            checkBox.setEnabled(true);
            checkBox.setVisible(true);
            checkBox.setSelected(value != null && (Boolean)value);
        } else if (row == 0 && column == 1) {
            table.setRowHeight(0, 30);

            JButton btn = (JButton) btnPanel.getComponent(1);
            JLabel label = (JLabel) btnPanel.getComponent(0);


            btn.setVerticalAlignment(SwingConstants.CENTER);
            btn.setBackground(table.getTableHeader().getBackground());

            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setBackground(table.getTableHeader().getBackground());

            btnPanel.setBackground(table.getTableHeader().getBackground());

        } else if (column == 0) {
            Color bg = isSelected ? table.getSelectionBackground() : table.getBackground();
            checkBox.setBackground(bg);
            checkBox.setEnabled(true);
            checkBox.setVisible(true);
            checkBox.setSelected(value != null && (Boolean)value);
        } else {
            Color bg = isSelected ? table.getSelectionBackground() : table.getBackground();
            c.setBackground(bg);
            ArrayList<TestRow> testRows = ((TestsTableModel) table.getModel()).getRows();
            TestRow t = testRows.get(row);
            c.setToolTipText(t.getFile().getName());
        }

        if (column == 0) {
            return this.checkBox;
        } else {
            if (row == 0) {
                return btnPanel;
            }
            return c;
        }

    }

}
