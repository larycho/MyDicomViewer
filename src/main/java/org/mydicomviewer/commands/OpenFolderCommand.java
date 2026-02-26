package org.mydicomviewer.commands;

import com.google.inject.Inject;
import org.mydicomviewer.services.FolderLoadManager;

import javax.swing.*;
import java.io.File;
import java.util.prefs.Preferences;

public class OpenFolderCommand {

    private final FolderLoadManager folderLoadManager;
    Preferences preferences = Preferences.userRoot().node("com/example/mydicomviewer/commands");
    private static final String LAST_OPEN_DIR = "last_open_dir";

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
        updateLastDirectory(file);
        openDicomDir(file);
    }

    private void openDicomDir(File file) {
        folderLoadManager.openFolder(file);
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser(getLastDirectory());

        fileChooser.setDialogTitle("Open Folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        return fileChooser;
    }

    private String getLastDirectory() {
        String dir = preferences.get(LAST_OPEN_DIR, System.getProperty("user.home"));
        File checkFile = new File(dir);
        if (!checkFile.exists()) {
            dir = System.getProperty("user.home");
        }
        return dir;
    }

    private void updateLastDirectory(File file) {
        String newDir = file.getAbsolutePath();
        preferences.put(LAST_OPEN_DIR, newDir);
    }
}

