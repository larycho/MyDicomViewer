package org.mydicomviewer.events.services;

import org.mydicomviewer.events.listeners.FileLoadedListener;
import org.mydicomviewer.models.DicomFile;

public interface FileLoadEventService {

    void addListener(FileLoadedListener listener);
    void notifyFinished(DicomFile file);
    void notifyFinished(DicomFile file, Object source);
}
