package br.edu.ufape.taiti.gui;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;

public class FeatureFileViewModel extends AbstractTableModel {

    private String[] columns;
    private ArrayList<FileLine> rows;
    private ArrayList<String> scenarios;
    private TestsTableModel tableModel;

    public FeatureFileViewModel(File file, ArrayList<FileLine> rows, ArrayList<String> scenarios, TestsTableModel tableModel) {
        columns = new String[]{"", file.getName()};
        this.rows = rows;
        this.scenarios = scenarios;
        this.tableModel = tableModel;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FileLine fileLine = rows.get(rowIndex);
        Object value = null;

        if (columnIndex == 0) {
            value = fileLine.getCheckbox();
        } else if (columnIndex == 1) {
            value = fileLine.getLine();
        } else {
            System.err.println("elemento inválido na tabela");
        }

        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        FileLine fileLine = rows.get(rowIndex);
        String line = fileLine.getLine();
        if (columnIndex == 0 && line.strip().startsWith("Scenario")) {
            if (!fileLine.getCheckbox()) {
                fileLine.setCheckbox(true);
                scenarios.add(line);
                tableModel.addRow(new TestRow(false, line));
            } else {
                fileLine.setCheckbox(false);
                scenarios.remove(line);
                tableModel.removeRow(new TestRow(false, line));
            }
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }


}
