package org.example.mydicomviewer.processing.file;

public enum PhotometricInterpretation {
    MONOCHROME1("MONOCHROME1"),
    MONOCHROME2("MONOCHROME2"),
    PALETTE_COLOR("PALETTE COLOR"),
    RGB("RGB");

    private final String textRepresentation;
    PhotometricInterpretation(String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    @Override
    public String toString() {
        return textRepresentation;
    }
}
