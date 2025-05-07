

import java.awt.*;
import java.awt.geom.AffineTransform;

public class RotatingBlade extends GameObject {
    private int angle = 0;

    public RotatingBlade(int x, int y, int size) {
        super(x, y, size, size);
    }

    @Override
    public void update() {
        angle += 5;
        if (angle >= 360) angle = 0;
    }
}
