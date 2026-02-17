package org.mydicomviewer.events;

import java.io.File;
import java.util.EventObject;

public class FileLoadStartedEvent extends EventObject {

    private final File file;

    public FileLoadStartedEvent(Object source, File file) {
        super(source);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
