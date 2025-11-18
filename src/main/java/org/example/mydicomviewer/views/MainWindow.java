package org.example.mydicomviewer.views;

import com.google.inject.Inject;
import org.example.mydicomviewer.views.filelist.FileListPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private MainMenuBar menuBar;
    private ToolBar toolBar;
    private DrawingOverlayPanel drawingPanel;
    private TagPanel tagPanel;
    private MultipleImagePanel multipleImagePanel;
    private FileListPanel fileListPanel;

    @Inject
    public MainWindow(MainMenuBar menuBar,
                      ToolBar toolBar,
                      TagPanel tagPanel,
                      MultipleImagePanel multipleImagePanel,
                      FileListPanel fileListPanel) {
        this.menuBar = menuBar;
        this.toolBar = toolBar;
        this.tagPanel = tagPanel;
        this.multipleImagePanel = multipleImagePanel;
        this.fileListPanel = fileListPanel;

        setupFrame();

        setupSubPanels();

    }

    private void setupSubPanels() {
        add(multipleImagePanel, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        add(tagPanel, BorderLayout.EAST);
        add(fileListPanel, BorderLayout.WEST);

        setJMenuBar(menuBar);
    }

    private void setupFrame() {
        setTitle("My Dicom Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }
}
