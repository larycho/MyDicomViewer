package org.example.mydicomviewer.views.image.panel;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.views.image.DrawingTool;
import org.example.mydicomviewer.views.image.ImageTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InnerImagePanelImpl extends JPanel implements InnerImagePanel {

    private final ImagePanelWrapper imagePanelWrapper;
    private final ImageManager imageManager;

    private BufferedImage image;
    private ImageTool tool;

    private OverlayText overlayText;
    private boolean overlayEnabled;

    private final ArrayList<DrawableShape> shapes = new ArrayList<>();

    private double zoom = 1.0;
    private Point offset = new Point(0, 0);

    private JLabel topLeft;
    private JLabel topRight;
    private JLabel bottomLeft;
    private JLabel bottomRight;

    private boolean hasBeenDrawn = false;

    public InnerImagePanelImpl(ImagePanelWrapper imagePanelWrapper, ImageManager imageManager) {
        this.imagePanelWrapper = imagePanelWrapper;
        this.imageManager = imageManager;

        setVisualParameters();
        setOverlay();
    }

    private void setVisualParameters() {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void setOverlay() {
        overlayText = imagePanelWrapper.getOverlay();
        addCornerLabels();
        updateBottomRight();
    }

    private void addCentering() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerImage();
                removeComponentListener(this);
            }
        });
    }

    private void addCornerLabels() {
        if (!overlayEnabled) { return; }

        addTopLeftText();
        addTopRightText();
        addBottomLeftText();
        addBottomRightText();
    }

    private void addTopLeftText() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        String topLeftText = toHtml(overlayText.getTopLeft());
        topLeft = new JLabel(topLeftText);
        topLeft.setForeground(Color.white);
        add(topLeft, c);
    }

    private void addTopRightText() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        String topRightText = toHtml(overlayText.getTopRight());
        topRight = new JLabel(topRightText);
        topRight.setForeground(Color.white);
        add(topRight, c);
    }

    private void addBottomLeftText() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        String bottomLeftText = toHtml(overlayText.getBottomLeft());
        bottomLeft = new JLabel(bottomLeftText);
        bottomLeft.setForeground(Color.white);
        add(bottomLeft, c);
    }

    private void addBottomRightText() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        String bottomRightText = toHtml(overlayText.getBottomRight());
        bottomRight = new JLabel(bottomRightText);
        bottomRight.setForeground(Color.white);
        add(bottomRight, c);
    }

    private String toHtml(String text) {
        String[] lines = text.split("\n");
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        for (String line : lines) {
            html.append(line);
            html.append("<br>");
        }
        html.append("</html>");
        return html.toString();
    }

    private void updateBottomRight() {
        if (!overlayEnabled) { return; }

        String bottomRightText = overlayText.getBottomRight();

        DecimalFormat df = new DecimalFormat("0.00");
        String zoomText = "Zoom: " + df.format(zoom);
        String windowLevelText = "Window Level: " + imagePanelWrapper.getWindowLevel();
        String windowWidthText = "Window Width: " + imagePanelWrapper.getWindowWidth();

        String combined = bottomRightText + "\n" + zoomText + "\n" + windowLevelText + "\n" + windowWidthText;
        bottomRight.setText(toHtml(combined));
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
        updateBottomRight();
        repaint();
    }

    @Override
    public void setShapes(List<DrawableShape> shapes) {
        this.shapes.clear();
        this.shapes.addAll(shapes);
    }

    @Override
    public void addShape(DrawableShape shape) {
        shapes.add(shape);
        imageManager.addShape(shape);
    }

    @Override
    public void setImageTool(ImageTool imageTool) {
        if (tool != null) {
            removeMouseListener(tool);
            removeMouseMotionListener(tool);
            removeMouseWheelListener(tool);
        }

        tool = imageTool;
        addMouseListener(tool);
        addMouseMotionListener(tool);
        addMouseWheelListener(tool);

    }

    @Override
    public void setDrawingTool(DrawingTool drawingTool) {
        setImageTool(drawingTool);
        drawingTool.setImagePanel(this);
    }

    @Override
    public void toggleOverlay() {
        overlayEnabled = !overlayEnabled;
        if (overlayEnabled) {
            setOverlay();
        }
        else {
            clearOverlay();
        }
    }

    private void clearOverlay() {
        remove(topLeft);
        remove(topRight);
        remove(bottomLeft);
        remove(bottomRight);
        repaint();
    }

    @Override
    public double getZoom() {
        return zoom;
    }

    @Override
    public void setZoom(double zoom) {
        this.zoom = zoom;
        updateBottomRight();
    }

    @Override
    public void setOffset(Point p) {
        offset = new Point(p);
    }

    @Override
    public Point getOffset() {
        return new Point(offset);
    }

    @Override
    public void refresh() {
        repaint();
    }

    @Override
    public double getDistance(Point2D.Double p1, Point2D.Double p2) {
        return imageManager.getDistance(p1, p2);
    }

    @Override
    public void centerImage() {

        if (!imageCanBeDrawn()) { return; }

        double newZoom = calculateZoomForCentering();
        setZoom(newZoom);

        Point newOffset = calculateOffsetForCentering();
        setOffset(newOffset);
    }

    private Point calculateOffsetForCentering() {

        int x = getHorizontalCenteringOffset();
        int y = getVerticalCenteringOffset();

        return new Point(x, y);
    }

    private int getHorizontalCenteringOffset() {

        int panelWidth = this.getWidth();
        int imageWidth = (int) (image.getWidth() * zoom);

        return (panelWidth - imageWidth) / 2;
    }

    private int getVerticalCenteringOffset() {

        int panelHeight = this.getHeight();

        double ratio = imageManager.getAspectRatioShift();
        double imageHeight = image.getHeight() * zoom * ratio;

        return (int) ((panelHeight - imageHeight) / 2);
    }

    private double calculateZoomForCentering() {

        double horizontalZoom = calculateZoomForHorizontalFilling();
        double verticalZoom = calculateZoomForVerticalFilling();

        return Math.min(horizontalZoom, verticalZoom);
    }

    private double calculateZoomForHorizontalFilling() {

        int panelWidth = this.getWidth();
        int imageWidth = image.getWidth();

        return (double) panelWidth / imageWidth;
    }

    private double calculateZoomForVerticalFilling() {

        int panelHeight = this.getHeight();
        int imageHeight = image.getHeight();
        double ratio = imageManager.getAspectRatioShift();

        return (double) panelHeight / (ratio * imageHeight);
    }

    private boolean imageCanBeDrawn() {

        if (image == null) { return false; }

        return image.getWidth() != 0 && image.getHeight() != 0;
    }

    @Override
    public AffineTransform getTransform() {
        double ratio = imageManager.getAspectRatioShift();

        AffineTransform transform = new AffineTransform();
        transform.translate(offset.x, offset.y);
        transform.scale(zoom, zoom * ratio);

        return transform;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!hasBeenDrawn) {
            centerImage();
            hasBeenDrawn = true;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        AffineTransform transform = getTransform();

        g2.drawImage(image, transform, null);

        for (DrawableShape shape : shapes) {
            shape.draw(g2, transform);
        }
    }
}
