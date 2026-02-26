package org.mydicomviewer.services.implementations;

import com.google.inject.Inject;
import org.mydicomviewer.events.services.FrameSkipEventService;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.services.OpenFileManager;
import org.mydicomviewer.ui.MultipleImagePanel;
import org.mydicomviewer.ui.image.ImagePanelWrapper;

import java.io.File;
import java.util.List;

public class FrameSkipManagerImpl implements org.mydicomviewer.services.FrameSkipManager {

    private final MultipleImagePanel multipleImagePanel;
    private final OpenFileManager openFileManager;
    private final FrameSkipEventService frameSkipEventService;

    @Inject
    public FrameSkipManagerImpl(MultipleImagePanel multipleImagePanel,
                                OpenFileManager openFileManager,
                                FrameSkipEventService frameSkipEventService) {
        this.multipleImagePanel = multipleImagePanel;
        this.openFileManager = openFileManager;
        this.frameSkipEventService = frameSkipEventService;
    }

    @Override
    public void manageFileSkip(List<File> fileList, int frame) {
        DicomFile dicomFile = isFileOpened(fileList);
        if (dicomFile == null) {
            openFileManager.openFragmentedFileUsingWorker(fileList);
        }
        else {
            frameSkipEventService.notifyListeners(this, dicomFile, frame);
        }
    }

    private DicomFile isFileOpened(List<File> files) {
        List<ImagePanelWrapper> wrappers = multipleImagePanel.getAllImages();

        for (ImagePanelWrapper wrapper : wrappers) {

            File openedFile = wrapper.getDicomFile().getFile();
            boolean isOnList = isFileOnList(files, openedFile);

            if (isOnList) {
                return wrapper.getDicomFile();
            }
        }

        return null;
    }

    private boolean isFileOnList(List<File> files, File openedFile) {
        for (File file : files) {
            if (openedFile.getAbsolutePath().equals(file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }
}
