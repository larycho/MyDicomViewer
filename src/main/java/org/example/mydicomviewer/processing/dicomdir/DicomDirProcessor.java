package org.example.mydicomviewer.processing.dicomdir;

import org.example.mydicomviewer.views.filelist.FileNodeData;

import java.io.File;
import java.util.List;

public interface DicomDirProcessor {
    List<FileNodeData> openDicomDirectory(File file);
}
