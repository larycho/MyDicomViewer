package org.example.mydicomviewer.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.example.mydicomviewer.services.ScreenModeProvider;
import org.example.mydicomviewer.services.ScreenModeProviderImpl;
import org.example.mydicomviewer.views.*;

public class MainAppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MainWindow.class).in(Singleton.class);

        bind(ScreenModeProvider.class).to(ScreenModeProviderImpl.class).in(Singleton.class);
    }
}
