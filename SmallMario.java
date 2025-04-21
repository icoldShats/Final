package state;

import main.Player;
import java.awt.Graphics;
import java.awt.Color;

public class SmallMario implements PlayerState {
    @Override
    public void move(Player player) {
        if (player.left) player.setDx(-5);
        else if (player.right) player.setDx(5);
        else player.setDx(0);

        player.setX(player.getX() + player.getDx());
    }
