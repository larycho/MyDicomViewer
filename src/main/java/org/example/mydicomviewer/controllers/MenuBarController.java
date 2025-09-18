package org.example.mydicomviewer.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.mydicomviewer.commands.OpenFileCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuBarController {

    public MenuItem openFileItem;

    public void onOpenFileClick(ActionEvent actionEvent) {
        var openFileCommand = new OpenFileCommand();
        openFileCommand.execute();
    }

}
