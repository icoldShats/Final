package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import factory.GameElementFactory;
import factory.ClassicStyleFactory;
import java.awt.Rectangle;
import command.*;
import manager.ScoreManager;
import builder.EnemyBuilder;
import observer.CoinScoreListener;


public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 800;
    private static final int LEVEL_WIDTH = 2000;

    private Player player;
    private Enemy enemy;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Platform> platforms = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList<>();
    private ArrayList<Box> boxes = new ArrayList<>();
    private ArrayList<Lever> levers = new ArrayList<>();
    private ArrayList<Door> doors = new ArrayList<>();
    private ArrayList<RotatingBlade> blades = new ArrayList<>();
    private ArrayList<Lava> lava = new ArrayList<>();
    private Rectangle finishLine;
    private boolean gameOver = false, gameWon = false;
    private int  cameraX = 0;
    private boolean paused = false;
    private int playerX = 400, playerY = 550;
    private int enemyX = new Random().nextInt(750), enemyY = 0;
    private int enemySpeed = 2;

    private Thread thread;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private main.GameLevel2 nextLevel;

    private JPanel buttonsPanel;
    private JButton toggleButton;

    public GamePanel(CardLayout cardLayout, JPanel mainPanel, main.GameLevel2 nextLevel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.nextLevel = nextLevel;

        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocusInWindow();
        setLayout(null);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(null);
        buttonsPanel.setBounds(1350, 30, 150, 180);
        buttonsPanel.setBackground(new Color(0, 0, 0, 150));
        buttonsPanel.setVisible(false);

        JButton pauseButton = new JButton("Pause");
        pauseButton.setBounds(15, 10, 120, 30);
        Command pauseCmd = new PauseCommand(pauseButton, () -> paused = !paused);
        pauseButton.addActionListener(e -> pauseCmd.execute());
        buttonsPanel.add(pauseButton);

        JButton menuButton = new JButton("Menu");
        menuButton.setBounds(15, 50, 120, 30);
        Command menuCmd = new MenuCommand(cardLayout, mainPanel);
        menuButton.addActionListener(e -> menuCmd.execute());
        buttonsPanel.add(menuButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(15, 90, 120, 30);
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

        mainPanel.add(this, "level1");
        mainPanel.add(nextLevel, "level2");

        cardLayout.show(mainPanel, "level1");
    }
    private final GameElementFactory factory = new ClassicStyleFactory();

    private void initGame() {
        player = factory.createPlayer(100, 470);
        enemy = new EnemyBuilder()
                .setX(600).setY(200).setSpeed(3).setBounds(500, 700).build();
        enemies.add(new EnemyBuilder()
                .setX(1400).setY(220).setSpeed(2).setBounds(1300, 1600).build());

        platforms.clear();
        platforms.add(factory.createPlatform(0, 540, 2000, 500));
        platforms.add(factory.createPlatform(200, 450, 100, 20));
        platforms.add(factory.createPlatform(400, 370, 100, 20));
        platforms.add(factory.createPlatform(600, 300, 100, 20));
        platforms.add(factory.createPlatform(800, 300, 100, 20));
        platforms.add(factory.createPlatform(1000, 150, 100, 20));

        coins.clear();

        int[][] coinPositions = {
                {239, 420},
                {440, 340},
                {640, 270},
                {890, 200},
                {1100, 250}
        };

        for (int[] pos : coinPositions) {
            Coin c = factory.createCoin(pos[0], pos[1]);
            c.addListener(new CoinScoreListener());
            coins.add(c);
        }



        levers.clear();
        levers.add(factory.createLever(1200, 480));

        doors.clear();
        doors.add(factory.createDoor(1300, 0, 20, 540));

        lava.clear();
        lava.add(factory.createLava(700, 538, 400, 30));

        finishLine = new Rectangle(1400, 540 - 60, 20, 60);
        ScoreManager.getInstance().resetScore();
        ScoreManager.getInstance().resetLives();

        gameWon = false;
        gameOver = false;
    }


    public void restartGame() {
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
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void update() {
        if (gameOver || paused) return;

        boolean onAnyPlatform = false;
        for (Platform p : platforms) {
            if (player.getY() + player.getHeight() >= p.getY() &&
                    player.getY() + player.getHeight() <= p.getY() + 10 &&
                    player.getX() + player.getWidth() > p.getX() &&
                    player.getX() < p.getX() + p.getWidth()) {
                player.setY(p.getY() - player.getHeight());
                player.setDy(0);
                player.setOnGround(true);
                onAnyPlatform = true;
                break;
            }
        }
        Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        Rectangle enemyBounds = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());

        if (playerBounds.intersects(enemyBounds)) {
            ScoreManager.getInstance().loseLife();
            if (ScoreManager.getInstance().getLives() <= 0) {
                gameOver = true;
            } else {
                player.setX(100); // Возврат в начальное положение
                player.setY(470);
            }
        }

        for (Coin c : coins) {
            if (!c.isCollected() && new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                    .intersects(c.getBounds())) {
                c.collect();
                ScoreManager.getInstance().addScore(1);

            }
        }

        if (!onAnyPlatform) player.setOnGround(false);

        player.update();
        enemy.update();


        cameraX = player.getX() - WIDTH / 2;
        if (cameraX < 0) cameraX = 0;
        if (cameraX > LEVEL_WIDTH - WIDTH) cameraX = LEVEL_WIDTH - WIDTH;

        if (player.getX() + player.getWidth() >= finishLine.x) {
            gameWon = true;
            gameOver = true;

            // ПРАВИЛЬНО: запускаем второй уровень!
            nextLevel.restartGame();
            nextLevel.startGame();
            cardLayout.show(mainPanel, "level2"); // <-- ПРАВИЛЬНО, раньше тут было level1
        }
        for (Lever lever : levers) {
            if (new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                    .intersects(lever.getBounds())) {
                lever.activate();  // Игрок активирует рычаг
            }
        }
        for (Lava l : lava) {
            if (l.checkCollision(player)) {
                ScoreManager.getInstance().resetLives();
                gameOver = true;  // Заканчиваем игру
                break;  // Выход из цикла, если столкновение с лавой
            }
        }

        for (Door door : doors) {
            for (Lever lever : levers) {
                if (lever.isActivated()) {
                    door.open();  // Открытие двери
                }
            }
        }

        for (Door door : doors) {
            if (!door.isOpen() && new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
                    .intersects(door.getBounds())) {
                player.setX(player.getX() - 10);
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.CYAN);

        for (Platform platform : platforms) platform.draw(g, cameraX);
        for (Coin coin : coins) coin.draw(g, cameraX);
        for (Lever lever : levers) lever.draw(g, cameraX);
        for (Door door : doors) door.draw(g, cameraX);
        for (RotatingBlade blade : blades) blade.draw(g, cameraX);
        for (Lava l : lava) l.draw(g, cameraX);  // Отрисовка лавы

        if (player != null) player.draw(g, cameraX);
        if (enemy != null) enemy.draw(g, cameraX);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + ScoreManager.getInstance().getScore(), 10, 20);
        g.drawString("Lives: " + ScoreManager.getInstance().getLives(), 10, 40);


        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(gameWon ? Color.GREEN : Color.RED);
            g.drawString(gameWon ? "Level Completed!" : "Game Over", 650, 300);
        }
    }

}

