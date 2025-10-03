package org.example.mydicomviewer.display;

import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;
import com.pixelmed.display.event.FrameSelectionChangeEvent;
import com.pixelmed.display.event.WindowCenterAndWidthChangeEvent;
import com.pixelmed.event.ApplicationEventDispatcher;
import com.pixelmed.event.EventContext;

import javax.swing.*;
import java.awt.*;

public class DicomDisplayPanelImpl extends JPanel implements DicomDisplayPanel {

    private SingleImagePanel imagePanel;
    private SourceImage sourceImage;
    private int currentFrameIndex = 0;
    private int maxFrameIndex;

    public DicomDisplayPanelImpl(SingleImagePanel panel, SourceImage sourceImage) {
        this.imagePanel = panel;
        this.sourceImage = sourceImage;
        this.maxFrameIndex = sourceImage.getNumberOfFrames();
    }

    private void setCurrentFrameIndex(int index) {
        this.currentFrameIndex = index;
    }

    @Override
    public void refresh() {
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    @Override
    public JComponent getPanel() {
        return imagePanel;
    }

    @Override
    public Dimension getPreferredSize() {
        return imagePanel.getPreferredSize();
    }

    @Override
    public void nextFrame() {
        int nextIndex = calculateNextFrameIndex();

        EventContext context = new EventContext("Requested next frame");
        FrameSelectionChangeEvent event = new FrameSelectionChangeEvent(context, nextIndex);
        ApplicationEventDispatcher.getApplicationEventDispatcher().processEvent(event);

        setCurrentFrameIndex(nextIndex);
    }

    private int calculateNextFrameIndex() {
        if ( (currentFrameIndex + 1) < maxFrameIndex) {
            return currentFrameIndex + 1;
        }
        else return 0;
    }

    @Override
    public void previousFrame() {
        int previousIndex = calculatePreviousFrameIndex();

        EventContext context = new EventContext("Requested previous frame");
        FrameSelectionChangeEvent event = new FrameSelectionChangeEvent(context, previousIndex);
        ApplicationEventDispatcher.getApplicationEventDispatcher().processEvent(event);

        setCurrentFrameIndex(previousIndex);
    }

    private int calculatePreviousFrameIndex() {
        if ( currentFrameIndex == 0 ) {
            return maxFrameIndex - 1;
        }
        else return currentFrameIndex - 1;
    }

    public void setWindowing(double center, double width) {
        EventContext context = new EventContext("Requested change of window to one of width: " + width + ", center: " + center);
        WindowCenterAndWidthChangeEvent event = new WindowCenterAndWidthChangeEvent(context, center, width);
        ApplicationEventDispatcher.getApplicationEventDispatcher().processEvent(event);
    }
}
