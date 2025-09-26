package org.example.mydicomviewer.events;

import org.example.mydicomviewer.models.DicomFile;

import java.util.EventObject;

public class FileLoadedEvent extends EventObject {

    private DicomFile file;

    public FileLoadedEvent(Object source, DicomFile file) {
        super(source);
        this.file = file;
    }

    public DicomFile getFile() {
        return file;
    }
}
