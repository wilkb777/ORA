/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.models;

import java.util.List;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class LrpSeries {
    private final XYSeries lrpSeries, maximaSeries, hiddenMaximaSeries;
    private final List<XYSeries> fwhmSeries;

    public LrpSeries(XYSeries lrpSeries, XYSeries maximaSeries, XYSeries hiddenMaximaSeries, List<XYSeries> fwhmSeries) {
        this.lrpSeries = lrpSeries;
        this.maximaSeries = maximaSeries;
        this.hiddenMaximaSeries = hiddenMaximaSeries;
        this.fwhmSeries = fwhmSeries;
    }

    public XYSeries getLrpSeries() {
        return lrpSeries;
    }

    public XYSeries getMaximaSeries() {
        return maximaSeries;
    }

    public XYSeries getHiddenMaximaSeries() {
        return hiddenMaximaSeries;
    }

    public List<XYSeries> getFwhmSeries() {
        return fwhmSeries;
    }
    
}
