package org.example.mydicomviewer.processing.dicomdir;

import java.io.File;

public class DicomDirPath {

    private String patientId;
    private String studyId;
    private String seriesId;
    private String instanceId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DicomDirPath other) {

            boolean isPatientIdEqual = this.patientId.equals(other.getPatientId());
            boolean isStudyIdEqual = this.studyId.equals(other.getStudyId());
            boolean isSeriesIdEqual = this.seriesId.equals(other.getSeriesId());

            return isPatientIdEqual && isStudyIdEqual && isSeriesIdEqual;
        }

        return false;
    }

}
