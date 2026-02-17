package org.mydicomviewer.ui.updaters;

import com.google.inject.Inject;
import org.mydicomviewer.events.FileLoadStartedEvent;
import org.mydicomviewer.events.FileLoadedEvent;
import org.mydicomviewer.events.services.FileLoadEventService;
import org.mydicomviewer.events.services.FileLoadStartedEventService;
import org.mydicomviewer.events.listeners.FileLoadStartedListener;
import org.mydicomviewer.events.listeners.FileLoadedListener;
import org.mydicomviewer.ui.Footer;

import java.io.File;

public class FooterUpdater implements FileLoadedListener, FileLoadStartedListener {

    private final Footer footer;

    @Inject
    public FooterUpdater(
            Footer footer,
            FileLoadStartedEventService fileLoadStartedEventService,
            FileLoadEventService fileLoadEventService
    ) {
        this.footer = footer;
        fileLoadStartedEventService.addListener(this);
        fileLoadEventService.addListener(this);
    }

    @Override
    public void loadStarted(FileLoadStartedEvent event) {
        File file = event.getFile();
        footer.setLabelText("Opening file: " + file.getName());
        footer.startProgress();
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        footer.setLabelText("Ready | My Dicom Viewer ");
        footer.stopProgress();
    }
}
