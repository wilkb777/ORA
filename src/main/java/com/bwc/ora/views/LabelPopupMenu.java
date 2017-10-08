/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

import com.bwc.ora.models.Lrp;
import com.bwc.ora.models.RetinalBand;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.ui.TextAnchor;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class LabelPopupMenu extends JPopupMenu {

    private final ChartPanel chartPanel;
    private final XYItemEntity item;
    private final Lrp lrp;

    public LabelPopupMenu(ChartPanel chartPanel, XYItemEntity item, Lrp lrp) {
        super("Retinal Band Labels");
        this.chartPanel = chartPanel;
        this.item = item;
        this.lrp = lrp;
        initMenu();
    }

    private void initMenu() {
        //determine if the point clicked already has an annotation
        boolean hasAnnotationAlready = hasAnnoationAlready();

        if (hasAnnotationAlready) {
            //add label to allow users to deselect the label for a given peak
            JMenuItem nonItem = new JMenuItem("Remove Label");
            nonItem.addActionListener(e -> {
                removeAnnotation();
                lrp.setAnnotations(LrpDisplayFrame.getInstance().getAnnotations());
            });
            add(nonItem);
        }

        //add list of possible labels for a point to the popup menu
        Arrays.stream(RetinalBand.values())
                .map(RetinalBand::toString)
                .map(label -> {
                    XYPointerAnnotation pointer = new XYPointerAnnotation(
                            label,
                            item.getDataset().getXValue(item.getSeriesIndex(), item.getItem()),
                            item.getDataset().getYValue(item.getSeriesIndex(), item.getItem()),
                            0);
                    pointer.setBaseRadius(35.0);
                    pointer.setTipRadius(10.0);
                    pointer.setFont(new Font("SansSerif", Font.PLAIN, 9));
                    pointer.setPaint(Color.blue);
                    pointer.setTextAnchor(TextAnchor.CENTER_LEFT);
                    JMenuItem l = new JMenuItem(label);
                    l.addActionListener(e -> {
                        if (hasAnnotationAlready) {
                            removeAnnotation();
                        }
                        chartPanel.getChart().getXYPlot().addAnnotation(pointer);
                        lrp.setAnnotations(LrpDisplayFrame.getInstance().getAnnotations());
                    });
                    return l;
                })
                .forEach(this::add);
    }

    private void removeAnnotation() {
        for (Object annotation : chartPanel.getChart().getXYPlot().getAnnotations()) {
            if (annotation instanceof XYPointerAnnotation) {
                XYPointerAnnotation pointer = (XYPointerAnnotation) annotation;
                if (pointer.getX() == item.getDataset().getXValue(item.getSeriesIndex(), item.getItem())
                        && pointer.getY() == item.getDataset().getYValue(item.getSeriesIndex(), item.getItem())) {
                    chartPanel.getChart().getXYPlot().removeAnnotation(pointer);
                    break;
                }
            }
        }
    }

    private boolean hasAnnoationAlready() {
        boolean hasAnnotationAlready = false;
        for (Object annotation : chartPanel.getChart().getXYPlot().getAnnotations()) {
            if (annotation instanceof XYPointerAnnotation) {
                XYPointerAnnotation pointer = (XYPointerAnnotation) annotation;
                if (pointer.getX() == item.getDataset().getXValue(item.getSeriesIndex(), item.getItem())
                        && pointer.getY() == item.getDataset().getYValue(item.getSeriesIndex(), item.getItem())) {
                    hasAnnotationAlready = true;
                    break;
                }
            }
        }
        return hasAnnotationAlready;
    }

}
