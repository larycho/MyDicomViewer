package org.example.mydicomviewer.views;

import org.example.mydicomviewer.commands.OpenFileCommand;
import org.example.mydicomviewer.listeners.FileLoadedListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainMenuBar extends JMenuBar {

    private JMenu fileMenu;

    private List<FileLoadedListener> listeners = new ArrayList<>();

    public MainMenuBar() {
        setupFileMenu();
    }

    public void addFileLoadedListener(FileLoadedListener fileLoadedListener) {
        listeners.add(fileLoadedListener);
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
                OpenFileCommand command = new OpenFileCommand(listeners);
                command.execute();
            }

        });
        fileMenu.add(openFile);

        return fileMenu;
    }
}
