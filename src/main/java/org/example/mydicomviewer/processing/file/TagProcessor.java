package org.example.mydicomviewer.processing.file;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.Tag;
import org.example.mydicomviewer.models.TagAddress;

import java.util.Optional;

public class TagProcessor {

    private final DicomFile dicomFile;

    public TagProcessor(DicomFile file) {
        this.dicomFile = file;
    }

    public Optional<Double> getWindowCenter() {

        TagAddress address = TagNumber.WINDOW_CENTER.getAddress();

        return getTagValue(address);
    }

    public Optional<Double> getWindowWidth() {

        TagAddress address = TagNumber.WINDOW_WIDTH.getAddress();

        return getTagValue(address);
    }

    public Optional<Double> getRescaleIntercept() {

        TagAddress address = TagNumber.RESCALE_INTERCEPT.getAddress();

        return getTagValue(address);
    }

    public Optional<Double> getRescaleSlope() {

        TagAddress address = TagNumber.RESCALE_SLOPE.getAddress();

        return getTagValue(address);
    }

    private Optional<Double> getTagValue(TagAddress address) {

        if (dicomFile.containsTag(address)) {

            Tag tag = dicomFile.getTag(address);
            String content = tag.getValue();
            return parseDoubleValue(content);

        }
        return Optional.empty();
    }

    private Optional<Double> parseDoubleValue(String text) {

        try {
            Double center = Double.parseDouble(text);
            return Optional.of(center);
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
