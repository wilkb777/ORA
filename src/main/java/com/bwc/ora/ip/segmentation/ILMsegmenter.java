package com.bwc.ora.ip.segmentation;

import com.bwc.ora.collections.Collections;
import com.bwc.ora.ip.Blur;
import com.bwc.ora.ip.ContrastAdjust;
import com.bwc.ora.ip.NoiseReduction;
import com.bwc.ora.models.*;
import org.jfree.data.xy.XYDataItem;
import uk.me.berndporr.iirj.Butterworth;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
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
        //        Butterworth butterworth = new Butterworth();
        //        butterworth.lowPass(1, 250, 10);
        IntStream.rangeClosed(Math.min(windowCorner.x, otherWindowCorner.x), Math.max(windowCorner.x, otherWindowCorner.x))
                 .mapToObj(lrpCenterXPosition -> new Lrp("data " + lrpCenterXPosition,
                         lrpCenterXPosition,
                         lrpCenterYPosition,
                         5,
                         Math.abs(windowCorner.y - otherWindowCorner.y),
                         LrpType.PERIPHERAL,
                         transformedOct))
                 .peek(lrp -> lrp.setSmoothingAlpha(45))
                 .map(lrp -> {
                     LrpSeries lrpAndPeaksData = lrp.getAllSeriesData();
                     //process lrp to find second derivative peak right at edge
                     //of the vitrious/ILM boundary
                     List<XYDataItem> pointList = (List<XYDataItem>) lrpAndPeaksData.getLrpSeries().getItems();
                     AtomicInteger maxIntensity = new AtomicInteger(0);
                     for (int i = 0; i < 10; i++) {
                         if (pointList.get(i).getYValue() > maxIntensity.get()) {
                             maxIntensity.set((int) Math.round(pointList.get(i).getYValue()));
                         }
                     }
                     maxIntensity.incrementAndGet();
                     List<XYDataItem> secondDerivativePeaksList = (List<XYDataItem>) lrpAndPeaksData.getHiddenMaximaSeries().getItems();
                     XYDataItem ilmPeak = secondDerivativePeaksList.stream()
                                                                   .filter(p -> p.getYValue() > maxIntensity.get())
                                                                   .min(Comparator.comparingDouble(XYDataItem::getXValue))
                                                                   .orElse(secondDerivativePeaksList.get(0));
                     //smooth line by running it through a lowpass filter
                     //                     return new Point(lrp.getLrpCenterXPosition() - 3, (int) Math.round(butterworth.filter(ilmPeak.getXValue())));
                     return new Point(lrp.getLrpCenterXPosition(), (int) Math.round(ilmPeak.getXValue()));
                 })
                 .forEach(octPolyLine::add);

        OctPolyLine filteredLine = new OctPolyLine("filt-seg", 11111);
        filteredLine.add(octPolyLine.get(0));
        for (int i = 1; i < octPolyLine.size(); i++) {
            //            output[i] = (int) Math.round(output[i - 1] + smoothingAlpha * (intesityValues[i] - output[i - 1]));
            filteredLine.add(new Point(
                    octPolyLine.get(i).x - 2,
                    (int) Math.round(filteredLine.get(i - 1).y + 0.2D * (octPolyLine.get(i).y - filteredLine.get(i - 1).y))
            ));
        }

        return filteredLine;
    }
}
