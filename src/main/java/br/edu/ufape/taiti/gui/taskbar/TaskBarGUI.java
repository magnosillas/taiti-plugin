package br.edu.ufape.taiti.gui.taskbar;


import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.gui.TaitiDialog;
import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.settings.TaitiSettingsState;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;


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
    private JList<String> tasksList;
    private final ArrayList<String> people;
    private final DefaultListModel<String> listPeopleModel;
    private final ArrayList<Task> storys;

    public TaskBarGUI(ToolWindow toolWindow, Project project) {

        people = new ArrayList<>();
        listPeopleModel = new DefaultListModel<>();
        tasksList.setModel(listPeopleModel);
        addPlaceHolderStyle(txtSearch);
        storys = new ArrayList<>();


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
                listPeopleModel.clear();
                listPeopleModel.addAll(searchList(txtSearch.getText(),people));
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

        /**
         * Este método pega a referência ao projeto atualmente aberto no IntelliJ
         * e cria um objeto TaitiDialog, responsável por mostrar a janela da aplicação.
         */
        addButton.addActionListener(e -> {

            TaitiDialog taitiDialog = new TaitiDialog(project);
            taitiDialog.show();
        });


        tasksList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                JList l = (JList)e.getSource();
                ListModel m = l.getModel();
                int index = l.locationToIndex(e.getPoint());
                if( index>-1 ) {
                    /**String que fica na tooltip da tasklist **/
                    l.setToolTipText("<html>" + storys.get(index).getStoryName() +
                                    "<br>TaskID: #" + storys.get(index).getId()+
                                    "<br>OwnerID: "+ storys.get(index).getOwnerID() +"</html>");
                }
            }
        });
        Runnable drawRunnable = new Runnable() {
            public void run() {
                configTaskList(pivotalTracker);
            }
        };

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(drawRunnable , 0, 1, TimeUnit.MINUTES);
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
        try {

            JSONArray plannedStories = pivotalTracker.getPlannedStories();
            //Primeiro reseto as listas
            people.clear();
            storys.clear();

            //Adiciono minhas tasks que não começaram e que tem o arquivo scenarios
            for(Object obj : plannedStories){
                Task plannedStory = new Task((JSONObject) obj);
                JSONObject taitiComment = pivotalTracker.getTaitiComment(pivotalTracker.getComments(String.valueOf(plannedStory.getId())));
                if(plannedStory.getState().equals("unstarted") && taitiComment.getString("text").equals("[TAITI] Scenarios")){
                    storys.add(plannedStory);
                    addListElement("<html><b>" + plannedStory.getStoryName()  + "</b></html>");
                }
            }

            //Adiciono as outras tasks que não são minhas e que começaram
            for( Object obj : plannedStories){
                Task plannedStory = new Task((JSONObject) obj);
                JSONObject taitiComment = pivotalTracker.getTaitiComment(pivotalTracker.getComments(String.valueOf(plannedStory.getId())));
                if(plannedStory.getState().equals("started") && taitiComment.getString("text").equals("[TAITI] Scenarios")){
                    storys.add(plannedStory);
                    addListElement("<html>" + plannedStory.getStoryName()  + "</html>");
                }
            }
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }

    public void addListElement(String task){
        people.add(task);
        listPeopleModel.removeAllElements();
        for (String p : people) {
            listPeopleModel.addElement(p);
        }


    }


    public JPanel getContent() {
        return TaskBar;
    }


}
