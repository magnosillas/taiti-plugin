package br.edu.ufape.taiti.gui;

import javax.swing.*;
import java.awt.*;

public class CheckBoxEditor extends DefaultCellEditor {

    public CheckBoxEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String fileLine = (String) table.getModel().getValueAt(row, 1);
        if (fileLine.strip().startsWith("Scenario")) {
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
        return null;
    }

}
