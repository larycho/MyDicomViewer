package org.mydicomviewer.ui.filelist;

public class FileNode {

    private final FileNodeType type;
    private final FileNodeData data;

    public FileNode(FileNodeType type, FileNodeData data) {
        this.type = type;
        this.data = data;
    }

    public FileNodeType getType() {
        return type;
    }

    public FileNodeData getData() {
        return data;
    }

}
