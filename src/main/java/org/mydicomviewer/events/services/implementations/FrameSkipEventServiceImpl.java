package org.mydicomviewer.events.services.implementations;

import com.google.inject.Singleton;
import org.mydicomviewer.events.FrameSkipEvent;
import org.mydicomviewer.events.listeners.FrameSkipEventListener;
import org.mydicomviewer.models.DicomFile;

import java.util.ArrayList;

@Singleton
public class FrameSkipEventServiceImpl implements org.mydicomviewer.events.services.FrameSkipEventService {

    private final ArrayList<FrameSkipEventListener> listeners = new ArrayList<>();

    @Override
    public void addListener(FrameSkipEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyListeners(Object source,DicomFile sourceFiles, int fragmentIndex) {
        FrameSkipEvent event = new FrameSkipEvent(source, sourceFiles, fragmentIndex);
        for (FrameSkipEventListener listener : listeners) {
            listener.frameSkipped(event);
        }
    }
}
