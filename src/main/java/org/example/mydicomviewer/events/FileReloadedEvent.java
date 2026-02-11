package org.example.mydicomviewer.events;

import java.io.File;
import java.util.EventObject;

public class FileReloadedEvent extends EventObject {

    private final File file;

    public FileReloadedEvent(Object source, File file) {
        super(source);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

}
