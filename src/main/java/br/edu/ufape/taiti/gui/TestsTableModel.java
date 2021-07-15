package br.edu.ufape.taiti.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TestsTableModel extends AbstractTableModel {

    private final String[] columns;
    private ArrayList<TestRow> rows;

    public TestsTableModel() {
        columns = new String[]{"", "Tests"};
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
        switch (columnIndex) {
            case 0:
                value = testRow.getCheckbox();
                break;
            case 1:
                value = testRow.getTest();
                break;
            default:
                System.err.println("elemento inválido na tabela");
        }

        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        TestRow testRow = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                if (!testRow.getCheckbox()) {
                    testRow.setCheckbox(true);
                } else {
                    testRow.setCheckbox(false);
                }
                break;
            case 1:
                testRow.setTest(aValue.toString());
                break;
            default:
                System.err.println("alteração inválida na tabela");
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

    public void addRow(TestRow row) {
        rows.add(row);
        fireTableDataChanged();
    }

    public void removeRow(TestRow row) {
        rows.remove(row);
        fireTableDataChanged();
    }
}
