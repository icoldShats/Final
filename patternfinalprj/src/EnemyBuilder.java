package builder;

import main.Enemy;

public class EnemyBuilder {
    private int x = 0;
    private int y = 0;
    private int width = 40;
    private int height = 40;
    private int speed = 2;
    private int leftBound = 0;
    private int rightBound = 800;

    public EnemyBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public EnemyBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public EnemyBuilder setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public EnemyBuilder setBounds(int left, int right) {
        this.leftBound = left;
        this.rightBound = right;
        return this;
    }

    public Enemy build() {
        Enemy enemy = new Enemy(x, y);
        enemy.setWidth(width);
        enemy.setHeight(height);
        enemy.setSpeed(speed);
        enemy.setLeftBound(leftBound);
        enemy.setRightBound(rightBound);
        return enemy;
    }
}