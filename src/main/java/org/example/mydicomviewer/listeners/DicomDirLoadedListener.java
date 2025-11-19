package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.DicomDirLoadedEvent;

public interface DicomDirLoadedListener {
    void dicomDirLoaded(DicomDirLoadedEvent dicomDirLoadedEvent);
}
