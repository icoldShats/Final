package main;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import state.PlayerState;
import state.SmallMario;
import state.BigMario;

public class Player extends GameObject {
    public boolean left, right, jumping;
    public boolean onGround = false;

    private int gravity = 1;
    private int maxFallSpeed = 10;

    private PlayerState state;

    public Player(int x, int y) {
        super(x, y, 40, 40);
        this.state = new SmallMario();
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void update() {
        state.move(this);

        if (jumping && onGround) {
            state.jump(this);
        }

        dy += gravity;
        if (dy > maxFallSpeed) dy = maxFallSpeed;
        y += dy;
        if (x < 0) x = 0;
        if (x > 3000 - width) x = 3000 - width;

    }
    public void draw(Graphics g, int cameraX) {
        state.draw(g, this, cameraX);
    }

    @Override
    public void draw(Graphics g) {}

    public void keyPressed(KeyEvent e) {
        System.out.println("Нажата клавиша: " + e.getKeyCode()); // 💥 тест
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) left = true;
        if (key == KeyEvent.VK_RIGHT) right = true;
        if (key == KeyEvent.VK_UP) jumping = true;

        if (key == KeyEvent.VK_1) setState(new SmallMario());
        if (key == KeyEvent.VK_2) setState(new BigMario());
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) left = false;
        if (key == KeyEvent.VK_RIGHT) right = false;
        if (key == KeyEvent.VK_UP) jumping = false;
    }
    public void setDx(int dx) { this.dx = dx; }
    public int getDx() { return dx; }

    public void setX(int x) { this.x = x; }
    public int getX() { return x; }

    public void setY(int y) { this.y = y; }
    public int getY() { return y; }

    public void setDy(int dy) { this.dy = dy; }
    public int getDy() { return dy; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
