package state;

import main.Player;
import java.awt.Graphics;

public interface PlayerState {
    void move(Player player);
    void jump(Player player);
    void draw(Graphics g, Player player, int cameraX);
}
