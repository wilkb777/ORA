package com.bwc.ora.models;

/**
 * Created by Brandon on 5/18/2017.
 */
public enum AnalysisMode {
    FREE_FORM("Free Form"), PREFORMATTED("Preformatted"), EZ_DETECTION("EZ Detection");

    private final String modeText;

    AnalysisMode(String modeText) {
        this.modeText = modeText;
    }

    public String getModeText() {
        return modeText;
    }
}
