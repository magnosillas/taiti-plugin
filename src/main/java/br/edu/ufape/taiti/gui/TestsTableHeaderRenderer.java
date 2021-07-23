package br.edu.ufape.taiti.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TestsTableHeaderRenderer extends DefaultTableCellRenderer {

    private JCheckBox checkBox;

    public TestsTableHeaderRenderer() {
        this.checkBox = new JCheckBox();
        this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);;
        if (row == 0 && column == 0) {
            Color bg = table.getTableHeader().getBackground();
            checkBox.setBackground(bg);
            checkBox.setEnabled(true);
            checkBox.setVisible(true);
            checkBox.setSelected(value != null && (Boolean)value);
        } else if (row == 0 && column == 1) {
            c.setBackground(table.getTableHeader().getBackground());
        } else if (column == 0) {
            Color bg = isSelected ? table.getSelectionBackground() : table.getBackground();
            checkBox.setBackground(bg);
            checkBox.setEnabled(true);
            checkBox.setVisible(true);
            checkBox.setSelected(value != null && (Boolean)value);
        } else {
            Color bg = isSelected ? table.getSelectionBackground() : table.getBackground();
            c.setBackground(bg);
        }

        if (column == 0) {
            return this.checkBox;
        } else {
            return c;
        }

    }

}
