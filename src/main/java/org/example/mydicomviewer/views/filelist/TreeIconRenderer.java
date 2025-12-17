package org.example.mydicomviewer.views.filelist;

import org.example.mydicomviewer.models.DicomDirectoryRecord;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class TreeIconRenderer extends DefaultTreeCellRenderer {

    private Icon patientIcon;
    private Icon seriesIcon;
    private Icon studyIcon;
    private Icon imageIcon;

    public TreeIconRenderer() {
        patientIcon = new ImageIcon("src/main/resources/org/example/mydicomviewer/icons/patient.png");
        seriesIcon = new ImageIcon("src/main/resources/org/example/mydicomviewer/icons/series.png");
        studyIcon = new ImageIcon("src/main/resources/org/example/mydicomviewer/icons/study.png");
        imageIcon = new ImageIcon("src/main/resources/org/example/mydicomviewer/icons/image.png");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();
        String type = null;

        if (userObject instanceof MyTreeNode treeNode) {
            type = treeNode.getType();
        }
        else if (userObject instanceof DicomDirectoryRecord record) {
            type = record.getType();
        }

        if (type != null) {
            switch (type) {
                case "PATIENT":
                    setIcon(patientIcon);
                    break;
                case "SERIES":
                    setIcon(seriesIcon);
                    break;
                case "STUDY":
                    setIcon(studyIcon);
                    break;
                case "IMAGE":
                    setIcon(imageIcon);
                    break;
                default:
                    setIcon(imageIcon);
                    break;
            }
        }
        else {
            setIcon(imageIcon);
        }

        return this;
    }
}
