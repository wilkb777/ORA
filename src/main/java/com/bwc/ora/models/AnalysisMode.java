package com.bwc.ora.models;

/**
 * Created by Brandon on 5/18/2017.
 */
public enum AnalysisMode {
    FREE_FORM("Free Form"), PREFORMATTED("Preformatted"), MULTI_LRP_FREE_FORM("Multi LRP Free Form");

    private final String modeText;

    AnalysisMode(String modeText) {
        this.modeText = modeText;
    }

    public String getModeText() {
        return modeText;
    }
}
