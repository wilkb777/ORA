/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.collections.events.OctDrawnLineCollectionEvent;
import com.bwc.ora.collections.events.OctDrawnLineCollectionEventListener;
import com.bwc.ora.collections.events.OctDrawnPointCollectionEvent;
import com.bwc.ora.collections.events.OctDrawnPointCollectionEventListener;
import com.bwc.ora.models.DisplaySettings;
import com.bwc.ora.models.OctPolyLine;
import com.bwc.ora.views.OctDrawnPoint;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class OctDrawnLinesCollection extends LinkedList<OctPolyLine> {

    private ArrayList<OctDrawnLineCollectionEventListener> listenerList = new ArrayList<>();

    public OctDrawnLinesCollection() {
        //add listener for checking if the lines should be drawn
        ModelsCollection.getInstance().getDisplaySettings().addPropertyChangeListener(evt -> {
            if (DisplaySettings.PROP_SHOW_LINES_ON_OCT.equals(evt.getPropertyName())) {
                forEach(line -> line.setDisplay((boolean) evt.getNewValue()));
            }
        });
    }

    @Override
    public void clear() {
        if (size() > 0) {
            super.clear();
            listenerList.forEach(l -> l.handleEvent(OctDrawnLineCollectionEvent.CLEARED));
        }
    }

    @Override
    public boolean add(OctPolyLine e) {
        boolean res = super.add(e);
        if (res) {
            listenerList.forEach(l -> l.handleEvent(OctDrawnLineCollectionEvent.ADDED));
        }
        return res;
    }

    public boolean addCollectionEventListener(OctDrawnLineCollectionEventListener eventListener) {
        return listenerList.add(eventListener);
    }

    public boolean removeCollectionEventListener(OctDrawnLineCollectionEventListener eventListener) {
        return listenerList.remove(eventListener);
    }
}
