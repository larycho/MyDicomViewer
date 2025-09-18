package org.example.mydicomviewer.commands;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenFileCommand {

    public void execute() {
        FileChooser fileChooser = createFileChooser();

        Stage stage = new Stage();

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            openFile(file);
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

    private void openFile(File file) {
        // TODO
    }
}
