package src;

import java.awt.*;

public class MovingPlatform extends GameObject {
    private int speed;
    private int leftBound, rightBound;

    public MovingPlatform(int x, int y, int width, int height, int speed, int leftBound, int rightBound) {
        super(x, y, width, height);
        this.speed = speed;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }
    @Override
    public void update() {
        x += speed;

        if (x < leftBound || x + width > rightBound) {
            speed *= -1;
        }
    }
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }

    public void draw(Graphics g, int cameraX) {
        g.setColor(Color.YELLOW);
        g.fillRect(x - cameraX, y, width, height);
    }
}