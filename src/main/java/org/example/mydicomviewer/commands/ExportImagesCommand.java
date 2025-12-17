package org.example.mydicomviewer.commands;

import com.google.inject.Inject;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.services.SelectedImageManager;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

public class ExportImagesCommand {

    private SelectedImageManager selectedImageManager;
    private boolean allFramesExported = false;
    private boolean shapesExported = false;
    private JLabel chosenLocationLabel;
    private File directory;

    @Inject
    public ExportImagesCommand(SelectedImageManager selectedImageManager) {
        this.selectedImageManager = selectedImageManager;
    }

    public void execute() {
        JFrame frame = createPopupWindow();
        frame.setVisible(true);

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
        JLabel choosePrompt = getChoicePrompt();
        panel.add(choosePrompt);

        JButton selectLocationButton = getSelectLocationButton();
        panel.add(selectLocationButton);

        setupChosenLocationLabel();
        panel.add(chosenLocationLabel);

        JCheckBox allFrames = getFrameCheckBox();
        panel.add(allFrames);

        JCheckBox shapesExport = getShapesExportCheckBox();
        panel.add(shapesExport);

        JButton exportButton = getExportButton(frame);
        panel.add(exportButton);
    }

    private JButton getExportButton(JFrame frame) {
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(e -> {
            exportImage();
            frame.dispose();
            });
        return exportButton;
    }

    private JCheckBox getShapesExportCheckBox() {
        JCheckBox shapesExport = new JCheckBox("Export drawn shapes");
        shapesExport.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        shapesExport.addActionListener(e -> {shapesExported = shapesExport.isSelected();});
        return shapesExport;
    }

    private JCheckBox getFrameCheckBox() {
        JCheckBox allFrames = new JCheckBox("Export all frames");
        allFrames.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        allFrames.addActionListener(e -> {allFramesExported = allFrames.isSelected();});
        return allFrames;
    }

    private void setupChosenLocationLabel() {
        chosenLocationLabel = new JLabel("Chosen location: location not chosen...");
        chosenLocationLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
    }

    private JButton getSelectLocationButton() {
        JButton selectLocationButton = new JButton("Select location");
        selectLocationButton.addActionListener(e -> {selectFolder();});
        return selectLocationButton;
    }

    private static JLabel getChoicePrompt() {
        JLabel choosePrompt = new JLabel("Choose location to save files to: ");
        choosePrompt.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
        return choosePrompt;
    }

    public static BufferedImage cloneImage(BufferedImage source) {
        BufferedImage clone = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                source.getType()
        );

        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();

        return clone;
    }

    private void exportImage() {
        ImagePanelWrapper imagePanel = selectedImageManager.getSelectedImage();

        if (imagePanel != null) {
            Map<Integer, List<DrawableShape>> shapes = imagePanel.getAllShapes();
            int frame = imagePanel.getCurrentFrameNumber();

            DicomFile file = imagePanel.getDicomFile();
            String fileName = file.getFileName();
            List<DicomImage> images = file.getImages();

            if (!allFramesExported) {
                exportToSingleFile(images, frame, fileName);
            }
            else {
                exportToMultipleFiles(images, fileName);
            }
        }

    }

    private void exportToMultipleFiles(List<DicomImage> images, String fileName) {
        for (int i = 0; i < images.size(); i++) {
            try {
                File output = new File(directory, fileName + "_" + (i + 1) + ".png");

                BufferedImage image = images.get(i).getImage();

                ImageIO.write(image, "png", output);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void exportToSingleFile(List<DicomImage> images, int frame, String fileName) {
        try {
            BufferedImage image = images.get(frame).getImage();

            File output = new File(directory, fileName + "_" + frame + ".png");
            ImageIO.write(image, "png", output);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        chosenLocationLabel.setText("Chosen location: " + file.getAbsolutePath());
        directory = file;
    }


    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Open Folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        return fileChooser;
    }
}
