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

    public void resetToDefaultSettings() {
        AnalysisSettings as = new AnalysisSettings();
        setCurrentAnalysisMode(as.getCurrentAnalysisMode());
    }

    public void loadSettings(AnalysisSettings analysisSettings) {
        setCurrentAnalysisMode(analysisSettings.getCurrentAnalysisMode());
    }

    public AnalysisMode getCurrentAnalysisMode() {
        return currentAnalysisMode;
    }

    public void setCurrentAnalysisMode(AnalysisMode newAnlysisMode) {
        oldAnalysisMode = currentAnalysisMode;
        currentAnalysisMode = newAnlysisMode;
        propertyChangeSupport.firePropertyChange(PROP_CURRENT_ANALYSIS_MODE, oldAnalysisMode, currentAnalysisMode);
    }

    public void revertAnalysisModeToPreviousMode() {
        currentAnalysisMode = oldAnalysisMode;
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
