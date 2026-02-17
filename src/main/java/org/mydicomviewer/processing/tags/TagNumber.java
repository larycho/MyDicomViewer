package org.mydicomviewer.processing.tags;

import org.mydicomviewer.models.TagAddress;

public enum TagNumber {
    MEDIA_STORAGE_SOP_CLASS_UID("0002", "0002"),
    PATIENT_ID("0010", "0020"),
    STUDY_ID("0020", "0010"),
    SERIES_NUMBER("0020", "0011"),
    WINDOW_CENTER("0028", "1050"),
    WINDOW_WIDTH("0028", "1051"),
    RESCALE_INTERCEPT("0028", "1052"),
    RESCALE_SLOPE("0028", "1053"),
    PIXEL_REPRESENTATION("0028", "0103"),
    PHOTOMETRIC_INTERPRETATION("0028", "0004"),
    SOP_INSTANCE_UID("0008", "0018"),
    STUDY_INSTANCE_UID("0020", "000D"),
    SERIES_INSTANCE_UID("0020", "000E"),
    INSTANCE_NUMBER("0020", "0013"),
    MODALITY("0008", "0060");

    private final String groupNumber;
    private final String elementNumber;

    TagNumber(String groupNumber, String elementNumber) {
        this.groupNumber = groupNumber;
        this.elementNumber = elementNumber;
    }

    public TagAddress getAddress() {
        return new TagAddress(groupNumber, elementNumber);
    }
}
