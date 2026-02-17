package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.OptionalBinder;
import org.mydicomviewer.processing.io.file.FileImagesProcessor;
import org.example.FileImagesProcessorAlternative;

public class ImageProcessorPluginModule extends AbstractModule {
    @Override
    public void configure() {
        OptionalBinder.newOptionalBinder(binder(), FileImagesProcessor.class).setBinding().to(FileImagesProcessorAlternative.class);
    }
}
