package org.mydicomviewer.processing.io.file;

import org.mydicomviewer.models.DicomFile;

import java.io.File;
import java.util.List;

public interface FragmentedFileProcessor {
    DicomFile readFragmentedFile(List<File> files);
}
