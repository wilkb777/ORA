package com.bwc.ora.views.menus;

import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.*;
import com.bwc.ora.views.dialog.ORADialogs;

import javax.swing.*;

/**
 * Created by Brandon on 1/16/2017.
 */
public class AnalysisPresetsMenu extends JMenu {
    private JRadioButtonMenuItem freeformCheckBox = new JRadioButtonMenuItem(AnalysisMode.FREE_FORM.getModeText(),
            false);
    private JRadioButtonMenuItem ezCheckBox = new JRadioButtonMenuItem(AnalysisMode.EZ_DETECTION.getModeText(), false);
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
        add(ezCheckBox);

        buttonGroup.add(preformattedCheckBox);
        buttonGroup.add(freeformCheckBox);
        buttonGroup.add(ezCheckBox);

        connectSettingsToModel();
    }

    private void connectSettingsToModel() {
        freeformCheckBox.addActionListener(evt -> analysisSettings.setCurrentAnalysisMode(AnalysisMode.FREE_FORM));
        ezCheckBox.addActionListener(evt -> analysisSettings.setCurrentAnalysisMode(AnalysisMode.EZ_DETECTION));
        preformattedCheckBox.addActionListener(evt -> analysisSettings.setCurrentAnalysisMode(AnalysisMode.PREFORMATTED));
        analysisSettings.addPropertyChangeListener(evt -> {
            if (AnalysisSettings.PROP_CURRENT_ANALYSIS_MODE.equals(evt.getPropertyName())) {
                if (evt.getNewValue().equals(evt.getOldValue())) {
                    return;
                }

                if (JOptionPane.YES_OPTION == ORADialogs.bringUpAnalysisModeChangeWarning(null)) {
                    switch ((AnalysisMode) evt.getNewValue()) {
                    case PREFORMATTED:
                        System.out.println(AnalysisMode.PREFORMATTED);
                        break;
                    case FREE_FORM:
                        System.out.println(AnalysisMode.FREE_FORM);
                        break;
                    case EZ_DETECTION:
                        System.out.println(AnalysisMode.EZ_DETECTION);
                        break;
                    }
                } else {
                    analysisSettings.revertAnalysisModeToPreviousMode();
                    switch ((AnalysisMode) evt.getOldValue()) {
                    case PREFORMATTED:
                        preformattedCheckBox.setSelected(true);
                        break;
                    case FREE_FORM:
                        freeformCheckBox.setSelected(true);
                        break;
                    case EZ_DETECTION:
                        ezCheckBox.setSelected(true);
                        break;
                    }
                }
            }
        });
    }
}
