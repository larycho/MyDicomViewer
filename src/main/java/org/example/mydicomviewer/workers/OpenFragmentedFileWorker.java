package org.example.mydicomviewer.workers;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.file.FragmentedFileProcessor;
import org.example.mydicomviewer.services.FileLoadEventService;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OpenFragmentedFileWorker extends SwingWorker<DicomFile, Void>  {

    private List<File> files;
    private final FragmentedFileProcessor fileProcessor;
    private final FileLoadEventService fileLoadEventService;

    public OpenFragmentedFileWorker(FileLoadEventService fileLoadEventService, FragmentedFileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
        this.fileLoadEventService = fileLoadEventService;
        files = new ArrayList<>();
    }

    public void setFiles(List<File> files) {
        this.files = new ArrayList<>(files);
    }

    @Override
    protected DicomFile doInBackground() {
        return fileProcessor.readFragmentedFile(files);
    }

    @Override
    protected void done() {
        try {
            fileLoadEventService.notifyFinished(this.get(), this);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
