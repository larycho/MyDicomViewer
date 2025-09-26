package org.example.mydicomviewer;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;
import org.example.mydicomviewer.models.DicomFile;

import java.io.IOException;

public class ImagePanelGeneratorImpl implements ImagePanelGenerator {

    @Override
    public DicomDisplayPanel createImageNode(DicomFile file) {
        SourceImage image = getSourceImage(file);
        SingleImagePanel panel = new SingleImagePanel(image);
        return createDicomDisplayPanel(panel);
    }

    private SourceImage getSourceImage(DicomFile file) {
        String path = file.getFilePath();
        return getSourceImage(path);
    }

    private SourceImage getSourceImage(String filePath) {
        try {
            return new SourceImage(filePath);
        } catch (IOException | DicomException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    private DicomDisplayPanel createDicomDisplayPanel(SingleImagePanel panel) {
        return new DicomDisplayPanelImpl(panel);
    }

}
