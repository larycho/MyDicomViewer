package org.mydicomviewer.commands;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.mydicomviewer.processing.io.export.SaveFormat;
import org.mydicomviewer.processing.io.export.SaveParams;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.ui.notifications.NotificationService;
import org.mydicomviewer.ui.image.SelectedImageManager;
import org.mydicomviewer.ui.image.ImagePanelWrapper;
import org.mydicomviewer.workers.SaveFileWorker;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class ExportImagesCommand {

    private final SelectedImageManager selectedImageManager;
    private boolean allFramesExported = false;
    private int currentFrame = 0;
    private boolean shapesExported = false;
    private SaveFormat saveFormat = SaveFormat.PNG;
    private File directory;

    private JTextField chosenLocationField;

    private final NotificationService notificationService;
    private final Provider<SaveFileWorker> saveWorkerProvider;

    Preferences preferences = Preferences.userRoot().node("com/example/mydicomviewer/commands");
    private static final String LAST_OPEN_DIR = "last_open_dir";

    @Inject
    public ExportImagesCommand(SelectedImageManager selectedImageManager,
                               NotificationService notificationService,
                               Provider<SaveFileWorker> saveWorkerProvider) {
        this.selectedImageManager = selectedImageManager;
        this.notificationService = notificationService;
        this.saveWorkerProvider = saveWorkerProvider;

        directory = new File(getLastDirectory());
    }

    public void execute() {
        ImagePanelWrapper imagePanel = selectedImageManager.getSelectedImage();

        if (imagePanel == null) {
            notificationService.showInfoMessage("No loaded file", "Please load a file first");
        }
        else {
            JFrame frame = createPopupWindow();
            frame.setVisible(true);
        }
    }

    private JFrame createPopupWindow() {
        JFrame frame = createBasicFrame();
        JPanel panel = createBasicPanel();

        fillPanel(panel, frame);

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);

        return frame;
    }

    private static JPanel createBasicPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    private static JFrame createBasicFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("Export Images");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return frame;
    }

    private void fillPanel(JPanel panel, JFrame frame) {
        JPanel directoryChoicePanel = getDirectoryChoicePanel();
        directoryChoicePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(directoryChoicePanel);

        chosenLocationField = getChosenLocationField();
        chosenLocationField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(chosenLocationField);

        JPanel formatPanel = getFileFormatPanel();
        formatPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(formatPanel);

        JCheckBox allFrames = getFrameCheckBox();
        allFrames.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(allFrames);

        JButton exportButton = getExportButton(frame);
        exportButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(exportButton);

    }

    private JTextField getChosenLocationField() {
        JTextField chosenLocationField = new JTextField(directory.getAbsolutePath());
        chosenLocationField.setEditable(false);
        return chosenLocationField;
    }

    private JPanel getDirectoryChoicePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel choosePrompt = getChoicePrompt();
        panel.add(choosePrompt);

        JButton selectLocationButton = getSelectLocationButton();
        panel.add(selectLocationButton);

        return panel;
    }

    private JPanel getFileFormatPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel formatLabel = new JLabel("Format: ");
        panel.add(formatLabel);

        JComboBox<?> formatComboBox = getSaveFormatComboBox();
        panel.add(formatComboBox);

        return panel;
    }

    private JButton getExportButton(JFrame frame) {
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(e -> {

            export();
            frame.dispose();
            });
        return exportButton;
    }

    private JCheckBox getShapesExportCheckBox() {
        JCheckBox shapesExport = new JCheckBox("Export drawn shapes");
        shapesExport.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        shapesExport.addActionListener(e -> shapesExported = shapesExport.isSelected());
        return shapesExport;
    }

    private JCheckBox getFrameCheckBox() {
        JCheckBox allFrames = new JCheckBox("Export all frames");
        allFrames.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        allFrames.addActionListener(e -> allFramesExported = allFrames.isSelected());
        return allFrames;
    }

    private JComboBox<SaveFormat> getSaveFormatComboBox() {
        JComboBox<SaveFormat> box = new JComboBox<>(SaveFormat.values());
        box.setSelectedItem(saveFormat);
        box.addActionListener(e -> saveFormat = (SaveFormat) box.getSelectedItem());
        box.setMaximumSize(box.getPreferredSize());
        return box;
    }

    private JButton getSelectLocationButton() {
        JButton selectLocationButton = new JButton("Select location");
        selectLocationButton.addActionListener(e -> selectFolder());
        return selectLocationButton;
    }

    private JLabel getChoicePrompt() {
        JLabel choosePrompt = new JLabel("Choose location to save files to: ");
        choosePrompt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        return choosePrompt;
    }

    private void export() {
        ImagePanelWrapper imagePanel = selectedImageManager.getSelectedImage();

        if (imagePanel == null) {
            notificationService.showInfoMessage("No loaded file", "Please load a file first");
        }
        else {
            currentFrame = imagePanel.getCurrentFrameNumber();
            DicomFile file = imagePanel.getDicomFile();
            saveFileUsingWorker(file);
        }

    }

    private void saveFileUsingWorker(DicomFile file) {
        SaveParams params = makeSaveParams(file);
        SaveFileWorker worker = saveWorkerProvider.get();
        worker.setSaveParams(params);
        worker.execute();
    }

    private SaveParams makeSaveParams(DicomFile dicomFile) {
        SaveParams params = new SaveParams();
        ArrayList<DicomFile> files = new ArrayList<>();
        files.add(dicomFile);
        params.setFiles(files);
        params.setTargetDirectory(directory);
        params.setFormat(saveFormat);
        params.setSingleFrame(!allFramesExported);
        params.setFrameNumber(currentFrame);
        return params;
    }

    public void selectFolder() {
        JFileChooser fileChooser = createFileChooser();

        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {

            fileChosenResponse(fileChooser);

        }
    }

    private void fileChosenResponse(JFileChooser fileChooser) {
        File file = fileChooser.getSelectedFile();
        updateLastDirectory(file);
        chosenLocationField.setText(file.getAbsolutePath());
        chosenLocationField.setCaretPosition(0);
        directory = file;
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
