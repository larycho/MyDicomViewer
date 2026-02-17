module org.mydicomviewer {
    requires dcm4che.core;
    requires pixelmed;
    requires com.formdev.flatlaf;
    requires ij;
    requires sc.fiji.ij3d;
    requires org.scijava.parsington;
    requires com.google.guice;
    requires net.imagej;
    requires java.prefs;
    requires java.desktop;
    requires org.kordamp.ikonli.swing;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.slf4j;
    requires io.scif;
    requires com.github.benmanes.caffeine;

    uses org.mydicomviewer.plugin.Plugin;

    exports org.mydicomviewer;
    exports org.mydicomviewer.ui;
    exports org.mydicomviewer.ui.filelist;
    exports org.mydicomviewer.services;
    exports org.mydicomviewer.processing.io.file;
    exports org.mydicomviewer.commands;
    exports org.mydicomviewer.events.listeners;
    exports org.mydicomviewer.processing.io.dicomdir;
    exports org.mydicomviewer.plugin;
    exports org.mydicomviewer.tools.shapes;
    exports org.mydicomviewer.ui.image;
    exports org.mydicomviewer.models;
    exports org.mydicomviewer.events;
    exports org.mydicomviewer.processing.windowing;
    exports org.mydicomviewer.ui.display;
    exports org.mydicomviewer.processing.overlay;
    exports org.mydicomviewer.tools;
    exports org.mydicomviewer.tools.factories;
    exports org.mydicomviewer.events.services;
    exports org.mydicomviewer.events.services.implementations;
    exports org.mydicomviewer.services.implementations;
    exports org.mydicomviewer.processing.tags;
    exports org.mydicomviewer.processing.spatial;
    exports org.mydicomviewer.ui.updaters;
    exports org.mydicomviewer.processing.io.export;
    exports org.mydicomviewer.ui.notifications;
    exports org.mydicomviewer.ui.image.shapes;
    exports org.mydicomviewer.workers;
}