package org.example.mydicomviewer.display;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;
import org.example.mydicomviewer.models.DicomFile;

import java.io.IOException;

public class ImagePanelGeneratorImpl implements ImagePanelGenerator {

    @Override
    public DicomDisplayPanel createImageNode(DicomFile file) {
        SourceImage image = file.getSourceImage();
        SingleImagePanel panel = new SingleImagePanel(image);
        return createDicomDisplayPanel(panel, image);
    }

    private DicomDisplayPanel createDicomDisplayPanel(SingleImagePanel panel, SourceImage image) {
        return new DicomDisplayPanelImpl(panel, image);
    }

}
