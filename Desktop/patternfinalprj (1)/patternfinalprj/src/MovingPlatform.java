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

    }
    public void draw(Graphics g) {
    }
}