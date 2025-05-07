package src;

import java.awt.*;


public class Door extends GameObject {
    private boolean open = false;
    public Door(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    @Override
    public void draw(Graphics g) {
    }
    @Override
    public void update() {
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void open() {
        open = true;
    }
    public boolean isOpen() {
        return open;
    }

    public void draw(Graphics g, int cameraX) {
        if (!open) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x - cameraX, y, width, height);
        }
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}