package org.example.mydicomviewer.display;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;

public interface OverlayGenerator {
    OverlayText createOverlayText(DicomFile dicomFile);
}
