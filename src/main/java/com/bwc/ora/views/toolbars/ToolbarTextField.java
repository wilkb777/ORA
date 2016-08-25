/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views.toolbars;

import java.awt.Dimension;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ToolbarTextField extends JFormattedTextField{

    public ToolbarTextField(Object value) {
        super(value);
        setPreferredSize(new Dimension(40, getPreferredSize().height));
        setHorizontalAlignment(JTextField.CENTER);
    }
    
}
