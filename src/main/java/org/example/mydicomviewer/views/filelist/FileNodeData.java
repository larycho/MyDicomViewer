package org.example.mydicomviewer.views.filelist;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class FileNodeData {

    private File file;

    // User friendly identifiers
    private String patientId;
    private String studyId;
    private String seriesNumber;
    private Integer instanceNumber;

    // UIDs - unique identifiers
    // They are not very user-friendly,
    // but very important for actually defining patient/study/series structure
    private String studyInstanceUid;
    private String seriesInstanceUid;
    // unique image identifier
    private String sopInstanceUid;

    public Optional<File> getFile() {
        return Optional.ofNullable(file);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Optional<String> getFileName() {

        if (getFile().isPresent()) {
            return Optional.of(getFile().get().getName());
        }

        return Optional.empty();
    }

    public Optional<String> getPatientId() {
        return Optional.ofNullable(patientId);
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Optional<String> getStudyId() {
        return Optional.ofNullable(studyId);
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public Optional<String> getSeriesNumber() {
        return Optional.ofNullable(seriesNumber);
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public Optional<String> getStudyInstanceUid() {
        return Optional.ofNullable(studyInstanceUid);
    }

    public void setStudyInstanceUid(String studyInstanceUid) {
        this.studyInstanceUid = studyInstanceUid;
    }

    public Optional<String> getSeriesInstanceUid() {
        return Optional.ofNullable(seriesInstanceUid);
    }

    public void setSeriesInstanceUid(String seriesInstanceUid) {
        this.seriesInstanceUid = seriesInstanceUid;
    }

    public Optional<String> getSopInstanceUid() {
        return Optional.ofNullable(sopInstanceUid);
    }

    public void setSopInstanceUid(String sopInstanceUid) {
        this.sopInstanceUid = sopInstanceUid;
    }

    public Optional<Integer> getInstanceNumber() {
        return Optional.ofNullable(instanceNumber);
    }

    public void setInstanceNumber(Integer instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileNodeData that = (FileNodeData) o;

        // checks if all the fields are equal
        if (!Objects.equals(file, that.file)) return false;
        if (!Objects.equals(patientId, that.patientId)) return false;
        if (!Objects.equals(studyId, that.studyId)) return false;
        if (!Objects.equals(seriesNumber, that.seriesNumber)) return false;
        if (!Objects.equals(studyInstanceUid, that.studyInstanceUid)) return false;
        if (!Objects.equals(seriesInstanceUid, that.seriesInstanceUid)) return false;
        if (!Objects.equals(sopInstanceUid, that.sopInstanceUid)) return false;
        return Objects.equals(instanceNumber, that.instanceNumber);
    }
}
