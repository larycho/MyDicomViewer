package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.example.mydicomviewer.views.image.DrawingTool;

public class RectanglePluginModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), DrawingTool.class).addBinding().to(RectangleTool.class);
    }
}
