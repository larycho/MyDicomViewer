package org.example.mydicomviewer.processing.file;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.Tag;
import org.example.mydicomviewer.models.TagAddress;
import org.example.mydicomviewer.processing.image.WindowingParameters;

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

    public Optional<Boolean> getPixelRepresentation() {

        TagAddress address = TagNumber.PIXEL_REPRESENTATION.getAddress();

        return getBooleanTagValue(address);
    }

    public Optional<PhotometricInterpretation> getPhotometricInterpretation() {

        TagAddress address = TagNumber.PHOTOMETRIC_INTERPRETATION.getAddress();

        String value = getStringTagValue(address).orElse("");

        switch (value) {
            case "MONOCHROME1":
                return Optional.of(PhotometricInterpretation.MONOCHROME1);
            case "MONOCHROME2":
                return Optional.of(PhotometricInterpretation.MONOCHROME2);
            case "RGB":
                return Optional.of(PhotometricInterpretation.RGB);
        }

        return Optional.empty();
    }

    private Optional<String> getStringTagValue(TagAddress address) {

        if (dicomFile.containsTag(address)) {

            Tag tag = dicomFile.getTag(address);
            String content = tag.getValue();
            return Optional.of(content);

        }
        return Optional.empty();
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

    private Optional<Boolean> getBooleanTagValue(TagAddress address) {

        if (dicomFile.containsTag(address)) {

            Tag tag = dicomFile.getTag(address);
            String content = tag.getValue();

            Optional<Boolean> intResult = parseBooleanFromInt(content);
            if (intResult.isPresent()) {
                return intResult;
            }
        }
        return Optional.empty();
    }

    private Optional<Boolean> parseBooleanFromInt(String content) {
        Optional<Integer> intResult = parseIntValue(content);

        if (intResult.isPresent()) {
            if (intResult.get() == 1) {
                return Optional.of(true);
            }
            if (intResult.get() == 0) {
                return Optional.of(false);
            }
        }
        return Optional.empty();
    }

    private Optional<Boolean> parseBooleanValue(String text) {

        try {
            Boolean center = Boolean.parseBoolean(text);
            return Optional.of(center);
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<Integer> parseIntValue(String text) {

        try {
            Integer value = Integer.parseInt(text);
            return Optional.of(value);
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public WindowingParameters getWindowingParameters() {

        Optional<Double> center = getWindowCenter();
        Optional<Double> width = getWindowWidth();
        Optional<Double> slope = getRescaleSlope();
        Optional<Double> intercept = getRescaleIntercept();
        Optional<Boolean> signed = getPixelRepresentation();
        Optional<PhotometricInterpretation> photometricInterpretation = getPhotometricInterpretation();

        int windowLevel = (int) Math.round(center.orElse(150.0));
        int windowWidth = (int) Math.round(width.orElse(300.0));

        if (windowWidth == 0 && windowLevel == 0) {
            windowWidth = 400;
            windowLevel = 50;
        }

        double rescaleIntercept = intercept.orElse(0.0);
        double rescaleSlope = slope.orElse(1.0);
        boolean isSigned = signed.orElse(false);
        PhotometricInterpretation photometric = photometricInterpretation.orElse(PhotometricInterpretation.RGB);

        WindowingParameters windowingParameters = new WindowingParameters();
        windowingParameters.setWindowLevel(windowLevel);
        windowingParameters.setWindowWidth(windowWidth);
        windowingParameters.setRescaleSlope(rescaleSlope);
        windowingParameters.setRescaleIntercept(rescaleIntercept);
        windowingParameters.setSigned(isSigned);
        windowingParameters.setPhotometricInterpretation(photometric);
        return windowingParameters;
    }

}
