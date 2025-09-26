package org.example.mydicomviewer.processing.file;

import org.example.mydicomviewer.models.TagGroup;

import java.io.File;

public interface FileTagsProcessor {
    TagGroup getTagsFromFile(File file);
}
