package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.display.*;
import org.example.mydicomviewer.events.*;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.image.WindowPreset;
import org.example.mydicomviewer.services.*;
import org.example.mydicomviewer.views.MultipleImagePanel;

import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImagePanelFactory;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import java.io.File;
import java.util.List;

@Singleton
public class ImageDisplayer implements FileLoadedListener, PanelSelectedListener, DicomDirLoadedListener, FolderLoadedListener, FragmentedFileSelectedListener, ToolBarEventListener {

    private final MultipleImagePanel multipleImagePanel;
    private final ImagePanelSelectedEventService imagePanelSelectedEventService;

    @Inject
    public ImageDisplayer(MultipleImagePanel multipleImagePanel,
                          FileLoadEventService fileLoadEventService,
                          ImagePanelSelectedEventService panelSelectedService,
                          DicomDirLoadManager dicomDirLoadManager,
                          FolderLoadedEventService folderLoadedEventService,
                          FragmentedFileEventService fragmentedFileEventService,
                          ToolBarEventService toolBarEventService
    ) {
        this.multipleImagePanel = multipleImagePanel;
        this.imagePanelSelectedEventService = panelSelectedService;
        fileLoadEventService.addListener(this);
        panelSelectedService.addListener(this);
        dicomDirLoadManager.addListener(this);
        folderLoadedEventService.addListener(this);
        fragmentedFileEventService.addListener(this);
        toolBarEventService.addListener(this);
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

    @Override
    public void fragmentedFileSelected(FragmentedFileSelectedEvent event) {

        if (!isFileOpened(event.getSourceFiles())) { return; }

        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();

        for (ImagePanelWrapper wrapper : wrappers) {

            changeFrameIfCorrectFile(event, wrapper);

        }
    }

    private void changeFrameIfCorrectFile(FragmentedFileSelectedEvent event, ImagePanelWrapper wrapper) {

        if (isCorrectFileForFrameChange(event, wrapper)) {

            int index = event.getInstanceNumber();
            wrapper.moveToFrame(index);

        }
    }

    private boolean isCorrectFileForFrameChange(FragmentedFileSelectedEvent event, ImagePanelWrapper wrapper) {
        File openedFile = wrapper.getDicomFile().getFile();
        boolean fileOpened = isFileOnList(event.getSourceFiles(), openedFile);

        Axis axis = wrapper.getAxis();

        return fileOpened && (axis == Axis.Z);
    }

    private boolean isFileOpened(List<File> files) {
        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();

        for (ImagePanelWrapper wrapper : wrappers) {

            File openedFile = wrapper.getDicomFile().getFile();
            boolean isOnList = isFileOnList(files, openedFile);

            if (isOnList) {
                return true;
            }
        }

        return false;
    }

    private boolean isFileOnList(List<File> files, File openedFile) {
        for (File file : files) {
            if (openedFile.getAbsolutePath().equals(file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void panelSelected(PanelSelectedEvent event) {
        multipleImagePanel.setSelectedImage(event.getPanel());
    }

    @Override
    public void presetSelected(PresetSelectedEvent event) {

        WindowPreset preset = event.getPreset();
        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();

        for (ImagePanelWrapper wrapper : wrappers) {
            wrapper.setWindowing(preset.getLevel(), preset.getWidth());
        }
    }

    @Override
    public void screenModeSelected(ScreenModeSelectedEvent event) {
        SplitScreenMode mode = event.getMode();

        if (multipleImagePanel != null) {
            multipleImagePanel.setAndApplyMode(mode);
        }
    }

    @Override
    public void windowingChanged(WindowingChangedEvent event) {
        int center = event.getWindowCenter();
        int width = event.getWindowWidth();

        if (multipleImagePanel.areAnyImagesAdded()) {
            ImagePanelWrapper imagePanel = multipleImagePanel.getSelectedImage();
            if (imagePanel != null) {
                imagePanel.setWindowing(center, width);
            }
        }
    }

    @Override
    public void windowingReset(WindowingResetEvent event) {
        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();

        for (ImagePanelWrapper wrapper : wrappers) {
            wrapper.resetWindowing();
        }
    }
}
