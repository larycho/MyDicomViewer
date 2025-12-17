
package org.example;


import org.example.mydicomviewer.models.shapes.DrawableShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Rectangle implements DrawableShape {

    private Rectangle2D rectangle;
    private Color color;
    private float thickness;
    private Point2D origin;

    private String label;

    private boolean isBeingDrawn = false;
    private boolean finishingDrawing = false;

    public Rectangle(Color color, float thickness) {
        this.rectangle = new Rectangle2D.Float();
        this.color = color;
        this.thickness = thickness;
    }

    public void start(double x, double y) {
        origin = new Point2D.Double(x, y);
        rectangle.setRect(x, y, 0, 0);
        isBeingDrawn = true;
    }

    public void setEndPoint(double x, double y) {
        double width = x - origin.getX();
        double height = y - origin.getY();

        Point2D.Double start = new Point2D.Double(origin.getX(), origin.getY());

        if (width < 0) {
            width = -width;
            start.x = origin.getX() - width;
        }

        if (height < 0) {
            height = -height;
            start.y = origin.getY() - height;
        }

        rectangle.setRect(start.getX(), start.getY(), width, height);
    }

    public void end(double x, double y) {
        setEndPoint(x, y);
        isBeingDrawn = false;
        finishingDrawing = true;
    }

    public Point2D.Double getStartPoint() {
        return new Point2D.Double(rectangle.getX(), rectangle.getY());
    }

    public Point2D.Double getEndPoint() {
        return new Point2D.Double(rectangle.getX() + rectangle.getWidth(), rectangle.getY() - rectangle.getHeight());
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.draw(rectangle);
    }

    private void drawLabel(Graphics2D g, AffineTransform affineTransform) {
        Point2D.Double transformedPoint = new Point2D.Double();
        affineTransform.transform(getLabelAnchorPoint(), transformedPoint);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getHeight();

        int rectX = (int) transformedPoint.getX() + 10;
        int rectY = (int) transformedPoint.getY() + 5;

        g.setColor(new Color(0, 70, 0, 150));
        g.fillRect(rectX - 2, rectY - textHeight + 2, textWidth + 4, textHeight);

        g.setColor(Color.WHITE);
        g.drawString(label, rectX, rectY);

        g.setColor(this.color);
    }

    private Point2D.Double getLabelAnchorPoint() {

        double x = rectangle.getMaxX();
        double y = rectangle.getCenterY();

        return new Point2D.Double(x, y);
    }

    @Override
    public void draw(Graphics2D g, AffineTransform affineTransform) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        Shape transformedShape = affineTransform.createTransformedShape(rectangle);
        g.draw(transformedShape);

        if (label != null) {
            drawLabel(g, affineTransform);
        }
    }
}
