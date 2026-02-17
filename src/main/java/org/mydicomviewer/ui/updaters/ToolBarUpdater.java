package org.mydicomviewer.ui.updaters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.events.DicomDirLoadedEvent;
import org.mydicomviewer.events.FileLoadedEvent;
import org.mydicomviewer.events.FolderLoadedEvent;
import org.mydicomviewer.events.listeners.DicomDirLoadedListener;
import org.mydicomviewer.events.listeners.FileLoadedListener;
import org.mydicomviewer.events.listeners.FolderLoadedListener;
import org.mydicomviewer.services.DicomDirLoadManager;
import org.mydicomviewer.events.services.FileLoadEventService;
import org.mydicomviewer.events.services.FolderLoadedEventService;
import org.mydicomviewer.ui.ToolBar;

@Singleton
public class ToolBarUpdater implements FolderLoadedListener, DicomDirLoadedListener, FileLoadedListener {

    private ToolBar toolBar;

    @Inject
    public ToolBarUpdater(ToolBar toolBar,
                          FileLoadEventService fileLoadEventService,
                          DicomDirLoadManager dicomDirLoadManager,
                          FolderLoadedEventService folderLoadedEventService) {

        this.toolBar = toolBar;

        fileLoadEventService.addListener(this);
        dicomDirLoadManager.addListener(this);
        folderLoadedEventService.addListener(this);
    }

    @Override
    public void dicomDirLoaded(DicomDirLoadedEvent dicomDirLoadedEvent) {
        toolBar.showDefaultPreset();
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        toolBar.showDefaultPreset();
    }

    @Override
    public void folderLoaded(FolderLoadedEvent event) {
        toolBar.showDefaultPreset();
    }
}
