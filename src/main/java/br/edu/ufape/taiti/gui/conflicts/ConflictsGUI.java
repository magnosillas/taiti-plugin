package br.edu.ufape.taiti.gui.conflicts;

import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.service.Stories;
import br.edu.ufape.taiti.service.Task;
import br.edu.ufape.taiti.settings.TaitiSettingsState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class ConflictsGUI {
    private static DefaultTableModel modeloTabela;

    private JPanel ConflictsPanel;
    private JTable ShowTable;
    private PivotalTracker pivotalTracker;
    private static Project project;
    private DefaultTableModel modeloaTabela;


    public ConflictsGUI(ToolWindow toolWindow, Project project){
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.retrieveStoredCredentials(project);
        pivotalTracker = new PivotalTracker(settings.getToken(), settings.getPivotalURL(), project);
        this.project = project;
        createTable();

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
                new String[]{"Task ID", "Description", "URL", "Conflict Risk Rate", "Conflict Files"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tornar todas as células não editáveis
            }
        };


        ShowTable.setModel(modeloTabela);
        TableColumnModel columns = ShowTable.getColumnModel();
        columns.getColumn(0).setMinWidth(100);
        columns.getColumn(1).setMinWidth(250);
        columns.getColumn(2).setMinWidth(260);
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





    }

    static public void fillTable(Task task){
        modeloTabela.setRowCount(0);


        ArrayList<Task> conflictTasks = task.getConflictTasks();
        if(conflictTasks.size() > 0 ){


            ArrayList<ArrayList<Object[]>> conflictScenarios = task.getConflictScenarios();

            for (int i = 0; i < conflictTasks.size(); i++) {
                int taskId = conflictTasks.get(i).getId();
                String taskDescription = conflictTasks.get(i).getName();
                String taskUrl = conflictTasks.get(i).getUrl();
                ArrayList<Object[]> conflictTaskScenarios = conflictScenarios.get(i);
                String stringConflicts = "";
                int conflictRisk = 0;
                for( Object[] scenarios : conflictTaskScenarios){
                    String path = (String)scenarios[0];

                    path = path.substring(project.getBasePath().length());
                    String linesNum = "";
                    for (int num : (ArrayList<Integer>)scenarios[1]) {
                        linesNum += num + ",";
                        conflictRisk ++;
                    }


                    linesNum = "[" + linesNum.substring(0, linesNum.length() - 1) + "]";
                    stringConflicts += path + "; " + linesNum + "\n";

                }

                modeloTabela.addRow(new Object[]{taskId, taskDescription,taskUrl,conflictRisk,stringConflicts});



            }
        }else{

        }


    }




    public JPanel getContent() {
        return ConflictsPanel;
    }
}
