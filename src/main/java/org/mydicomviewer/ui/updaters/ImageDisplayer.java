package org.mydicomviewer.ui.updaters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.events.*;
import org.mydicomviewer.ui.display.SplitScreenMode;
import org.mydicomviewer.events.services.*;
import org.mydicomviewer.events.listeners.*;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.processing.windowing.WindowPreset;
import org.mydicomviewer.services.*;
import org.mydicomviewer.ui.MultipleImagePanel;

import org.mydicomviewer.processing.spatial.Axis;
import org.mydicomviewer.ui.image.ImagePanelFactory;
import org.mydicomviewer.ui.image.ImagePanelWrapper;

import java.io.File;
import java.util.List;

@Singleton
public class ImageDisplayer implements FileLoadedListener, PanelSelectedListener, DicomDirLoadedListener, FolderLoadedListener, FragmentedFileSelectedListener, ToolBarEventListener, FrameSkipEventListener {

    private final MultipleImagePanel multipleImagePanel;
    private final ImagePanelSelectedEventService imagePanelSelectedEventService;
    private final OpenFileManager openFileManager;
    private final ImagePanelFactory imagePanelFactory;

    @Inject
    public ImageDisplayer(MultipleImagePanel multipleImagePanel,
                          FileLoadEventService fileLoadEventService,
                          ImagePanelSelectedEventService panelSelectedService,
                          DicomDirLoadManager dicomDirLoadManager,
                          FolderLoadedEventService folderLoadedEventService,
                          FragmentedFileEventService fragmentedFileEventService,
                          ToolBarEventService toolBarEventService,
                          OpenFileManager openFileManager,
                          FrameSkipEventService frameSkipEventService,
                          ImagePanelFactory imagePanelFactory
    ) {
        this.multipleImagePanel = multipleImagePanel;
        this.imagePanelSelectedEventService = panelSelectedService;
        this.openFileManager = openFileManager;
        this.imagePanelFactory = imagePanelFactory;
        fileLoadEventService.addListener(this);
        panelSelectedService.addListener(this);
        dicomDirLoadManager.addListener(this);
        folderLoadedEventService.addListener(this);
        fragmentedFileEventService.addListener(this);
        toolBarEventService.addListener(this);
        frameSkipEventService.addListener(this);
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

        if (dicomFile.isDicomdir()) { return; }
        if (dicomFile.getImages().isEmpty()) { return; }

        ImagePanelWrapper wrapper = imagePanelFactory.createRegularImagePanel(dicomFile);
        this.multipleImagePanel.addImage(wrapper);
        //wrapper.addPanelSelectedService(imagePanelSelectedEventService);
    }

    @Override
    public void fragmentedFileSelected(FragmentedFileSelectedEvent event) {

        if (!isFileOpened(event.getSourceFiles())) { openFileManager.openFragmentedFileUsingWorker(event.getSourceFiles()); }

        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();

        for (ImagePanelWrapper wrapper : wrappers) {

            changeFrameIfCorrectFile(event, wrapper);

        }
    }

    private void changeFrameIfCorrectFile(FragmentedFileSelectedEvent event, ImagePanelWrapper wrapper) {

        if (isCorrectFileForFrameChange(event.getSourceFiles(), wrapper)) {

            int index = event.getInstanceNumber();
            wrapper.moveToFrame(index);

        }
    }


    private boolean isCorrectFileForFrameChange(List<File> sourceFiles, ImagePanelWrapper wrapper) {
        File openedFile = wrapper.getDicomFile().getFile();
        boolean fileOpened = isFileOnList(sourceFiles, openedFile);

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

    @Override
    public void frameSkipped(FrameSkipEvent event) {
        if (! (event.getSource() instanceof FrameSkipManager)) { return; }
        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();
        DicomFile file = event.getSourceFile();

        for (ImagePanelWrapper wrapper : wrappers) {

            if (wrapper.getDicomFile() == file && wrapper.getAxis() == Axis.Z) {
                wrapper.moveToFrame(event.getFrameNumber());
            }

        }
    }
}
