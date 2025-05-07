

import java.awt.Graphics;



public abstract class GameObject {
    protected int x, y;
    protected int width, height;
    protected int dx, dy;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }}
