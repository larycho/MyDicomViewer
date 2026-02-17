package org.mydicomviewer.ui.image;

import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.ui.image.regular.ImageManagerImpl;
import org.mydicomviewer.ui.image.regular.ImagePanelImpl;
import org.mydicomviewer.ui.image.regular.ImagePanelToolbarImpl;
import org.mydicomviewer.ui.image.reslice.ImageManagerResliceImpl;
import org.mydicomviewer.ui.image.reslice.ImagePanelResliceToolbarImpl;

public class ImagePanelFactory {

    public static ImagePanelWrapper createRegularImagePanel(DicomFile file) {

        ImageManager manager = new ImageManagerImpl(file);
        ImagePanelWrapper wrapper = new ImagePanelWrapperImpl(manager);
        ImagePanelToolbar toolbar = new ImagePanelToolbarImpl(wrapper);
        ImagePanelImpl imagePanel = new ImagePanelImpl(manager, wrapper, toolbar);
        wrapper.setImagePanel(imagePanel);
        wrapper.displayDefaultImage();

        return wrapper;
    }

    public static ImagePanelWrapper createResliceImagePanel(DicomFile file) {

        ImageManager manager = new ImageManagerResliceImpl(file);
        ImagePanelWrapper wrapper = new ImagePanelWrapperImpl(manager);
        ImagePanelToolbar toolbar = new ImagePanelResliceToolbarImpl(wrapper);
        ImagePanelImpl imagePanel = new ImagePanelImpl(manager, wrapper, toolbar);
        wrapper.setImagePanel(imagePanel);
        wrapper.displayDefaultImage();

        return wrapper;
    }
}
