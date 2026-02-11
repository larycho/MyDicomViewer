package org.example.mydicomviewer.models.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class FreehandLine implements DrawableShape {

    private final Path2D.Double path;
    private Color color;
    private float width;

    public FreehandLine(Color color, float width) {
        this.path = new Path2D.Double();
        this.color = color;
        this.width = width;
    }

    public void start(double x, double y) {
        path.moveTo(x, y);
    }

    public void lineTo(double x, double y) {
        path.lineTo(x, y);
    }

    public void end(double x, double y) {
        path.lineTo(x, y);
    }

    @Override
    public void draw(Graphics2D g, AffineTransform affineTransform) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));

        Shape transformedShape = affineTransform.createTransformedShape(path);
        g.draw(transformedShape);
    }

}
