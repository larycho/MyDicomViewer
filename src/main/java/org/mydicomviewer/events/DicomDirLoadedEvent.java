package org.mydicomviewer.events;

import org.mydicomviewer.ui.filelist.FileNodeData;

import java.util.EventObject;
import java.util.List;

public class DicomDirLoadedEvent extends EventObject {

    private final List<FileNodeData> extractedFiles;

    public DicomDirLoadedEvent(Object source, List<FileNodeData> extractedFiles) {
        super(source);
        this.extractedFiles = extractedFiles;
    }

    public List<FileNodeData> getExtractedFiles() {
        return extractedFiles;
    }
}
