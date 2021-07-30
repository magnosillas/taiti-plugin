package br.edu.ufape.taiti.gui.fileview;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;

import static br.edu.ufape.taiti.gui.Constants.FIRST_ROW_HEIGHT;

public class FileLineRenderer extends DefaultTableCellRenderer {

    private File file;

    public FileLineRenderer(File file) {
        this.file = file;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c;
        String fileLine = (String) table.getModel().getValueAt(row, 1);
        if (fileLine.strip().equals(file.getName()) && row == 0) {
            table.setRowHeight(0, FIRST_ROW_HEIGHT);
            c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(table.getTableHeader().getBackground());
        } else {
            c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Color bg = isSelected ? table.getSelectionBackground() : table.getBackground();
            c.setBackground(bg);
        }
        return c;
    }

}
