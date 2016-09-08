/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

import com.bwc.ora.OraUtils;
import com.bwc.ora.models.DisplaySettings;
import com.bwc.ora.collections.ModelsCollection;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class OraMenuBar extends JMenuBar {

    //file menu options
    JMenu fileMenu = new JMenu();
    JMenuItem newAnalysis = new JMenuItem();
    JMenuItem testAnalysis = new JMenuItem();
    JMenuItem open = new JMenuItem();
    JMenuItem export = new JMenuItem();
    JMenuItem save = new JMenuItem();
    JMenuItem exit = new JMenuItem();
    //display menu options
    JMenu displayMenu = new JMenu();
    JCheckBoxMenuItem displayScaleBarsCheckBox = new JCheckBoxMenuItem("Display Scale Bars", true);
    JCheckBoxMenuItem displayFileNameCheckBox = new JCheckBoxMenuItem("Display OCT File Name", true);
    JCheckBoxMenuItem displayLrpPeaksCheckBox = new JCheckBoxMenuItem("Display LRP Peaks", true);
    JCheckBoxMenuItem showFwhmCheckBox = new JCheckBoxMenuItem("Display FWHM on LRP", false);

    public OraMenuBar() {
        initMenus();
    }

    private void initMenus() {
        /*
         Add different menus to the menu bar, order added determines order listed in bar
         */
        initFileMenu();
        initSettingsMenu();
        connectFileOperations();
        connectSettingsToModel();
    }

    private void initFileMenu() {
        fileMenu.setText("File");

        newAnalysis.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newAnalysis.setText("New Analysis");
        fileMenu.add(newAnalysis);

        testAnalysis.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        testAnalysis.setText("Test Analysis");
        fileMenu.add(testAnalysis);

        open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        open.setText("Open Analysis");
        fileMenu.add(open);

        export.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        export.setText("Export Analysis Results");
        fileMenu.add(export);

        save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        save.setText("Save Analysis");
        save.setEnabled(false);
        fileMenu.add(save);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exit.setText("Quit");
        fileMenu.add(exit);

        this.add(fileMenu);
    }

    private void initSettingsMenu() {

        displayMenu.setText("Settings");

        displayMenu.add(displayFileNameCheckBox);
        displayMenu.add(displayScaleBarsCheckBox);
        displayMenu.add(displayLrpPeaksCheckBox);
        displayMenu.add(showFwhmCheckBox);

        this.add(displayMenu);
    }

    private void connectFileOperations() {
        newAnalysis.addActionListener(OraUtils.newAnalysisActionListener);
        testAnalysis.addActionListener(OraUtils.testAnalysisActionListener);
        export.addActionListener(OraUtils.exportAnalysisActionListener);
    }

    private void connectSettingsToModel() {
        DisplaySettings settings = ModelsCollection.getInstance().getDisplaySettings();

        displayFileNameCheckBox.setSelected(settings.isDisplayFileName());
        displayFileNameCheckBox.addItemListener(evt -> settings.setDisplayFileName(displayFileNameCheckBox.isSelected()));
        settings.addPropertyChangeListener(evt -> {
            if (DisplaySettings.PROP_DISPLAY_FILE_NAME.equals(evt.getPropertyName())) {
                displayFileNameCheckBox.setSelected(settings.isDisplayFileName());
            }
        });

        displayScaleBarsCheckBox.setSelected(settings.isDisplayScaleBarsOnOct());
        displayScaleBarsCheckBox.addItemListener(evt -> settings.setDisplayScaleBarsOnOct(displayScaleBarsCheckBox.isSelected()));
        settings.addPropertyChangeListener(evt -> {
            if (DisplaySettings.PROP_DISPLAY_SCALE_BARS_ON_OCT.equals(evt.getPropertyName())) {
                displayScaleBarsCheckBox.setSelected(settings.isDisplayScaleBarsOnOct());
            }
        });

        displayLrpPeaksCheckBox.setSelected(settings.isShowLrpPeaks());
        displayLrpPeaksCheckBox.addItemListener(evt -> settings.setShowLrpPeaks(displayLrpPeaksCheckBox.isSelected()));
        settings.addPropertyChangeListener(evt -> {
            if (DisplaySettings.PROP_SHOW_LRP_PEAKS.equals(evt.getPropertyName())) {
                displayLrpPeaksCheckBox.setSelected(settings.isShowLrpPeaks());
            }
        });

        showFwhmCheckBox.setSelected(settings.isShowFwhmOnLrp());
        showFwhmCheckBox.addItemListener(evt -> settings.setShowFwhmOnLrp(showFwhmCheckBox.isSelected()));
        settings.addPropertyChangeListener(evt -> {
            if (DisplaySettings.PROP_SHOW_FWHM_ON_LRP.equals(evt.getPropertyName())) {
                showFwhmCheckBox.setSelected(settings.isShowFwhmOnLrp());
            }
        });
    }
}
