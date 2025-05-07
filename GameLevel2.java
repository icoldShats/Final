package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import factory.GameElementFactory;
import factory.ClassicStyleFactory;
import manager.ScoreManager;
import java.awt.Rectangle;
import builder.EnemyBuilder;
import observer.CoinScoreListener;
import command.*;


public class GameLevel2 extends JPanel implements Runnable {
    private static final int LEVEL_WIDTH = 3000;


    private Player player;
    private ArrayList<Platform> platforms = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Lava> lava = new ArrayList<>();
    private ArrayList<RotatingBlade> blades = new ArrayList<>();
    private ArrayList<MovingPlatform> movingPlatforms = new ArrayList<>();

    private boolean paused = false;
    private Enemy enemy;
    private int cameraX = 0;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean recentlyHit = false;
    private long lastHitTime = 0;
    private final int HIT_COOLDOWN = 1000;
    private int playerX = 400, playerY = 550;
    private int enemyX = new Random().nextInt(750), enemyY = 0;
    private int enemySpeed = 2;

    private Rectangle finishLine;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Thread thread;

    private JPanel buttonsPanel;
    private JButton toggleButton;

    public GameLevel2(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocusInWindow();
        setLayout(null);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(null);
        buttonsPanel.setBounds(1350, 30, 150, 180);
        buttonsPanel.setBackground(new Color(0, 0, 0, 150));
        buttonsPanel.setVisible(false);

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                requestFocusInWindow();
            }
        });

        JButton pauseButton = new JButton("Pause");
        pauseButton.setBounds(15, 10, 120, 30); // ← добавлено!
        Command pauseCmd = new PauseCommand(pauseButton, () -> paused = !paused);
        pauseButton.addActionListener(e -> pauseCmd.execute());
        buttonsPanel.add(pauseButton);

        JButton menuButton = new JButton("Menu");
        menuButton.setBounds(15, 50, 120, 30); // ← добавлено!
        Command menuCmd = new MenuCommand(cardLayout, mainPanel);
        menuButton.addActionListener(e -> menuCmd.execute());
        buttonsPanel.add(menuButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(15, 90, 120, 30); // ← добавлено!
        Command exitCmd = new ExitCommand();
        exitButton.addActionListener(e -> exitCmd.execute());
        buttonsPanel.add(exitButton);

        JButton restartButton = new JButton("Restart");
        restartButton.setBounds(15, 130, 120, 30);
        restartButton.addActionListener(e -> {
            restartGame();
        });
        buttonsPanel.add(restartButton);

        add(buttonsPanel);

        toggleButton = new JButton("☰");
        toggleButton.setBounds(1459, 10, 30, 30);
        toggleButton.setFont(new Font("Arial", Font.BOLD, 20));
        toggleButton.addActionListener(e -> {
            buttonsPanel.setVisible(!buttonsPanel.isVisible());
            requestFocusInWindow();
        });
        add(toggleButton);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (buttonsPanel.isVisible()) {
                    if (!buttonsPanel.getBounds().contains(e.getPoint())) {
                        buttonsPanel.setVisible(false);
                    }
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver && player != null) player.keyPressed(e);
            }

            public void keyReleased(KeyEvent e) {
                if (!gameOver && player != null) player.keyReleased(e);
            }
        });

        initGame();
        mainPanel.add(this, "level2");

        cardLayout.show(mainPanel, "level2");
    }
    private final GameElementFactory factory = new ClassicStyleFactory();

    private void initGame() {
        player = factory.createPlayer(100, 470);
        enemies.clear();
        enemy = new EnemyBuilder()
                .setX(600).setY(200).setSpeed(3).setBounds(500, 700).build();

        enemies.add(new EnemyBuilder()
                .setX(2100).setY(420).setSpeed(2).setBounds(2000, 2150).build());



        platforms.clear();
        platforms.add(factory.createPlatform(0, 540, 2500, 500));
        platforms.add(factory.createPlatform(300, 420, 100, 20));
        platforms.add(factory.createPlatform(600, 350, 120, 20));
        platforms.add(factory.createPlatform(900, 300, 130, 20));
        platforms.add(factory.createPlatform(1300, 250, 100, 20));
        platforms.add(factory.createPlatform(1600, 320, 120, 20));
        platforms.add(factory.createPlatform(1900, 370, 150, 20));
        platforms.add(factory.createPlatform(2200, 420, 100, 20));
        platforms.add(factory.createPlatform(1200, 300, 20, 20));

        movingPlatforms.clear();
        movingPlatforms.add(new MovingPlatform(500, 420, 100, 20, 3, 500, 700));
        movingPlatforms.add(new MovingPlatform(1500, 300, 100, 20, -2, 1400, 1900));

        coins.clear();

        int[][] coinPositions = {
                {339, 300},
                {647, 320},
                {2000, 340},
                {1100, 180},
                {1340, 220},
                {1920, 340}
        };

        for (int[] pos : coinPositions) {
            Coin c = factory.createCoin(pos[0], pos[1]);
            c.addListener(new CoinScoreListener());
            coins.add(c);
        }





        lava.clear();
        lava.add(factory.createLava(300, 538, 200, 30));
        lava.add(factory.createLava(1200, 538, 200, 30));

        finishLine = new Rectangle(2400, 440, 20, 100);
        ScoreManager.getInstance().resetScore();
        ScoreManager.getInstance().resetLives();

        gameWon = false;
        gameOver = false;
    }




    public void restartGame() {
        initGame();
        gameOver = false;
        requestFocusInWindow();
    }

    public void startGame() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
        requestFocusInWindow();
    }

    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void update() {
        if (gameOver || paused) return;
        player.update();
        for (Enemy e : enemies) e.update();
        for (MovingPlatform mp : movingPlatforms) {
            mp.update();

            if (new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight()).intersects(mp.getBounds())) {

                if (!recentlyHit) {
                    ScoreManager.getInstance().loseLife();
                    recentlyHit = true;
                    lastHitTime = System.currentTimeMillis();

                    if (ScoreManager.getInstance().getLives() <= 0) {
                        gameOver = true;
                    } else {
                        player.setX(100);
                        player.setY(470);
                    }
                }
            }
        }

        long currentTime = System.currentTimeMillis();
        for (Enemy e : enemies) {
            if (!recentlyHit && new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                    .intersects(new Rectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight()))) {
                ScoreManager.getInstance().loseLife();
                recentlyHit = true;
                lastHitTime = currentTime;

                if (ScoreManager.getInstance().getLives() <= 0) {
                    gameOver = true;
                } else {
                    player.setX(100);
                    player.setY(470);
                }
                break;
            }
        }

        if (recentlyHit && currentTime -

                lastHitTime >= HIT_COOLDOWN) {
            recentlyHit = false;
        }

        boolean onPlatform = false;
        for (Platform p : platforms) {
            if (player.getY() + player.getHeight() >= p.getY() &&
                    player.getY() + player.getHeight() <= p.getY() + 10 &&
                    player.getX() + player.getWidth() > p.getX() &&
                    player.getX() < p.getX() + p.getWidth()) {
                player.setY(p.getY() - player.getHeight());
                player.setDy(0);
                player.setOnGround(true);
                onPlatform = true;
                break;
            }
        }

        for (MovingPlatform mp : movingPlatforms) {
            if (new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                    .intersects(mp.getBounds())) {
                player.setX(mp.getX() - player.getWidth());
                player.setOnGround(true);
            }
        }

        if (!onPlatform) player.setOnGround(false);

        for (Coin c : coins) {
            if (!c.isCollected() && new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                    .intersects(c.getBounds())) {
                c.collect();
                ScoreManager.getInstance().addScore(1);

            }
        }

        for (Lava l : lava) {
            if (l.checkCollision(player)) {
                ScoreManager.getInstance().resetLives();
                gameOver = true;
                break;
            }
        }

        if (player.getX() + player.getWidth() >= finishLine.x) {
            gameWon = true;
            gameOver = true;
        }
        cameraX = Math.max(0, Math.min(player.getX() - 400, 2500 - getWidth()));
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.CYAN);

        for (Platform p : platforms) p.draw(g, cameraX);
        for (Coin c : coins) c.draw(g, cameraX);
        for (Enemy e : enemies) e.draw(g, cameraX);
        for (Lava l : lava) l.draw(g, cameraX);
        for (RotatingBlade b : blades) b.draw(g, cameraX);
        for (MovingPlatform mp : movingPlatforms) mp.draw(g, cameraX);

        player.draw(g, cameraX);

        g.setColor(Color.WHITE);
        g.fillRect(finishLine.x - cameraX, finishLine.y, finishLine.width, finishLine.height);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + ScoreManager.getInstance().getScore(), 10, 20);
        g.drawString("Lives: " + ScoreManager.getInstance().getLives(), 10, 40);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(gameWon ? Color.GREEN : Color.RED);
            g.drawString(gameWon ? "Level Completed!" : "Game Over", 650, 300);
        }
    }
}
