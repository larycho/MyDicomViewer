package org.example.mydicomviewer.views.filelist;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.commands.OpenDicomDirCommand;
import org.example.mydicomviewer.commands.OpenFileCommand;
import org.example.mydicomviewer.listeners.FileLoadedListener;
import org.example.mydicomviewer.services.DicomDirLoadManager;
import org.example.mydicomviewer.services.OpenFileManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

@Singleton
public class FileListPanel extends JPanel {

    private JPanel collapsedPanel;
    private JPanel expandedPanel;
    private CardLayout cardLayout;
    private JButton expandButton;
    private JButton collapseButton;
    private JButton addFileButton;
    private JButton addDicomdirButton;
    private FileListScrollPane scrollPane;

    private ArrayList<FileLoadedListener> listeners;
    private OpenFileCommand openFileCommand;
    private OpenDicomDirCommand commandDir;
    private OpenFileManager openFileManager;

    @Inject
    public FileListPanel(OpenFileCommand command, OpenDicomDirCommand commandDir, OpenFileManager openFileManager) {
        this.openFileCommand = command;
        this.commandDir = commandDir;
        this.openFileManager = openFileManager;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        createCollapsedPanel();
        createExpandedPanel();
        setCardSides();

        listeners = new ArrayList<>();
        setupActionListeners();
    }

    public void addFileToList(DefaultMutableTreeNode node) {
        scrollPane.addNode(node);
    }

    public void addFileToList(DefaultMutableTreeNode node, DefaultMutableTreeNode parent) {
        scrollPane.addNode(node, parent);
    }

    public void addFileLoadedListener(FileLoadedListener listener) {
        listeners.add(listener);
    }

    private void setupActionListeners() {
        collapseButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(this, "Collapsed");
            this.getParent().repaint();
            this.getParent().revalidate();
        });

        expandButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(this, "Expanded");
            this.getParent().repaint();
            this.getParent().revalidate();
        });

        addFileButton.addActionListener((ActionEvent e) -> {
           openFileCommand.execute();
        });

        addDicomdirButton.addActionListener((ActionEvent e) -> {
            commandDir.execute();
        });
    }

    private void setCardSides() {
        add(collapsedPanel, "Collapsed");
        add(expandedPanel, "Expanded");
    }

    private void createExpandedPanel() {
        expandedPanel = new JPanel();
        expandedPanel.setLayout(new BoxLayout(expandedPanel, BoxLayout.Y_AXIS));
        expandedPanel.setPreferredSize(new Dimension(300, 100));

        collapseButton = new JButton("Hide panel");
        addFileButton = new JButton("Add new file");
        addDicomdirButton = new JButton("Add a Dicomdir");

        scrollPane = new FileListScrollPane(openFileManager);
        expandedPanel.add(collapseButton);
        expandedPanel.add(addFileButton);
        expandedPanel.add(addDicomdirButton);
        expandedPanel.add(scrollPane);

        collapseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addFileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addDicomdirButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void createCollapsedPanel() {
        collapsedPanel = new JPanel();
        expandButton = new JButton(">");
        collapsedPanel.add(expandButton);
    }

    // Needed for this panel to resize according to the size of its content
    @Override
    public Dimension getPreferredSize() {
        for (Component c : getComponents()) {
            if (c.isVisible()) {
                return c.getPreferredSize();
            }
        }
        return super.getPreferredSize();
    }
}
