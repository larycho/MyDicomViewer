package org.example.mydicomviewer.views.image;

import org.example.mydicomviewer.models.shapes.FreehandLine;
import org.example.mydicomviewer.views.NestedImagePanel;
import org.example.mydicomviewer.views.image.panel.InnerImagePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class PencilTool implements DrawingTool {

    private InnerImagePanel imagePanel;
    private FreehandLine line;

//    public PencilTool(NestedImagePanel imagePanel) {
//        this.imagePanel = imagePanel;
//    }

    public PencilTool() {}

//    public void setImagePanel(NestedImagePanel imagePanel) {
//        this.imagePanel = imagePanel;
//    }

    public void setImagePanel(InnerImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (imagePanel == null) {return;}

        Point2D.Double p = transformPoint(e.getX(), e.getY());

        line = new FreehandLine(Color.GREEN, 3.0f);
        line.start(p.getX(), p.getY());
        imagePanel.addShape(line);
        imagePanel.refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (imagePanel == null || line == null) {return;}

        Point2D.Double p = transformPoint(e.getX(), e.getY());

        line.end(p.getX(), p.getY());
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
        if (imagePanel == null || line == null) {return;}

        Point2D.Double p = transformPoint(e.getX(), e.getY());

        line.lineTo(p.getX(), p.getY());
        imagePanel.refresh();
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
        return "Pencil";
    }
}
