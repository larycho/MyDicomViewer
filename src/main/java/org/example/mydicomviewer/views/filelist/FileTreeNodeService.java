package org.example.mydicomviewer.views.filelist;

import org.example.mydicomviewer.models.DicomFile;

import java.io.File;
import java.util.List;

public interface FileTreeNodeService {

    FileNodeData getFileNodeData(DicomFile file);

    FileNodeData getFileNodeData(File file);

    List<FileNodeData> getFileNodeDataList(List<File> files);

}
