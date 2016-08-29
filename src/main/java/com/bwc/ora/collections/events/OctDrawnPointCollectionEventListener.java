/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections.events;

import java.util.EventListener;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public interface OctDrawnPointCollectionEventListener extends EventListener {

    public void handleEvent(OctDrawnPointCollectionEvent evt);
}
