package com.bwc.ora.views.menus;

import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.LrpSettings;
import com.bwc.ora.models.OctSettings;

import javax.swing.*;

/**
 * Created by Brandon on 1/16/2017.
 */
public class AnalysisPresetsMenu extends JMenu {
    private JRadioButtonMenuItem defaultCheckBox = new JRadioButtonMenuItem("Default", true);
    private JRadioButtonMenuItem tempCheckBox = new JRadioButtonMenuItem("Temp", false);
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
        add(defaultCheckBox);
        add(tempCheckBox);

        buttonGroup.add(defaultCheckBox);
        buttonGroup.add(tempCheckBox);

        connectSettingsToModel();
    }

    private void connectSettingsToModel() {
        //temporary way of handling this
        defaultCheckBox.addItemListener(evt -> modelsCollection.resetSettingsToDefault());

        tempCheckBox.addItemListener(evt -> {
            modelsCollection.resetSettingsToDefault();
            OctSettings octSettings = modelsCollection.getOctSettings();
            octSettings.setxScale(5.2625D);
            octSettings.setyScale(1.5031D);
            octSettings.setSmoothingFactor(0.7D);
            LrpSettings lrpSettings = modelsCollection.getLrpSettings();
            lrpSettings.setLrpHeight(460);
            lrpSettings.setNumberOfLrp(3);
            lrpSettings.setLrpSeperationDistance(25);
        });
    }
}
