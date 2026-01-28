package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.FragmentedFileSelectedEvent;

import java.util.EventListener;

public interface FragmentedFileSelectedListener extends EventListener {
    void fragmentedFileSelected(FragmentedFileSelectedEvent event);
}
