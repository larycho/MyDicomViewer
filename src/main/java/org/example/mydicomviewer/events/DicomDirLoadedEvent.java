package org.example.mydicomviewer.events;

import org.example.mydicomviewer.models.DicomDirectory;

import java.util.EventObject;

public class DicomDirLoadedEvent extends EventObject {

    private DicomDirectory dicomDirectory;
    public DicomDirLoadedEvent(Object source, DicomDirectory directory) {
        super(source);
        this.dicomDirectory = directory;
    }

    public DicomDirectory getDicomDirectory() {
        return dicomDirectory;
    }
}
