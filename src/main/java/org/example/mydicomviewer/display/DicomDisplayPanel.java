package org.example.mydicomviewer.display;

import javafx.scene.Node;

public interface DicomDisplayPanel {
    Node toNode();

    void refresh();
}
