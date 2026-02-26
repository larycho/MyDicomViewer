package org.mydicomviewer;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.mydicomviewer.modules.MainAppModule;
import org.mydicomviewer.plugin.PluginManager;
import com.google.inject.Module;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyDicomViewer {

    public static void main(String[] args) {
        setupLookAndFeel();

        List<Module> modules = getModules();

        Injector injector = Guice.createInjector(modules);

        MainApp app = injector.getInstance(MainApp.class);
        app.start();
    }

    private static List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        modules.add(new MainAppModule());
        modules.addAll(getPluginModules());
        return modules;
    }

    private static List<Module> getPluginModules() {
        File directory = new File("plugins/");
        PluginManager manager = new PluginManager();
        return manager.getPluginModules(directory);
    }

    private static void setupLookAndFeel() {
        FlatIntelliJLaf.setup();
    }

}
