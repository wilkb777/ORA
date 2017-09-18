/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.util;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ChangeSupport {

    private final JComponent listeningComponent;

    public ChangeSupport(JComponent listeningComponent) {
        this.listeningComponent = listeningComponent;
    }

    public void fireStateChanged(ChangeEvent evt) {
        ChangeListener[] listeners = listeningComponent.getListeners(ChangeListener.class);
        if (listeners != null && listeners.length > 0) {
            evt = (evt == null) ? new ChangeEvent(listeningComponent) : evt;
            for (ChangeListener listener : listeners) {
                listener.stateChanged(evt);
            }
        }
    }
}
