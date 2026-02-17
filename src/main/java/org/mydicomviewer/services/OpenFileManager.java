package org.mydicomviewer.services;

import java.io.File;
import java.util.List;

public interface OpenFileManager {

    void openFileUsingWorker(File file);

    void openFragmentedFileUsingWorker(List<File> files);

}
