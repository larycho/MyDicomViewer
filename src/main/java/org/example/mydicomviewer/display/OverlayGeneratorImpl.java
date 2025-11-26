package org.example.mydicomviewer.display;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.Tag;
import org.example.mydicomviewer.models.TagGroup;

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
            String address = tag.getAddress();
            switch (address) {
                case PATIENT_NAME:
                    overlayText.addTopLeft("Name: " + tag.getValue());
                    break;
                case PATIENT_SEX:
                    overlayText.addTopLeft(tag.getValue());
                    break;
                case SERIES_DESCRIPTION:
                    overlayText.addTopLeft("Series description: " + tag.getValue());
                    break;
                case CONTENT_DATE:
                    String date = tag.getValue();
                    String year = date.substring(0, 4);
                    String month = date.substring(4, 6);
                    String day = date.substring(6, 8);
                    overlayText.addTopRight(day + "-" + month + "-" + year);
                    break;
                case CONTENT_TIME:
                    String time = tag.getValue();
                    String hour = time.substring(0, 2);
                    String minute = time.substring(2, 4);
                    String second = time.substring(4, 6);
                    overlayText.addTopRight(hour + ":" + minute + ":" + second);
                    break;
                case NUMBER_OF_FRAMES:
                    overlayText.addBottomLeft("Number of frames: " + tag.getValue());
                    break;
                case SERIES_NUMBER:
                    overlayText.addBottomLeft("Series number: " + tag.getValue());
                    break;
                case PATIENT_ID:
                    overlayText.addTopLeft("Patient ID: " + tag.getValue());
                    break;
                case MODALITY:
                    overlayText.addBottomLeft("Modality: " + tag.getValue());
                    break;
                default:
                    break;
            }
        }

        return overlayText;
    }
}
