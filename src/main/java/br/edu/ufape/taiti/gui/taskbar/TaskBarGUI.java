package br.edu.ufape.taiti.gui.taskbar;



import br.edu.ufape.taiti.gui.TaitiDialog;

import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.service.Stories;
import br.edu.ufape.taiti.service.Task;
import br.edu.ufape.taiti.settings.TaitiSettingsState;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import kong.unirest.json.JSONArray;


import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class TaskBarGUI {


    private JPanel TaskBar;
    private JPanel buttonsPanel;
    private JButton refreshButton;
    private JButton addButton;
    private JTextField txtSearch;
    private JList<String> unstartedList;
    private JList<String> startedList;
    private final ArrayList<String> list1;
    private final ArrayList<String> list2;
    private final DefaultListModel<String> listUnstartedModel;
    private final DefaultListModel<String> listStartedModel;
    private final ArrayList<Task> storysList1;
    private final ArrayList<Task> storysList2;
    private JSONArray files;
    private Project project;

    public TaskBarGUI(ToolWindow toolWindow, Project project) {

        this.project=project;

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        listUnstartedModel = new DefaultListModel<>();
        unstartedList.setModel(listUnstartedModel);
        listStartedModel = new DefaultListModel<>();
        startedList.setModel(listStartedModel);
        addPlaceHolderStyle(txtSearch);
        storysList1 = new ArrayList<>();
        storysList2 = new ArrayList<>();


        //Inicializa um objeto PivotalTracker para busca de dados
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.retrieveStoredCredentials(project);
        PivotalTracker pivotalTracker = new PivotalTracker(settings.getToken(), settings.getPivotalURL(), project);

        configTaskList(pivotalTracker);

        refreshButton.addActionListener(e -> configTaskList(pivotalTracker));

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                listUnstartedModel.clear();
                listUnstartedModel.addAll(searchList(txtSearch.getText(), list1));
                listStartedModel.clear();
                listStartedModel.addAll(searchList(txtSearch.getText(), list2));
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


        unstartedList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                JList l = (JList)e.getSource();

                int index = l.locationToIndex(e.getPoint());
                if( index>-1 ) {
                    /*String que fica na tooltip da tasklist **/
                    l.setToolTipText("<html>" + storysList1.get(index).getStoryName() +
                                    "<br>TaskID: #" + storysList1.get(index).getId()+
                                    "<br>Owner: "+ storysList1.get(index).getPersonName() +"</html>");
                }
            }
        });
        startedList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                JList l = (JList)e.getSource();

                int index = l.locationToIndex(e.getPoint());
                if( index>-1 ) {
                    /*String que fica na tooltip da tasklist **/
                    l.setToolTipText("<html>" + storysList2.get(index).getStoryName() +
                            "<br>TaskID: #" + storysList2.get(index).getId()+
                            "<br>Owner: "+ storysList2.get(index).getPersonName() +"</html>");
                }
            }
        });
        /*
         * Essa parte é responsável pelo Refresh de tempo em tempo
         */

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> configTaskList(pivotalTracker), 0, 1, TimeUnit.MINUTES);

    }


    // Função para dar search na lista de strings
    private List<String> searchList(String searchWords, List<String> listOfStrings) {
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));
        return listOfStrings.stream().filter(input -> searchWordsArray.stream().allMatch(word ->
                input.toLowerCase().contains(word.toLowerCase()))).collect(Collectors.toList());
    }

    public void addPlaceHolderStyle(JTextField textField){
        Font font = textField.getFont();
        font = font.deriveFont(Font.ITALIC);
        textField.setFont(font);
        textField.setForeground(Color.GRAY); //PlaceHolder font color
    }

    public void removePlaceHolderStyle(JTextField textField){
        Font font = textField.getFont();
        font = font.deriveFont(Font.PLAIN);
        textField.setFont(font);
        textField.setForeground(Color.lightGray); //PlaceHolder font color
    }

    public void configTaskList(PivotalTracker pivotalTracker){

        /**
         * Primeiramente esvazio o array que contem as tasks para preenche-lo novamente com as informações mais recentes
         */

            Stories plannedStories = new Stories(pivotalTracker, project);
            plannedStories.clearLists();
            plannedStories.startList();
            listUnstartedModel.removeAllElements();
            listStartedModel.removeAllElements();
            list1.clear();
            list2.clear();
            storysList1.clear();

            // Add the unstarted stories to the main list first
            for(Task unstartedStory : plannedStories.getUnstartedStories()){
                storysList1.add(unstartedStory);
                String storyName = truncateStoryName(unstartedStory.getStoryName());
                addListElement(storyName,list1,listUnstartedModel );
            }

            // Add the started stories to the main list
            for(Task startedStory : plannedStories.getStartedStories()){
                storysList2.add(startedStory);
                String storyName = truncateStoryName(startedStory.getStoryName());
                addListElement(storyName,list2,listStartedModel);
            }
    }
 // Função para limitar o texto das tasks na TaskList
    private String truncateStoryName(String storyName){
        if (storyName.length() > 50) {
            storyName = String.format("%s...", storyName.substring(0, 50));
        }
        return storyName;
    }

    public void addListElement(String task, ArrayList<String> list, DefaultListModel<String> model){
        list.add(task);
        model.removeAllElements();
        for (String p : list) {
            model.addElement(p);
        }
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
