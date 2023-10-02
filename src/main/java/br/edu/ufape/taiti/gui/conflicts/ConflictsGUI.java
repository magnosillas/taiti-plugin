package br.edu.ufape.taiti.gui.conflicts;

import br.edu.ufape.taiti.service.Task;
import br.ufpe.cin.tan.conflict.ConflictAnalyzer;
import br.ufpe.cin.tan.conflict.PlannedTask;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ConflictsGUI {
    private static DefaultTableModel modeloTabela;

    private JPanel ConflictsPanel;
    private static JPanel labelPanel;
    private JBTable ShowTable;

    private static Project project;



    public ConflictsGUI(ToolWindow toolWindow, Project project){


        this.project = project;
        ConflictsPanel = new JPanel();
        ShowTable = new JBTable();
        labelPanel = new JPanel();
        createTable();
        setLabel("Double-click a task in the TaskList to view its conflicting tasks");

        ShowTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = ShowTable.rowAtPoint(e.getPoint());
                int column = ShowTable.columnAtPoint(e.getPoint());
                if (row > -1 && column > -1) {
                    ShowTable.setToolTipText("<html>" + modeloTabela.getValueAt(row,1) +
                            "<br>URL: #" + modeloTabela.getValueAt(row,2)+
                            "<br>Scenarios: "+ modeloTabela.getValueAt(row,4) +"</html>");
                } else {
                    ShowTable.setToolTipText(null);
                }
            }
        });
    }

    private void createTable() {


        modeloTabela = new DefaultTableModel(null,
                new String[]{"Task ID", "Description", "URL", "Absolute Conflict Rate", "Conflict Files"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tornar todas as células não editáveis
            }
        };
        ShowTable.setModel(modeloTabela);


        TableColumnModel columns = ShowTable.getColumnModel();
        columns.getColumn(0).setMinWidth(80);
        columns.getColumn(1).setMinWidth(200);
        columns.getColumn(2).setMinWidth(230);
        columns.getColumn(3).setMinWidth(100);
        columns.getColumn(4).setMinWidth(400);

        ShowTable.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);


        ShowTable.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea textArea = new JTextArea();
                textArea.setText((String) value);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                return textArea;
            }
        });


        ConflictsPanel.setLayout(new BorderLayout());


        JScrollPane scrollPane = new JBScrollPane(ShowTable);
        if (ConflictsPanel != null) {
            ConflictsPanel.add(labelPanel, BorderLayout.NORTH);
            ConflictsPanel.add(scrollPane, BorderLayout.CENTER);
        }






    }

    static public void fillTable(Task task){
        modeloTabela.setRowCount(0);


        ArrayList<Task> conflictTasks = task.getConflictTasks();
        if(conflictTasks.size() > 0 ){


            ArrayList<LinkedHashMap<String, Serializable>>  conflictScenarios = task.getConflictScenarios();

            for (int i = 0; i < conflictTasks.size(); i++) {

//                PlannedTask conflictITest = conflictTasks.get(i).getiTesk();
//                ConflictAnalyzer conflictAnalyzer = new ConflictAnalyzer();
//                conflictAnalyzer.computeConflictRiskForPair(task.getiTesk(), conflictITest);
//                int conflictFilesNum = conflictAnalyzer.getConflictResult().getConflictingFiles().size();
//                double conflictRate = conflictAnalyzer.getConflictResult().getRelativeConflictRate();


                int taskId = conflictTasks.get(i).getId();
                String taskDescription = conflictTasks.get(i).getName();
                String taskUrl = conflictTasks.get(i).getUrl();
                LinkedHashMap<String, Serializable> conflictTaskScenarios = conflictScenarios.get(i);
                String stringConflicts = "";
                int conflictRisk = 0;

                String path = (String)conflictTaskScenarios.get("path");


                String linesNum = "";
                for (int num : (ArrayList<Integer>)conflictTaskScenarios.get("lines")) {
                    linesNum += num + ",";
                    conflictRisk ++;
                }


                linesNum = "[" + linesNum.substring(0, linesNum.length() - 1) + "]";
                stringConflicts += path + "; " + linesNum + "\n";

                modeloTabela.addRow(new Object[]{taskId, taskDescription,taskUrl,conflictRisk,stringConflicts});

            }





        }


    }

    public static void setLabel(String texto){
        labelPanel.removeAll();
        labelPanel.validate();
        labelPanel.add(new JLabel(texto));
    }


    public JPanel getContent() {
        return ConflictsPanel;
    }
}