/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

import com.bwc.ora.views.menus.AnalysisPresetsMenu;
import com.bwc.ora.views.menus.FileMenu;
import com.bwc.ora.views.menus.SettingsMenu;

import javax.swing.*;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class OraMenuBar extends JMenuBar {

    public OraMenuBar() {
        initMenus();
    }

    private void initMenus() {
        /*
         Add different menus to the menu bar, order added determines order listed in bar
         */
        add(new FileMenu());
        add(new SettingsMenu());
        add(new AnalysisPresetsMenu());
    }

}
