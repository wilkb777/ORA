package com.bwc.ora.analysis;

import com.bwc.ora.OraUtils;
import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.LrpSettings;
import com.bwc.ora.models.OctSettings;

public class EZAnalysis implements Analysis {
    @Override
    public void run() {
        OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();
        if (!(octSettings.getyScale() > 0 && octSettings.getxScale() > 0)) {
            throw new AnalysisConditionsNotMetException("X and Y scale must be positive, non-zero decimal numbers.");
        }

        //change settings to make the ananlysis go 30px in both directions from the supplied anchor
        LrpSettings lrpsettings = ModelsCollection.getInstance().getLrpSettings();
        lrpsettings.setDistanceUnitsInPixels(true);
        lrpsettings.setNumberOfLrp(61);
        lrpsettings.setLrpSeperationDistance(1);
        lrpsettings.setLrpWidth(3);

        /*
         Based on the current settings generate the appropriate number of LRPs for the user to analyze
         */
        OraUtils.setLrpsForAnalysis();

        //disable settings panels so no changes to the settings can be made
        Collections.getInstance().getViewsCollection().disableViewsInputs();

        //move to analysis tab
        Collections.getInstance().getViewsCollection().setAnalysisTabAsSelectedTab();
    }
}
