package org.example.mydicomviewer.processing.file;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;
import org.example.mydicomviewer.models.Tag;
import org.example.mydicomviewer.models.TagAddress;
import org.example.mydicomviewer.models.TagGroup;

import java.io.File;
import java.io.IOException;

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

        Attributes attributes = dicomInputStream.readDataset();
        return createTagGroup(attributes);
    }

    private TagGroup createTagGroup(Attributes attributes) {

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
