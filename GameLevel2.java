package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameLevel2 extends JPanel implements Runnable {
    private Player player;
    private ArrayList<Platform> platforms = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Rectangle finishLine;

    private int cameraX = 0, score = 0, lives = 3;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean recentlyHit = false;
    private long lastHitTime = 0;
    private final int HIT_COOLDOWN = 1000;

    private long startTime = System.currentTimeMillis();

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Thread thread;

    private JLabel resultLabel; 

    public GameLevel2(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocusInWindow();
        setLayout(null);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver && player != null) player.keyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                if (!gameOver && player != null) player.keyReleased(e);
            }
        });

        resultLabel = new JLabel("");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultLabel.setBounds(200, 250, 400, 50);
        add(resultLabel);

        initGame();
    }

    private void initGame() {
        player = new Player(100, 470);
        platforms.clear();
        coins.clear();
        enemies.clear();

        platforms.add(new Platform(0, 540, 2500, 60));
        platforms.add(new Platform(300, 420, 100, 20));
        platforms.add(new Platform(600, 350, 100, 20));
        platforms.add(new Platform(900, 300, 100, 20));
        platforms.add(new Platform(1300, 250, 100, 20));
        platforms.add(new Platform(1600, 320, 100, 20));
        platforms.add(new Platform(1900, 370, 100, 20));

        enemies.add(new Enemy(500, 500));
        enemies.add(new Enemy(1400, 220));
        enemies.add(new Enemy(1800, 500));

        coins.add(new Coin(320, 390));
        coins.add(new Coin(620, 320));
        coins.add(new Coin(1320, 220));
        coins.add(new Coin(1920, 340));

        finishLine = new Rectangle(2400, 440, 20, 100);
        startTime = System.currentTimeMillis();
        score = 0;
        lives = 3;
        gameWon = false;
    }

    public void startGame() {
        startTime = System.currentTimeMillis();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void run() {
        while (true) {
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
        for (Enemy e : enemies) e.update();

        cameraX = player.getX() - 400;
        if (cameraX < 0) cameraX = 0;
        if (cameraX > 2500 - 800) cameraX = 2500 - 800;

        boolean onPlatform = false;
        for (Platform p : platforms) {
            if (player.getY() + player.getHeight() >= p.getY() &&
                    player.getY() + player.getHeight() <= p.getY() + 10 &&
                    player.getX() + player.getWidth() > p.getX() &&
                    player.getX() < p.getX() + p.getWidth()) {
                player.setY(p.getY() - player.getHeight());
                player.setDy(0);
                player.onGround = true;
                onPlatform = true;
                break;
            }
        }
        if (!onPlatform) player.onGround = false;

        for (Coin c : coins) {
            if (!c.isCollected() &&
                    new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                            .intersects(c.getBounds())) {
                c.collect();
                score++;
            }
        }

        long currentTime = System.currentTimeMillis();
        for (Enemy e : enemies) {
            if (!recentlyHit &&
                    new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                            .intersects(new Rectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight()))) {

                lives--;
                recentlyHit = true;
                lastHitTime = currentTime;

                if (lives <= 0) {
                    gameOver = true;
                } else {
                    player.setX(100);
                    player.setY(470);
                }
                break;
            }
        }

        if (recentlyHit && currentTime - lastHitTime >= HIT_COOLDOWN) {
            recentlyHit = false;
        }

        if (player.getX() + player.getWidth() >= finishLine.x) {
            gameOver = true;
            gameWon = true;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.LIGHT_GRAY);

        for (Platform p : platforms) p.draw(g, cameraX);
        for (Coin c : coins) c.draw(g, cameraX);
        for (Enemy e : enemies) e.draw(g, cameraX);
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

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            if (gameWon) {
                g.setColor(Color.GREEN.darker());
                g.drawString("YOU WIN!", 250, 300);

                // Показываем результат
                resultLabel.setText("Вы завершили уровень за " + getElapsedTimeInSeconds() + " секунд!");
            } else {
                g.setColor(Color.BLACK);
                g.drawString("КОНЕЦ ИГРЫ", 250, 300);
                resultLabel.setText("Вы проиграли! Попробуйте снова.");
            }
        }
    }

    public int getElapsedTimeInSeconds() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }
}
