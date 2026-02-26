package org.mydicomviewer.plugin;

import com.google.inject.Module;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginManager {

    public List<Module> getPluginModules(File directory) {
        try {
            ClassLoader classLoader = searchForPlugins(directory);
            return collectPluginModules(classLoader);

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ClassLoader searchForPlugins(File directory) throws Exception {

        File[] jars = directory.listFiles((dir, name) -> name.endsWith(".jar"));

        if (jars == null) {
            return null;
        }
        if (jars.length == 0) {
            return null;
        }

        URL[] urls = convertFilesToURLs(jars);

        return new URLClassLoader(urls, PluginManager.class.getClassLoader());
    }

    private URL[] convertFilesToURLs(File[] jars) throws MalformedURLException {
        URL[] urls = new URL[jars.length];

        for (int i = 0; i < jars.length; i++) {
            urls[i] = jars[i].toURI().toURL();
        }

        return urls;
    }

    private List<Module> collectPluginModules(ClassLoader pluginClassLoader) {
        List<Module> plugins = new ArrayList<>();

        ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class, pluginClassLoader);

        for (Plugin plugin : serviceLoader) {
            Module module = plugin.getModule();
            plugins.add(module);
        }

        return plugins;
    }

}
