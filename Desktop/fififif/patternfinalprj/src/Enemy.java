package main;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends GameObject {
    private int direction = 1;
    private int speed = 2;
    private int leftBound = 550;
    private int rightBound = 750;

    public Enemy(int x, int y) {
        super(x, y, 40, 40);
    }

    @Override
    public void update() {
        x += speed * direction;

        if (x < leftBound || x + width > rightBound) {
            direction *= -1;
        }
    }

    @Override
    public void draw(Graphics g) {
    }

    public void draw(Graphics g, int cameraX) {
        g.setColor(Color.GREEN);
        g.fillRect(x - cameraX, y, width, height);
    }
}
