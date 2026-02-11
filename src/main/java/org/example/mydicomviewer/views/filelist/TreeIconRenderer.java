package org.example.mydicomviewer.views.filelist;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class TreeIconRenderer extends DefaultTreeCellRenderer {

    private final Icon patientIcon;
    private final Icon seriesIcon;
    private final Icon studyIcon;
    private final Icon imageIcon;

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

        if (userObject instanceof FileNode fileNode) {
            setDisplayParams(fileNode);
            return this;
        }

        setIcon(imageIcon);
        return this;
    }

    private void setDisplayParams(FileNode node) {
        FileNodeType type = node.getType();

        if (type != null) {
            switch (type) {
                case PATIENT:
                    setUpPatient(node);
                    break;
                case SERIES:
                    setUpSeries(node);
                    break;
                case STUDY:
                    setUpStudy(node);
                    break;
                default:
                    setUpImage(node);
                    break;
            }
        }

    }

    private void setUpPatient(FileNode fileNode) {
        setIcon(patientIcon);
        FileNodeData data = fileNode.getData();
        if (data.getPatientId().isPresent()) {
            String patientId = data.getPatientId().get();
            setText("Patient ID: " + patientId);
        }
        else {
            setText("Patient");
        }
    }

    private void setUpStudy(FileNode fileNode) {
        setIcon(studyIcon);
        FileNodeData data = fileNode.getData();
        if (data.getStudyId().isPresent()) {
            String patientId = data.getStudyId().get();
            setText("Study: " + patientId);
        }
        else {
            setText("Study");
        }
    }

    private void setUpSeries(FileNode fileNode) {
        setIcon(seriesIcon);
        FileNodeData data = fileNode.getData();
        if (data.getSeriesNumber().isPresent()) {
            String patientId = data.getSeriesNumber().get();
            setText("Series: " + patientId);
        }
        else {
            setText("Series");
        }
    }

    private void setUpImage(FileNode fileNode) {
        setIcon(seriesIcon);
        FileNodeData data = fileNode.getData();
        if (data.getFileName().isPresent()) {
            String fileName = data.getFileName().get();
            setText(fileName);
        }
        else {
            setText("Image");
        }
    }
}

