

import java.awt.*;
import java.awt.geom.AffineTransform;

public class RotatingBlade extends GameObject {
    private int angle = 0;

    public RotatingBlade(int x, int y, int size) {
        super(x, y, size, size);
    }
}