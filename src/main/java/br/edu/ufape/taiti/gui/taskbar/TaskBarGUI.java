package br.edu.ufape.taiti.gui.taskbar;


import br.edu.ufape.taiti.gui.TaitiDialog;
import br.edu.ufape.taiti.gui.conflicts.ConflictsGUI;
import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.service.Stories;
import br.edu.ufape.taiti.service.Task;
import br.edu.ufape.taiti.settings.TaitiSettingsState;
import br.ufpe.cin.tan.conflict.ConflictAnalyzer;
import br.ufpe.cin.tan.conflict.PlannedTask;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.JBColor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TaskBarGUI {


    private final DefaultTableModel modelo1;
    private final DefaultTableModel modelo2;
    private JPanel TaskBar;
    private JPanel buttonsPanel;
    private JButton refreshButton;
    private JButton addButton;
    private JTextField txtSearch;

    private JTable unstartedTable;
    private JTable startedTable;

    private final ArrayList<Task> storysList1;
    private final ArrayList<Task> storysList2;

    private final Project project;

    public TaskBarGUI(ToolWindow toolWindow, Project project) {

        this.project=project;


        addPlaceHolderStyle(txtSearch);
        storysList1 = new ArrayList<>();
        storysList2 = new ArrayList<>();

        modelo1 = new DefaultTableModel(null,new String[]{"<html><b>My unstarted tasks</b></html>", "<html><b>Scenarios</b></html>"}){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tornar todas as células não editáveis
            }
        };
        unstartedTable.setModel(modelo1);

        // Definir a largura da segunda coluna como 20 pixels
        unstartedTable.getColumnModel().getColumn(1).setMaxWidth(100);

        //centralizar os numeros de Scenarios
        TableColumnModel columns = unstartedTable.getColumnModel();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(1).setCellRenderer(centerRenderer);

        modelo2 = new DefaultTableModel(null,new String[]{"<html><b>Potential conflict-inducing tasks</b></html>"}){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tornar todas as células não editáveis
            }
        };
        startedTable.setModel(modelo2);


        configTaskList();

        refreshButton.addActionListener(e -> {
            // Crie a tela de loading
            LoadingScreen loadingScreen = new LoadingScreen(getContent());
            loadingScreen.setVisible(true);

            // Inicie a operação de atualização (simulada aqui com um atraso de 3 segundos)
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Coloque sua lógica de atualização aqui
                    configTaskList();
                    // Simula uma operação demorada
                    loadingScreen.dispose();
                    return null;
                }


            };

            worker.execute();
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo1);
                unstartedTable.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter(txtSearch.getText()));

                TableRowSorter<DefaultTableModel> sorter2 = new TableRowSorter<>(modelo2);
                startedTable.setRowSorter(sorter2);
                sorter2.setRowFilter(RowFilter.regexFilter(txtSearch.getText()));

            }
        });

        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);

                txtSearch.setText(null);
                txtSearch.requestFocus();
                //remove placeholder style
                removePlaceHolderStyle(txtSearch);

            }
        });
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(txtSearch.getText().length()==0){
                    addPlaceHolderStyle(txtSearch);
                    txtSearch.setText("Search");
                }
            }
        });

        /*
         * Este método pega a referência ao projeto atualmente aberto no IntelliJ
         * e cria um objeto TaitiDialog, responsável por mostrar a janela da aplicação.
         */
        addButton.addActionListener(e -> {


            TaitiDialog taitiDialog = new TaitiDialog(project, this);
            taitiDialog.show();
        });


        /*
         * Essa parte é responsável pelo Refresh de tempo em tempo
         */

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> configTaskList(), 0, 5, TimeUnit.MINUTES);


        unstartedTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = unstartedTable.rowAtPoint(e.getPoint());
                int column = unstartedTable.columnAtPoint(e.getPoint());
                if (row > -1 && column > -1) {
                    unstartedTable.setToolTipText("<html>" + storysList1.get(row).getStoryName() +
                            "<br>TaskID: #" + storysList1.get(row).getId()+
                            "<br>Owner: "+ storysList1.get(row).getPersonName() +"</html>");
                } else {
                    unstartedTable.setToolTipText(null);
                }

            }
        });

        startedTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = startedTable.rowAtPoint(e.getPoint());
                int column = startedTable.columnAtPoint(e.getPoint());
                if (row > -1 && column > -1) {
                    startedTable.setToolTipText("<html>" + storysList2.get(row).getStoryName() +
                            "<br>TaskID: #" + storysList2.get(row).getId()+
                            "<br>Owner: "+ storysList2.get(row).getPersonName() +"</html>");
                } else {
                    startedTable.setToolTipText(null);
                }

            }
        });
        unstartedTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) { // Verifica se foi um clique duplo
                    int index = unstartedTable.getSelectedRow(); // Obtém o índice da linha selecionada
                    Task task = storysList1.get(index);


                    task.checkConflictRisk(storysList2);


                    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
                    ToolWindow myToolWindow = toolWindowManager.getToolWindow("Conflicts");

                    String text = "Conflict table for task " + task.getName() + " which contains "
                            + (int) task.getConflictRate() + " absolute conflicts.";


                    ConflictsGUI.setLabel(text);
                    ConflictsGUI.fillTable(task);

                    if (myToolWindow != null) {
                        myToolWindow.show(null);
                    }

                }

            }
        });
    }




    public void addPlaceHolderStyle(JTextField textField){
        Font font = textField.getFont();
        font = font.deriveFont(Font.ITALIC);
        textField.setFont(font);
        textField.setForeground(JBColor.GRAY); //PlaceHolder font color
    }

    public void removePlaceHolderStyle(JTextField textField){
        Font font = textField.getFont();
        font = font.deriveFont(Font.PLAIN);
        textField.setFont(font);
        textField.setForeground(JBColor.LIGHT_GRAY); //PlaceHolder font color
    }

    public void configTaskList(){
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.retrieveStoredCredentials(project);
        PivotalTracker pivotalTracker = new PivotalTracker(settings.getToken(), settings.getPivotalURL(), project);

        if( pivotalTracker.checkStatus() == 200) {

            /**
             * Primeiramente esvazio o array que contem as tasks para preenche-lo novamente com as informações mais recentes
             */

            Stories plannedStories = new Stories(pivotalTracker, project, settings.getGithubURL());
            plannedStories.clearLists();
            plannedStories.startList();
            limparListas();

//            ConflictAnalyzer conflictAnalyzer = new ConflictAnalyzer();
//            ArrayList<PlannedTask> plannedTaskArrayList = new ArrayList<>();
//            for(Task unstartedTask : plannedStories.getUnstartedStories()){
//                plannedTaskArrayList.add(unstartedTask.getiTesk());
//            }
//            for(Task startedTask : plannedStories.getStartedStories()){
//                startedTask.setConflictRate(conflictAnalyzer.meanRelativeConflictRiskForTasks(startedTask.getiTesk(),plannedTaskArrayList));
//            }

            // Add the unstarted stories to the main list first
            atualizarListas(plannedStories.getUnstartedStories(), storysList1, modelo1);

            // Add the started stories to the main list
            atualizarListas2(plannedStories.getStartedStories(), storysList2, modelo2);
        }
    }

    private void limparListas() {

        storysList1.clear();
        storysList2.clear();

        modelo1.setRowCount(0);
        modelo2.setRowCount(0);

    }

    private void atualizarListas(List<Task> Stories, ArrayList<Task> storysList, DefaultTableModel model) {

        for(Task Story : Stories){
            storysList.add(Story);
            String storyName = truncateStoryName(Story.getStoryName());

            ArrayList<LinkedHashMap<String, Serializable>> scenario = Story.getScenarios();
            int sum = 0;

            for (LinkedHashMap<String, Serializable> lines : scenario) { // percorro scenario por scenario

                ArrayList<Integer> number = (ArrayList<Integer>) lines.get("lines");
                sum += number.size();
            }
            model.addRow(new Object[]{storyName, sum});


        }
    }
    private void atualizarListas2(List<Task> Stories, ArrayList<Task> storysList, DefaultTableModel model) {

        for(Task Story : Stories){
            storysList.add(Story);
            String storyName = truncateStoryName(Story.getStoryName());


            model.addRow(new Object[]{storyName});


        }
    }


    // Função para limitar o texto das tasks na TaskList
    private String truncateStoryName(String storyName){
        if (storyName.length() > 50) {
            storyName = String.format("%s...", storyName.substring(0, 50));
        }
        return storyName;
    }



    public ArrayList<Task> getStorysList1() {
        return storysList1;
    }

    public ArrayList<Task> getStorysList2() {
        return storysList2;
    }

    public JPanel getContent() {
        return TaskBar;
    }



}
