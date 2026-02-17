package org.example;

import org.mydicomviewer.plugin.Plugin;

public class ImageProcessorPlugin implements Plugin {

    @Override
    public com.google.inject.Module getModule() {
        return new ImageProcessorPluginModule();
    }
}
