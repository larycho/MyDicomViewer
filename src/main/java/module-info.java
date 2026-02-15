module org.example.mydicomviewer {
    uses org.example.mydicomviewer.plugin.Plugin;
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


    exports org.example.mydicomviewer;
    exports org.example.mydicomviewer.views;
    exports org.example.mydicomviewer.views.filelist;
    exports org.example.mydicomviewer.services;
    exports org.example.mydicomviewer.processing.file;
    exports org.example.mydicomviewer.commands;
    exports org.example.mydicomviewer.listeners;
    exports org.example.mydicomviewer.processing.dicomdir;
    exports org.example.mydicomviewer.plugin;
    exports org.example.mydicomviewer.views.image;
    exports org.example.mydicomviewer.models.shapes;
    exports org.example.mydicomviewer.views.image.panel;
    exports org.example.mydicomviewer.models;
    exports org.example.mydicomviewer.events;
    exports org.example.mydicomviewer.processing.image;
    exports org.example.mydicomviewer.display;
    exports org.example.mydicomviewer.display.overlay;
    exports org.example.mydicomviewer.export;
    exports org.example.mydicomviewer.workers;
}