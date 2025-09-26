package org.example.mydicomviewer.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.mydicomviewer.listeners.ImageDisplayer;

public class MainWindowController {
    @FXML
    public VBox dicomdirPanel;
    @FXML
    private MainImagePanelController mainImagePanelIncludeController;
    @FXML
    private MenuBarController menuBarIncludeController;

    @FXML
    public void initialize() {
        addListeners();
    }

    private void addListeners() {
        ImageDisplayer imageDisplayer = new ImageDisplayer(this.mainImagePanelIncludeController);
        this.menuBarIncludeController.addFileLoadedListener(imageDisplayer);
    }
}
