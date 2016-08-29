/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.LrpCollection;
import com.bwc.ora.collections.OctDrawnPointCollection;
import com.bwc.ora.models.LrpSeries;
import com.bwc.ora.models.LrpSettings;
import com.bwc.ora.models.Models;
import com.bwc.ora.models.OctSettings;
import com.bwc.ora.models.RetinalBand;
import com.bwc.ora.views.render.HighlightXYRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class LrpDisplayFrame extends JFrame {

    private LrpCollection lrps = Collections.getInstance().getLrpCollection();
    private OctDrawnPointCollection octDrawnPointsCollection = Collections.getInstance().getOctDrawnPointCollection();
    private final LrpSettings lrpSettings = Models.getInstance().getLrpSettings();
    private final OctSettings octSettings = Models.getInstance().getOctSettings();
    private final ChartPanel chartPanel;
    private XYSeries lrpSeries = null, maximaSeries = null, hiddenMaximaSeries = null;

    private LrpDisplayFrame() {
        //set up frame and chart
        //configure initial display setting for the panel
        chartPanel = new ChartPanel(null);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        this.setSize(800, 800);
        add(chartPanel, BorderLayout.CENTER);

        //add listener to check to see which LRP should be displayed
        lrps.addListSelectionListener((ListSelectionEvent e) -> {
            if (e.getValueIsAdjusting() == false) {
                if (lrps.getSelectedIndex() > -1) {
                    setChart(lrps.getSelectedValue().getAllSeriesData());
                    setVisible(true);
                } else {
                    setVisible(false);
                }
            }
        });

        //add listener to check for updates to lrp settings to change lrp
        lrpSettings.addPropertyChangeListener(e -> {
            if (lrps.getSelectedIndex() > -1) {
                updateSeries(lrps.getSelectedValue().getAllSeriesData());
            }
        });

        //add listener to check for updates to oct settings to change lrp
        octSettings.addPropertyChangeListener(e -> {
            if (lrps.getSelectedIndex() > -1) {
                switch (e.getPropertyName()) {
                    case OctSettings.PROP_APPLY_CONTRAST_ADJUSTMENT:
                    case OctSettings.PROP_APPLY_NOISE_REDUCTION:
                    case OctSettings.PROP_DISPLAY_LOG_OCT:
                    case OctSettings.PROP_SHARPEN_KERNEL_RADIUS:
                    case OctSettings.PROP_SHARPEN_WEIGHT:
                    case OctSettings.PROP_SMOOTHING_FACTOR:
                        updateSeries(lrps.getSelectedValue().getAllSeriesData());
                    default:
                        break;
                }
            }
        });
    }

    public static LrpDisplayFrame getInstance() {
        return LrpDisplayFrameHolder.INSTANCE;
    }

    private static class LrpDisplayFrameHolder {

        private static final LrpDisplayFrame INSTANCE = new LrpDisplayFrame();
    }

    private void setChart(LrpSeries series) {
        lrpSeries = series.getLrpSeries();
        maximaSeries = series.getMaximaSeries();
        hiddenMaximaSeries = series.getHiddenMaximaSeries();
        XYSeriesCollection graphData = new XYSeriesCollection();

        //add series data to graph
        graphData.addSeries(lrpSeries);
        graphData.addSeries(maximaSeries);
        graphData.addSeries(hiddenMaximaSeries);
        series.getFwhmSeries().forEach(graphData::addSeries);

        //create the chart for displaying the data
        chartPanel.setChart(
                ChartFactory.createXYLineChart(
                        "LRP",
                        "Pixel Height",
                        "Reflectivity",
                        graphData,
                        PlotOrientation.HORIZONTAL,
                        true,
                        true,
                        false));

        //create a custom renderer to control the display of each series
        //set draw properties for the LRP data
        HighlightXYRenderer renderer = new HighlightXYRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesPaint(0, Color.RED);

        //set draw properties for the maxima data
        renderer.setDrawOutlines(true);
        renderer.setUseOutlinePaint(true);
        renderer.setUseFillPaint(true);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesFilled(1, true);
        renderer.setSeriesFillPaint(1, Color.BLUE);
        renderer.setSeriesShape(1, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));

        //set draw properties for the hidden maxima data
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesShapesFilled(2, true);
        renderer.setSeriesFillPaint(2, Color.MAGENTA);
        renderer.setSeriesShape(2, new Rectangle2D.Double(-3.0, -3.0, 6.0, 6.0));

        //set draw properties for each of the full-width half-max lines
        for (int i = 3; i < series.getFwhmSeries().size() + 3; i++) {
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, false);
            renderer.setSeriesPaint(i, Color.BLACK);
            renderer.setSeriesVisibleInLegend(i, false, false);
        }

        //add mouse listener for chart to detect when to display labels for
        //peaks in the pop-up menu, also specify what to do when label is clicked
        chartPanel.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                ChartEntity entity = cme.getEntity();
                if (entity instanceof XYItemEntity
                        && cme.getTrigger().getButton() == MouseEvent.BUTTON1) {

                    XYItemEntity item = (XYItemEntity) entity;
                    LabelPopupMenu labelMenu = new LabelPopupMenu(chartPanel, item, lrps.getSelectedValue());

                    labelMenu.show(chartPanel, cme.getTrigger().getX(), cme.getTrigger().getY());
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                ChartEntity entity = cme.getEntity();
                if (!(entity instanceof XYItemEntity)) {
                    renderer.setHighlightedItem(-1, -1);
                    octDrawnPointsCollection.clear();
                } else {
                    XYItemEntity xyent = (XYItemEntity) entity;
                    renderer.setHighlightedItem(xyent.getSeriesIndex(), xyent.getItem());
                    int x = lrps.getSelectedValue().getLrpCenterXPosition();
                    //get the Y value of the item on the LRP (in the case of 
                    // the graph this is the X value since the orientation of the plot is flipped)
                    int y = (int) Math.round(xyent.getDataset().getXValue(xyent.getSeriesIndex(), xyent.getItem()));
                    octDrawnPointsCollection.add(new OctDrawnPoint("Peak", new Point(x, y)));
                }
            }

        });

        chartPanel.getChart().getXYPlot().setRenderer(renderer);

        //mark the Domain (which appears as the range in a horizontal graph) 
        // axis as inverted so LRP matches with OCT
        ValueAxis domainAxis = chartPanel.getChart().getXYPlot().getDomainAxis();
        if (domainAxis instanceof NumberAxis) {
            NumberAxis axis = (NumberAxis) domainAxis;
            axis.setInverted(true);
        }

        //disable the need for the range of the chart to include zero
        ValueAxis rangeAxis = chartPanel.getChart().getXYPlot().getRangeAxis();
        if (rangeAxis instanceof NumberAxis) {
            NumberAxis axis = (NumberAxis) rangeAxis;
            axis.setAutoRangeIncludesZero(false);
        }

    }

    private void updateSeries(LrpSeries lrpSeries) {
        this.lrpSeries.clear();
        ((List<XYDataItem>) (Object) lrpSeries.getLrpSeries().getItems()).forEach(item -> {
            this.lrpSeries.add(item, false);
        });
        this.lrpSeries.fireSeriesChanged();

        this.maximaSeries.clear();
        ((List<XYDataItem>) (Object) lrpSeries.getMaximaSeries().getItems()).forEach(item -> {
            this.maximaSeries.add(item, false);
        });
        this.maximaSeries.fireSeriesChanged();

        this.hiddenMaximaSeries.clear();
        ((List<XYDataItem>) (Object) lrpSeries.getHiddenMaximaSeries().getItems()).forEach(item -> {
            this.hiddenMaximaSeries.add(item, false);
        });
        this.hiddenMaximaSeries.fireSeriesChanged();

        XYSeriesCollection graphData = (XYSeriesCollection) chartPanel.getChart().getXYPlot().getDataset();
        for (int i = 3; i < graphData.getSeriesCount(); i++) {
            graphData.removeSeries(i);
        }
        lrpSeries.getFwhmSeries().forEach(fwhmSeries -> {
            try {
                graphData.addSeries(fwhmSeries);
            } catch (IllegalArgumentException e) {
                //thrown when duplicate FWHM is calculated for some strange reason
                //just ignore failure
            }
        });
        //set draw properties of the for each of the full-width half-max lines
        for (int i = 3; i < lrpSeries.getFwhmSeries().size() + 3; i++) {
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, false);
            renderer.setSeriesPaint(i, Color.BLACK);
            renderer.setSeriesVisibleInLegend(i, false, false);
            chartPanel.getChart().getXYPlot().setRenderer(i, renderer);
        }
    }
}
