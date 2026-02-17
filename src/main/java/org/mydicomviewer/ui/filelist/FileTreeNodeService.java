package org.mydicomviewer.ui.filelist;

import org.mydicomviewer.models.DicomFile;

import java.io.File;
import java.util.List;

public interface FileTreeNodeService {

    FileNodeData getFileNodeData(DicomFile file);

    FileNodeData getFileNodeData(File file);

    List<FileNodeData> getFileNodeDataList(List<File> files);

    List<FileNodeData> getFileNodesWithSeriesUid(FileNodeData mainFile, List<FileNodeData> data);

    List<File> convertFileNodesToList(List<FileNodeData> data);
}
