package org.example.mydicomviewer.processing.file;

import org.example.mydicomviewer.models.TagAddress;

public enum TagNumber {
    WINDOW_CENTER("0028", "1050"),
    WINDOW_WIDTH("0028", "1051"),
    RESCALE_INTERCEPT("0028", "1052"),
    RESCALE_SLOPE("0028", "1053");

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
