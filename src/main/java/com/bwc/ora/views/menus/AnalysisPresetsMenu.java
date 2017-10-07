package com.bwc.ora.views.menus;

import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.*;
import com.bwc.ora.views.dialog.ORADialogs;

import javax.swing.*;

/**
 * Created by Brandon on 1/16/2017.
 */
public class AnalysisPresetsMenu extends JMenu {
    private JRadioButtonMenuItem freeformCheckBox = new JRadioButtonMenuItem(AnalysisMode.MULTI_LRP_FREE_FORM.getModeText(), false);
    private JRadioButtonMenuItem preformattedCheckBox = new JRadioButtonMenuItem(
            AnalysisMode.PREFORMATTED.getModeText(), true);
    private AnalysisSettings analysisSettings = ModelsCollection.getInstance().getAnalysisSettings();
    private ButtonGroup buttonGroup = new ButtonGroup();

    public AnalysisPresetsMenu() {
        setText("Preset Analyses");
        init();
    }

    private void init() {
        add(preformattedCheckBox);
        add(freeformCheckBox);

        buttonGroup.add(preformattedCheckBox);
        buttonGroup.add(freeformCheckBox);

        connectSettingsToModel();
    }

    private void connectSettingsToModel() {
        freeformCheckBox.addActionListener(evt -> analysisSettings.setCurrentAnalysisMode(AnalysisMode.MULTI_LRP_FREE_FORM, true));
        preformattedCheckBox.addActionListener(evt -> analysisSettings.setCurrentAnalysisMode(AnalysisMode.PREFORMATTED, true));
        analysisSettings.addPropertyChangeListener(evt -> {
            if(AnalysisSettings.PROP_RESET_TO_DEFAULT.equals(evt.getPropertyName())){
                switch (ModelsCollection.getInstance().getAnalysisSettings().getCurrentAnalysisMode()) {
                case PREFORMATTED:
                    preformattedCheckBox.setSelected(true);
                    break;
                case MULTI_LRP_FREE_FORM:
                    freeformCheckBox.setSelected(true);
                    break;
                }
            }
        });
        analysisSettings.addPropertyChangeListener(evt -> {
            if (AnalysisSettings.PROP_CURRENT_ANALYSIS_MODE.equals(evt.getPropertyName())) {
                if (evt.getNewValue().equals(evt.getOldValue())) {
                    return;
                }

                if (JOptionPane.YES_OPTION == ORADialogs.bringUpAnalysisModeChangeWarning(null)) {
                    Collections.getInstance().resetCollectionsForNewAnalysis();
                    System.out.println(evt.getNewValue());
                } else {
                    analysisSettings.setCurrentAnalysisMode((AnalysisMode) evt.getOldValue(), false);
                    switch ((AnalysisMode) evt.getOldValue()) {
                    case PREFORMATTED:
                        preformattedCheckBox.setSelected(true);
                        break;
                    case MULTI_LRP_FREE_FORM:
                        freeformCheckBox.setSelected(true);
                        break;
                    }
                }
            }
        });
    }
}
