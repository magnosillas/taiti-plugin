package br.edu.ufape.taiti.gui.taskbar;


import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;


import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Color;
import java.awt.Font;


public class MyToolWindow {

    private JPanel myToolWindowContent;
    private JButton refreshButton;
    private JButton addButton;
    private JTextField txtSearch;
    private JList tasksList;
    private JPanel buttonsPanel;
    private ArrayList<String> people;
    private DefaultListModel listPeopleModel;

    public MyToolWindow(ToolWindow toolWindow) {

        people = new ArrayList<>();
        listPeopleModel = new DefaultListModel();
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
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.setToolTipText("Shit");
            }
        });
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if(txtSearch.getText().equals("Search")){
                    txtSearch.setText(null);
                    txtSearch.requestFocus();
                    //remove placeholder style
                    removePlaceHolderStyle(txtSearch);
                }
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
    }
    // Função para dar search na lista de strings
    private List<String> searchList(String searchWords, List<String> listOfStrings) {
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));
        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    public void addPlaceHolderStyle(JTextField textField){
        Font font = textField.getFont();
        font = font.deriveFont(Font.ITALIC);
        textField.setFont(font);
        textField.setForeground(Color.gray); //PlaceHolder font color
    }

    public void removePlaceHolderStyle(JTextField textField){
        Font font = textField.getFont();
        font = font.deriveFont(Font.PLAIN);
        textField.setFont(font);
        textField.setForeground(Color.LIGHT_GRAY); //PlaceHolder font color
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }


}
