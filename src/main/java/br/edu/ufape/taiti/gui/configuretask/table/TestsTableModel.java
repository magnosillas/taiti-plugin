package br.edu.ufape.taiti.gui.configuretask.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TestsTableModel extends AbstractTableModel {

    private String[] columns;
    private ArrayList<TestRow> rows;

    public TestsTableModel() {
        columns = new String[]{"Tests"};
        rows = new ArrayList<>();
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
        TestRow testRow = rows.get(rowIndex);
        Object value = null;
        if (columnIndex == 0) {
            value = testRow;
        }

        return value;
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
        return false;
    }

    public void addRow(TestRow row) {
        rows.add(row);
        fireTableDataChanged();
    }

    public void removeRow(TestRow row) {
        rows.remove(row);
        fireTableDataChanged();
    }
}
