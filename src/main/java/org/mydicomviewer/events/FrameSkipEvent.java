package org.mydicomviewer.events;

import org.mydicomviewer.models.DicomFile;

import java.util.EventObject;

public class FrameSkipEvent extends EventObject {

    private final DicomFile sourceFile;
    private final int frameNumber;

    public FrameSkipEvent(Object source, DicomFile sourceFile, int frameNumber) {
        super(source);
        this.frameNumber = frameNumber;
        this.sourceFile = sourceFile;
    }

    public DicomFile getSourceFile() {
        return sourceFile;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

}
