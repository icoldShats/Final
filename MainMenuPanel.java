package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(JFrame frame, CardLayout cardLayout, JPanel mainPanel) {
        setLayout(null);
        setBackground(Color.CYAN);

        JLabel title = new JLabel("Red Ball");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setBounds(650, 150, 500, 60);
        add(title);

        JButton startButton = new JButton("Начать игру");
        startButton.setBounds(655, 250, 200, 50);
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        add(startButton);

        JButton exitButton = new JButton("Выход");
        exitButton.setBounds(655, 320, 200, 50);
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        add(exitButton);

        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "level1");

            SwingUtilities.invokeLater(() -> {
                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof GamePanel gp) {
                        gp.restartGame();
                        gp.startGame();
                        gp.requestFocusInWindow();
                    }
                }
            });
        });



        exitButton.addActionListener(e -> {
            System.exit(0);
        });
    }
}
