package org.example.mydicomviewer.models.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;

public interface DrawableShape {
    void draw(Graphics2D g);
    void draw(Graphics2D g, AffineTransform affineTransform);
}
