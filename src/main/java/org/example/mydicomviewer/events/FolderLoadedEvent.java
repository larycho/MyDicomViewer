package org.example.mydicomviewer.events;

import org.example.mydicomviewer.views.filelist.FileTreeNode;

import java.util.EventObject;

public class FolderLoadedEvent extends EventObject {

    FileTreeNode tree;

    public FolderLoadedEvent(FileTreeNode tree, Object source) {
        super(source);
        this.tree = tree;
    }

    public FileTreeNode getTree() { return tree; }
}
