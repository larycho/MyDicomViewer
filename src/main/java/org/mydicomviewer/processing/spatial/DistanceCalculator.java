package org.mydicomviewer.processing.spatial;

import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.tools.shapes.Point3D;

public interface DistanceCalculator {

    void setFile(DicomFile file);

    double calculateDistance(Point3D p1, Point3D p2);

    double calculateAspectRatio(Axis axis);
}
