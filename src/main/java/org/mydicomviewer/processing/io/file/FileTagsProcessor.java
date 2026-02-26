package org.mydicomviewer.processing.io.file;

import org.mydicomviewer.models.TagGroup;
import org.mydicomviewer.models.TagSeries;

import java.io.File;

public interface FileTagsProcessor {
    TagGroup getTagsFromFile(File file);
}
