package org.mydicomviewer.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import org.mydicomviewer.events.services.*;
import org.mydicomviewer.events.services.implementations.*;
import org.mydicomviewer.processing.io.export.SaveManager;
import org.mydicomviewer.processing.io.export.SaveManagerImpl;
import org.mydicomviewer.plugin.StartupAction;
import org.mydicomviewer.processing.io.dicomdir.DicomDirProcessor;
import org.mydicomviewer.processing.io.dicomdir.DicomDirProcessorImpl;
import org.mydicomviewer.processing.io.file.*;
import org.mydicomviewer.processing.windowing.WindowingProcessor;
import org.mydicomviewer.processing.windowing.WindowingProcessorImpl;
import org.mydicomviewer.services.*;
import org.mydicomviewer.services.implementations.DicomDirLoadManagerImpl;
import org.mydicomviewer.services.implementations.FolderLoadManagerImpl;
import org.mydicomviewer.services.implementations.OpenFileManagerImpl;
import org.mydicomviewer.services.implementations.ScreenModeProviderImpl;
import org.mydicomviewer.tools.DrawingTool;
import org.mydicomviewer.tools.LineTool;
import org.mydicomviewer.tools.OvalTool;
import org.mydicomviewer.tools.PencilTool;
import org.mydicomviewer.tools.factories.LineToolFactory;
import org.mydicomviewer.tools.factories.OvalToolFactory;
import org.mydicomviewer.tools.factories.PencilToolFactory;
import org.mydicomviewer.ui.image.SelectedImageManager;
import org.mydicomviewer.ui.image.SelectedImageManagerImpl;
import org.mydicomviewer.ui.filelist.FileTreeNodeService;
import org.mydicomviewer.ui.filelist.FileTreeNodeServiceImpl;
import org.mydicomviewer.tools.factories.DrawingToolFactory;
import org.mydicomviewer.ui.notifications.NotificationService;
import org.mydicomviewer.ui.notifications.NotificationServiceImpl;
import org.mydicomviewer.ui.updaters.*;
import org.mydicomviewer.ui.MainWindow;

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
        bind(NotificationService.class).to(NotificationServiceImpl.class);
        bind(SaveManager.class).to(SaveManagerImpl.class);
        bind(WindowingProcessor.class).to(WindowingProcessorImpl.class);

        bind(FileListUpdater.class).asEagerSingleton();
        bind(TagDisplayer.class).asEagerSingleton();
        bind(FooterUpdater.class).asEagerSingleton();
        bind(ToolBarUpdater.class).asEagerSingleton();
        bind(ImageDisplayer.class).asEagerSingleton();
        bind(ResliceDisplayer.class).asEagerSingleton();

        Multibinder.newSetBinder(binder(), StartupAction.class);
        Multibinder<DrawingToolFactory> toolBinder = Multibinder.newSetBinder(binder(), DrawingToolFactory.class);
        toolBinder.addBinding().to(PencilToolFactory.class);
        toolBinder.addBinding().to(OvalToolFactory.class);
        toolBinder.addBinding().to(LineToolFactory.class);
    }
}
