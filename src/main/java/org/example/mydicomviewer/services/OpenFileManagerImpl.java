package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.processing.file.FileProcessor;
import org.example.mydicomviewer.workers.OpenFileWorker;

import java.io.File;

@Singleton
public class OpenFileManagerImpl implements OpenFileManager {

    private final FileProcessor fileProcessor;
    private final FileLoadEventService fileLoadEventService;
    private final FileLoadStartedEventService fileLoadStartedEventService;

    @Inject
    public OpenFileManagerImpl (
            FileLoadEventService fileLoadEventService,
            FileProcessor fileProcessor,
            FileLoadStartedEventService fileLoadStartedEventService
    ) {
        this.fileProcessor = fileProcessor;
        this.fileLoadEventService = fileLoadEventService;
        this.fileLoadStartedEventService = fileLoadStartedEventService;
    }

    @Override
    public void openFileUsingWorker(File file) {
        fileLoadStartedEventService.notifyStarted(file);
        OpenFileWorker worker = new OpenFileWorker(fileLoadEventService, fileProcessor);
        worker.setFile(file);
        worker.execute();
    }
}
