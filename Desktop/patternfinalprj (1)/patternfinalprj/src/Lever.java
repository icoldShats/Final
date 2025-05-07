package src;

import java.awt.*;

public class Lever extends GameObject{
    private boolean activated = false;
    public Lever(int x, int y) {
        super(x, y, 30, 30);
    }
    @Override
    public void draw(Graphics g) {

    }
    @Override
    public void update() {
    }
    public boolean isActivated() {
        return activated;
    }
    public void activate() {
        activated = true;
    }

    public void draw(Graphics g, int cameraX) {
        g.setColor(activated ? Color.GREEN : Color.RED);
        g.fillRect(x - cameraX, y, width, height);
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
