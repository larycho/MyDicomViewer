package org.mydicomviewer.processing.io.file;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;
import org.mydicomviewer.models.Tag;
import org.mydicomviewer.models.TagAddress;
import org.mydicomviewer.models.TagGroup;
import org.mydicomviewer.models.TagSeries;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileTagsProcessorImpl implements FileTagsProcessor {

    public TagGroup getTagsFromFile(File file) {

        try (DicomInputStream dicomInputStream = new DicomInputStream(file)) {
            return getTagsFromDicomInputStream(dicomInputStream);
        }
        catch (IOException e) {
            return new TagGroup();
        }
    }

    private TagGroup getTagsFromDicomInputStream(DicomInputStream dicomInputStream) throws IOException {
        TagGroup metaInfoTags = getMetaInfoTags(dicomInputStream);
        TagGroup datasetTags = getDatasetTags(dicomInputStream);
        return mergeGroups(metaInfoTags, datasetTags);
    }

    private TagGroup getMetaInfoTags(DicomInputStream dicomInputStream) throws IOException {
        Attributes attributes = dicomInputStream.readFileMetaInformation();
        return createTagGroup(attributes);
    }

    private TagGroup getDatasetTags(DicomInputStream dicomInputStream) throws IOException {
        Attributes attributes = dicomInputStream.readDatasetUntilPixelData();
        return createTagGroup(attributes);
    }

    private TagGroup mergeGroups(TagGroup group1, TagGroup group2) {
        List<Tag> list1 = group1.allTags();
        List<Tag> list2 = group2.allTags();

        TagGroup mergedGroup = new TagGroup();
        for (Tag tag : list1) {
            mergedGroup.addTag(tag);
        }
        for (Tag tag : list2) {
            mergedGroup.addTag(tag);
        }
        return mergedGroup;
    }

    private TagGroup createTagGroup(Attributes attributes) {

        if (attributes == null) {
            return new TagGroup();
        }
        int[] tags = attributes.tags();
        TagGroup tagGroup = new TagGroup();

        for (int tag : tags) {

            Tag newTag = createTag(tag, attributes);
            tagGroup.addTag(newTag);

        }

        return tagGroup;
    }

    private Tag createTag(int tagNumber, Attributes attributes) {

        TagAddress address = createTagAddress(tagNumber);
        String description = ElementDictionary.getStandardElementDictionary().keywordOf(tagNumber);
        String value = attributes.getString(tagNumber);

        return new Tag(address, description, value);
    }

    private TagAddress createTagAddress(int tagNumber) {

        String addressAsString = TagUtils.toString(tagNumber);

        String[] addressParts = addressAsString.split(",");
        String[] cleanParts = cleanUpAddressParts(addressParts);

        String groupNumber = cleanParts[0];
        String elementNumber = cleanParts[1];


        return new TagAddress(groupNumber, elementNumber);
    }

    private String[] cleanUpAddressParts(String[] addressParts) {
        String groupNumber = addressParts[0];
        groupNumber = groupNumber.replace("(", "");
        groupNumber = groupNumber.trim();

        String elementNumber = addressParts[1];
        elementNumber = elementNumber.replace(")", "");
        elementNumber = elementNumber.trim();

        return new String[]{groupNumber, elementNumber};
    }
}
