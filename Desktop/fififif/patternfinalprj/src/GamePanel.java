> gulnur:
        package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final int LEVEL_WIDTH = 2000;

    private Player player;
    private Enemy enemy;
    private ArrayList<Platform> platforms = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList<>();
    private Rectangle finishLine;
    private boolean gameOver = false, gameWon = false;
    private int score = 0, lives = 3, cameraX = 0;

    private long startTime = System.currentTimeMillis();

    private Thread thread;
    private boolean running;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameLevel2 nextLevel;

    public GamePanel(CardLayout cardLayout, JPanel mainPanel, GameLevel2 nextLevel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.nextLevel = nextLevel;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver) player.keyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                if (!gameOver) player.keyReleased(e);
            }
        });

        initGame();
    }

    private void initGame() {
        player = new Player(100, 470);
        enemy = new Enemy(600, 500);

        platforms.clear();
        platforms.add(new Platform(0, 540, LEVEL_WIDTH, 60));
        platforms.add(new Platform(250, 450, 100, 20));
        platforms.add(new Platform(400, 400, 100, 20));
        platforms.add(new Platform(550, 350, 100, 20));

        coins.clear();
        coins.add(new Coin(270, 420));
        coins.add(new Coin(420, 370));
        coins.add(new Coin(720, 270));

        finishLine = new Rectangle(1800, 540 - 60, 20, 60);
        score = 0;
        lives = 3;
        gameWon = false;
        startTime = System.currentTimeMillis();
    }

    public void restartGame() {
        if (thread != null && thread.isAlive()) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
        initGame();
        gameOver = false;
    }


    public void startGame() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }


    public void run() {
        running = true;
        System.out.println("Поток запущен для " + (gameWon ? "2-го раунда" : "1-го раунда"));
        while (running) {
            update();
            repaint();
            try {
                Thread.sleep(16);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void update() {
        if (gameOver) return;
        if (getElapsedTimeInSeconds() >= 20) {
            gameOver = true;
            return;
        }

        player.update();
        enemy.update();

        boolean onAnyPlatform = false;
        for (Platform p : platforms) {
            if (player.getY() + player.getHeight() >= p.getY() &&
                    player.getY() + player.getHeight() <= p.getY() + 10 &&
                    player.getX() + player.getWidth() > p.getX() &&
                    player.getX() < p.getX() + p.getWidth()) {
                player.setY(p.getY() - player.getHeight());
                player.setDy(0);
                player.onGround = true;
                onAnyPlatform = true;
                break;
            }
        }
        if (!onAnyPlatform) player.onGround = false;

        for (Coin c : coins) {
            if

> gulnur:
            (!c.isCollected() &&
                    new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                            .intersects(c.getBounds())) {
                c.collect();
                score++;
            }
        }

        cameraX = player.getX() - WIDTH / 2;
        if (cameraX < 0) cameraX = 0;
        if (cameraX > LEVEL_WIDTH - WIDTH) cameraX = LEVEL_WIDTH - WIDTH;

        if (new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                .intersects(new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight()))) {
            lives--;
            if (lives <= 0) gameOver = true;
            else {
                player.setX(100);
                player.setY(470);
            }
        }

        if (player.getX() + player.getWidth() >= finishLine.x) {
            gameWon = true;
            gameOver = true;

            SwingUtilities.invokeLater(() -> {
                nextLevel.startGame(); /
                cardLayout.show(mainPanel, "level2");
                nextLevel.requestFocusInWindow();
            });

        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.CYAN);

        for (Platform p : platforms) p.draw(g, cameraX);
        for (Coin c : coins) c.draw(g, cameraX);
        enemy.draw(g, cameraX);
        player.draw(g, cameraX);

        g.setColor(Color.WHITE);
        g.fillRect(finishLine.x - cameraX, finishLine.y, finishLine.width, finishLine.height);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Счёт: " + score, 20, 30);
        g.setColor(Color.RED);
        g.drawString("Жизни: " + lives, 20, 60);
        g.setColor(Color.BLUE);
        g.drawString("Время: " + getElapsedTimeInSeconds() + " сек", 20, 90);

        if (gameOver && !gameWon) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("GAME OVER", 250, 300);
        }
    }

    public int getElapsedTimeInSeconds() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }
}
