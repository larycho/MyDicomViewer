package org.example.mydicomviewer.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import org.example.mydicomviewer.listeners.FileListUpdater;
import org.example.mydicomviewer.listeners.FooterUpdater;
import org.example.mydicomviewer.listeners.TagDisplayer;
import org.example.mydicomviewer.plugin.StartupAction;
import org.example.mydicomviewer.processing.dicomdir.DicomDirProcessor;
import org.example.mydicomviewer.processing.dicomdir.DicomDirProcessorImpl;
import org.example.mydicomviewer.processing.file.FileImagesProcessor;
import org.example.mydicomviewer.processing.file.FileImagesProcessorImpl;
import org.example.mydicomviewer.processing.file.FileProcessor;
import org.example.mydicomviewer.processing.file.FileProcessorImpl;
import org.example.mydicomviewer.services.*;
import org.example.mydicomviewer.views.*;

public class MainAppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MainWindow.class).in(Singleton.class);

        bind(ScreenModeProvider.class).to(ScreenModeProviderImpl.class).in(Singleton.class);
        bind(OpenFileManager.class).to(OpenFileManagerImpl.class);
        bind(FileLoadEventService.class).to(FileLoadEventServiceImpl.class);
        bind(FileLoadStartedEventService.class).to(FileLoadStartedEventServiceImpl.class);
        bind(FileImagesProcessor.class).to(FileImagesProcessorImpl.class);
        bind(FileProcessor.class).to(FileProcessorImpl.class);
        bind(DicomDirProcessor.class).to(DicomDirProcessorImpl.class);
        bind(DicomDirLoadManager.class).to(DicomDirLoadManagerImpl.class);
        bind(FileListUpdater.class).asEagerSingleton();
        bind(TagDisplayer.class).asEagerSingleton();
        bind(FooterUpdater.class).asEagerSingleton();

        Multibinder.newSetBinder(binder(), StartupAction.class);
    }
}
