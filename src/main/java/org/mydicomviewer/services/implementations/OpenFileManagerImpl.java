package org.mydicomviewer.services.implementations;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.events.services.FileLoadEventService;
import org.mydicomviewer.events.services.FileLoadStartedEventService;
import org.mydicomviewer.processing.io.file.FileProcessor;
import org.mydicomviewer.processing.io.file.FragmentedFileProcessor;
import org.mydicomviewer.workers.OpenFileWorker;
import org.mydicomviewer.workers.OpenFragmentedFileWorker;
import org.mydicomviewer.services.OpenFileManager;

import java.io.File;
import java.util.List;

@Singleton
public class OpenFileManagerImpl implements OpenFileManager {

    private final FileProcessor fileProcessor;
    private final FragmentedFileProcessor fragmentedFileProcessor;
    private final FileLoadEventService fileLoadEventService;
    private final FileLoadStartedEventService fileLoadStartedEventService;

    @Inject
    public OpenFileManagerImpl (
            FileLoadEventService fileLoadEventService,
            FileProcessor fileProcessor,
            FragmentedFileProcessor fragmentedFileProcessor,
            FileLoadStartedEventService fileLoadStartedEventService
    ) {
        this.fileProcessor = fileProcessor;
        this.fileLoadEventService = fileLoadEventService;
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

}
