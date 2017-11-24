package com.bwc.ora.ip.segmentation;

import com.bwc.ora.ip.Blur;
import com.bwc.ora.ip.ContrastAdjust;
import com.bwc.ora.ip.NoiseReduction;
import com.bwc.ora.models.*;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        Oct.getInstance().manualTransformOct(
                true,
                new Blur(2.0),
                new ContrastAdjust(),
                new NoiseReduction()
        );

        int lrpCenterYPosition = Math.min(windowCorner.y, otherWindowCorner.y) + (Math.abs(windowCorner.y - otherWindowCorner.y) / 2);
        OctPolyLine octPolyLine = new OctPolyLine("ILM segment", 10000);
        IntStream.rangeClosed(Math.min(windowCorner.x, otherWindowCorner.x), Math.max(windowCorner.x, otherWindowCorner.x))
                 .mapToObj(lrpCenterXPosition -> new Lrp(null,
                         lrpCenterXPosition,
                         lrpCenterYPosition,
                         5,
                         Math.abs(windowCorner.y - otherWindowCorner.y),
                         LrpType.PERIPHERAL))
                 .map(lrp -> {
                     lrp.setSmoothingAlpha(45);
                     LrpSeries lrpAndPeaksData = lrp.getAllSeriesData();
                     //process lrp to find second derivative peak right at edge
                     //of the vitrious/ILM boundary
                     return new Point();
                 })
                 .forEach(octPolyLine::add);

        return octPolyLine;
    }
}
