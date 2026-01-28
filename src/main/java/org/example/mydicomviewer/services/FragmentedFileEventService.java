package org.example.mydicomviewer.services;
import org.example.mydicomviewer.listeners.FragmentedFileSelectedListener;

import java.io.File;
import java.util.List;

public interface FragmentedFileEventService {

    void addListener(FragmentedFileSelectedListener listener);

    void notifyListeners(List<File> sourceFiles, int fragmentIndex);
}
