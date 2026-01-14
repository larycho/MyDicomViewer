package org.example.mydicomviewer.views;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.commands.OpenDicomDirCommand;
import org.example.mydicomviewer.commands.OpenFileCommand;
import org.example.mydicomviewer.commands.OpenFolderCommand;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.listeners.ImageDisplayer;
import org.example.mydicomviewer.listeners.ResliceDisplayer;
import org.example.mydicomviewer.services.ScreenModeProvider;
import org.example.mydicomviewer.services.ScreenModeProviderImpl;
import org.example.mydicomviewer.services.SelectedImageManager;
import org.example.mydicomviewer.views.image.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class ToolBar extends JToolBar {

    private JButton manualWindowing;
    private JButton reslice;
    private JButton modes;
    private JButton addFiles;

    private JPopupMenu addFilesMenu;

    private Set<DrawingTool> tools;
    private final ImageDisplayer imageDisplayer;
    private final ResliceDisplayer resliceDisplayer;
    private final SelectedImageManager selectedImageManager;

    private final OpenFileCommand openFileCommand;
    private final OpenDicomDirCommand commandDir;
    private final OpenFolderCommand openFolderCommand;

    @Inject
    public ToolBar(ImageDisplayer imageDisplayer,
                   ResliceDisplayer resliceDisplayer,
                   Set<DrawingTool> pluginTools,
                   SelectedImageManager selectedImageManager,
                   OpenFileCommand openFileCommand,
                   OpenDicomDirCommand commandDir,
                   OpenFolderCommand openFolderCommand) {
        super();

        this.openFileCommand = openFileCommand;
        this.openFolderCommand = openFolderCommand;
        this.commandDir = commandDir;

        createToolList(pluginTools);

        this.selectedImageManager = selectedImageManager;

        addFilesButton();
        addResliceButton();
        addScreenModeButton();
        addDrawingTools();

        this.imageDisplayer = imageDisplayer;
        this.resliceDisplayer = resliceDisplayer;
        setupListeners();
    }

    private void createToolList(Set<DrawingTool> pluginTools) {
        tools = new HashSet<>();

        tools.add(new OvalTool());
        tools.add(new PencilTool());
        tools.add(new LineTool());

        tools.addAll(pluginTools);
    }

    private void setupListeners() {
        addScreenModeListeners(imageDisplayer);
        addResliceListeners(resliceDisplayer);
    }

    private void addScreenModeButton() {
        modes = new JButton("Modes");
        add(modes);
    }

    public void addScreenModeListeners(ImageDisplayer imageDisplayer) {
        ScreenModeProvider screenModeProvider = new ScreenModeProviderImpl();
        List<SplitScreenMode> modes = screenModeProvider.getAvailableScreenModes();

        JPopupMenu popupMenu = new JPopupMenu();
        for (SplitScreenMode mode : modes) {
            JMenuItem item = new JMenuItem(mode.toString());
            item.addActionListener(e -> imageDisplayer.changeScreenMode(mode));
            popupMenu.add(item);
        }

        this.modes.addActionListener(e -> popupMenu.show(this.modes, 0, this.modes.getHeight()));
    }

    private void addResliceButton() {
        reslice = new JButton("Reslice");
        add(reslice);
        addSeparator();
    }

    public void addResliceListeners(ResliceDisplayer resliceDisplayer) {
        reslice.addActionListener(e -> resliceDisplayer.display());
    }

    private void addDrawingTools() {
        addSeparator();
        JLabel toolLabel = new JLabel("Select tool:");
        add(toolLabel);

        JComboBox<DrawingTool> tools = new JComboBox<>();

        for (DrawingTool tool : this.tools) {
            tools.addItem(tool);
        }

        JButton apply = new JButton("Apply drawing tool");
        apply.addActionListener(e -> {
            DrawingTool selectedTool = (DrawingTool) tools.getSelectedItem();
            if (selectedTool != null) {
                selectedImageManager.setDrawingTool(selectedTool);
            }
        });

        JButton settings = new JButton("Settings");

        JPopupMenu popupMenu = new JPopupMenu();
        JCheckBoxMenuItem everyFrame = new JCheckBoxMenuItem("Draw shapes separately for every frame");
        everyFrame.setSelected(false);
        everyFrame.addActionListener(e -> {
        });

        popupMenu.add(everyFrame);

        settings.addActionListener(e -> popupMenu.show(settings, 0, settings.getHeight()));

        tools.setMaximumSize(tools.getPreferredSize());
        tools.setMaximumRowCount(10);
        add(tools);
        add(apply);
        add(settings);
        addSeparator();
    }

    private void addWindowingButtons() {
        ImageIcon draw = new ImageIcon("src/main/resources/org/example/mydicomviewer/icons/windowing.png");
        manualWindowing = new JButton(draw);
        manualWindowing.setToolTipText("Manual windowing");
        add(manualWindowing);
    }

    public void addWindowingListeners(ImageDisplayer imageDisplayer) {
        manualWindowing.addActionListener(e -> {
            WindowingPopup popup = new WindowingPopup();
            int result = JOptionPane.showConfirmDialog(null, popup, "Manual Windowing", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                changeWindowParameters(imageDisplayer, popup);
            }

        });
    }

    private void changeWindowParameters(ImageDisplayer imageDisplayer, WindowingPopup popup) {
        double center = popup.getWindowCenter();
        double width = popup.getWindowWidth();
        imageDisplayer.setWindowing(center, width);
    }

    private void addFilesButton() {
        addFiles = new JButton("Add new...");

        createAddFilesPopupMenu();
        addFiles.addActionListener(e -> addFilesMenu.show(addFiles, 0, addFiles.getHeight()));

        add(addFiles);
        addSeparator();
    }

    private void createAddFilesPopupMenu() {
        addFilesMenu = new JPopupMenu();

        JMenuItem singleFile = new JMenuItem("...single DICOM file");
        singleFile.addActionListener((ActionEvent e) -> openFileCommand.execute());
        addFilesMenu.add(singleFile);

        JMenuItem folder = new JMenuItem("...DICOM image folder");
        folder.addActionListener((ActionEvent e) -> openFolderCommand.execute());
        addFilesMenu.add(folder);

        JMenuItem dicomdir = new JMenuItem("...DICOMDIR");
        dicomdir.addActionListener((ActionEvent e) -> commandDir.execute());
        addFilesMenu.add(dicomdir);

    }
}
