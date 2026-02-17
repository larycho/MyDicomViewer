package org.mydicomviewer.ui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.commands.ExportImagesCommand;
import org.mydicomviewer.commands.OpenDicomDirCommand;
import org.mydicomviewer.commands.OpenFileCommand;
import org.mydicomviewer.commands.OpenFolderCommand;
import org.mydicomviewer.ui.image.SelectedImageManager;
import org.mydicomviewer.ui.display.SplitScreenMode;
import org.mydicomviewer.events.services.ResliceEventService;
import org.mydicomviewer.events.services.ToolBarEventService;
import org.mydicomviewer.processing.windowing.WindowPreset;
import org.mydicomviewer.services.*;
import org.mydicomviewer.services.implementations.ScreenModeProviderImpl;
import org.mydicomviewer.services.implementations.WindowingPresetProviderImpl;
import org.mydicomviewer.tools.*;
import org.kordamp.ikonli.materialdesign2.*;
import org.kordamp.ikonli.swing.FontIcon;
import org.mydicomviewer.tools.factories.DrawingToolFactory;
import org.mydicomviewer.tools.factories.LineToolFactory;
import org.mydicomviewer.tools.factories.OvalToolFactory;
import org.mydicomviewer.tools.factories.PencilToolFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

@Singleton
public class ToolBar extends JToolBar {

    private JButton manualWindowing;
    private JButton reslice;
    private JButton modes;
    private JButton addFiles;

    private JComboBox<WindowPreset> presetComboBox;

    private JPopupMenu addFilesMenu;

    private Set<DrawingToolFactory> tools;
    private List<WindowPreset> presets;

    private final SelectedImageManager selectedImageManager;
    private final ToolBarEventService toolBarEventService;
    private final ResliceEventService resliceEventService;

    private final OpenFileCommand openFileCommand;
    private final OpenDicomDirCommand commandDir;
    private final OpenFolderCommand openFolderCommand;
    private final ExportImagesCommand exportImagesCommand;

    private final int DEFAULT_ICON_SIZE = 40;
    private final Color DEFAULT_ICON_COLOR = UIManager.getColor("Component.accentColor");

    @Inject
    public ToolBar(Set<DrawingToolFactory> pluginTools,
                   SelectedImageManager selectedImageManager,
                   OpenFileCommand openFileCommand,
                   OpenDicomDirCommand commandDir,
                   OpenFolderCommand openFolderCommand,
                   ToolBarEventService toolBarEventService,
                   ResliceEventService resliceEventService,
                   ExportImagesCommand exportImagesCommand) {
        super();

        this.openFileCommand = openFileCommand;
        this.openFolderCommand = openFolderCommand;
        this.commandDir = commandDir;
        this.exportImagesCommand = exportImagesCommand;

        createToolList(pluginTools);
        createPresetList();

        this.selectedImageManager = selectedImageManager;
        this.toolBarEventService = toolBarEventService;
        this.resliceEventService = resliceEventService;

        addFilesButton();
        addExportButton();
        addResliceButton();
        addScreenModeButton();
        addWindowingButtons();
        addWindowPresetSelection();
        addDrawingTools();

        setupListeners();
    }

