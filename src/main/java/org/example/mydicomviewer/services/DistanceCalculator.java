package org.example.mydicomviewer.services;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.shapes.Point3D;
import org.example.mydicomviewer.views.image.panel.Axis;

public interface DistanceCalculator {

    void setFile(DicomFile file);

    double calculateDistance(Point3D p1, Point3D p2);

    double calculateAspectRatio(Axis axis);
}
