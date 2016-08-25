/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views.toolbars;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ToolbarUtilities {

    public static final float TOOLBAR_SUBCOMPONENT_FONT_SIZE = 13F;
    public static final int TOOLBAR_HORIZONTAL_STRUT_WIDTH = 8;

    public static void setSubcomponentFontSize(float size, JPanel toolbarPanel) {
        Arrays.stream(toolbarPanel.getComponents())
                .forEach(component -> {
                    component.setFont(component.getFont().deriveFont(size));
                });
    }

    public static void addLabelTextRows(ArrayList<Component> labels,
            ArrayList<Component> inputs,
            GridBagLayout gridbag,
            Container container) {
        int numLabels = labels.size();

        for (int i = 0; i < numLabels; i++) {
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.EAST;
            c.insets = new Insets(3, 0, 0, 0);  //top padding
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            container.add(labels.get(i), c);

            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.EAST;
            c.insets = new Insets(3, 0, 0, 0);  //top padding
            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(inputs.get(i), c);
        }
    }

    public static JPanel getPanelWithSliderAndTextField(JFormattedTextField textField, JSlider slider) {
        JPanel p = new JPanel();
        BoxLayout b = new BoxLayout(p, BoxLayout.LINE_AXIS);
        p.setLayout(b);
        p.add(Box.createHorizontalGlue());
        textField.setMinimumSize(new Dimension(35, textField.getMinimumSize().height));
        p.add(textField);
        p.add(Box.createHorizontalStrut(5));
        p.add(slider);
        return p;
    }
}
