package org.mydicomviewer.services;

import java.io.File;
import java.util.List;

public interface FrameSkipManager {
    void manageFileSkip(List<File> fileList, int frame);
}
