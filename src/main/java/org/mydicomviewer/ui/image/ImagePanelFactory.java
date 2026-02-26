package org.mydicomviewer.ui.image;

import com.google.inject.Inject;
import org.mydicomviewer.events.services.FrameSkipEventService;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.ui.image.regular.ImageManagerImpl;
import org.mydicomviewer.ui.image.regular.ImagePanelImpl;
import org.mydicomviewer.ui.image.regular.ImagePanelToolbarImpl;
import org.mydicomviewer.ui.image.reslice.ImageManagerResliceImpl;
import org.mydicomviewer.ui.image.reslice.ImagePanelResliceToolbarImpl;

public class ImagePanelFactory {

    private final FrameSkipEventService frameSkipEventService;
    private final ImagePanelSelectedEventService imagePanelSelectedEventService;

    @Inject
    public ImagePanelFactory(ImagePanelSelectedEventService imagePanelSelectedEventService,
                             FrameSkipEventService frameSkipEventService) {
        this.imagePanelSelectedEventService = imagePanelSelectedEventService;
        this.frameSkipEventService = frameSkipEventService;
    }

    public ImagePanelWrapper createRegularImagePanel(DicomFile file) {

        ImageManager manager = new ImageManagerImpl(file);
        ImagePanelWrapper wrapper = new ImagePanelWrapperImpl(manager);
        ImagePanelToolbar toolbar = new ImagePanelToolbarImpl(wrapper);
        toolbar.addPanelSelectedService(imagePanelSelectedEventService);
        toolbar.addFrameSkipService(frameSkipEventService);
        ImagePanelImpl imagePanel = new ImagePanelImpl(manager, wrapper, toolbar);
        wrapper.setImagePanel(imagePanel);
        wrapper.displayDefaultImage();

        return wrapper;
    }

    public ImagePanelWrapper createResliceImagePanel(DicomFile file) {

        ImageManager manager = new ImageManagerResliceImpl(file);
        ImagePanelWrapper wrapper = new ImagePanelWrapperImpl(manager);
        ImagePanelToolbar toolbar = new ImagePanelResliceToolbarImpl(wrapper);
        toolbar.addPanelSelectedService(imagePanelSelectedEventService);
        toolbar.addFrameSkipService(frameSkipEventService);
        ImagePanelImpl imagePanel = new ImagePanelImpl(manager, wrapper, toolbar);
        wrapper.setImagePanel(imagePanel);
        wrapper.displayDefaultImage();

        return wrapper;
    }
}
