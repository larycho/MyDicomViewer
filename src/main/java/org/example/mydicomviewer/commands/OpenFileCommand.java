package org.example.mydicomviewer.commands;

import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.listeners.FileLoadedListener;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.file.FileProcessor;
import org.example.mydicomviewer.processing.file.FileProcessorImpl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenFileCommand {

    private List<FileLoadedListener> listeners;

    public OpenFileCommand() {
        this.listeners = new ArrayList<>();
    }

    public OpenFileCommand(List<FileLoadedListener> listeners) {
        this.listeners = listeners;
    }

    public void execute() {
        JFileChooser fileChooser = createFileChooser();

        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {

            fileChosenResponse(fileChooser);

        }
        else {
            // TODO
        }
    }

    private void fileChosenResponse(JFileChooser fileChooser) {
        File file = fileChooser.getSelectedFile();
        DicomFile dicomFile = openFile(file);
        fileLoaded(dicomFile);
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

    private DicomFile openFile(File file) {
        // TODO
        FileProcessor fileProcessor = new FileProcessorImpl();
        return fileProcessor.readFile(file);
    }

    private void fileLoaded(DicomFile dicomFile) {
        FileLoadedEvent event = new FileLoadedEvent(this, dicomFile);
        notifyListeners(event);
    }

    private void notifyListeners(FileLoadedEvent event) {
        for (FileLoadedListener listener : listeners) {
            listener.fileLoaded(event);
        }
    }
}