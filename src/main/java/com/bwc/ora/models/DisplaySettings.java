/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class DisplaySettings {
    
    private boolean displayFileName = true;
    public static final String PROP_DISPLAY_FILE_NAME = "displayFileName";
    private boolean showLrpPeaks = true;
    public static final String PROP_SHOW_LRP_PEAKS = "showLrpPeaks";
    private boolean showFwhmOnLrp = false;
    public static final String PROP_SHOW_FWHM_ON_LRP = "showFwhmOnLrp";
    private boolean displayScaleBarsOnOct = true;
    public static final String PROP_DISPLAY_SCALE_BARS_ON_OCT = "displayScaleBarsOnOct";

    /**
     * Get the value of displayScaleBarsOnOct
     *
     * @return the value of displayScaleBarsOnOct
     */
    public boolean isDisplayScaleBarsOnOct() {
        return displayScaleBarsOnOct;
    }

    /**
     * Set the value of displayScaleBarsOnOct
     *
     * @param displayScaleBarsOnOct new value of displayScaleBarsOnOct
     */
    public void setDisplayScaleBarsOnOct(boolean displayScaleBarsOnOct) {
        boolean oldDisplayScaleBarsOnOct = this.displayScaleBarsOnOct;
        this.displayScaleBarsOnOct = displayScaleBarsOnOct;
        propertyChangeSupport.firePropertyChange(PROP_DISPLAY_SCALE_BARS_ON_OCT, oldDisplayScaleBarsOnOct, displayScaleBarsOnOct);
    }

    /**
     * Get the value of showFwhmOnLrp
     *
     * @return the value of showFwhmOnLrp
     */
    public boolean isShowFwhmOnLrp() {
        return showFwhmOnLrp;
    }

    /**
     * Set the value of showFwhmOnLrp
     *
     * @param showFwhmOnLrp new value of showFwhmOnLrp
     */
    public void setShowFwhmOnLrp(boolean showFwhmOnLrp) {
        boolean oldShowFwhmOnLrp = this.showFwhmOnLrp;
        this.showFwhmOnLrp = showFwhmOnLrp;
        propertyChangeSupport.firePropertyChange(PROP_SHOW_FWHM_ON_LRP, oldShowFwhmOnLrp, showFwhmOnLrp);
    }

    /**
     * Get the value of showLrpPeaks
     *
     * @return the value of showLrpPeaks
     */
    public boolean isShowLrpPeaks() {
        return showLrpPeaks;
    }

    /**
     * Set the value of showLrpPeaks
     *
     * @param showLrpPeaks new value of showLrpPeaks
     */
    public void setShowLrpPeaks(boolean showLrpPeaks) {
        boolean oldShowLrpPeaks = this.showLrpPeaks;
        this.showLrpPeaks = showLrpPeaks;
        propertyChangeSupport.firePropertyChange(PROP_SHOW_LRP_PEAKS, oldShowLrpPeaks, showLrpPeaks);
    }

    /**
     * Get the value of displayFileName
     *
     * @return the value of displayFileName
     */
    public boolean isDisplayFileName() {
        return displayFileName;
    }

    /**
     * Set the value of displayFileName
     *
     * @param displayFileName new value of displayFileName
     */
    public void setDisplayFileName(boolean displayFileName) {
        boolean oldDisplayFileName = this.displayFileName;
        this.displayFileName = displayFileName;
        propertyChangeSupport.firePropertyChange(PROP_DISPLAY_FILE_NAME, oldDisplayFileName, displayFileName);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

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
