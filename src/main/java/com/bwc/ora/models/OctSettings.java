/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.models;

import com.bwc.ora.ip.Blur;
import com.bwc.ora.ip.ContrastAdjust;
import com.bwc.ora.ip.FilterOperation;
import com.bwc.ora.ip.NoiseReduction;
import com.bwc.ora.ip.Sharpen;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class OctSettings {

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private boolean applyNoiseReduction = false;
    public static final String PROP_APPLY_NOISE_REDUCTION = "applyNoiseReduction";
    private boolean applyContrastAdjustment = false;
    public static final String PROP_APPLY_CONTRAST_ADJUSTMENT = "applyContrastAdjustment";
    private boolean displayLogOct = true;
    public static final String PROP_DISPLAY_LOG_OCT = "displayLogOct";
    private static final int[] smoothingFactorSliderRange = new int[] { 0, 50 };
    private static final double smoothingFactorSliderMultiplier = 0.1D;
    private double smoothingFactor = 0D;
    public static final String PROP_SMOOTHING_FACTOR = "smoothingFactor";
    private static final int[] sharpenWeightSliderRange = new int[] { 0, 100 };
    private static final float sharpenWeightSliderMultiplier = 0.01F;
    private float sharpenWeight = 0F;
    public static final String PROP_SHARPEN_WEIGHT = "sharpenWeight";
    private static final int[] sharpenKernelRadiusSliderRange = new int[] { 0, 200 };
    private static final double sharpenKernelRadiusSliderMultiplier = 0.1D;
    private double sharpenKernelRadius = 0D;
    public static final String PROP_SHARPEN_KERNEL_RADIUS = "sharpenKernelRadius";
    private double xScale = 0D; //microns per pixel
    public static final String PROP_X_SCALE = "xScale";
    private double yScale = 0D; //microns per pixel
    public static final String PROP_Y_SCALE = "yScale";
    private double zoom = 1D;
    public static final String PROP_ZOOM = "zoom";
    private static final int[] zoomSliderRange = new int[] { 1, 50 };

    public void resetToDefaultSettings() {
        OctSettings ds = new OctSettings();
        setApplyNoiseReduction(ds.applyNoiseReduction);
        setApplyContrastAdjustment(ds.applyContrastAdjustment);
        setDisplayLogOct(ds.displayLogOct);
        setSmoothingFactor(ds.smoothingFactor);
        setSharpenWeight(ds.sharpenWeight);
        setSharpenKernelRadius(ds.sharpenKernelRadius);
        setxScale(ds.xScale);
        setyScale(ds.yScale);
    }

    public void loadSettings(OctSettings newOctSettings) {
        setApplyNoiseReduction(newOctSettings.applyNoiseReduction);
        setApplyContrastAdjustment(newOctSettings.applyContrastAdjustment);
        setDisplayLogOct(newOctSettings.displayLogOct);
        setSmoothingFactor(newOctSettings.smoothingFactor);
        setSharpenWeight(newOctSettings.sharpenWeight);
        setSharpenKernelRadius(newOctSettings.sharpenKernelRadius);
        setxScale(newOctSettings.xScale);
        setyScale(newOctSettings.yScale);
    }

    public static double getSmoothingFactorSliderMultiplier() {
        return smoothingFactorSliderMultiplier;
    }

    public static float getSharpenWeightSliderMultiplier() {
        return sharpenWeightSliderMultiplier;
    }

    public static double getSharpenKernelRadiusSliderMultiplier() {
        return sharpenKernelRadiusSliderMultiplier;
    }

    public static int[] getSmoothingFactorSliderRange() {
        return smoothingFactorSliderRange;
    }

    public static int[] getSharpenWeightSliderRange() {
        return sharpenWeightSliderRange;
    }

    public static int[] getSharpenKernelRadiusSliderRange() {
        return sharpenKernelRadiusSliderRange;
    }

    public ArrayList<FilterOperation> getActiveOperations() {
        ArrayList<FilterOperation> ops = new ArrayList<>();
        if (sharpenWeight > 0F && sharpenKernelRadius > 0D) {
            ops.add(new Sharpen(sharpenKernelRadius, sharpenWeight));
        }
        if (smoothingFactor > 0D) {
            ops.add(new Blur(smoothingFactor));
        }
        if (applyContrastAdjustment) {
            ops.add(new ContrastAdjust());
        }
        if (applyNoiseReduction) {
            ops.add(new NoiseReduction());
        }
        return ops;
    }

    /**
     * Get the value of yScale
     *
     * @return the value of yScale
     */
    public double getyScale() {
        return yScale;
    }

    /**
     * Set the value of yScale
     *
     * @param yScale new value of yScale
     */
    public void setyScale(double yScale) {
        if (yScale == this.yScale) {
            return;
        }
        double oldyScale = this.yScale;
        this.yScale = yScale;
        propertyChangeSupport.firePropertyChange(PROP_Y_SCALE, oldyScale, yScale);
    }

    /**
     * Get the value of xScale
     *
     * @return the value of xScale
     */
    public double getxScale() {
        return xScale;
    }

    /**
     * Set the value of xScale
     *
     * @param xScale new value of xScale
     */
    public void setxScale(double xScale) {
        if (xScale == this.xScale) {
            return;
        }
        double oldxScale = this.xScale;
        this.xScale = xScale;
        propertyChangeSupport.firePropertyChange(PROP_X_SCALE, oldxScale, xScale);
    }

    /**
     * Get the value of sharpenKernelRadius. "Radius (Sigma)" is the standard
     * deviation (blur radius) of the Gaussian blur that is subtracted when
     * sharpening an image.
     *
     * @return the value of sharpenKernelRadius
     */
    public double getSharpenKernelRadius() {
        return sharpenKernelRadius;
    }

    /**
     * Set the value of sharpenKernelRadius
     *
     * @param sharpenKernelRadius new value of sharpenKernelRadius
     */
    public void setSharpenKernelRadius(double sharpenKernelRadius) {
        if (sharpenKernelRadius == this.sharpenKernelRadius) {
            return;
        }
        double oldSharpenKernelRadius = this.sharpenKernelRadius;
        this.sharpenKernelRadius = sharpenKernelRadius;
        propertyChangeSupport.firePropertyChange(PROP_SHARPEN_KERNEL_RADIUS, oldSharpenKernelRadius, sharpenKernelRadius);
    }

    /**
     * Get the value of sharpenWeight. "Mask Weight" determines the strength of
     * filtering, where "Mask Weight"=1 would be an infinite weight of the
     * high-pass filtered image that is added.
     *
     * @return the value of sharpenWeight
     */
    public float getSharpenWeight() {
        return sharpenWeight;
    }

    /**
     * Set the value of sharpenWeight
     *
     * @param sharpenWeight new value of sharpenWeight
     */
    public void setSharpenWeight(float sharpenWeight) {
        if (sharpenWeight == this.sharpenWeight) {
            return;
        }
        double oldSharpenWeight = this.sharpenWeight;
        this.sharpenWeight = sharpenWeight;
        propertyChangeSupport.firePropertyChange(PROP_SHARPEN_WEIGHT, oldSharpenWeight, sharpenWeight);
    }

    /**
     * Get the value of smoothingFactor
     *
     * @return the value of smoothingFactor
     */
    public double getSmoothingFactor() {
        return smoothingFactor;
    }

    /**
     * Set the value of smoothingFactor
     *
     * @param smoothingFactor new value of smoothingFactor
     */
    public void setSmoothingFactor(double smoothingFactor) {
        if (smoothingFactor == this.smoothingFactor) {
            return;
        }
        double oldSmoothingFactor = this.smoothingFactor;
        this.smoothingFactor = smoothingFactor;
        propertyChangeSupport.firePropertyChange(PROP_SMOOTHING_FACTOR, oldSmoothingFactor, smoothingFactor);
    }

    /**
     * Get the value of displayLogOct
     *
     * @return the value of displayLogOct
     */
    public boolean isDisplayLogOct() {
        return displayLogOct;
    }

    /**
     * Set the value of displayLogOct
     *
     * @param displayLogOct new value of displayLogOct
     */
    public void setDisplayLogOct(boolean displayLogOct) {
        if (displayLogOct == this.displayLogOct) {
            return;
        }
        boolean oldDisplayLogOct = this.displayLogOct;
        this.displayLogOct = displayLogOct;
        propertyChangeSupport.firePropertyChange(PROP_DISPLAY_LOG_OCT, oldDisplayLogOct, displayLogOct);
    }

    /**
     * Get the value of applyContrastAdjustment
     *
     * @return the value of applyContrastAdjustment
     */
    public boolean isApplyContrastAdjustment() {
        return applyContrastAdjustment;
    }

    /**
     * Set the value of applyContrastAdjustment
     *
     * @param applyContrastAdjustment new value of applyContrastAdjustment
     */
    public void setApplyContrastAdjustment(boolean applyContrastAdjustment) {
        if (applyContrastAdjustment == this.applyContrastAdjustment) {
            return;
        }
        boolean oldApplyContrastAdjustment = this.applyContrastAdjustment;
        this.applyContrastAdjustment = applyContrastAdjustment;
        propertyChangeSupport.firePropertyChange(PROP_APPLY_CONTRAST_ADJUSTMENT, oldApplyContrastAdjustment, applyContrastAdjustment);
    }

    /**
     * Get the value of applyNoiseReduction
     *
     * @return the value of applyNoiseReduction
     */
    public boolean isApplyNoiseReduction() {
        return applyNoiseReduction;
    }

    /**
     * Set the value of applyNoiseReduction
     *
     * @param applyNoiseReduction new value of applyNoiseReduction
     */
    public void setApplyNoiseReduction(boolean applyNoiseReduction) {
        if (applyNoiseReduction == this.applyNoiseReduction) {
            return;
        }
        boolean oldApplyNoiseReduction = this.applyNoiseReduction;
        this.applyNoiseReduction = applyNoiseReduction;
        propertyChangeSupport.firePropertyChange(PROP_APPLY_NOISE_REDUCTION, oldApplyNoiseReduction, applyNoiseReduction);
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
