/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.models.*;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ModelsCollection {

    private final LrpSettings lrpSettings = new LrpSettings();
    private final OctSettings octSettings = new OctSettings();
    private final DisplaySettings displaySettings = new DisplaySettings();
    private final ScaleBar scaleBar = new ScaleBar("scale_bars", 100);
    private final AnalysisSettings analysisSettings = new AnalysisSettings();

    private ModelsCollection() {
    }

    public static ModelsCollection getInstance() {
        return ModelsHolder.INSTANCE;
    }

    private static class ModelsHolder {

        private static final ModelsCollection INSTANCE = new ModelsCollection();
    }

    public LrpSettings getLrpSettings() {
        return lrpSettings;
    }

    public OctSettings getOctSettings() {
        return octSettings;
    }

    public DisplaySettings getDisplaySettings() {
        return displaySettings;
    }

    public ScaleBar getScaleBar() {
        return scaleBar;
    }

    public AnalysisSettings getAnalysisSettings() {
        return analysisSettings;
    }

    public void resetSettingsToDefault() {
        lrpSettings.resetToDefaultSettings();
        octSettings.resetToDefaultSettings();
        displaySettings.resetToDefaultSettings();
        analysisSettings.resetToDefaultSettings();
    }
}
