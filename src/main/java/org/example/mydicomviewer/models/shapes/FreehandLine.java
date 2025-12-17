package org.example.mydicomviewer.models.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class FreehandLine implements DrawableShape {

    private Path2D.Double path;
    private Color color;
    private float width;
    private boolean isBeingModified = true;
    private boolean finishingDrawing = false;

    public FreehandLine(Color color, float width) {
        this.path = new Path2D.Double();
        this.color = color;
        this.width = width;
    }

    public void start(double x, double y) {
        path.moveTo(x, y);
        isBeingModified = true;
    }

    public void lineTo(double x, double y) {
        path.lineTo(x, y);
    }

    public void end(double x, double y) {
        path.lineTo(x, y);
        isBeingModified = false;
        finishingDrawing = true;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.draw(path);
    }

    @Override
    public void draw(Graphics2D g, AffineTransform affineTransform) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));

        Shape transformedShape = affineTransform.createTransformedShape(path);
        g.draw(transformedShape);
    }

}
