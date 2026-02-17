package org.mydicomviewer.processing.io.dicomdir;

import com.google.inject.Inject;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.media.DicomDirReader;
import org.mydicomviewer.ui.filelist.FileNodeData;
import org.mydicomviewer.ui.filelist.FileNodeType;
import org.mydicomviewer.ui.filelist.FileTreeNodeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DicomDirProcessorImpl implements DicomDirProcessor {

    private File sourceFile;
    private final FileTreeNodeService fileTreeNodeService;

    @Inject
    public DicomDirProcessorImpl(FileTreeNodeService fileTreeNodeService) {
        this.fileTreeNodeService = fileTreeNodeService;
    }

    @Override
    public List<FileNodeData> openDicomDirectory(File file) {

        sourceFile = file;
        List<FileNodeData> foundFiles = new ArrayList<>();

        try (DicomDirReader reader = new DicomDirReader(file)) {
            Attributes attributes = reader.readFirstRootDirectoryRecord();
            foundFiles = findAllFilesInDirectory(attributes, reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundFiles;
    }

    private List<FileNodeData> findAllFilesInDirectory(Attributes parentAttributes, DicomDirReader reader) throws IOException {

        List<FileNodeData> totalFiles = new ArrayList<>();

        // Will become false when all record in the directory are found
        while (parentAttributes != null) {
            // Attempts to find an image file corresponding to these attributes
            Optional<FileNodeData> newFile = getFileNodeData(parentAttributes);
            newFile.ifPresent(totalFiles::add);
            // Checks for children and calls the same method
            Attributes childAttributes = reader.readLowerDirectoryRecord(parentAttributes);
            List<FileNodeData> childFiles = findAllFilesInDirectory(childAttributes, reader);

            totalFiles.addAll(childFiles);
            // Looks for the next record - if it can't find anymore it sets parentAttributes to null
            parentAttributes = reader.readNextDirectoryRecord(parentAttributes);
        }

        return totalFiles;
    }

    private Optional<FileNodeData> getFileNodeData(Attributes attributes) {
        Optional<File> file = findImageFile(attributes);
        return file.map(fileTreeNodeService::getFileNodeData);
    }

    private Optional<File> findImageFile(Attributes parentAttributes) {
        FileNodeType type = getFileNodeType(parentAttributes);

        if (type == FileNodeType.IMAGE) {
            String sopUID = parentAttributes.getString(Tag.ReferencedSOPInstanceUIDInFile);
            String fileName = parentAttributes.getString(Tag.ReferencedFileID);

            Optional<Path> filePath = findFile(fileName, sopUID);
            if (filePath.isPresent()) {

                return Optional.of(filePath.get().toFile());
            }
        }
        return Optional.empty();
    }

    private Optional<Path> findFile(String fileName, String sopUID) {

        File directory = sourceFile.getParentFile();

        try (Stream<Path> stream = Files.walk(directory.toPath())) {
            return stream.filter(path -> path.getFileName().toString().equals(fileName))
                    .filter(path -> {
                        File file = path.toFile();
                        return hasMatchingUid(file, sopUID);
                    }).findFirst();
        }
        catch (IOException e) {
            return Optional.empty();
        }
    }

    private boolean hasMatchingUid(File file, String uid) {

        try (DicomInputStream dis = new DicomInputStream(file)) {

            Optional<String> result = getUid(dis);
            return result.map(s -> s.equals(uid)).orElse(false);

        }
        catch (IOException e) {
            return false;
        }
    }

    private Optional<String> getUid(DicomInputStream dis) throws IOException {

        Attributes attributes = dis.readDatasetUntilPixelData();
        String uid = attributes.getString(Tag.SOPInstanceUID, "");

        if (uid.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(uid);
    }

    private FileNodeType getFileNodeType(Attributes recordAttributes) {
        String recordType = recordAttributes.getString(Tag.DirectoryRecordType, "");

        return switch (recordType) {
            case "PATIENT" -> FileNodeType.PATIENT;
            case "STUDY" -> FileNodeType.STUDY;
            case "SERIES" -> FileNodeType.SERIES;
            case "IMAGE" -> FileNodeType.IMAGE;
            default -> FileNodeType.PRIVATE;
        };
    }
}
