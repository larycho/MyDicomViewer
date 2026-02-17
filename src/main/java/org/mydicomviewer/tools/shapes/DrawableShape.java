package org.mydicomviewer.tools.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;

public interface DrawableShape {
    void draw(Graphics2D g, AffineTransform affineTransform);
}
