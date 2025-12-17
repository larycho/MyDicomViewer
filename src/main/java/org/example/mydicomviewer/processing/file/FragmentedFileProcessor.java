package org.example.mydicomviewer.processing.file;

import org.example.mydicomviewer.models.DicomFile;

import java.io.File;
import java.util.List;

public interface FragmentedFileProcessor {
    DicomFile readFragmentedFile(List<File> files);
}
