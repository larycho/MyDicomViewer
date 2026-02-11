package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.events.FragmentedFileSelectedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.Tag;
import org.example.mydicomviewer.models.TagGroup;
import org.example.mydicomviewer.services.DicomDirLoadManager;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.FolderLoadedEventService;
import org.example.mydicomviewer.services.FragmentedFileEventService;
import org.example.mydicomviewer.views.TagPanel;

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

    public TagDisplayer(TagPanel tagPanel) {
        this.tagPanel = tagPanel;
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
