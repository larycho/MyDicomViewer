package org.mydicomviewer.tools.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class StraightLine implements DrawableShape {

    private Line2D.Double line;
    private Color color;
    private float width;

    private String label;

    public StraightLine(Color color, float width) {
        this.color = color;
        this.width = width;
        this.line = new Line2D.Double();
    }

    public void start(double x, double y) {
        line.setLine(x, y, x, y);
    }

    public void setEndPoint(double x, double y) {
        line.setLine(line.getX1(), line.getY1(), x, y);
    }

    public void end(double x, double y) {
        setEndPoint(x, y);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Point2D.Double getStartPoint() {
        return new Point2D.Double(line.getX1(), line.getY1());
    }

    public Point2D.Double getEndPoint() {
        return new Point2D.Double(line.getX2(), line.getY2());
    }

    @Override
    public void draw(Graphics2D g, AffineTransform affineTransform) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        Shape transformedShape = affineTransform.createTransformedShape(line);
        g.draw(transformedShape);

        if (label != null) {
            drawLabel(g, affineTransform);
        }
    }

    private void drawLabel(Graphics2D g, AffineTransform affineTransform) {
        Point2D.Double transformedPoint = new Point2D.Double();
        affineTransform.transform(getEndPoint(), transformedPoint);
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
}
