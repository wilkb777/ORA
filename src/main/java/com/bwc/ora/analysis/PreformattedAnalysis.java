package com.bwc.ora.analysis;

import com.bwc.ora.OraUtils;
import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.OctSettings;

public class PreformattedAnalysis implements Analysis {
    @Override
    public void run() {
        OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();
        if (!(octSettings.getyScale() > 0 && octSettings.getxScale() > 0)) {
            throw new AnalysisConditionsNotMetException("X and Y scale must be positive, non-zero decimal numbers.");
        }

        /*
         Based on the current settings generate the appropriate number of LRPs for the user to analyze
         */
        OraUtils.setLrpsForAnalysis();

        //disable settings panels so no changes to the settings can be made
        Collections.getInstance().getViewsCollection().disableSettingsTabsInputs();

        //move to analysis tab
        Collections.getInstance().getViewsCollection().setAnalysisTabAsSelectedTab();
    }


}
