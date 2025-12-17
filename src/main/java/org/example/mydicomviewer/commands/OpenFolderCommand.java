package org.example.mydicomviewer.commands;

import com.google.inject.Inject;
import org.example.mydicomviewer.services.DicomDirLoadManager;
import org.example.mydicomviewer.services.FolderLoadManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenFolderCommand {

    private final FolderLoadManager folderLoadManager;

    @Inject
    public OpenFolderCommand(FolderLoadManager folderLoadManager) {
        this.folderLoadManager = folderLoadManager;
    }

    public void execute() {
        JFileChooser fileChooser = createFileChooser();

        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {

            fileChosenResponse(fileChooser);

        }
    }

    private void fileChosenResponse(JFileChooser fileChooser) {
        File file = fileChooser.getSelectedFile();
        openDicomDir(file);
    }

    private void openDicomDir(File file) {
        folderLoadManager.openFolder(file);
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Open Folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        return fileChooser;
    }
}

