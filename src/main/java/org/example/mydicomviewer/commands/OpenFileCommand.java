package org.example.mydicomviewer.commands;

import com.google.inject.Inject;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.listeners.FileLoadedListener;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.file.FileProcessor;
import org.example.mydicomviewer.processing.file.FileProcessorImpl;
import org.example.mydicomviewer.services.OpenFileManager;
import org.example.mydicomviewer.workers.OpenFileWorker;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OpenFileCommand {

    private List<FileLoadedListener> listeners;
    private OpenFileManager openFileManager;

    @Inject
    public OpenFileCommand(OpenFileManager openFileManager) {
        this.openFileManager = openFileManager;
    }

    public void execute() {
        JFileChooser fileChooser = createFileChooser();

        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {

            fileChosenResponse(fileChooser);

        }
    }

    private void fileChosenResponse(JFileChooser fileChooser) {
        File file = fileChooser.getSelectedFile();
        openFileUsingManager(file);
    }

    private void openFileUsingManager(File file) {
        openFileManager.openFileUsingWorker(file);
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Open File");
        fileChooser = addFileFilters(fileChooser);

        return fileChooser;
    }

    private JFileChooser addFileFilters(JFileChooser fileChooser) {
        var fileExtensions = getFileFilters();

        for (var extension : fileExtensions) {
            fileChooser.addChoosableFileFilter(extension);
        }
        return fileChooser;
    }

    private List<FileNameExtensionFilter> getFileFilters() {
        List<FileNameExtensionFilter> fileExtensions = new ArrayList<>();
        fileExtensions.add(new FileNameExtensionFilter("DICOM Files", "*.dcm"));
        return fileExtensions;
    }
}