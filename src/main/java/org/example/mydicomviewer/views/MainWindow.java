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
    private Footer footer;

    @Inject
    public MainWindow(MainMenuBar menuBar,
                      ToolBar toolBar,
                      TagPanel tagPanel,
                      MultipleImagePanel multipleImagePanel,
                      FileListPanel fileListPanel,
                      Footer footer) {
        this.menuBar = menuBar;
        this.toolBar = toolBar;
        this.tagPanel = tagPanel;
        this.multipleImagePanel = multipleImagePanel;
        this.fileListPanel = fileListPanel;
        this.footer = footer;

        setupFrame();

        setupSubPanels();

    }

    private void setupSubPanels() {
        add(multipleImagePanel, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        add(footer, BorderLayout.SOUTH);
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
