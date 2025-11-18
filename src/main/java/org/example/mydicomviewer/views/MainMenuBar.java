package org.example.mydicomviewer.views;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.commands.OpenFileCommand;
import org.example.mydicomviewer.listeners.FileLoadedListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MainMenuBar extends JMenuBar {

    private JMenu fileMenu;
    private OpenFileCommand command;

    @Inject
    public MainMenuBar(OpenFileCommand command) {
        this.command = command;
        setupFileMenu();
    }

    private void setupFileMenu() {
        this.fileMenu = createFileMenu();
        this.add(this.fileMenu);
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem openFile = new JMenuItem("Open file...");
        openFile.addActionListener(e -> command.execute());
        fileMenu.add(openFile);

        return fileMenu;
    }
}
