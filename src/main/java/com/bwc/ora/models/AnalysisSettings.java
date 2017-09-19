package com.bwc.ora.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by Brandon on 5/18/2017.
 */
public class AnalysisSettings {
    private AnalysisMode currentAnalysisMode = AnalysisMode.PREFORMATTED;
    private AnalysisMode oldAnalysisMode = currentAnalysisMode;
    public static final String PROP_CURRENT_ANALYSIS_MODE = "currentAnalysisMode";
    public static final String PROP_RESET_TO_DEFAULT = "resetAnalysisMode";

    public void resetToDefaultSettings() {
        AnalysisSettings as = new AnalysisSettings();
        setCurrentAnalysisMode(as.getCurrentAnalysisMode(), false);
        propertyChangeSupport.firePropertyChange(PROP_RESET_TO_DEFAULT, null, null);
    }

    public void loadSettings(AnalysisSettings analysisSettings) {
        setCurrentAnalysisMode(analysisSettings.getCurrentAnalysisMode(), false);
    }

    public AnalysisMode getCurrentAnalysisMode() {
        return currentAnalysisMode;
    }

    public void setCurrentAnalysisMode(AnalysisMode newAnlysisMode, boolean firePropChange) {
        oldAnalysisMode = currentAnalysisMode;
        currentAnalysisMode = newAnlysisMode;
        if (firePropChange) {
            propertyChangeSupport.firePropertyChange(PROP_CURRENT_ANALYSIS_MODE, oldAnalysisMode, currentAnalysisMode);
        }
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
