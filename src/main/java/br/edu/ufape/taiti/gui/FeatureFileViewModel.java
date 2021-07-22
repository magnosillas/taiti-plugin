package br.edu.ufape.taiti.gui;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;

public class FeatureFileViewModel extends AbstractTableModel {

    private String[] columns;
    private ArrayList<FileLine> rows;
    private ArrayList<ScenarioTestInformaiton> scenarios;
    private TestsTableModel tableModel;
    private File file;

    public FeatureFileViewModel(File file, ArrayList<FileLine> rows, ArrayList<ScenarioTestInformaiton> scenarios, TestsTableModel tableModel) {
        this.file = file;
        this.rows = rows;
        this.scenarios = scenarios;
        this.tableModel = tableModel;
        columns = new String[]{"", this.file.getName()};
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
        String line = fileLine.getLine().strip();
        if (columnIndex == 0 && line.startsWith("Scenario")) {
            if (!fileLine.getCheckbox()) {
                fileLine.setCheckbox(true);
                scenarios.add(new ScenarioTestInformaiton(this.file.getPath(), fileLine.getLineNumber()));
                tableModel.addRow(new TestRow(false, line));
            } else {
                fileLine.setCheckbox(false);
                scenarios.remove(new ScenarioTestInformaiton(this.file.getPath(), fileLine.getLineNumber()));
                tableModel.removeRow(new TestRow(false, line));
            }

        } else if (columnIndex == 0 && rowIndex == 0 && line.equals(file.getName())) {
            if (!fileLine.getCheckbox()) {
                fileLine.setCheckbox(true);
                for (int r = 2; r < getRowCount(); r++) {
                    if (!rows.get(r).getCheckbox()) {
                        setValueAt("", r, 0);
                    }
                }
            } else {
                fileLine.setCheckbox(false);
                for (int r = 2; r < getRowCount(); r++) {
                    if (rows.get(r).getCheckbox()) {
                        setValueAt("", r, 0);
                    }
                }
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
