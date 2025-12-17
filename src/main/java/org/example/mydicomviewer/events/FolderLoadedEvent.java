package org.example.mydicomviewer.events;

import org.example.mydicomviewer.processing.dicomdir.DicomDirPath;
import org.example.mydicomviewer.views.filelist.MyTreeNode;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Map;

public class FolderLoadedEvent extends EventObject {

    MyTreeNode tree;

    public FolderLoadedEvent(MyTreeNode tree, Object source) {
        super(source);
        this.tree = tree;
    }

    public MyTreeNode getTree() { return tree; }
}
