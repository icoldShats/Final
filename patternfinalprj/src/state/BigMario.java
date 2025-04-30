package state;

import main.Player;
import java.awt.Graphics;
import java.awt.Color;

public class BigMario implements PlayerState {
    @Override
    public void move(Player player) {
        if (player.left) player.setDx(-6);
        else if (player.right) player.setDx(6);
        else player.setDx(0);

        player.setX(player.getX() + player.getDx());
    }

    @Override
    public void jump(Player player) {
        if (player.onGround) {
            player.setDy(-18);
            player.onGround = false;
        }
    }

    @Override
    public void draw(Graphics g, Player player, int cameraX) {
        g.setColor(Color.RED);
        g.fillRect(player.getX() - cameraX, player.getY(), player.getWidth(), player.getHeight());
    }

}
