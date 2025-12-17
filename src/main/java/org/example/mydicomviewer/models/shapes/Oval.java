package org.example.mydicomviewer.models.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Oval implements DrawableShape {

    private Ellipse2D.Double oval;
    private Point2D.Double origin;
    private Color color;
    private float thickness;

    private boolean isBeingDrawn = false;
    private boolean endingDrawing = false;

    private String label;

    public Oval(Color color, float thickness) {
        this.oval = new Ellipse2D.Double();
        this.color = color;
        this.thickness = thickness;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void start(double x, double y) {
        origin = new Point2D.Double(x, y);
        oval.setFrame(x, y, 1, 1);
        isBeingDrawn = true;
    }

    public void setBottomRightPoint(double x, double y) {
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

        oval.setFrame(start.getX(), start.getY(), width, height);
    }

    public void end(double x, double y) {
        setBottomRightPoint(x, y);
        isBeingDrawn = false;
        endingDrawing = true;
    }

    public double getWidth() {
        return oval.getWidth();
    }

    public double getHeight() {
        return oval.getHeight();
    }

    public Point2D.Double getOrigin() {
        return new Point2D.Double(origin.getX(), origin.getY());
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.draw(oval);
    }

    @Override
    public void draw(Graphics2D g, AffineTransform affineTransform) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        Shape transformedShape = affineTransform.createTransformedShape(oval);
        g.draw(transformedShape);

        if (label != null) {
            drawLabel(g, affineTransform);
        }

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

        double x = oval.getMaxX();
        double y = oval.getCenterY();

        return new Point2D.Double(x, y);
    }
}
