package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.display.*;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.events.PanelSelectedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.views.MultipleImagePanel;

import org.example.mydicomviewer.views.image.panel.ImagePanelFactory;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import static java.lang.Math.round;



@Singleton
public class ImageDisplayer implements FileLoadedListener, PanelSelectedListener {

    private final MultipleImagePanel multipleImagePanel;
    private ImagePanelSelectedEventService imagePanelSelectedEventService;

    @Inject
    public ImageDisplayer(MultipleImagePanel multipleImagePanel,
                          FileLoadEventService fileLoadEventService,
                          ImagePanelSelectedEventService panelSelectedService
    ) {
        this.multipleImagePanel = multipleImagePanel;
        this.imagePanelSelectedEventService = panelSelectedService;
        fileLoadEventService.addListener(this);
        panelSelectedService.addListener(this);
    }

    public ImageDisplayer(MultipleImagePanel multipleImagePanel) {
        this.multipleImagePanel = multipleImagePanel;
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile dicomFile = event.getFile();


        ImagePanelWrapper wrapper = ImagePanelFactory.createRegularImagePanel(dicomFile);
        this.multipleImagePanel.addImage(wrapper);
        wrapper.addPanelSelectedService(imagePanelSelectedEventService);
    }

    public void changeScreenMode(SplitScreenMode mode) {
        if (multipleImagePanel != null) {
            multipleImagePanel.setAndApplyMode(mode);
        }
    }

    public void nextFrame() {
        if (multipleImagePanel.areAnyImagesAdded()) {
            ImagePanelWrapper wrapper = multipleImagePanel.getSelectedImage();
            if (wrapper != null) {
                wrapper.moveToNextFrame();
            }
        }
    }

    public void previousFrame() {
        if (multipleImagePanel.areAnyImagesAdded()) {
            ImagePanelWrapper wrapper = multipleImagePanel.getSelectedImage();
            if (wrapper != null) {
                wrapper.moveToPreviousFrame();
            }
        }
    }

    public void setWindowing(int center, int width) {
        if (multipleImagePanel.areAnyImagesAdded()) {
            ImagePanelWrapper imagePanel = multipleImagePanel.getSelectedImage();
            if (imagePanel != null) {
                imagePanel.setWindowing(center, width);
            }
        }
    }

    public void setWindowing(double center, double width) {
        if (multipleImagePanel.areAnyImagesAdded()) {
            ImagePanelWrapper imagePanel = multipleImagePanel.getSelectedImage();
            if (imagePanel != null) {
                imagePanel.setWindowing((int) round(center), (int) round(width));
            }
        }
    }

    @Override
    public void panelSelected(PanelSelectedEvent event) {
        multipleImagePanel.setSelectedImage(event.getPanel());
    }
}
