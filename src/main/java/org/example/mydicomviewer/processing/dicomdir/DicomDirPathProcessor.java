
package org.example.mydicomviewer.processing.dicomdir;

import java.io.File;
import java.util.Optional;

public interface DicomDirPathProcessor {
    Optional<DicomDirPath> getDicomDirPath(File file);
}
