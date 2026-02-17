package org.mydicomviewer.processing.windowing;

public class WindowingParameters {

    private int windowWidth;
    private int windowLevel;
    // Identity transform by default
    private double rescaleSlope = 1.0;
    private double rescaleIntercept = 0.0;

    private boolean isSigned;
    private PhotometricInterpretation photometricInterpretation;

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        if (windowWidth == 0) {
            this.windowWidth = 1;
        }
        else {
            this.windowWidth = windowWidth;
        }
    }

    public int getWindowLevel() {
        return windowLevel;
    }

    public void setWindowLevel(int windowLevel) {
        this.windowLevel = windowLevel;
    }

    public double getRescaleSlope() {
        return rescaleSlope;
    }

    public void setRescaleSlope(double rescaleSlope) {
        this.rescaleSlope = rescaleSlope;
    }

    public double getRescaleIntercept() {
        return rescaleIntercept;
    }

    public void setRescaleIntercept(double rescaleIntercept) {
        this.rescaleIntercept = rescaleIntercept;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public PhotometricInterpretation getPhotometricInterpretation() {
        return photometricInterpretation;
    }

    public void setPhotometricInterpretation(PhotometricInterpretation photometricInterpretation) {
        this.photometricInterpretation = photometricInterpretation;
    }

}
