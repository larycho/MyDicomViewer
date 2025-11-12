package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.display.DicomDisplayPanel;
import org.example.mydicomviewer.display.ImagePanelGenerator;
import org.example.mydicomviewer.display.ImagePanelGeneratorImpl;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.views.MainImagePanel;
import org.example.mydicomviewer.views.MultipleImagePanel;

public class ImageDisplayer implements FileLoadedListener {

    private MultipleImagePanel multipleImagePanel;

    public ImageDisplayer(MultipleImagePanel multipleImagePanel) {
        this.multipleImagePanel = multipleImagePanel;
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile dicomFile = event.getFile();

        ImagePanelGenerator generator = new ImagePanelGeneratorImpl();
        DicomDisplayPanel imageNode = generator.createImageNode(dicomFile);
        MainImagePanel imagePanel = new MainImagePanel();
        imagePanel.addImagePanel(imageNode);
        this.multipleImagePanel.addImage(imagePanel);
    }

    public void changeScreenMode(SplitScreenMode mode) {
        if (multipleImagePanel != null) {
            multipleImagePanel.setAndApplyMode(mode);
        }
    }

    public void nextFrame() {
        if (multipleImagePanel.areAnyImagesAdded()) {
            MainImagePanel imagePanel = multipleImagePanel.getSelectedImage();

            boolean displayIsSet = imagePanel.isDisplayPanelSet();

            if (displayIsSet) {
                DicomDisplayPanel displayPanel = imagePanel.getDisplayPanel();
                displayPanel.nextFrame();
            }
        }
    }

    public void previousFrame() {
        if (multipleImagePanel.areAnyImagesAdded()) {
            MainImagePanel imagePanel = multipleImagePanel.getSelectedImage();
            boolean displayIsSet = imagePanel.isDisplayPanelSet();

            if (displayIsSet) {
                DicomDisplayPanel displayPanel = imagePanel.getDisplayPanel();
                displayPanel.previousFrame();
            }
        }
    }

    public void setWindowing(double center, double width) {
        if (multipleImagePanel.areAnyImagesAdded()) {
            MainImagePanel imagePanel = multipleImagePanel.getSelectedImage();
            boolean displayIsSet = imagePanel.isDisplayPanelSet();

            if (displayIsSet) {
                DicomDisplayPanel displayPanel = imagePanel.getDisplayPanel();
                displayPanel.setWindowing(center, width);
            }
        }
    }
}
