package org.mydicomviewer.events.services;

import org.mydicomviewer.events.listeners.FrameSkipEventListener;
import org.mydicomviewer.models.DicomFile;

public interface FrameSkipEventService {
    void addListener(FrameSkipEventListener listener);
    void notifyListeners(Object source, DicomFile sourceFiles, int fragmentIndex);
}
