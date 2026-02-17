package org.mydicomviewer.processing.overlay;

import org.mydicomviewer.models.DicomFile;

public interface OverlayGenerator {
    OverlayText createOverlayText(DicomFile dicomFile);
}
