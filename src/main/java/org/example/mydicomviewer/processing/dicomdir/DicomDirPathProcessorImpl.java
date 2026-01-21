package org.example.mydicomviewer.processing.dicomdir;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;
import org.example.mydicomviewer.processing.file.TagNumber;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class DicomDirPathProcessorImpl implements DicomDirPathProcessor {
    @Override
    public Optional<DicomDirPath> getDicomDirPath(File file) {

        try (DicomInputStream dicomInputStream = new DicomInputStream(file)) {
            return Optional.of(getPathFromDicomInputStream(dicomInputStream));
        }
        catch (IOException e) {
            return Optional.empty();
        }
    }

    private DicomDirPath getPathFromDicomInputStream(DicomInputStream dicomInputStream) throws IOException {

        Attributes attributes = dicomInputStream.readDataset();
        Attributes meta = dicomInputStream.readFileMetaInformation();
        handleDicomDir(meta);
        return createDicomDirPath(attributes);
    }

    private void handleDicomDir(Attributes attributes) throws IOException {

        boolean isDicomDir = checkIfDicomDir(attributes);

        if (isDicomDir) {
            throw new IOException();
        }
    }

    private boolean checkIfDicomDir(Attributes attributes) {

        String value = getMediaStorageSOPClassUID(attributes);

        if (value != null) {
            return value.contains("1.2.840.10008.1.3.10");
        }

        return false;
    }

    private static String getMediaStorageSOPClassUID(Attributes attributes) {
        int[] tags = attributes.tags();
        String value = "";
        for (int tag : tags) {
            String address = TagUtils.toString(tag);
            String sought = TagNumber.MEDIA_STORAGE_SOP_CLASS_UID.getAddress().toString();
            if (address.equals(sought)) {
                value = attributes.getString(tag);
                break;
            }
        }
        return value;
    }

    private DicomDirPath createDicomDirPath(Attributes attributes) {

        int[] tags = attributes.tags();
        DicomDirPath path = new DicomDirPath();

        for (int tag : tags) {

            addToPathIfRelevant(attributes, tag, path);
        }

        return path;
    }

    private static void addToPathIfRelevant(Attributes attributes, int tag, DicomDirPath path) {
        String address = TagUtils.toString(tag);

        switch (address) {
            case "(0010,0020)":
                String patientId = attributes.getString(tag);
                path.setPatientId(patientId);
                break;
            case "(0020,0010)":
                String studyId = attributes.getString(tag);
                path.setStudyId(studyId);
                break;
            case "(0020,0011)":
                String seriesId = attributes.getString(tag);
                path.setSeriesId(seriesId);
                break;
            case "(0020,0013)":
                String instanceId = attributes.getString(tag);
                path.setInstanceId(instanceId);
                break;
            case "(0020,000D)":
                String studyInstanceId = attributes.getString(tag);
                path.setStudyInstanceUID(studyInstanceId);
                break;
            case "(0020,000E)":
                String seriesInstanceId = attributes.getString(tag);
                path.setSeriesInstanceUID(seriesInstanceId);
                break;
            default:
                break;
        }
    }

}

