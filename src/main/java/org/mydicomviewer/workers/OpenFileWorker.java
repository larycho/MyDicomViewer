package org.mydicomviewer.workers;

import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.processing.io.file.FileProcessor;
import org.mydicomviewer.events.services.FileLoadEventService;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.ExecutionException;

public class OpenFileWorker extends SwingWorker<DicomFile, Void> {

    private File file;
    private final FileProcessor fileProcessor;
    private final FileLoadEventService fileLoadEventService;

    public OpenFileWorker(FileLoadEventService fileLoadEventService, FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
        this.fileLoadEventService = fileLoadEventService;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    protected DicomFile doInBackground() {
        return fileProcessor.readFile(file);
    }

    @Override
    protected void done() {
        try {
            fileLoadEventService.notifyFinished(this.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
