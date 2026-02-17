package org.mydicomviewer.processing.overlay;

import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.models.Tag;
import org.mydicomviewer.models.TagGroup;

import java.util.List;

public class OverlayGeneratorImpl implements OverlayGenerator {

    private final String PATIENT_NAME = "(0010,0010)";
    private final String PATIENT_SEX = "(0010,0040)";
    private final String SERIES_DESCRIPTION = "(0008,103E)";
    private final String CONTENT_DATE = "(0008,0023)";
    private final String CONTENT_TIME = "(0008,0033)";
    private final String NUMBER_OF_FRAMES = "(0028,0008)";
    private final String SERIES_NUMBER = "(0020,0011)";
    private final String PATIENT_ID = "(0010,0020)";
    private final String MODALITY = "(0008,0060)";

    @Override
    public OverlayText createOverlayText(DicomFile dicomFile) {

        TagGroup group = dicomFile.getTags();
        OverlayText overlayText = new OverlayText();
        List<Tag> tags = group.allTags();

        for (Tag tag : tags) {

            addTextBasedOnAddress(tag, overlayText);
        }

        return overlayText;
    }

    private void addTextBasedOnAddress(Tag tag, OverlayText overlayText) {

        String value = getTagValue(tag);
        String address = tag.getAddress().toString();

        switch (address) {
            case PATIENT_NAME:
                overlayText.addTopLeft("Name: " + value);
                break;
            case PATIENT_SEX:
                overlayText.addTopLeft(value);
                break;
            case SERIES_DESCRIPTION:
                overlayText.addTopLeft("Series description: " + value);
                break;
            case CONTENT_DATE:
                String year = value.substring(0, 4);
                String month = value.substring(4, 6);
                String day = value.substring(6, 8);
                overlayText.addTopRight(day + "-" + month + "-" + year);
                break;
            case CONTENT_TIME:
                String hour = value.substring(0, 2);
                String minute = value.substring(2, 4);
                String second = value.substring(4, 6);
                overlayText.addTopRight(hour + ":" + minute + ":" + second);
                break;
            case NUMBER_OF_FRAMES:
                overlayText.addBottomLeft("Number of frames: " + value);
                break;
            case SERIES_NUMBER:
                overlayText.addBottomLeft("Series number: " + value);
                break;
            case PATIENT_ID:
                overlayText.addTopLeft("Patient ID: " + value);
                break;
            case MODALITY:
                overlayText.addBottomLeft("Modality: " + value);
                break;
            default:
                break;
        }
    }

    private static String getTagValue(Tag tag) {
        String value = tag.getValue();

        if (value == null || value.isEmpty()) {
            value = "Unknown";
        }
        return value;
    }
}
