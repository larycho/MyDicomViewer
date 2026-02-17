package org.mydicomviewer.processing.io.dicomdir;

import org.mydicomviewer.ui.filelist.FileNodeData;

import java.io.File;
import java.util.List;

public interface DicomDirProcessor {
    List<FileNodeData> openDicomDirectory(File file);
}
