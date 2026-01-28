package org.example.mydicomviewer.events;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class FragmentedFileSelectedEvent extends EventObject {

    private final List<File> sourceFiles;
    int instanceNumber;

    public FragmentedFileSelectedEvent(Object source, List<File> sourceFiles, int instanceNumber) {
        super(source);
        this.sourceFiles = new ArrayList<>(sourceFiles);
        this.instanceNumber = instanceNumber;
    }

    public List<File> getSourceFiles() {
        return sourceFiles;
    }

    public int getInstanceNumber() {
        return instanceNumber;
    }

}
