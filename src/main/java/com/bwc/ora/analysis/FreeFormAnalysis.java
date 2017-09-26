package com.bwc.ora.analysis;

import com.bwc.ora.OraUtils;
import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.LrpCollection;
import com.bwc.ora.views.OCTDisplayPanel;

import javax.swing.*;

public class FreeFormAnalysis implements Analysis {
    @Override
    public void run() {
        //create LRP
        OraUtils.generateAnchorLrp(false, null);
    }
}
