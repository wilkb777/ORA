package com.bwc.ora.views.menus;

import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.AnalysisMode;
import com.bwc.ora.models.LrpSettings;
import com.bwc.ora.models.OctSettings;

import javax.swing.*;

/**
 * Created by Brandon on 1/16/2017.
 */
public class AnalysisPresetsMenu extends JMenu {
    private JRadioButtonMenuItem freeformCheckBox = new JRadioButtonMenuItem(AnalysisMode.FREE_FORM.getModeText(),
            true);
    private JRadioButtonMenuItem ezCheckBox = new JRadioButtonMenuItem(AnalysisMode.EZ_DETECTION.getModeText(), false);
    private JRadioButtonMenuItem preformattedCheckBox = new JRadioButtonMenuItem(
            AnalysisMode.PREFORMATTED.getModeText(), true);
    private ModelsCollection modelsCollection = ModelsCollection.getInstance();
    private ButtonGroup buttonGroup = new ButtonGroup();

    public AnalysisPresetsMenu() {
        setText("Preset Analyses");
        init();
    }

    public AnalysisPresetsMenu(String s) {
        super(s);
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

    }
}
