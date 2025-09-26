package org.example.mydicomviewer;

import javafx.scene.Node;
import org.example.mydicomviewer.models.DicomFile;

public interface ImagePanelGenerator {

    DicomDisplayPanel createImageNode(DicomFile file);

}
