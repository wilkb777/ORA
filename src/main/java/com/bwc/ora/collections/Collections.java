/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.views.OCTOverlay;

import javax.swing.*;
import java.util.stream.Stream;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class Collections {

    private final LrpCollection lrpCollection;
    private final ViewsCollection viewsCollection;
    private final OctDrawnPointCollection octDrawnPointCollection;
    private final OctDrawnLinesCollection octDrawnLineCollection;
    private final ModelsCollection modelsCollection = ModelsCollection.getInstance();

    private Collections() {
        //initialize the lrp collection and set display settings
        lrpCollection = new LrpCollection();
        lrpCollection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lrpCollection.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        lrpCollection.setVisibleRowCount(1);
        //initialize the views collection
        viewsCollection = new ViewsCollection();
        //initialize the Drawn OCT points collection
        octDrawnPointCollection = new OctDrawnPointCollection();
        octDrawnLineCollection = new OctDrawnLinesCollection();
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
        return Stream
                .of(
                        lrpCollection.streamSelected(),
                        octDrawnPointCollection.stream(),
                        octDrawnLineCollection.stream(),
                        Stream.of(modelsCollection.getScaleBar(), modelsCollection.getOctWindowSelector()))
                .flatMap(stream -> stream);
    }

    public ViewsCollection getViewsCollection() {
        return viewsCollection;
    }

    public OctDrawnPointCollection getOctDrawnPointCollection() {
        return octDrawnPointCollection;
    }

    public OctDrawnLinesCollection getOctDrawnLineCollection() {
        return octDrawnLineCollection;
    }

    public void resetCollectionsForNewAnalysis() {
        viewsCollection.enableSettingsTabsInputs();
        viewsCollection.setOctSettingsAsSelectedTab();
        lrpCollection.clearLrps();
        octDrawnPointCollection.clear();
        octDrawnLineCollection.clear();
    }
}
