package org.example.mydicomviewer.views.image.panel;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.views.image.panel.regular.ImageManagerImpl;
import org.example.mydicomviewer.views.image.panel.regular.ImagePanelImpl;
import org.example.mydicomviewer.views.image.panel.regular.ImagePanelToolbarImpl;
import org.example.mydicomviewer.views.image.panel.reslice.ImageManagerResliceImpl;
import org.example.mydicomviewer.views.image.panel.reslice.ImagePanelResliceToolbarImpl;

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
