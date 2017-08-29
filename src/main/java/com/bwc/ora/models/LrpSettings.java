/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.models;

import com.bwc.ora.collections.ModelsCollection;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class LrpSettings {
    /*
     Initial values for this model are the default values of settings for the LRPs
     */

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private transient final Integer[] lrpWidthOptions = new Integer[] { 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25 };
    private transient final int[] lrpSmoothingRange = new int[] { 0, 49 };
    private int lrpWidth = 5;
    public static final String PROP_LRP_WIDTH = "lrpWidth";
    private int lrpHeight = 300;
    public static final String PROP_LRP_HEIGHT = "lrpHeight";
    private int numberOfLrp = 1;
    public static final String PROP_NUMBER_OF_LRP = "numberOfLrp";
    private int lrpSmoothingFactor = 0;
    public static final double LRP_SMOOTHING_FACTOR_MULTIPLIER = 0.02D;
    public static final String PROP_LRP_SMOOTHING_FACTOR = "lrpSmoothingFactor";
    private double lrpSeperationDistance = 300D;
    public static final String PROP_LRP_SEPERATION_DISTANCE = "lrpSeperationDistance";
    private boolean distanceUnitsInPixels = false;
    public static final String PROP_DISTANCE_UNITS_IN_PIXELS = "distanceUnitsInPixels";

    public void resetToDefaultSettings() {
        LrpSettings ds = new LrpSettings();
        setLrpHeight(ds.lrpHeight);
        setLrpWidth(ds.lrpWidth);
        setNumberOfLrp(ds.numberOfLrp);
        setLrpSmoothingFactor(ds.lrpSmoothingFactor);
        setLrpSeperationDistance(ds.lrpSeperationDistance);
        setDistanceUnitsInPixels(ds.distanceUnitsInPixels);
    }

    public void loadSettings(LrpSettings newLrpSettings) {
        setLrpHeight(newLrpSettings.lrpHeight);
        setLrpWidth(newLrpSettings.lrpWidth);
        setNumberOfLrp(newLrpSettings.numberOfLrp);
        setLrpSmoothingFactor(newLrpSettings.lrpSmoothingFactor);
        setLrpSeperationDistance(newLrpSettings.lrpSeperationDistance);
        setDistanceUnitsInPixels(newLrpSettings.distanceUnitsInPixels);
    }

    /**
     * Convenience method for getting the separation distance between LRPs
     * regardless of the distance units specified by the user.
     *
     * @return
     */
    public int getLrpSeperationDistanceInPixels() {
        if (distanceUnitsInPixels) {
            return (int) Math.round(lrpSeperationDistance);
        } else {
            OctSettings s = ModelsCollection.getInstance().getOctSettings();
            return (int) Math.round((1D / s.getxScale()) * lrpSeperationDistance);
        }
    }

    /**
     * Get the value of distanceUnitsInPixels
     *
     * @return the value of distanceUnitsInPixels
     */
    public boolean isDistanceUnitsInPixels() {
        return distanceUnitsInPixels;
    }

    /**
     * Set the value of distanceUnitsInPixels
     *
     * @param distanceUnitsInPixels new value of distanceUnitsInPixels
     */
    public void setDistanceUnitsInPixels(boolean distanceUnitsInPixels) {
        if (this.distanceUnitsInPixels == distanceUnitsInPixels) {
            return;
        }
        boolean oldDistanceUnitsInPixels = this.distanceUnitsInPixels;
        this.distanceUnitsInPixels = distanceUnitsInPixels;
        propertyChangeSupport.firePropertyChange(PROP_DISTANCE_UNITS_IN_PIXELS, oldDistanceUnitsInPixels, distanceUnitsInPixels);
    }

    /**
     * Get the value of lrpSeperationDistance
     *
     * @return the value of lrpSeperationDistance
     */
    public double getLrpSeperationDistance() {
        return lrpSeperationDistance;
    }

    /**
     * Set the value of lrpSeperationDistance
     *
     * @param lrpSeperationDistance new value of lrpSeperationDistance
     */
    public void setLrpSeperationDistance(double lrpSeperationDistance) {
        if (this.lrpSeperationDistance == lrpSeperationDistance) {
            return;
        }
        double oldLrpSeperationDistance = this.lrpSeperationDistance;
        this.lrpSeperationDistance = lrpSeperationDistance;
        propertyChangeSupport.firePropertyChange(PROP_LRP_SEPERATION_DISTANCE, oldLrpSeperationDistance, lrpSeperationDistance);
    }

    /**
     * Get the value of lrpSmoothingFactor
     *
     * @return the value of lrpSmoothingFactor
     */
    public int getLrpSmoothingFactor() {
        return lrpSmoothingFactor;
    }

    /**
     * Set the value of lrpSmoothingFactor
     *
     * @param lrpSmoothingFactor new value of lrpSmoothingFactor
     */
    public void setLrpSmoothingFactor(int lrpSmoothingFactor) {
        if (this.lrpSmoothingFactor == lrpSmoothingFactor) {
            return;
        }
        int oldLrpSmoothingFactor = this.lrpSmoothingFactor;
        this.lrpSmoothingFactor = lrpSmoothingFactor;
        propertyChangeSupport.firePropertyChange(PROP_LRP_SMOOTHING_FACTOR, oldLrpSmoothingFactor, lrpSmoothingFactor);
    }

    /**
     * Get the value of numberOfLrp
     *
     * @return the value of numberOfLrp
     */
    public int getNumberOfLrp() {
        return numberOfLrp;
    }

    /**
     * Set the value of numberOfLrp
     *
     * @param numberOfLrp new value of numberOfLrp
     */
    public void setNumberOfLrp(int numberOfLrp) throws IllegalArgumentException {
        if (this.numberOfLrp == numberOfLrp) {
            return;
        }
        int oldNumberOfLrp = this.numberOfLrp;
        this.numberOfLrp = numberOfLrp;
        propertyChangeSupport.firePropertyChange(PROP_NUMBER_OF_LRP, oldNumberOfLrp, numberOfLrp);
    }

    /**
     * Get the value of lrpWidth
     *
     * @return the value of lrpWidth
     */
    public int getLrpWidth() {
        return lrpWidth;
    }

    /**
     * Set the value of lrpWidth
     *
     * @param lrpWidth new value of lrpWidth
     */
    public void setLrpWidth(int lrpWidth) {
        if (this.lrpWidth == lrpWidth) {
            return;
        }
        int oldLrpWidth = this.lrpWidth;
        this.lrpWidth = lrpWidth;
        propertyChangeSupport.firePropertyChange(PROP_LRP_WIDTH, oldLrpWidth, lrpWidth);
    }

    /**
     * Get the value of lrpHeight
     *
     * @return the value of lrpHeight
     */
    public int getLrpHeight() {
        return lrpHeight;
    }

    /**
     * Set the value of lrpHeight
     *
     * @param lrpHeight new value of lrpHeight
     */
    public void setLrpHeight(int lrpHeight) {
        if (this.lrpHeight == lrpHeight) {
            return;
        }
        int oldLrpHeight = this.lrpHeight;
        this.lrpHeight = lrpHeight;
        propertyChangeSupport.firePropertyChange(PROP_LRP_HEIGHT, oldLrpHeight, lrpHeight);
    }

    public Integer[] getLrpWidthOptions() {
        return lrpWidthOptions;
    }

    public int[] getLrpSmoothingRange() {
        return lrpSmoothingRange;
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

    public void addFirstPriorityPropertyChangeListener(PropertyChangeListener listener) {
        //likely a hacky solution but works so don't change!
        PropertyChangeListener[] curProps = propertyChangeSupport.getPropertyChangeListeners();
        Arrays.stream(curProps).forEach(propertyChangeSupport::removePropertyChangeListener);
        propertyChangeSupport.addPropertyChangeListener(listener);
        Arrays.stream(curProps).forEach(propertyChangeSupport::addPropertyChangeListener);
    }
}
