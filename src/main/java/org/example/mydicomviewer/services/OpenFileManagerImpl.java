package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.processing.file.FileProcessor;
import org.example.mydicomviewer.workers.OpenFileWorker;

import java.io.File;

@Singleton
public class OpenFileManagerImpl implements OpenFileManager {

    private FileProcessor fileProcessor;
    private FileLoadEventService fileLoadEventService;

    @Inject
    public OpenFileManagerImpl (FileLoadEventService fileLoadEventService, FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
        this.fileLoadEventService = fileLoadEventService;
    }

    @Override
    public void openFileUsingWorker(File file) {
        OpenFileWorker worker = new OpenFileWorker(fileLoadEventService, fileProcessor);
        worker.setFile(file);
        worker.execute();
    }
}
