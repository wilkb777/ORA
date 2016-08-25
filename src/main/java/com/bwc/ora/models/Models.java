/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.models;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class Models {
    
    private final LrpSettings lrpSettings = new LrpSettings();
    private final OctSettings octSettings = new OctSettings();
    private final DisplaySettings displaySettings = new DisplaySettings();
    
    private Models() {
    }
    
    public static Models getInstance() {
        return ModelsHolder.INSTANCE;
    }

    private static class ModelsHolder {

        private static final Models INSTANCE = new Models();
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
    
}
