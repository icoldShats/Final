package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Player extends GameObject {
    public boolean left, right, jumping;
    private boolean onGround = false;

    private int gravity = 1;
    private int maxFallSpeed = 10;

    private int dx = 0;
    private int dy = 0;

    private double rotationAngle = 0;

    public Player(int x, int y) {
        super(x, y, 40, 40);
    }

    public void update() {
        x += dx;

        if (jumping && onGround) {
            System.out.println("ПРЫЖОК!!!");
            dy = -18;
            onGround = false;
        }

        dy += gravity;
        if (dy > maxFallSpeed) dy = maxFallSpeed;
        y += dy;

        if (left) rotationAngle -= 0.1;
        if (right) rotationAngle += 0.1;

        if (x < 0) x = 0;
        if (x > 3000 - width) x = 3000 - width;
    }

    public void draw(Graphics g, int cameraX) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        int centerX = x - cameraX + width / 2;
        int centerY = y + height / 2;

        g2d.translate(centerX, centerY);
        g2d.rotate(rotationAngle);
        g2d.setColor(Color.RED);
        g2d.fillOval(-width / 2, -height / 2, width, height);

        g2d.setTransform(old);
    }

    @Override
    public void draw(Graphics g) {
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = true;
            dx = -6;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = true;
            dx = 6;
        }
        if (key == KeyEvent.VK_SPACE) jumping = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = false;
            dx = right ? 6 : 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = false;
            dx = left ? -6 : 0;
        }
        if (key == KeyEvent.VK_SPACE) jumping = false;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
}
