package org.example.mydicomviewer;

import javafx.scene.Node;

public interface DicomDisplayPanel {
    Node toNode();

    void refresh();
}
