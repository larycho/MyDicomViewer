package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.listeners.DicomDirLoadedListener;
import org.example.mydicomviewer.models.DicomDirectory;
import org.example.mydicomviewer.processing.dicomdir.DicomDirProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DicomDirLoadManagerImpl implements DicomDirLoadManager {

    private DicomDirProcessor dicomDirProcessor;
    private List<DicomDirLoadedListener> listeners = new ArrayList<>();

    @Inject
    public DicomDirLoadManagerImpl(DicomDirProcessor dicomDirProcessor) {
        this.dicomDirProcessor = dicomDirProcessor;
    }

    @Override
    public void openDicomDir(File file) {
        DicomDirectory directory = dicomDirProcessor.openDicomDirectory(file);
        if (directory != null) {
            fireDicomDirLoadedEvent(directory);
        }
    }

    public void addListener(DicomDirLoadedListener listener) {
        listeners.add(listener);
    }

    private void fireDicomDirLoadedEvent(DicomDirectory directory) {
        DicomDirLoadedEvent event = new DicomDirLoadedEvent(this, directory);
        for (DicomDirLoadedListener listener : listeners) {
            listener.dicomDirLoaded(event);
        }
    }
}
