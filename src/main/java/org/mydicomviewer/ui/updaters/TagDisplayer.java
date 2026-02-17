package org.mydicomviewer.ui.updaters;

import com.google.inject.Inject;
import org.mydicomviewer.events.DicomDirLoadedEvent;
import org.mydicomviewer.events.FileLoadedEvent;
import org.mydicomviewer.events.FolderLoadedEvent;
import org.mydicomviewer.events.FragmentedFileSelectedEvent;
import org.mydicomviewer.events.listeners.DicomDirLoadedListener;
import org.mydicomviewer.events.listeners.FileLoadedListener;
import org.mydicomviewer.events.listeners.FolderLoadedListener;
import org.mydicomviewer.events.listeners.FragmentedFileSelectedListener;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.models.Tag;
import org.mydicomviewer.models.TagGroup;
import org.mydicomviewer.services.DicomDirLoadManager;
import org.mydicomviewer.events.services.FileLoadEventService;
import org.mydicomviewer.events.services.FolderLoadedEventService;
import org.mydicomviewer.events.services.FragmentedFileEventService;
import org.mydicomviewer.ui.TagPanel;

import javax.swing.*;
import java.util.ArrayList;

public class TagDisplayer implements FileLoadedListener, FolderLoadedListener, DicomDirLoadedListener, FragmentedFileSelectedListener {

    private final TagPanel tagPanel;

    @Inject
    public TagDisplayer(TagPanel tagPanel,
                        FileLoadEventService fileLoadEventService,
                        FolderLoadedEventService folderLoadedEventService,
                        DicomDirLoadManager dicomDirLoadManager,
                        FragmentedFileEventService fragmentedFileEventService) {
        this.tagPanel = tagPanel;
        fileLoadEventService.addListener(this);
        folderLoadedEventService.addListener(this);
        dicomDirLoadManager.addListener(this);
        fragmentedFileEventService.addListener(this);
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile dicomFile = event.getFile();
        ArrayList<Tag> tags = getTagsFromFile(dicomFile);

        if (!tags.isEmpty()) {

            int rows = tags.size();
            int cols = 3;

            String[][] dataArray = createTagListArray(tags, rows, cols);
            JTable table = createTagTable(dataArray);
            tagPanel.addTabletoScrollPane(table);

        }
    }

    private JTable createTagTable(String[][] data) {
        String[] columnNames = { "Tag", "Description", "Value" };
        return new JTable(data, columnNames);
    }

    private ArrayList<Tag> getTagsFromFile(DicomFile dicomFile) {
        TagGroup tagGroup = dicomFile.getTags();
        if (tagGroup != null) {
            return tagGroup.allTags();
        }
        return new ArrayList<>();
    }

    private String[][] createTagListArray(ArrayList<Tag> tags, int rows, int cols) {
        String[][] dataArray = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            Tag tag = tags.get(i);

            dataArray[i][0] = tag.getAddress().toString();
            dataArray[i][1] = tag.getDescription();
            dataArray[i][2] = getTagValue(tag);
        }
        return dataArray;
    }

    private String getTagValue(Tag tag) {
        String missingMessage = "Not specified";
        if (tag.getValue() == null || tag.getValue().isEmpty()) {
            return missingMessage;
        }
        else {
            return tag.getValue();
        }
    }

    @Override
    public void dicomDirLoaded(DicomDirLoadedEvent dicomDirLoadedEvent) {
        tagPanel.clearScrollPane();
    }

    @Override
    public void folderLoaded(FolderLoadedEvent event) {
        tagPanel.clearScrollPane();
    }

    @Override
    public void fragmentedFileSelected(FragmentedFileSelectedEvent event) {
    }
}
