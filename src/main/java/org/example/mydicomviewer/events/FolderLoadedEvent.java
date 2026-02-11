package org.example.mydicomviewer.events;

import org.example.mydicomviewer.views.filelist.FileNodeData;

import java.util.EventObject;
import java.util.List;

public class FolderLoadedEvent extends EventObject {

    List<FileNodeData> data;

    public FolderLoadedEvent(List<FileNodeData> data, Object source) {
        super(source);
        this.data = data;
    }

    public List<FileNodeData> getData() {
        return data;
    }

}
