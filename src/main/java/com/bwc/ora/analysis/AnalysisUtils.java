package com.bwc.ora.analysis;

import com.bwc.ora.models.AnalysisMode;

import javax.swing.*;

public class AnalysisUtils {

    public static boolean runAnalysis(AnalysisMode currentAnalysisMode) {
        try {
            switch (currentAnalysisMode) {
            case PREFORMATTED:
                new PreformattedAnalysis().run();
                break;
            case MULTI_LRP_FREE_FORM:
                new MultiLRPFreeFormAnalysis().run();
                break;
            }
            return true;
        } catch (AnalysisConditionsNotMetException e) {
            JOptionPane.showMessageDialog(null, "An error was encountered:\n" + e.getMessage()
                    + "\nRevise settings and try to run the analysis again.", "Settings Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
