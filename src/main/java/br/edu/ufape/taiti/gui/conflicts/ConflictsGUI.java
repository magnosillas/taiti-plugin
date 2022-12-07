package br.edu.ufape.taiti.gui.conflicts;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class ConflictsGUI {
    private JPanel ConflictsPanel;
    private JTable ShowTable;

    public ConflictsGUI(ToolWindow toolWindow, Project project){

        createTable();

    }

    private void createTable() {

        Object[][] data = { {"#754924836","Como Nutricionista eu quero fazer Login no sistema para cadastrar e acompanhar meus pacientes",
                            "https://www.pivotaltracker.com/story/show/183924843",2,null},
                            {"#183924836","Ele Gosta","https://www.pivotaltracker.com/story/show/183924836",4,null}

                            };
        ShowTable.setModel(new DefaultTableModel(

                data,
                new String[]{"Task ID", "Description","URL", "Conflict Risk", "Conflict Tasks"}));
        TableColumnModel columns = ShowTable.getColumnModel();
        columns.getColumn(1).setMinWidth(300);
        columns.getColumn(2).setMinWidth(300);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
        columns.getColumn(4).setCellRenderer(centerRenderer);





    }




    public JPanel getContent() {
        return ConflictsPanel;
    }
}
