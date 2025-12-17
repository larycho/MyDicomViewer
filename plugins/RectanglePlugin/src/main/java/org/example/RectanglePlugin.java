package org.example;

import org.example.mydicomviewer.plugin.Plugin;

public class RectanglePlugin implements Plugin {
    @Override
    public com.google.inject.Module getModule() {
        return new RectanglePluginModule();
    }
}
