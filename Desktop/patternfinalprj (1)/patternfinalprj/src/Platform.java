package main;

import java.awt.Color;
import java.awt.Graphics;

public class Platform extends GameObject {

    public Platform(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
    }

    public void draw(Graphics g, int cameraX) {
        g.setColor(new Color(139, 69, 19));
        g.fillRect(x - cameraX, y, width, height);
    }

    @Override
    public void draw(Graphics g) {}
}
