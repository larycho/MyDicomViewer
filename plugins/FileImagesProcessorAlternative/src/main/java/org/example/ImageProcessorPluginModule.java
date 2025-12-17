package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.OptionalBinder;
import org.example.mydicomviewer.processing.file.FileImagesProcessor;

public class ImageProcessorPluginModule extends AbstractModule {

    @Override
    protected void configure() {
        OptionalBinder.newOptionalBinder(binder(), FileImagesProcessor.class).setBinding().to(FileImagesProcessorAlternative.class);
    }

}
