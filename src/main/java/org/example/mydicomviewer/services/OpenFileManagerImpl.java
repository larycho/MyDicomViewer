package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.processing.file.FileProcessor;
import org.example.mydicomviewer.processing.file.FragmentedFileProcessor;
import org.example.mydicomviewer.workers.OpenFileWorker;
import org.example.mydicomviewer.workers.OpenFragmentedFileWorker;

import java.io.File;
import java.util.List;

@Singleton
public class OpenFileManagerImpl implements OpenFileManager {

    private final FileProcessor fileProcessor;
    private final FragmentedFileProcessor fragmentedFileProcessor;
    private final FileLoadEventService fileLoadEventService;
    private final FileReloadEventService fileReloadEventService;
    private final FileLoadStartedEventService fileLoadStartedEventService;

    @Inject
    public OpenFileManagerImpl (
            FileLoadEventService fileLoadEventService,
            FileReloadEventService fileReloadEventService,
            FileProcessor fileProcessor,
            FragmentedFileProcessor fragmentedFileProcessor,
            FileLoadStartedEventService fileLoadStartedEventService
    ) {
        this.fileProcessor = fileProcessor;
        this.fileLoadEventService = fileLoadEventService;
        this.fileReloadEventService = fileReloadEventService;
        this.fragmentedFileProcessor = fragmentedFileProcessor;
        this.fileLoadStartedEventService = fileLoadStartedEventService;
    }

    @Override
    public void openFileUsingWorker(File file) {
        fileLoadStartedEventService.notifyStarted(file);
        OpenFileWorker worker = new OpenFileWorker(fileLoadEventService, fileProcessor);
        worker.setFile(file);
        worker.execute();
    }

    @Override
    public void openFragmentedFileUsingWorker(List<File> files) {
        if (!files.isEmpty()) {
            fileLoadStartedEventService.notifyStarted(files.get(0));
            OpenFragmentedFileWorker worker = new OpenFragmentedFileWorker(fileLoadEventService, fragmentedFileProcessor);
            worker.setFiles(files);
            worker.execute();
        }
    }

    @Override
    public void reopenFile(File file) {
        if (file != null) {
            fileReloadEventService.notifyReloaded(file);
        }
    }
}
