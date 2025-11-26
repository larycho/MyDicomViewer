package org.example.mydicomviewer.display;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.processing.image.WindowingProcessor;
import org.example.mydicomviewer.processing.image.WindowingProcessorImpl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImagePanelManager {

    private final DicomFile dicomFile;

    private int windowLevel = 150;
    private int windowWidth = 300;
    private int currentFrame = 0;

    public ImagePanelManager(DicomFile dicomFile) {
        this.dicomFile = dicomFile;
    }

    public OverlayText getOverlay() {
        OverlayGenerator overlayGenerator = new OverlayGeneratorImpl();
        return overlayGenerator.createOverlayText(dicomFile);
    }

    public int getWindowLevel() {
        return windowLevel;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowLevel(int windowLevel) {
        this.windowLevel = windowLevel;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public BufferedImage moveToNextFrame() {
        int maxIndex = dicomFile.getImages().size() - 1;
        if (currentFrame == maxIndex) {
            currentFrame = 0;
        }
        else {
            currentFrame++;
        }
        return getFrameForDisplay();
    }

    public BufferedImage moveToPreviousFrame() {
        int maxIndex = dicomFile.getImages().size() - 1;
        if (currentFrame == 0) {
            currentFrame = maxIndex;
        }
        else {
            currentFrame--;
        }
        return getFrameForDisplay();
    }

    public void setCurrentFrameNumber(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void changeWindowLevelBy(int delta) {
        windowLevel = windowLevel + delta;
    }

    public void changeWindowWidthBy(int delta) {
        windowWidth = windowWidth + delta;
    }

    public BufferedImage getFrameForDisplay() {
        List<DicomImage> images = this.dicomFile.getImages();
        DicomImage currentImage = images.get(currentFrame);

        WindowingProcessor windowingProcessor = new WindowingProcessorImpl();
        return windowingProcessor.applyWindowing(currentImage.getImage(), windowLevel, windowWidth);
    }
}
