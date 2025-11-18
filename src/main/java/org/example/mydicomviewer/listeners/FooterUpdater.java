package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import org.example.mydicomviewer.events.FileLoadStartedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.FileLoadStartedEventService;
import org.example.mydicomviewer.views.Footer;

import java.io.File;

public class FooterUpdater implements FileLoadedListener, FileLoadStartedListener {

    private final Footer footer;
    //private final FileLoadStartedEventService fileLoadStartedEventService;

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
