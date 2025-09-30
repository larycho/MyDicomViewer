package org.example.mydicomviewer.views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuBar extends JMenuBar {

    private JMenu fileMenu;

    public MainMenuBar() {
        setupFileMenu();
    }

    private void setupFileMenu() {
        this.fileMenu = createFileMenu();
        this.add(this.fileMenu);
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem openFile = new JMenuItem("Open file...");
        openFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(fileMenu);
            }

        });
        fileMenu.add(openFile);

        return fileMenu;
    }
}
