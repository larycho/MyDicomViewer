package org.example.mydicomviewer.processing.dicomdir;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;

import java.io.File;
import java.io.IOException;

public class DicomDirPathProcessorImpl implements DicomDirPathProcessor {
    @Override
    public DicomDirPath getDicomDirPath(File file) {

        try (DicomInputStream dicomInputStream = new DicomInputStream(file)) {
            return getPathFromDicomInputStream(dicomInputStream);
        }
        catch (IOException e) {
            // TODO
            return new DicomDirPath();
        }
    }

    private DicomDirPath getPathFromDicomInputStream(DicomInputStream dicomInputStream) throws IOException {

        Attributes attributes = dicomInputStream.readDataset();
        return createDicomDirPath(attributes);
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
            default:
                break;
        }
    }

}

