/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.collections.events.OctDrawnPointCollectionEvent;
import com.bwc.ora.collections.events.OctDrawnPointCollectionEventListener;
import com.bwc.ora.views.OctDrawnPoint;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class OctDrawnPointCollection extends LinkedList<OctDrawnPoint> {

    private ArrayList<OctDrawnPointCollectionEventListener> listenerList = new ArrayList<>();

    @Override
    public void clear() {
        if (size() > 0) {
            super.clear();
            listenerList.forEach(l -> l.handleEvent(OctDrawnPointCollectionEvent.CLEARED));
        }
    }

    @Override
    public boolean add(OctDrawnPoint e) {
        boolean res = super.add(e);
        if (res) {
            listenerList.forEach(l -> l.handleEvent(OctDrawnPointCollectionEvent.ADDED));
        }
        return res;
    }

    public boolean addCollectionEventListener(OctDrawnPointCollectionEventListener eventListener){
        return listenerList.add(eventListener);
    }
    
    public boolean removeCollectionEventListener(OctDrawnPointCollectionEventListener eventListener){
        return listenerList.remove(eventListener);
    }
}
