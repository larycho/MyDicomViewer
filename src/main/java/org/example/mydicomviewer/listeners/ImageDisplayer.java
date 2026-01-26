package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.display.*;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.events.PanelSelectedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.image.WindowPreset;
import org.example.mydicomviewer.services.*;
import org.example.mydicomviewer.views.MultipleImagePanel;

import org.example.mydicomviewer.views.image.panel.ImagePanelFactory;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import java.util.List;

import static java.lang.Math.round;



@Singleton
public class ImageDisplayer implements FileLoadedListener, PanelSelectedListener, DicomDirLoadedListener, FolderLoadedListener {

    private final MultipleImagePanel multipleImagePanel;
    private final ImagePanelSelectedEventService imagePanelSelectedEventService;

    @Inject
    public ImageDisplayer(MultipleImagePanel multipleImagePanel,
                          FileLoadEventService fileLoadEventService,
                          ImagePanelSelectedEventService panelSelectedService,
                          DicomDirLoadManager dicomDirLoadManager,
                          FolderLoadedEventService folderLoadedEventService
    ) {
        this.multipleImagePanel = multipleImagePanel;
        this.imagePanelSelectedEventService = panelSelectedService;
        fileLoadEventService.addListener(this);
        panelSelectedService.addListener(this);
        dicomDirLoadManager.addListener(this);
        folderLoadedEventService.addListener(this);
    }

    @Override
    public void folderLoaded(FolderLoadedEvent event) {
        this.multipleImagePanel.clearDisplay();
    }

    @Override
    public void dicomDirLoaded(DicomDirLoadedEvent dicomDirLoadedEvent) {
        this.multipleImagePanel.clearDisplay();
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

    public void setPreset(WindowPreset preset) {
        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();

        for (ImagePanelWrapper wrapper : wrappers) {
            wrapper.setWindowing(preset.getLevel(), preset.getWidth());
        }
    }

    public void setDefaultWindowing() {
        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();

        for (ImagePanelWrapper wrapper : wrappers) {
            wrapper.resetWindowing();
        }
    }

    @Override
    public void panelSelected(PanelSelectedEvent event) {
        multipleImagePanel.setSelectedImage(event.getPanel());
    }
}
