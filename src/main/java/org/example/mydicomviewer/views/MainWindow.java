package org.example.mydicomviewer.views;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setupMenuBar();
        setTitle("My Dicom Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void setupMenuBar() {
        MainMenuBar mainMenuBar = new MainMenuBar();
        this.setJMenuBar(mainMenuBar);
    }


}
