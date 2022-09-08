package br.edu.ufape.taiti.gui.configuretask.fileview;

import br.edu.ufape.taiti.tool.ScenarioTestInformation;
import br.edu.ufape.taiti.gui.configuretask.table.TestRow;
import br.edu.ufape.taiti.gui.configuretask.table.TestsTableModel;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;

/**
 * Modelo da tabela onde é mostrado o conteúdo do arquivo.
 */
public class FeatureFileViewModel extends AbstractTableModel {

    private String[] columns;
    private ArrayList<FileLine> rows;
    private ArrayList<ScenarioTestInformation> scenarios;
    private TestsTableModel tableModel;
    private File file;

    public FeatureFileViewModel(File file, ArrayList<FileLine> rows, ArrayList<ScenarioTestInformation> scenarios, TestsTableModel tableModel) {
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
        }

        return value;
    }

    /**
     * Cada linha da tabela que é declarado um scenario deve ter um checkbox para selecionar o scenario e toda primeira linha
     * da tabela possui um checkbox onde é possível marcar todos os scenarios de uma vez. Ao selecionar os checkboxs, eles
     * são marcados nessa tabela e também na tabela onde mostra todos os scenarios selecionados.
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        FileLine fileLine = rows.get(rowIndex);
        String line = fileLine.getLine().strip();
        if (columnIndex == 0 && line.startsWith("Scenario")) {
            if (!fileLine.getCheckbox()) {
                fileLine.setCheckbox(true);
                scenarios.add(new ScenarioTestInformation(this.file.getPath(), fileLine.getLineNumber()));
                tableModel.addRow(new TestRow(file, line));
            } else {
                fileLine.setCheckbox(false);
                scenarios.remove(new ScenarioTestInformation(this.file.getPath(), fileLine.getLineNumber()));
                tableModel.removeRow(new TestRow(file, line));
            }

        } else if (columnIndex == 0 && rowIndex == 0 && line.equals(file.getName())) {
            if (!fileLine.getCheckbox()) {
                fileLine.setCheckbox(true);
                // Começa na terceira linha (r = 2), porque a primeira e segunda linha é o nome do arquivo e uma linha em branco, respectivamente.
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
