package org.example.mydicomviewer.controllers;
import javafx.embed.swing.SwingNode;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.example.mydicomviewer.DicomDisplayPanel;

public class MainImagePanelController {

    @FXML
    public StackPane stackPane;


    public void addSwingNodeToPane(SwingNode swingNode) {
        stackPane.getChildren().add(swingNode);
    }

    public void addNodeToPane(Node node) {
        stackPane.getChildren().add(node);
    }

    public void addNodeToPane(DicomDisplayPanel dicomDisplayPanel) {
        stackPane.getChildren().add(dicomDisplayPanel.toNode());
        dicomDisplayPanel.refresh();
    }
}
