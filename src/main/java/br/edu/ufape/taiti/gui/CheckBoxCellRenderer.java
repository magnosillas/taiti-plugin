package br.edu.ufape.taiti.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CheckBoxCellRenderer implements TableCellRenderer {

    private JCheckBox renderer;

    public CheckBoxCellRenderer() {
        renderer = new JCheckBox();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String fileLine = (String) table.getModel().getValueAt(row, 1);
        if (fileLine.strip().startsWith("Scenario")) {
            Color bg = isSelected ? table.getSelectionBackground() : table.getBackground();
            renderer.setBackground(bg);
            renderer.setEnabled(true);
            renderer.setVisible(true);
            renderer.setSelected(value != null && (Boolean)value);
        } else {
            return new JLabel("");
        }

        return renderer;
    }
}
