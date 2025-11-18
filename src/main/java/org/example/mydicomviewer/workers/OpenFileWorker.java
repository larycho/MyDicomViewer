package org.example.mydicomviewer.workers;

import com.google.inject.Inject;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.file.FileProcessor;
import org.example.mydicomviewer.processing.file.FileProcessorImpl;
import org.example.mydicomviewer.services.FileLoadEventService;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.ExecutionException;

public class OpenFileWorker extends SwingWorker<DicomFile, Void> {

    private File file;
    private FileProcessor fileProcessor;
    private FileLoadEventService fileLoadEventService;

    public OpenFileWorker(FileLoadEventService fileLoadEventService, FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
        this.fileLoadEventService = fileLoadEventService;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    protected DicomFile doInBackground() throws Exception {
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
