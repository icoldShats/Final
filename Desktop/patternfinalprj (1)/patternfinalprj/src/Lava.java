package src;

import java.awt.*;

public class Lava extends GameObject{
    public Lava(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
    }
    @Override
    public void draw(Graphics g) {
        draw(g, 0);
    }

    public void draw(Graphics g, int cameraX) {
        g.setColor(Color.ORANGE);
        g.fillRect(x - cameraX, y, width, height);
    }
    public boolean checkCollision(Player player) {
        Rectangle lavaRect = new Rectangle(x, y, width, height);
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        return lavaRect.intersects(playerRect);
    }

}
