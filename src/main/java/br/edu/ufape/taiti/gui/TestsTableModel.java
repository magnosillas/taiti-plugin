package br.edu.ufape.taiti.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TestsTableModel extends AbstractTableModel {

    private final String[] columns;
    private ArrayList<RowTest> rows;

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
        RowTest rowTest = rows.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = rowTest.getCheckbox();
                break;
            case 1:
                value = rowTest.getTest();
                break;
            default:
                System.err.println("elemento inválido na tabela");
        }

        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        RowTest rowTest = rows.get(rowIndex);
        switch (columnIndex) {
            case 0:
                if (!rowTest.getCheckbox()) {
                    rowTest.setCheckbox(true);
                } else {
                    rowTest.setCheckbox(false);
                }
                break;
            case 1:
                rowTest.setTest(aValue.toString());
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

    public void addRow(RowTest row) {
        rows.add(row);
        fireTableDataChanged();
    }

    public void removeRow(RowTest row) {
        rows.remove(row);
        fireTableDataChanged();
    }
}
