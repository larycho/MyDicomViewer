package org.example.mydicomviewer.views.image;

import org.example.mydicomviewer.models.shapes.Oval;
import org.example.mydicomviewer.models.shapes.StraightLine;
import org.example.mydicomviewer.views.NestedImagePanel;
import org.example.mydicomviewer.views.image.panel.InnerImagePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class OvalTool implements DrawingTool {
    private InnerImagePanel imagePanel;
    private Oval oval;

    public OvalTool() {}

    public void setImagePanel(InnerImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        oval = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (imagePanel == null) { return; }

        oval = new Oval(Color.GREEN, 3.0f);
        Point2D.Double p = transformPoint(e.getX(), e.getY());
        oval.start(p.getX(), p.getY());

        imagePanel.addShape(oval);
        updateLabel();
        imagePanel.refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (imagePanel == null || oval == null) {return;}

        Point2D.Double p = transformPoint(e.getX(), e.getY());
        oval.end(p.getX(), p.getY());

        updateLabel();
        imagePanel.refresh();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (imagePanel == null || oval == null) { return; }

        Point2D.Double p = transformPoint(e.getX(), e.getY());
        oval.setBottomRightPoint(p.getX(), p.getY());

        updateLabel();
        imagePanel.refresh();

    }

    private void updateLabel() {

        double area = calculateArea();
        double perimeter = calculatePerimeter();

        String areaLabel = String.format("Area: %.2f mm2 ", area);
        String perimeterLabel = String.format("Perimeter: %.2f mm2", perimeter);

        String label = areaLabel + "\n" + perimeterLabel;
        oval.setLabel(label);
    }

    private double calculateArea() {

        double a = getSemiMajorAxis();
        double b = getSemiMinorAxis();

        return a * b * Math.PI;
    }

    private double calculatePerimeter() {

        double a = getSemiMajorAxis();
        double b = getSemiMinorAxis();

        return Math.PI * Math.sqrt(2 * (a * a) + (b * b));
    }

    private double getSemiMajorAxis() {
        Point2D.Double origin = oval.getOrigin();
        double width = oval.getWidth();
        double height = oval.getHeight();

        Point2D.Double start = new Point2D.Double(origin.getX() + ( width / 2 ), origin.getY());
        Point2D.Double end = new Point2D.Double(origin.getX() + ( width / 2 ), origin.getY() + ( height / 2 ));

        return imagePanel.getDistance(start, end);
    }

    private double getSemiMinorAxis() {
        Point2D.Double origin = oval.getOrigin();
        double width = oval.getWidth();
        double height = oval.getHeight();

        Point2D.Double b1 = new Point2D.Double(origin.getX(), origin.getY() + ( height / 2 ));
        Point2D.Double b2 = new Point2D.Double(origin.getX() + ( width / 2 ), origin.getY() + ( height / 2 ));

        return imagePanel.getDistance(b1, b2);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    private Point2D.Double transformPoint(double x, double y) {
        double zoom = imagePanel.getZoom();
        Point pan = imagePanel.getOffset();

        try {
            AffineTransform transform = new AffineTransform();
            transform.scale(zoom, zoom);
            transform.translate(pan.x, pan.y);
            return (Point2D.Double) transform.inverseTransform(new Point2D.Double(x, y), null);
        } catch (NoninvertibleTransformException e) {
            return new Point2D.Double(x, y);
        }
    }

    @Override
    public String toString() {
        return "Ellipse";
    }
}