    private void addExportButton() {
        FontIcon exportIcon = FontIcon.of(MaterialDesignC.CONTENT_SAVE, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        JButton export = new JButton(exportIcon);
        export.setToolTipText("Export images");
        export.addActionListener(e -> exportImagesCommand.execute());
        add(export);
        addSeparator();
    }

    private void createToolList(Set<DrawingToolFactory> pluginTools) {
        tools = new HashSet<>();

        //tools.add(new OvalToolFactory());
        //tools.add(new PencilToolFactory());
        //tools.add(new LineToolFactory());

        tools.addAll(pluginTools);
    }

    private void createPresetList() {
        presets = new ArrayList<>();
        presets.add(new WindowPreset(0, 0, "Default"));

        WindowingPresetProvider presetProvider = new WindowingPresetProviderImpl();
        List<WindowPreset> additionalPresets = presetProvider.getAvailablePresets();
        additionalPresets.sort(Comparator.comparing(WindowPreset::getName));

        presets.addAll(additionalPresets);
    }

    private void setupListeners() {
        addScreenModeListeners();
        addWindowingListeners();
        addResliceListeners();
        addPresetSelectionListeners();
    }

    private void addScreenModeButton() {
        FontIcon icon = FontIcon.of(MaterialDesignV.VIEW_DASHBOARD, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        modes = new JButton(icon);
        modes.setToolTipText("Select screen mode");
        add(modes);
    }

    public void addScreenModeListeners() {
        ScreenModeProvider screenModeProvider = new ScreenModeProviderImpl();
        List<SplitScreenMode> modes = screenModeProvider.getAvailableScreenModes();

        JPopupMenu popupMenu = new JPopupMenu();
        for (SplitScreenMode mode : modes) {
            JMenuItem item = new JMenuItem(mode.toString());
            item.addActionListener(e -> toolBarEventService.notifyScreenModeChanged(mode));
            popupMenu.add(item);
        }

        this.modes.addActionListener(e -> popupMenu.show(this.modes, 0, this.modes.getHeight()));
    }

    private void addResliceButton() {
        FontIcon icon = FontIcon.of(MaterialDesignA.AXIS_ARROW, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        reslice = new JButton(icon);
        reslice.setToolTipText("Reslice - Multiplanar Reconstruction");
        add(reslice);
        addSeparator();
    }

    public void addResliceListeners() {
        reslice.addActionListener(e -> resliceEventService.notifyResliceRequested());
    }

    private void addDrawingTools() {
        addSeparator();

        JLabel toolLabel = new JLabel("Select tool:");

        JComboBox<DrawingToolFactory> toolsComboBox = getDrawingToolComboBox();

        JButton apply = getApplyDrawingButton(toolsComboBox);

        JButton settings = getSettingsButton();

        add(toolLabel);
        add(toolsComboBox);
        add(apply);
        add(settings);
        addSeparator();
    }

    private JButton getSettingsButton() {
        FontIcon settingsIcon = FontIcon.of(MaterialDesignC.COG, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        JButton settings = new JButton(settingsIcon);
        settings.setToolTipText("Draw settings");

        JPopupMenu popupMenu = getPopupMenu();

        settings.addActionListener(e -> popupMenu.show(settings, 0, settings.getHeight()));

        return settings;
    }

    private static JPopupMenu getPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JCheckBoxMenuItem everyFrame = new JCheckBoxMenuItem("Draw shapes separately for every frame");
        everyFrame.setSelected(false);
        everyFrame.addActionListener(e -> {});

        popupMenu.add(everyFrame);
        return popupMenu;
    }

    private JButton getApplyDrawingButton(JComboBox<DrawingToolFactory> toolsComboBox) {
        FontIcon drawIcon = FontIcon.of(MaterialDesignD.DRAW, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        JButton apply = new JButton(drawIcon);
        apply.setToolTipText("Apply drawing tool");
        apply.addActionListener(e -> {
            DrawingToolFactory factory = (DrawingToolFactory) toolsComboBox.getSelectedItem();
            if (factory != null) {
                DrawingTool selectedTool = factory.getTool();
                selectedImageManager.setDrawingTool(selectedTool);
            }
        });
        return apply;
    }

    private JComboBox<DrawingToolFactory> getDrawingToolComboBox() {
        JComboBox<DrawingToolFactory> tools = new JComboBox<>();

        for (DrawingToolFactory tool : this.tools) {
            tools.addItem(tool);
        }

        tools.addActionListener(e -> {
            DrawingToolFactory factory = (DrawingToolFactory) tools.getSelectedItem();
            if (factory != null) {
                DrawingTool selectedTool = factory.getTool();
                selectedImageManager.setDrawingTool(selectedTool);
            }
        });

        tools.setMaximumSize(tools.getPreferredSize());
        tools.setMaximumRowCount(10);
        return tools;
    }

    private void addWindowingButtons() {
        addSeparator();
        FontIcon icon = FontIcon.of(MaterialDesignC.CONTRAST_CIRCLE, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        manualWindowing = new JButton(icon);
        manualWindowing.setToolTipText("Manual windowing");
        add(manualWindowing);
    }

    public void addWindowingListeners() {
        manualWindowing.addActionListener(e -> {
            WindowingPopup popup = new WindowingPopup();
            int result = JOptionPane.showConfirmDialog(null, popup, "Manual Windowing", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                changeWindowParameters(popup);
            }

        });
    }

    private void changeWindowParameters(WindowingPopup popup) {
        int center = popup.getWindowCenter();
        int width = popup.getWindowWidth();
        toolBarEventService.notifyWindowingChanged(center, width);
    }

    private void addFilesButton() {
        FontIcon icon = FontIcon.of(MaterialDesignF.FOLDER_PLUS, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        addFiles = new JButton(icon);
        addFiles.setToolTipText("Add new files");

        createAddFilesPopupMenu();
        addFiles.addActionListener(e -> addFilesMenu.show(addFiles, 0, addFiles.getHeight()));

        add(addFiles);
        addSeparator();
    }

    private void createAddFilesPopupMenu() {
        addFilesMenu = new JPopupMenu();

        JMenuItem singleFile = new JMenuItem("Add a single file");
        singleFile.addActionListener((ActionEvent e) -> openFileCommand.execute());
        addFilesMenu.add(singleFile);

        JMenuItem folder = new JMenuItem("Add a DICOM image folder");
        folder.addActionListener((ActionEvent e) -> openFolderCommand.execute());
        addFilesMenu.add(folder);

        JMenuItem dicomdir = new JMenuItem("Add a DICOMDIR");
        dicomdir.addActionListener((ActionEvent e) -> commandDir.execute());
        addFilesMenu.add(dicomdir);

    }

    private void addWindowPresetSelection() {
        presetComboBox = new JComboBox<>();

        for (WindowPreset preset : this.presets) {
            presetComboBox.addItem(preset);
        }

        presetComboBox.setMaximumSize(presetComboBox.getPreferredSize());
        presetComboBox.setMaximumRowCount(10);
        add(presetComboBox);
    }

    private void addPresetSelectionListeners() {
        presetComboBox.addActionListener(e -> {

            if (e.getSource() instanceof JComboBox<?> comboBox) {
                if (comboBox.getSelectedItem() instanceof WindowPreset preset) {

                    if (preset.getName().equals("Default")) {
                        toolBarEventService.notifyWindowingReset();
                    }
                    else {
                        toolBarEventService.notifyPresetsChanged(preset);
                    }
                }
            }
        });
    }

    public void showDefaultPreset() {

        for (int i = 0; i < presetComboBox.getItemCount(); i++) {

            WindowPreset preset = presetComboBox.getItemAt(i);

            if (preset.getName().equals("Default")) {
                presetComboBox.setSelectedIndex(i);
                break;
            }
        }
    }
}
