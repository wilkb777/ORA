/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.OraUtils;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ViewsCollection extends ArrayList<JPanel> {

    ViewsCollection(int initialCapacity) {
        super(initialCapacity);
    }

    ViewsCollection() {
    }

    /**
     * Recursively disable all of the input elements of all the views registered
     * with this collections.
     */
    public void disableViewsInputs() {
        forEach(container -> OraUtils.setEnabled(container, false));
    }

    /**
     * Recursively enable all of the input elements of all the views registered
     * with this collections.
     */
    public void enableViewsInputs() {
        forEach(container -> OraUtils.setEnabled(container, true));
    }

}
