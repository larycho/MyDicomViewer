package org.mydicomviewer.ui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.commands.ExportImagesCommand;
import org.mydicomviewer.commands.OpenFileCommand;

import javax.swing.*;

@Singleton
public class MainMenuBar extends JMenuBar {

    private JMenu fileMenu;
    private final OpenFileCommand command;
    private final ExportImagesCommand exportCommand;

    @Inject
    public MainMenuBar(OpenFileCommand command,
                       ExportImagesCommand exportCommand) {
        this.command = command;
        this.exportCommand = exportCommand;
        //setupFileMenu();
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

        JMenuItem export = new JMenuItem("Export to PNG");
        export.addActionListener(e -> exportCommand.execute());
        fileMenu.add(export);

        return fileMenu;
    }
}
