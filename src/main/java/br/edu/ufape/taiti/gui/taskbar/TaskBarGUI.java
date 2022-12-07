package br.edu.ufape.taiti.gui.taskbar;


import br.edu.ufape.taiti.gui.TaitiDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public TaskBarGUI(ToolWindow toolWindow, Project project) {

        people = new ArrayList<>();
        listPeopleModel = new DefaultListModel<>();
        tasksList.setModel(listPeopleModel);
        addPlaceHolderStyle(txtSearch);





        refreshButton.addActionListener(e -> {
            people.add(txtSearch.getText());
            listPeopleModel.removeAllElements();
            for (String p : people) {
                listPeopleModel.addElement(p);
            }
            txtSearch.setText("");
        });

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
        addButton.addActionListener(e -> {

            TaitiDialog taitiDialog = new TaitiDialog(project);
            taitiDialog.show();
        });
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

    public JPanel getContent() {
        return TaskBar;
    }


}
