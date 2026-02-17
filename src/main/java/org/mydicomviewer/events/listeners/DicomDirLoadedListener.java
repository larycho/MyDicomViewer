package org.mydicomviewer.events.listeners;

import org.mydicomviewer.events.DicomDirLoadedEvent;

public interface DicomDirLoadedListener {
    void dicomDirLoaded(DicomDirLoadedEvent dicomDirLoadedEvent);
}
