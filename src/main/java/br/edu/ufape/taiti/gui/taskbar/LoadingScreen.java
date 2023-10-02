package br.edu.ufape.taiti.gui.taskbar;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JDialog {

    public LoadingScreen(JPanel parent) {
        super((JFrame) SwingUtilities.getWindowAncestor(parent), "Loading", true);
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Updating data, please wait...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
        setSize(200, 100);
        setLocationRelativeTo(parent);
    }
}