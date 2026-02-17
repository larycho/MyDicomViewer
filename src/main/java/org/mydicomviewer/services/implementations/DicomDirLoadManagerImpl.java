package org.mydicomviewer.services.implementations;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.events.DicomDirLoadedEvent;
import org.mydicomviewer.events.listeners.DicomDirLoadedListener;
import org.mydicomviewer.processing.io.dicomdir.DicomDirProcessor;
import org.mydicomviewer.services.DicomDirLoadManager;
import org.mydicomviewer.services.OpenFileManager;
import org.mydicomviewer.ui.filelist.FileNodeData;
import org.mydicomviewer.ui.filelist.FileTreeNodeService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DicomDirLoadManagerImpl implements DicomDirLoadManager {

    private final DicomDirProcessor dicomDirProcessor;
    private final OpenFileManager openFileManager;
    private final FileTreeNodeService fileTreeNodeService;
    private final List<DicomDirLoadedListener> listeners = new ArrayList<>();

    @Inject
    public DicomDirLoadManagerImpl(DicomDirProcessor dicomDirProcessor,
                                   OpenFileManager openFileManager,
                                   FileTreeNodeService fileTreeNodeService) {

        this.dicomDirProcessor = dicomDirProcessor;
        this.openFileManager = openFileManager;
        this.fileTreeNodeService = fileTreeNodeService;
    }

    @Override
    public void openDicomDir(File file) {

        List<FileNodeData> extractedFiles = dicomDirProcessor.openDicomDirectory(file);

        if (!extractedFiles.isEmpty()) {
            fireDicomDirLoadedEvent(extractedFiles);
            triggerFileOpen(extractedFiles);
        }
    }

    public void addListener(DicomDirLoadedListener listener) {
        listeners.add(listener);
    }

    private void fireDicomDirLoadedEvent(List<FileNodeData> files) {
        DicomDirLoadedEvent event = new DicomDirLoadedEvent(this, files);
        for (DicomDirLoadedListener listener : listeners) {
            listener.dicomDirLoaded(event);
        }
    }

    private void triggerFileOpen(List<FileNodeData> data) {
        if (data.isEmpty()) {
            return;
        }
        FileNodeData firstFile = data.get(0);
        if (firstFile.getFile().isEmpty()) {
            return;
        }

        List<File> otherFiles = findFilesFromTheSameSeries(firstFile, data);

        if (otherFiles.isEmpty()) {
            openFileManager.openFileUsingWorker(firstFile.getFile().get());
        }
        else {
            otherFiles.add(firstFile.getFile().get());
            openFileManager.openFragmentedFileUsingWorker(otherFiles);
        }
    }

    private List<File> findFilesFromTheSameSeries(FileNodeData mainFile, List<FileNodeData> data) {
        List<FileNodeData> nodes = fileTreeNodeService.getFileNodesWithSeriesUid(mainFile, data);
        return fileTreeNodeService.convertFileNodesToList(nodes);
    }
}
