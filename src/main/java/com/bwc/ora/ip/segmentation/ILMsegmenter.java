package com.bwc.ora.ip.segmentation;

import com.bwc.ora.ip.Blur;
import com.bwc.ora.ip.ContrastAdjust;
import com.bwc.ora.ip.NoiseReduction;
import com.bwc.ora.models.*;
import org.jfree.data.xy.XYDataItem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ILMsegmenter {

    public static OctPolyLine segmentILM(Point windowCorner, Point otherWindowCorner) {
        /*
        Apply image filtering and LRP analysis with the following settings:
        "lrpSettings": {
            "lrpWidth": 5,
            "lrpSmoothingFactor": 45,
            "lrpSeperationDistance": 1,
            "distanceUnitsInPixels": true
        },
        "octSettings": {
            "applyNoiseReduction": true,
            "applyContrastAdjustment": true,
            "displayLogOct": true,
            "smoothingFactor": 2.0
        }
         */
        BufferedImage transformedOct = Oct.getInstance().manualTransformOct(
                true,
                new Blur(2.0),
                new ContrastAdjust(),
                new NoiseReduction()
        );

        int lrpCenterYPosition = Math.min(windowCorner.y, otherWindowCorner.y) + (Math.abs(windowCorner.y - otherWindowCorner.y) / 2);
        OctPolyLine octPolyLine = new OctPolyLine("ILM segment", 10000);
        IntStream.rangeClosed(Math.min(windowCorner.x, otherWindowCorner.x), Math.max(windowCorner.x, otherWindowCorner.x))
                 .mapToObj(lrpCenterXPosition -> new Lrp("data " + lrpCenterXPosition,
                         lrpCenterXPosition,
                         lrpCenterYPosition,
                         5,
                         Math.abs(windowCorner.y - otherWindowCorner.y),
                         LrpType.PERIPHERAL,
                         transformedOct))
                 .map(lrp -> {
                     lrp.setSmoothingAlpha(45);
                     LrpSeries lrpAndPeaksData = lrp.getAllSeriesData();
                     //process lrp to find second derivative peak right at edge
                     //of the vitrious/ILM boundary
                     List<XYDataItem> pointList = (List<XYDataItem>) lrpAndPeaksData.getLrpSeries().getItems();
                     System.out.println(lrp.getName());
                     //                     pointList.forEach(p -> System.out.println(p.getYValue() + "\t" + p.getXValue()));
                     AtomicInteger maxIntensity = new AtomicInteger(0);
                     for (int i = 0; i < 10; i++) {
                         if (pointList.get(i).getYValue() > maxIntensity.get()) {
                             maxIntensity.set((int) Math.round(pointList.get(i).getYValue()));
                         }
                     }
                     System.out.println("max intensity: " + maxIntensity.get());

                     List<XYDataItem> secondDerivativePeaksList = (List<XYDataItem>) lrpAndPeaksData.getHiddenMaximaSeries().getItems();
                     XYDataItem ilmPeak = secondDerivativePeaksList.stream()
                                                                   .filter(p -> p.getYValue() > maxIntensity.get())
                                                                   .min(Comparator.comparingDouble(XYDataItem::getXValue))
                                                                   .orElse(secondDerivativePeaksList.get(0));

                     return new Point(lrp.getLrpCenterXPosition(), (int) Math.round(ilmPeak.getXValue()));
                 })
                 .forEach(octPolyLine::add);

        return octPolyLine;
    }
}
