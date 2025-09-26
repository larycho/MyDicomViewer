package org.example.mydicomviewer.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.mydicomviewer.commands.OpenFileCommand;
import org.example.mydicomviewer.listeners.FileLoadedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuBarController {

    public MenuItem openFileItem;

    private List<FileLoadedListener> listeners = new ArrayList<>();

    public void onOpenFileClick(ActionEvent actionEvent) {
        var openFileCommand = new OpenFileCommand(listeners);
        openFileCommand.execute();
    }

    public void addFileLoadedListener(FileLoadedListener fileLoadedListener) {
        listeners.add(fileLoadedListener);
    }
}
