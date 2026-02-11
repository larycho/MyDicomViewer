package org.example.mydicomviewer.services;

import org.example.mydicomviewer.listeners.FileLoadedListener;
import org.example.mydicomviewer.models.DicomFile;

public interface FileLoadEventService {

    void addListener(FileLoadedListener listener);
    void notifyFinished(DicomFile file);
    void notifyFinished(DicomFile file, Object source);
}
