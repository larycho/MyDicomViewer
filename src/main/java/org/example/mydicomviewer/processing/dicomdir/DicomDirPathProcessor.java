
package org.example.mydicomviewer.processing.dicomdir;

import java.io.File;

public interface DicomDirPathProcessor {
    DicomDirPath getDicomDirPath(File file);
}
