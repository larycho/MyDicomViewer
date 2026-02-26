package org.mydicomviewer.commands;

import com.google.inject.Inject;
import org.mydicomviewer.services.OpenFileManager;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class OpenFileCommand {

    private final OpenFileManager openFileManager;
    Preferences preferences = Preferences.userRoot().node("com/example/mydicomviewer/commands");
    private static final String LAST_OPEN_DIR = "last_open_dir";

    @Inject
    public OpenFileCommand(OpenFileManager openFileManager) {
        this.openFileManager = openFileManager;
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
        openFileUsingManager(file);
    }

    private void openFileUsingManager(File file) {
        openFileManager.openFileUsingWorker(file);
    }

    private JFileChooser createFileChooser() {

        JFileChooser fileChooser = new JFileChooser(getLastDirectory());

        fileChooser.setDialogTitle("Open File");
        fileChooser = addFileFilters(fileChooser);

        return fileChooser;
    }

    private JFileChooser addFileFilters(JFileChooser fileChooser) {
        var fileExtensions = getFileFilters();

        for (var extension : fileExtensions) {
            fileChooser.addChoosableFileFilter(extension);
        }
        return fileChooser;
    }

    private List<FileNameExtensionFilter> getFileFilters() {
        List<FileNameExtensionFilter> fileExtensions = new ArrayList<>();
        fileExtensions.add(new FileNameExtensionFilter("DICOM Files", "*.dcm"));
        return fileExtensions;
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
        String newDir = file.getParent();
        preferences.put(LAST_OPEN_DIR, newDir);
    }
}