package br.edu.ufape.taiti.gui.conflicts;

import br.edu.ufape.taiti.service.Task;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class ConflictsTable {
    private DefaultTableModel modeloTabela;
    private JTable table;

    public ConflictsTable() {
        table = new JTable();

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if (row > -1 && column > -1) {
                    table.setToolTipText("<html>" + modeloTabela.getValueAt(row, 1) +
                            "<br>URL: #" + modeloTabela.getValueAt(row, 2) +
                            "<br>Scenarios: " + modeloTabela.getValueAt(row, 4) + "</html>");
                } else {
                    table.setToolTipText(null);
                }
            }
        });

        modeloTabela = new DefaultTableModel(null,
                new String[]{"Task ID", "Description", "URL", "Absolute Conflict Risk", "Conflict Files"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tornar todas as células não editáveis
            }
        };

        //definindo tamanho das colunas
        table.setModel(modeloTabela);
        TableColumnModel columns = table.getColumnModel();
        columns.getColumn(0).setMinWidth(100);
        columns.getColumn(1).setMinWidth(250);
        columns.getColumn(2).setMinWidth(260);
        columns.getColumn(3).setMinWidth(100);
        columns.getColumn(4).setMinWidth(400);

        table.setRowHeight(20);
        // Centralizando algumas colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);

        // a coluna onde mostra os scenarios path pode ter mais de uma linha por causa disso:
        table.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JTextArea textArea = new JTextArea();
            textArea.setText((String) value);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            return textArea;
        });
    }

    public void fillTable(Task task, Project project) {
        modeloTabela.setRowCount(0);

        ArrayList<Task> conflictTasks = task.getConflictTasks();

        ArrayList<LinkedHashMap<String, Serializable>> conflictScenarios = task.getConflictScenarios();

        for (int i = 0; i < conflictTasks.size(); i++) {
            int taskId = conflictTasks.get(i).getId();
            String taskDescription = conflictTasks.get(i).getName();
            String taskUrl = conflictTasks.get(i).getUrl();
            LinkedHashMap<String, Serializable> conflictTaskScenarios = conflictScenarios.get(i);
            String stringConflicts = "";
            int conflictRisk = 0;

            String path = (String) conflictTaskScenarios.get("path");

            path = path.substring(Objects.requireNonNull(project.getBasePath()).length());
            String linesNum = "";
            for (int num : (ArrayList<Integer>) conflictTaskScenarios.get("lines")) {
                linesNum += num + ",";
                conflictRisk++;
            }


            linesNum = "[" + linesNum.substring(0, linesNum.length() - 1) + "]";
            stringConflicts += path + "; " + linesNum + "\n";

            modeloTabela.addRow(new Object[]{taskId, taskDescription, taskUrl, conflictRisk, stringConflicts});




        }
    }

    public JTable getTable() {
        return table;
    }
}
