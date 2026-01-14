package org.example.mydicomviewer.processing.dicomdir;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.media.DicomDirReader;
import org.example.mydicomviewer.models.DicomDirectory;
import org.example.mydicomviewer.models.DicomDirectoryRecord;
import org.example.mydicomviewer.views.filelist.FileNodeType;

import java.io.File;
import java.io.IOException;

public class DicomDirProcessorImpl implements DicomDirProcessor {

    private DicomDirectory mainDicomDirectory;

    @Override
    public DicomDirectory openDicomDirectory(File file) {

        DicomDirectoryRecord root = new DicomDirectoryRecord("root");
        mainDicomDirectory = new DicomDirectory(file, root);

        try (DicomDirReader reader = new DicomDirReader(file)) {
            Attributes attributes = reader.readFirstRootDirectoryRecord();
            addChildrenRecords(root, attributes, reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mainDicomDirectory;
    }

    private void addChildrenRecords(DicomDirectoryRecord parent, Attributes recordAttributes, DicomDirReader reader) throws IOException {
        // Will become false when all record in the directory are found
        while (recordAttributes != null) {
            // Creates a new record and adds it to the directory object
            DicomDirectoryRecord newRecord = addNewRecord(parent, recordAttributes);
            // Checks for children and calls the same method
            Attributes lowerRecordAttributes = reader.readLowerDirectoryRecord(recordAttributes);
            addChildrenRecords(newRecord, lowerRecordAttributes, reader);
            // Looks for the next record - if it can't find anymore it sets recordAttributes to null
            recordAttributes = reader.readNextDirectoryRecord(recordAttributes);
        }
    }

    private DicomDirectoryRecord addNewRecord(DicomDirectoryRecord parent, Attributes recordAttributes) {
        DicomDirectoryRecord newRecord = createRecord(recordAttributes);
        parent.addChild(newRecord);
        return newRecord;
    }

    private DicomDirectoryRecord createRecord(Attributes recordAttributes) {
        FileNodeType type = getFileNodeType(recordAttributes);
        String text = getRecordText(recordAttributes);
        return new DicomDirectoryRecord(type, text, mainDicomDirectory);
    }

    private String getRecordType(Attributes recordAttributes) {
        return recordAttributes.getString(Tag.DirectoryRecordType, "");
    }

    private String getRecordText(Attributes recordAttributes) {
        FileNodeType recordType = getFileNodeType(recordAttributes);//recordAttributes.getString(Tag.DirectoryRecordType, "");
        String text = "";

        switch (recordType) {
            case PATIENT:
                text = recordAttributes.getString(Tag.PatientName, "");
                break;
            case STUDY:
                text = recordAttributes.getString(Tag.StudyID, "");
                break;
            case SERIES:
                text = recordAttributes.getString(Tag.Modality, "");
                break;
            case IMAGE:
                text = recordAttributes.getString(Tag.ReferencedFileID, "");
                break;
            default:
                break;
        }

        return text;
    }

    private FileNodeType getFileNodeType(Attributes recordAttributes) {
        String recordType = recordAttributes.getString(Tag.DirectoryRecordType, "");

        switch (recordType) {
            case "PATIENT":
                return FileNodeType.PATIENT;
            case "STUDY":
                return FileNodeType.STUDY;
            case "SERIES":
                return FileNodeType.SERIES;
            case "IMAGE":
                return FileNodeType.IMAGE;
            default:
                return FileNodeType.PRIVATE;
        }
    }
}
