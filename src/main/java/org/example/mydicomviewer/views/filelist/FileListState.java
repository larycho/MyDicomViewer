package org.example.mydicomviewer.views.filelist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListState {

    private List<File> displayedFiles = new ArrayList<File>();

    public void addFile(File file) {
        displayedFiles.add(file);
    }

    public void addFiles(List<File> files) {
        displayedFiles.addAll(files);
    }

    public boolean containsFile(File file) {
        for (File displayedFile : displayedFiles) {
            if (displayedFile.getAbsolutePath().equals(file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }

}
