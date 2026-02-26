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
        //long startTime = System.nanoTime();
        fileLoadStartedEventService.notifyStarted(file);
        OpenFileWorker worker = new OpenFileWorker(fileLoadEventService, fileProcessor);
        worker.setFile(file);
        worker.execute();
        //long endTime = System.nanoTime();
        //long duration = (endTime - startTime) / 1_000_000;
        //System.out.println((endTime - startTime) / 10000);
        //System.out.println(duration + " ms");
    }

    @Override
    public void openFragmentedFileUsingWorker(List<File> files) {
        //long startTime = System.nanoTime();
        if (!files.isEmpty()) {
            fileLoadStartedEventService.notifyStarted(files.get(0));
            OpenFragmentedFileWorker worker = new OpenFragmentedFileWorker(fileLoadEventService, fragmentedFileProcessor);
            worker.setFiles(files);
            worker.execute();
        }
        //long endTime = System.nanoTime();
        //long duration = (endTime - startTime) / 1_000_000;
        //System.out.println(duration + " ms");
    }

}
