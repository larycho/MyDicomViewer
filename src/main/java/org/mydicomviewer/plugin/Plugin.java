package org.mydicomviewer.plugin;

import com.google.inject.Module;

public interface Plugin {
    Module getModule();
}
