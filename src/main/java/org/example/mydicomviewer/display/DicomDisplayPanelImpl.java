package org.example.mydicomviewer.display;

import com.pixelmed.display.SingleImagePanel;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;
import javax.swing.*;

public class DicomDisplayPanelImpl implements DicomDisplayPanel {

    private SingleImagePanel imagePanel;
    private SwingNode swingNode;

    public DicomDisplayPanelImpl(SingleImagePanel panel) {
        this.imagePanel = panel;
        this.swingNode = createSwingNode(panel);
    }

    public Node toNode() {
        return swingNode;
    }

    public void refresh() {
        Platform.runLater(() -> {
            SwingUtilities.invokeLater(() -> {
                imagePanel.revalidate();
                imagePanel.repaint();
            });
        });
    }

    private SwingNode createSwingNode(SingleImagePanel panel) {
        SwingNode node = new SwingNode();
        SwingUtilities.invokeLater(() -> {
            node.setContent(panel);
        });
        return node;
    }
}
