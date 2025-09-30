package org.example.mydicomviewer.display;

import org.example.mydicomviewer.models.DicomFile;

public interface ImagePanelGenerator {

    DicomDisplayPanel createImageNode(DicomFile file);

}
