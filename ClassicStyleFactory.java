 package factory;

import main.*;
import main.Coin;
import main.Door;
import main.Enemy;

public class ClassicStyleFactory implements GameElementFactory {
    @Override
    public Player createPlayer(int x, int y) {
        return new Player(x, y);
    }

    @Override
    public Enemy createEnemy(int x, int y) {
        return new Enemy(x, y);
    }

    @Override
    public Platform createPlatform(int x, int y, int width, int height) {
        return new Platform(x, y, width, height);
    }

    @Override
    public Coin createCoin(int x, int y) {
        return new Coin(x, y);
    }

    @Override
    public Lava createLava(int x, int y, int width, int height) {
        return new Lava(x, y, width, height);
    }

    @Override
    public Lever createLever(int x, int y) {
        return new Lever(x, y);
    }

    @Override
    public Door createDoor(int x, int y, int width, int height) {
        return new Door(x, y, width, height);
    }
}

