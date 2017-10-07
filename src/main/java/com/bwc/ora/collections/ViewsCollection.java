/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.OraUtils;
import com.bwc.ora.models.AnalysisMode;
import com.bwc.ora.views.toolbars.AnalysisPanel;
import com.bwc.ora.views.toolbars.LrpSettingsPanel;
import com.bwc.ora.views.toolbars.OctSettingsPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.*;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ViewsCollection extends ArrayList<JPanel> {

    private JTabbedPane settingsTabPane;
    private List<Component> enabledComponents = new LinkedList<>();

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
     * Recursively enable or disable the specified container and all of its
     * children components
     *
     * @param component
     * @param enabled
     */
    public static void setEnabled(Component component, boolean enabled) {
        component.setEnabled(enabled);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setEnabled(child, enabled);
            }
        }
    }

    private LinkedList<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        LinkedList<Component> compList = new LinkedList<>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }

    /**
     * Recursively disable all of the input elements of all the views registered
     * with this collections.
     */
    public void disableSettingsTabsInputs() {
        enabledComponents = stream()
                .filter(container -> !(container instanceof AnalysisPanel))
                .flatMap(jPanel -> getAllComponents(jPanel).stream())
                .filter(Component::isEnabled)
                .collect(Collectors.toList());

        enabledComponents.forEach(component -> component.setEnabled(false));
    }

    /**
     * Recursively enable all of the input elements of all the views registered
     * with this collections.
     */
    public void enableSettingsTabsInputs() {
        switch (ModelsCollection.getInstance().getAnalysisSettings().getCurrentAnalysisMode()) {
        case MULTI_LRP_FREE_FORM:
            enabledComponents.forEach(component -> component.setEnabled(true));
            break;
        default:
            stream().forEach(jPanel -> setEnabled(jPanel, true));
            break;
        }
    }

}
