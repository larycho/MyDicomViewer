package org.example.mydicomviewer;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;
import org.example.mydicomviewer.models.Tag;
import org.example.mydicomviewer.models.TagGroup;

import java.io.File;
import java.io.IOException;

public class FileTagsProcessorImpl implements FileTagsProcessor {

    public TagGroup getTagsFromFile(File file) {

        try (DicomInputStream dicomInputStream = new DicomInputStream(file)) {
            return getTagsFromDicomInputStream(dicomInputStream);
        }
        catch (IOException e) {
            // TODO
            return new TagGroup();
        }
    }

    private TagGroup getTagsFromDicomInputStream(DicomInputStream dicomInputStream) throws IOException {
        Attributes attributes = dicomInputStream.readDataset();
        return createTagGroupFromAttributes(attributes);
    }

    private TagGroup createTagGroupFromAttributes(Attributes attributes) {
        int[] tags = attributes.tags();
        TagGroup tagGroup = new TagGroup();

        for (int tag : tags) {

            Tag newTag = createTagFromTagNumber(tag, attributes);
            tagGroup.addTag(newTag);

        }

        return tagGroup;
    }

    private Tag createTagFromTagNumber(int tagNumber, Attributes attributes) {

        String address = TagUtils.toString(tagNumber);
        String description = ElementDictionary.getStandardElementDictionary().keywordOf(tagNumber);
        String value = attributes.getString(tagNumber);

        return new Tag(address, description, value);
    }
}
