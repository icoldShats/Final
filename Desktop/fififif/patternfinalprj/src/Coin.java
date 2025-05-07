package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Coin extends GameObject {
    private boolean collected = false;

    public Coin(int x, int y) {
        super(x, y, 20, 20);
    }

    @Override
    public void update() {
    }

    public void draw(Graphics g, int cameraX) {
        if (!collected) {
            g.setColor(Color.YELLOW);
            g.fillOval(x - cameraX, y, width, height);
        }
    }

    @Override
    public void draw(Graphics g) {
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }
}
