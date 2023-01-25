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
    private int ownerID;
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

        try {
           ownerID = pivotalTracker.getPersonId();
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }

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
        /**
         * Essa parte é responsável pelo Refresh de tempo em tempo
         */

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> configTaskList(pivotalTracker), 0, 60, TimeUnit.SECONDS);

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
            /**
             * Primeiramente esvazio o array que contem as tasks para preenche-lo novamente com as informações mais recentes
             */
            people.clear();
            storys.clear();
            listPeopleModel.removeAllElements();
            List<Task> unstartedStories = new ArrayList<>();
            List<Task> startedStories = new ArrayList<>();
            String scenarios = "";
            for(int i = 0; i < plannedStories.length(); i++){
                JSONObject obj = plannedStories.getJSONObject(i);
                Task plannedStory = new Task(obj);

                JSONObject taitiComment = pivotalTracker.getTaitiComment(pivotalTracker.getComments(String.valueOf(plannedStory.getId())));
                //Seleciono apenas as tasks que contem o arquivo [TAITI] Scenarios, ou seja, que já foram adicionados
                if(taitiComment == null) {
                    scenarios = "banana";
                }else {
                    scenarios = taitiComment.getString("text");
                }
                    if (scenarios.equals("[TAITI] Scenarios")) {
                        //Adiciono a uma lista as minhas tasks que ainda não começaram
                        if (plannedStory.getState().equals("unstarted") && plannedStory.getOwnerID() == ownerID) {
                            unstartedStories.add(plannedStory);
                        }
                        //Adiciono a uma lista as tasks que já começaram de outros membros
                        else if (plannedStory.getState().equals("started") && plannedStory.getOwnerID() != ownerID) {
                            startedStories.add(plannedStory);
                        }

                }
            }

            // Add the unstarted stories to the main list first
            for(Task unstartedStory : unstartedStories){
                storys.add(unstartedStory);
                String storyName = truncateStoryName(unstartedStory.getStoryName());
                addListElement("<html><b>" +  storyName + "</b></html>");
            }

            // Add the started stories to the main list
            for(Task startedStory : startedStories){
                storys.add(startedStory);
                String storyName = truncateStoryName(startedStory.getStoryName());
                addListElement("<html>" + storyName  + "</html>");
            }

        } catch (HttpException e) {
            e.printStackTrace();
        }
    }
 // Função para limitar o texto das tasks na TaskList
    private String truncateStoryName(String storyName){
        if (storyName.length() > 50) {
            storyName = String.format("%s...", storyName.substring(0, 50));
        }
        return storyName;
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
