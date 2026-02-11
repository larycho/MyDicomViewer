package org.example.mydicomviewer.views.filelist;

import com.google.inject.Inject;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomSeries;
import org.example.mydicomviewer.models.TagGroup;
import org.example.mydicomviewer.processing.file.FileTagsProcessor;
import org.example.mydicomviewer.processing.file.TagProcessor;

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
}
