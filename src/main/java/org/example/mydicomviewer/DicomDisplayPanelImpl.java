package org.example.mydicomviewer;

import com.pixelmed.display.SingleImagePanel;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;

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
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private SwingNode createSwingNode(SingleImagePanel panel) {
        SwingNode node = new SwingNode();
        node.setContent(panel);
        return node;
    }
}
