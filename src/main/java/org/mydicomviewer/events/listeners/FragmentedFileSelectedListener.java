package org.mydicomviewer.events.listeners;

import org.mydicomviewer.events.FragmentedFileSelectedEvent;

import java.util.EventListener;

public interface FragmentedFileSelectedListener extends EventListener {
    void fragmentedFileSelected(FragmentedFileSelectedEvent event);
}
