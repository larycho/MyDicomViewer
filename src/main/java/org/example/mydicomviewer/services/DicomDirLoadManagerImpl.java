package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.listeners.DicomDirLoadedListener;
import org.example.mydicomviewer.processing.dicomdir.DicomDirProcessor;
import org.example.mydicomviewer.views.filelist.FileNodeData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DicomDirLoadManagerImpl implements DicomDirLoadManager {

    private final DicomDirProcessor dicomDirProcessor;
    private final List<DicomDirLoadedListener> listeners = new ArrayList<>();

    @Inject
    public DicomDirLoadManagerImpl(DicomDirProcessor dicomDirProcessor) {
        this.dicomDirProcessor = dicomDirProcessor;
    }

    @Override
    public void openDicomDir(File file) {

        List<FileNodeData> extractedFiles = dicomDirProcessor.openDicomDirectory(file);

        if (!extractedFiles.isEmpty()) {
            fireDicomDirLoadedEvent(extractedFiles);
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
}
