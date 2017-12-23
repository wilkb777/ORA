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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PeakSegmentor {

    public static List<Point> getPeakPoints() {
    /*
        Apply image filtering and LRP analysis with the following settings:
        "lrpSettings": {
            "lrpWidth": 5,
            "lrpSmoothingFactor": 0,
            "lrpSeperationDistance": 1,
            "distanceUnitsInPixels": true
        },
        "octSettings": {
            "applyNoiseReduction": true,
            "applyContrastAdjustment": true,
            "displayLogOct": true,
            "smoothingFactor": 5.0
        }
     */

        List<Point> datr = IntStream.of(5, 6)
//                .parallel()
                .mapToObj(blurFactor -> Oct.getInstance().manualTransformOct(
                        true,
                        new ContrastAdjust(),
                        new NoiseReduction(),
                        new Blur(blurFactor))
                )
                .flatMap(transformedOct -> {
                    int lrpCenterYPosition = transformedOct.getHeight() / 2;
                    int startingX = 5;
                    int endingX = transformedOct.getWidth() - 5;
                    return IntStream.rangeClosed(startingX, endingX)
                            .mapToObj(lrpCenterXPosition -> new Lrp("data " + lrpCenterXPosition,
                                    lrpCenterXPosition,
                                    lrpCenterYPosition,
                                    5,
                                    transformedOct.getHeight() - 4,
                                    LrpType.PERIPHERAL,
                                    transformedOct))
                            .flatMap(lrp -> {
                                System.out.println("Processing " + lrp.getLrpCenterXPosition());
                                List<XYDataItem> peakpoints = (List<XYDataItem>) lrp.getAllSeriesData().getMaximaSeries().getItems();
                                return peakpoints.stream()
                                        .map(peakPoint -> new Point(lrp.getLrpCenterXPosition(), (int) Math.round(peakPoint.getXValue())));
                            });
                })
                .distinct()
                .collect(Collectors.toList());

        System.out.println("Number of peaks: "+datr.size());
        return datr;
    }


}
