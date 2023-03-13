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
import javax.swing.table.TableColumnModel;
import java.util.List;

public class ConflictsGUI {
    private JPanel ConflictsPanel;
    private JTable ShowTable;
    private PivotalTracker pivotalTracker;
    private Project project;

    public ConflictsGUI(ToolWindow toolWindow, Project project){
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.retrieveStoredCredentials(project);
        pivotalTracker = new PivotalTracker(settings.getToken(), settings.getPivotalURL(), project);
        this.project = project;
        createTable();

    }

    private void createTable() {





        Object[][] data = { { "#" + "u646465" ,"Como Nutricionista eu quero fazer Login no sistema para cadastrar e acompanhar meus pacientes",
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
