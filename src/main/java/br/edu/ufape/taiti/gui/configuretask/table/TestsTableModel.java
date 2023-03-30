package br.edu.ufape.taiti.gui.configuretask.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TestsTableModel extends AbstractTableModel {

    private String[] columns;
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
        }

        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        TestRow testRow = rows.get(rowIndex);

        if (columnIndex == 0 && rowIndex == 0) {
            if (!testRow.getCheckbox()) {
                testRow.setCheckbox(true);
                /*starts in second row (r = 1) because the first row is the header of the table*/
                for (int r = 1; r < getRowCount(); r++) {
                    if (!rows.get(r).getCheckbox()) {
                        setValueAt(null, r, 0);
                    }
                }
            } else {
                testRow.setCheckbox(false);
                for (int r = 1; r < getRowCount(); r++) {
                    if (rows.get(r).getCheckbox()) {
                        setValueAt(null, r, 0);
                    }
                }
            }

        } else if (columnIndex == 0) {
            if (!testRow.getCheckbox()) {
                testRow.setCheckbox(true);
            } else {
                testRow.setCheckbox(false);
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

    public void addRow(TestRow row) {
        rows.add(row);
        fireTableDataChanged();
    }

    public void removeRow(TestRow row) {
        rows.remove(row);
        fireTableDataChanged();
    }

    public TestRow findTestRow(String test) {
        for (TestRow t : rows) {
            if (t.getTest().equals(test)) {
                return t;
            }
        }

        return null;
    }

    public TestRow getRow(int row) {
        return rows.get(row);
    }
}