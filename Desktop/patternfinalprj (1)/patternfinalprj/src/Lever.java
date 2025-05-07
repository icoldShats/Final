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
}
