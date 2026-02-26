package org.mydicomviewer.ui.updaters;

import com.google.inject.Inject;
import org.mydicomviewer.events.*;
import org.mydicomviewer.events.listeners.*;
import org.mydicomviewer.events.services.*;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.models.Tag;
import org.mydicomviewer.models.TagGroup;
import org.mydicomviewer.services.DicomDirLoadManager;
import org.mydicomviewer.ui.TagPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TagDisplayer implements FileLoadedListener, FolderLoadedListener, DicomDirLoadedListener, FragmentedFileSelectedListener, FrameSkipEventListener, PanelSelectedListener {

    private final TagPanel tagPanel;

    @Inject
    public TagDisplayer(TagPanel tagPanel,
                        FileLoadEventService fileLoadEventService,
                        FolderLoadedEventService folderLoadedEventService,
                        DicomDirLoadManager dicomDirLoadManager,
                        FragmentedFileEventService fragmentedFileEventService,
                        FrameSkipEventService frameSkipEventService,
                        ImagePanelSelectedEventService imagePanelSelectedEventService) {
        this.tagPanel = tagPanel;
        fileLoadEventService.addListener(this);
        folderLoadedEventService.addListener(this);
        dicomDirLoadManager.addListener(this);
        fragmentedFileEventService.addListener(this);
        frameSkipEventService.addListener(this);
        imagePanelSelectedEventService.addListener(this);
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile dicomFile = event.getFile();
        if (dicomFile.getTags().allTags().isEmpty()) { return; }
        ArrayList<Tag> tags = getTagsFromFile(dicomFile);

        if (!tags.isEmpty()) {

            updateScrollPane(tags);

        }
    }

    private void updateScrollPane(List<Tag> tags) {
        int rows = tags.size();
        int cols = 3;

        String[][] dataArray = createTagListArray(tags, rows, cols);
        JTable table = createTagTable(dataArray);
        tagPanel.addTableToScrollPane(table);
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

    private String[][] createTagListArray(List<Tag> tags, int rows, int cols) {
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

    @Override
    public void frameSkipped(FrameSkipEvent event) {
        DicomFile file = event.getSourceFile();
        int frameNumber = event.getFrameNumber();
        TagGroup tagGroup = file.getTags(frameNumber);

        List<Tag> tags = (tagGroup != null) ? tagGroup.allTags() : new ArrayList<>();
        if (tags.isEmpty()) { return; }

        updateScrollPane(tags);
    }

    @Override
    public void panelSelected(PanelSelectedEvent event) {
        DicomFile file = event.getPanel().getDicomFile();
        int frameNumber = event.getPanel().getCurrentFrameNumber();
        TagGroup tagGroup = file.getTags(frameNumber);

        List<Tag> tags = (tagGroup != null) ? tagGroup.allTags() : new ArrayList<>();
        if (tags.isEmpty()) { return; }
        updateScrollPane(tags);
    }
}
