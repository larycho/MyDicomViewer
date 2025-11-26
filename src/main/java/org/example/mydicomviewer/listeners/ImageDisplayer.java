package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.display.*;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.views.MultipleImagePanel;
import org.example.mydicomviewer.views.SingularImagePanel;

import static java.lang.Math.round;


@Singleton
public class ImageDisplayer implements FileLoadedListener {

    private final MultipleImagePanel multipleImagePanel;

    @Inject
    public ImageDisplayer(MultipleImagePanel multipleImagePanel, FileLoadEventService fileLoadEventService) {
        this.multipleImagePanel = multipleImagePanel;
        fileLoadEventService.addListener(this);
    }

    public ImageDisplayer(MultipleImagePanel multipleImagePanel) {
        this.multipleImagePanel = multipleImagePanel;
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile dicomFile = event.getFile();

        ImagePanelManager imagePanelManager = new ImagePanelManager(dicomFile);
        SingularImagePanel singleImagePanel = new SingularImagePanel(imagePanelManager);

        this.multipleImagePanel.addImage(singleImagePanel);
    }

    public void changeScreenMode(SplitScreenMode mode) {
        if (multipleImagePanel != null) {
            multipleImagePanel.setAndApplyMode(mode);
        }
    }

    public void nextFrame() {
        if (multipleImagePanel.areAnyImagesAdded()) {
            SingularImagePanel imagePanel = multipleImagePanel.getSelectedImage();
            imagePanel.moveToNextFrame();
        }
    }

    public void previousFrame() {
        if (multipleImagePanel.areAnyImagesAdded()) {
            SingularImagePanel imagePanel = multipleImagePanel.getSelectedImage();
            imagePanel.moveToPreviousFrame();
        }
    }

    public void setWindowing(int center, int width) {
        if (multipleImagePanel.areAnyImagesAdded()) {
            SingularImagePanel imagePanel = multipleImagePanel.getSelectedImage();
            imagePanel.setWindowing(center, width);
        }
    }

    public void setWindowing(double center, double width) {
        if (multipleImagePanel.areAnyImagesAdded()) {
            SingularImagePanel imagePanel = multipleImagePanel.getSelectedImage();
            imagePanel.setWindowing((int) round(center), (int) round(width));
        }
    }
}
