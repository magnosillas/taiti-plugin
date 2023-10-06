package br.edu.ufape.taiti.gui.taskbar;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoadingScreen extends JPanel {

    public LoadingScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // Define o weighty para zero para evitar expansão vertical
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10); // Define um preenchimento entre os componentes

        // Load the GIF file from resources
        URL imageURL = getClass().getResource("/loading.gif");
        ImageIcon originalIcon = new ImageIcon(imageURL);
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon loadingIcon = new ImageIcon(scaledImage);

        // Add the scaled GIF to a JLabel and centralize it
        JLabel loadingLabel = new JLabel(loadingIcon);
        add(loadingLabel, gbc);

        // Increment the gridy to add the text label below the loading label
        gbc.gridy++;
        JLabel label = new JLabel("Updating data, please wait...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, gbc);

        // Set the preferred size for the panel
        setPreferredSize(new Dimension(200, 200));
    }
}