package org.example.mydicomviewer.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import org.example.mydicomviewer.listeners.*;
import org.example.mydicomviewer.plugin.StartupAction;
import org.example.mydicomviewer.processing.dicomdir.DicomDirProcessor;
import org.example.mydicomviewer.processing.dicomdir.DicomDirProcessorImpl;
import org.example.mydicomviewer.processing.file.*;
import org.example.mydicomviewer.services.*;
import org.example.mydicomviewer.views.*;
import org.example.mydicomviewer.views.filelist.FileTreeNodeService;
import org.example.mydicomviewer.views.filelist.FileTreeNodeServiceImpl;
import org.example.mydicomviewer.views.image.DrawingToolFactory;

public class MainAppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MainWindow.class).in(Singleton.class);

        OptionalBinder<FileImagesProcessor> imageProcessorBinder = OptionalBinder.newOptionalBinder(binder(), FileImagesProcessor.class);
        imageProcessorBinder.setDefault().to(FileImagesProcessorImpl.class);

        bind(ScreenModeProvider.class).to(ScreenModeProviderImpl.class).in(Singleton.class);
        bind(OpenFileManager.class).to(OpenFileManagerImpl.class);
        bind(FileLoadEventService.class).to(FileLoadEventServiceImpl.class);
        bind(FileLoadStartedEventService.class).to(FileLoadStartedEventServiceImpl.class);
        bind(FileTagsProcessor.class).to(FileTagsProcessorImpl.class);
        bind(FileProcessor.class).to(FileProcessorImpl.class);
        bind(DicomDirProcessor.class).to(DicomDirProcessorImpl.class);
        bind(DicomDirLoadManager.class).to(DicomDirLoadManagerImpl.class);
        bind(FragmentedFileProcessor.class).to(FragmentedFileProcessorImpl.class);
        bind(FolderLoadedEventService.class).to(FolderLoadedEventServiceImpl.class);
        bind(FolderLoadManager.class).to(FolderLoadManagerImpl.class);
        bind(ImagePanelSelectedEventService.class).to(ImagePanelSelectedEventServiceImpl.class);
        bind(SelectedImageManager.class).to(SelectedImageManagerImpl.class);
        bind(FragmentedFileEventService.class).to(FragmentedFileEventServiceImpl.class);
        bind(FileTreeNodeService.class).to(FileTreeNodeServiceImpl.class);
        bind(ToolBarEventService.class).to(ToolBarEventServiceImpl.class);
        bind(ResliceEventService.class).to(ResliceEventServiceImpl.class);

        bind(FileListUpdater.class).asEagerSingleton();
        bind(TagDisplayer.class).asEagerSingleton();
        bind(FooterUpdater.class).asEagerSingleton();
        bind(ToolBarUpdater.class).asEagerSingleton();
        bind(ImageDisplayer.class).asEagerSingleton();
        bind(ResliceDisplayer.class).asEagerSingleton();

        Multibinder.newSetBinder(binder(), StartupAction.class);
        Multibinder.newSetBinder(binder(), DrawingToolFactory.class);
    }
}
