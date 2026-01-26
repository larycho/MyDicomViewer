package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.services.DicomDirLoadManager;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.FolderLoadedEventService;
import org.example.mydicomviewer.views.ToolBar;

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
