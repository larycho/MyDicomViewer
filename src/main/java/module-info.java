module org.example.mydicomviewer {
    uses org.example.mydicomviewer.plugin.Plugin;
    requires dcm4che.core;
    requires pixelmed;
    requires com.formdev.flatlaf;
    requires ij;
    requires java.desktop;
    requires sc.fiji.ij3d;
    requires org.scijava.parsington;
    requires com.google.guice;


    exports org.example.mydicomviewer;
    exports org.example.mydicomviewer.views;
    exports org.example.mydicomviewer.views.filelist;
    exports org.example.mydicomviewer.views.reslice;
    exports org.example.mydicomviewer.services;
    exports org.example.mydicomviewer.processing.file;
    exports org.example.mydicomviewer.commands;
    exports org.example.mydicomviewer.listeners;
    exports org.example.mydicomviewer.processing.dicomdir;
}