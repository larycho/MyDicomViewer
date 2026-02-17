package org.mydicomviewer.ui.filelist;

import com.google.inject.Inject;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.models.DicomSeries;
import org.mydicomviewer.models.TagGroup;
import org.mydicomviewer.processing.io.file.FileTagsProcessor;
import org.mydicomviewer.processing.tags.TagProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTreeNodeServiceImpl implements FileTreeNodeService {

    private final FileTagsProcessor fileTagsProcessor;

    @Inject
    public FileTreeNodeServiceImpl(FileTagsProcessor fileTagsProcessor) {
        this.fileTagsProcessor = fileTagsProcessor;
    }

    @Override
    public FileNodeData getFileNodeData(DicomFile file) {

        FileNodeData fileNodeData = getTagData(file);
        fileNodeData.setFile(file.getFile());

        return fileNodeData;
    }

    @Override
    public FileNodeData getFileNodeData(File file) {

        TagGroup tags = fileTagsProcessor.getTagsFromFile(file);
        // temporary due to TagProcessor needing some improvements
        DicomFile tempFile = new DicomFile(file, tags, new DicomSeries());

        return getFileNodeData(tempFile);
    }

    @Override
    public List<FileNodeData> getFileNodeDataList(List<File> files) {

        ArrayList<FileNodeData> fileNodeDataList = new ArrayList<>();

        for (File file : files) {

            FileNodeData fileNodeData = getFileNodeData(file);
            fileNodeDataList.add(fileNodeData);
        }

        return fileNodeDataList;
    }

    private FileNodeData getTagData(DicomFile file) {

        TagProcessor tagProcessor = new TagProcessor(file);
        FileNodeData fileNodeData = new FileNodeData();

        fileNodeData.setFile(file.getFile());
        fileNodeData.setPatientId(tagProcessor.getPatientId().orElse(null));
        fileNodeData.setStudyId(tagProcessor.getStudyId().orElse(null));
        fileNodeData.setSeriesNumber(tagProcessor.getSeriesNumber().orElse(null));

        fileNodeData.setSeriesInstanceUid(tagProcessor.getSeriesInstanceUid().orElse(null));
        fileNodeData.setStudyInstanceUid(tagProcessor.getStudyInstanceUid().orElse(null));
        fileNodeData.setSopInstanceUid(tagProcessor.getSopInstanceUid().orElse(null));
        fileNodeData.setInstanceNumber(tagProcessor.getInstanceNumber().orElse(null));

        return fileNodeData;
    }

    @Override
    public List<FileNodeData> getFileNodesWithSeriesUid(FileNodeData mainFile, List<FileNodeData> data) {

        List<FileNodeData> result = new ArrayList<>();

        if (mainFile.getSeriesInstanceUid().isEmpty()) {
            return result;
        }

        for (FileNodeData file : data) {

            if (file.getSeriesInstanceUid().isPresent()) {
                if (file.getSeriesInstanceUid().get().equals(mainFile.getSeriesInstanceUid().get())) {
                    result.add(file);
                }
            }
        }
        return result;
    }

    @Override
    public List<File> convertFileNodesToList(List<FileNodeData> data) {
        List<File> result = new ArrayList<>();
        for (FileNodeData file : data) {
            if (file.getFile().isPresent()) {
                result.add(file.getFile().get());
            }
        }
        return result;
    }
}
