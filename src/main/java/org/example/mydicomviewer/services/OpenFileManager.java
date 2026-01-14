package org.example.mydicomviewer.services;

import org.example.mydicomviewer.models.DicomFile;

import java.io.File;
import java.util.List;

public interface OpenFileManager {

    void openFileUsingWorker(File file);

    void openFragmentedFileUsingWorker(List<File> files);

    void reopenFile(File file);
}
