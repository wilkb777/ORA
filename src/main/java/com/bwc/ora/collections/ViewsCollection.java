/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.OraUtils;
import com.bwc.ora.views.toolbars.AnalysisPanel;
import com.bwc.ora.views.toolbars.LrpSettingsPanel;
import com.bwc.ora.views.toolbars.OctSettingsPanel;

import java.util.ArrayList;
import javax.swing.*;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ViewsCollection extends ArrayList<JPanel> {

    private JTabbedPane settingsTabPane;

    public void setSettingsTabPane(JTabbedPane settingsTabPane) {
        this.settingsTabPane = settingsTabPane;
    }

    public void setOctSettingsAsSelectedTab() {
        int i = 0;
        for (; i < size(); i++) {
            if (get(i) instanceof OctSettingsPanel) {
                break;
            }
        }
        settingsTabPane.setSelectedIndex(i);
    }

    public void setAnalysisTabAsSelectedTab() {
        int i = 0;
        for (; i < size(); i++) {
            if (get(i) instanceof AnalysisPanel) {
                break;
            }
        }
        settingsTabPane.setSelectedIndex(i);
    }

    /**
     * Recursively disable all of the input elements of all the views registered
     * with this collections.
     */
    public void disableViewsInputs() {
        stream()
                .filter(container -> !(container instanceof AnalysisPanel))
                .forEach(container -> OraUtils.setEnabled(container, false));
    }

    /**
     * Recursively enable all of the input elements of all the views registered
     * with this collections.
     */
    public void enableViewsInputs() {
        forEach(container -> {
            OraUtils.setEnabled(container, true);
            if (container instanceof LrpSettingsPanel) {
                ((LrpSettingsPanel) container).disableRunAnalysisButton();
            }
        });
    }

}
