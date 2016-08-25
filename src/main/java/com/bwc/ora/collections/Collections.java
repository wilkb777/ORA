/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.views.OCTOverlay;
import java.util.stream.Stream;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class Collections {

    private final LrpCollection lrpCollection;
    private final ViewsCollection viewsCollection;

    private Collections() {
        //initialize the lrp collection and set display settings
        lrpCollection = new LrpCollection();
        lrpCollection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lrpCollection.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        lrpCollection.setVisibleRowCount(1);
        //initialize the views collection
        viewsCollection = new ViewsCollection();
    }

    public static Collections getInstance() {
        return CollectionsHolder.INSTANCE;
    }

    private static class CollectionsHolder {

        private static final Collections INSTANCE = new Collections();
    }

    public LrpCollection getLrpCollection() {
        return lrpCollection;
    }

    public Stream<OCTOverlay> getOverlaysStream() {
        return lrpCollection.streamSelected().map(lrp -> (OCTOverlay) lrp);
    }

    public ViewsCollection getViewsCollection() {
        return viewsCollection;
    }
}
