package src;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class RotatingBlade extends GameObject {
    private int angle = 0;

    public RotatingBlade(int x, int y, int size) {
        super(x, y, size, size);
    }

    @Override
    public void update() {
        angle += 5;
        if (angle >= 360) angle = 0;
    }

    @Override
    public void draw(Graphics g) {
        draw(g, 0);
    }
    public void draw(Graphics g, int cameraX) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform(); // сохраняем transform

        g2d.setColor(Color.GREEN);
        g2d.translate(x - cameraX + width / 2, y + height / 2);
        g2d.rotate(Math.toRadians(angle));
        g2d.fillRect(-width / 2, -height / 2, width, height);

        g2d.setTransform(old);
    }
}
