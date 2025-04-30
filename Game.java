package main;

import javax.swing.*;
import java.awt.*;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mario Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        GameLevel2 level2 = new GameLevel2(cardLayout, mainPanel);
        GamePanel level1 = new GamePanel(cardLayout, mainPanel, level2);

        MainMenuPanel menuPanel = new MainMenuPanel(frame, cardLayout, mainPanel);

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(level1, "level1");
        mainPanel.add(level2, "level2");

        frame.add(mainPanel);
        frame.setVisible(true);

        level1.startGame();
        cardLayout.show(mainPanel, "menu");
    }
}
