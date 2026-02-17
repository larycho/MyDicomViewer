package org.mydicomviewer.events.services;
import org.mydicomviewer.events.listeners.FragmentedFileSelectedListener;

import java.io.File;
import java.util.List;

public interface FragmentedFileEventService {

    void addListener(FragmentedFileSelectedListener listener);

    void notifyListeners(List<File> sourceFiles, int fragmentIndex);
}
