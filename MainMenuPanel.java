package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(JFrame frame, CardLayout cardLayout, JPanel mainPanel) {
        setLayout(null);
        setBackground(Color.CYAN);

        JLabel title = new JLabel("Super Mario Game");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setBounds(200, 100, 500, 60);
        add(title);

        JButton startButton = new JButton("Начать игру");
        startButton.setBounds(300, 250, 200, 50);
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        add(startButton);

        JButton exitButton = new JButton("Выход");
        exitButton.setBounds(300, 320, 200, 50);
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        add(exitButton);

        // Обработка кнопок
        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "level1");

            // Найдём GamePanel и дадим ему фокус
            SwingUtilities.invokeLater(() -> {
                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof GamePanel gp) {
                        gp.restartGame();     // сбрасываем уровень
                        gp.startGame();
                        gp.requestFocusInWindow(); // 💥 Фокус обязательно
                    }
                }
            });
        });



        exitButton.addActionListener(e -> {
            System.exit(0);
        });
    }
}
