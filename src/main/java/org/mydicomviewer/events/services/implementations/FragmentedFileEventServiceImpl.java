package org.mydicomviewer.events.services.implementations;

import com.google.inject.Singleton;
import org.mydicomviewer.events.FragmentedFileSelectedEvent;
import org.mydicomviewer.events.services.FragmentedFileEventService;
import org.mydicomviewer.events.listeners.FragmentedFileSelectedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class FragmentedFileEventServiceImpl implements FragmentedFileEventService {

    private final ArrayList<FragmentedFileSelectedListener> listeners = new ArrayList<>();

    @Override
    public void addListener(FragmentedFileSelectedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyListeners(List<File> sourceFiles, int fragmentIndex) {
        FragmentedFileSelectedEvent event = new FragmentedFileSelectedEvent(this, sourceFiles, fragmentIndex);
        for (FragmentedFileSelectedListener listener : listeners) {
            listener.fragmentedFileSelected(event);
        }
    }
}
