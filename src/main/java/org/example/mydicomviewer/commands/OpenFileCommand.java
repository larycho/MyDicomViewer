package org.example.mydicomviewer.commands;

import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.mydicomviewer.ImagePanelGenerator;
import org.example.mydicomviewer.ImagePanelGeneratorImpl;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.listeners.FileLoadedListener;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.file.FileProcessor;
import org.example.mydicomviewer.processing.file.FileProcessorImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
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
        FileChooser fileChooser = createFileChooser();

        Stage stage = new Stage();

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            DicomFile dicomFile = openFile(file);
            fileLoaded(dicomFile);
        }
        else {
            // TODO
        }
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open File");
        fileChooser = addExtensionFilters(fileChooser);

        return fileChooser;
    }

    private FileChooser addExtensionFilters(FileChooser fileChooser) {
        var fileExtensions = getExtensionFilters();
        fileChooser.getExtensionFilters().addAll(fileExtensions);
        return fileChooser;
    }

    private List<FileChooser.ExtensionFilter> getExtensionFilters() {
        List<FileChooser.ExtensionFilter> fileExtensions = new ArrayList<>();
        fileExtensions.add(new FileChooser.ExtensionFilter("DICOM Files", "*.dcm"));
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
