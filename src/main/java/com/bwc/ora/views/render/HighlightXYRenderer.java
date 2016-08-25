/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views.render;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashSet;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class HighlightXYRenderer extends XYLineAndShapeRenderer {

    /**
     * The index of the series to highlight (-1 for none).
     */
    private int highlightSeries = -1;

    /**
     * The index of the item to highlight (-1 for none).
     */
    private int highlightItem = -1;

    private HashSet<SelectedItem> selectedItems = new HashSet<>();

    /**
     * Sets the item to be highlighted (use (-1, -1) for no highlight).
     *
     * @param seriesIndex
     * @param itemIndex
     */
    public void setHighlightedItem(int seriesIndex, int itemIndex) {
        if (highlightSeries == seriesIndex && highlightItem == itemIndex) {
            return;  // nothing to do
        }
        highlightSeries = seriesIndex;
        highlightItem = itemIndex;
        notifyListeners(new RendererChangeEvent(this));
    }

    /**
     * Return a special color for the highlighted item.
     *
     * @param seriesIndex the series index.
     * @param itemIndex the item index.
     *
     * @return The outline paint.
     */
    @Override
    public Paint getItemOutlinePaint(int seriesIndex, int itemIndex) {
//        System.out.println("Looking for (" + seriesIndex + "," + itemIndex + ")");
        if (seriesIndex == highlightSeries && itemIndex == highlightItem) {
            return Color.yellow;
        }
        return super.getItemOutlinePaint(seriesIndex, itemIndex);
    }

    
    @Override
    public Paint getItemFillPaint(int seriesIndex, int itemIndex) {
        SelectedItem item = new SelectedItem(seriesIndex, itemIndex);
        if (selectedItems.contains(item)) {
            return Color.cyan;
        }
        return super.getItemFillPaint(seriesIndex, itemIndex); //To change body of generated methods, choose Tools | Templates.
    }

    public void addOrRemoveSelectedItem(int seriesIndex, int itemIndex) {
        SelectedItem item = new SelectedItem(seriesIndex, itemIndex);
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        notifyListeners(new RendererChangeEvent(this));
    }

    private class SelectedItem {

        /**
         * The index of the series to highlight (-1 for none).
         */
        private final int selectSeries;

        /**
         * The index of the item to highlight (-1 for none).
         */
        private final int selectItem;

        public SelectedItem(int selectSeries, int selectItem) {
            this.selectSeries = selectSeries;
            this.selectItem = selectItem;
        }

        public int getSelectSeries() {
            return selectSeries;
        }

        public int getSelectItem() {
            return selectItem;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 71 * hash + this.selectSeries;
            hash = 71 * hash + this.selectItem;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SelectedItem other = (SelectedItem) obj;
            if (this.selectSeries != other.selectSeries) {
                return false;
            }
            if (this.selectItem != other.selectItem) {
                return false;
            }
            return true;
        }

    }
}
