package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.Tag;
import org.example.mydicomviewer.models.TagGroup;
import org.example.mydicomviewer.views.TagPanel;

import javax.swing.*;
import java.util.ArrayList;

public class TagDisplayer implements FileLoadedListener {

    private TagPanel tagPanel;

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
        JTable table = new JTable(data, columnNames);
        return table;
    }

    private ArrayList<Tag> getTagsFromFile(DicomFile dicomFile) {
        TagGroup tagGroup = dicomFile.getTags();
        if (tagGroup != null) {
            return tagGroup.allTags();
        }
        return new ArrayList<>();
    }

    private static String[][] createTagListArray(ArrayList<Tag> tags, int rows, int cols) {
        String[][] dataArray = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            Tag tag = tags.get(i);

            dataArray[i][0] = tag.getAddress();
            dataArray[i][1] = tag.getDescription();
            dataArray[i][2] = tag.getValue();
        }
        return dataArray;
    }
}
