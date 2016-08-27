/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.models;

import com.bwc.ora.ip.ImageUtils;
import com.bwc.ora.views.OCTOverlay;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.IntStream;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class Lrp extends Rectangle implements OCTOverlay {

    private final String title;
    private final LrpType type;
    private boolean display = false;
    private static final Oct oct = Oct.getInstance();
    private final int lrpCenterXPosition;
    private List<XYPointerAnnotation> annotations = new LinkedList<>();

    public Lrp(String title, int x, int width, int height, LrpType type) {
        super(x - ((width - 1) / 2), (oct.getImageHeight() / 2) - (height / 2), width, height);
        lrpCenterXPosition = x;
        this.title = title;
        this.type = type;

        //add listener to check for updates to lrp settings to change lrp
        LrpSettings lrpSettings = Models.getInstance().getLrpSettings();
        lrpSettings.addFirstPriorityPropertyChangeListener(e -> {
            switch (e.getPropertyName()) {
                case LrpSettings.PROP_LRP_SMOOTHING_FACTOR:
                    break;
                case LrpSettings.PROP_LRP_HEIGHT:
                    this.height = lrpSettings.getLrpHeight();
                    this.y = (oct.getImageHeight() / 2) - (this.height / 2);
                    break;
                case LrpSettings.PROP_LRP_WIDTH:
                    this.width = lrpSettings.getLrpWidth();
                    this.x = lrpCenterXPosition - ((this.width - 1) / 2);
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Get an array containing the gray scale values of the LRP arranged from
     * smallest y-value to greatest y-value (i.e. top to bottom in the image).
     * The pixel data is taken from the transformed OCT with all appropriately
     * applied transformations. Each call to this method will go and grab the
     * pixel data allowing for updated intensity data to be grabbed immediately.
     *
     * @return
     */
    private int[] getIntensityValues() {

        int[] rgbArray = oct.getTransformedOct().getRGB(x, y, width, height, null, 0, width);

        return IntStream.range(0, height)
                .map(scanY
                        -> (int) Math.round(
                                Arrays.stream(rgbArray, width * scanY, width * (scanY + 1))
                                .map(ImageUtils::calculateGrayScaleValue)
                                .average()
                                .orElse(0)
                        )
                )
                .toArray();
    }

    public LrpSeries getAllSeriesData() {
        //create series for the LRP
        XYSeries lrpSeries = getLrpDataSeries();

        //create series for the local maxima in the LRP
        XYSeries maximaSeries = findMaximums(lrpSeries, "Local Maxima");

        //create series for peaks identified by the second derivative
        XYSeries hiddenMaxPoints = getMaximumsWithHiddenPeaks(lrpSeries, "Second Derivative Maxima");

        //create series for each full-width half-max for each local maxima peak
        List<XYSeries> fwhm = getFWHMForLRPPeaks(maximaSeries, lrpSeries);

        return new LrpSeries(lrpSeries, maximaSeries, hiddenMaxPoints, fwhm);
    }

    public XYSeries getLrpDataSeries() {
        XYSeries lrp = new XYSeries(getName() + " LRP");
        lrp.setKey(getName());

        int[] intensityValues = getIntensityValues();
        for (int i = 0; i < intensityValues.length; i++) {
            lrp.add(i + y, intensityValues[i]);
        }

        return lrp;
    }

    public static XYSeries findMaximums(XYSeries lrpSeries, String title) {
        XYSeries lrpMaxPoints = new XYSeries(title);
        XYDataItem leftPeakPoint = new XYDataItem(0, 0);
        int leftPeakPointIndex = 0;
        XYDataItem rightPeakPoint = new XYDataItem(0, 0);
        boolean first = true;
        int index = -1;
        List<XYDataItem> pointList = (List<XYDataItem>) lrpSeries.getItems();
        for (XYDataItem point : pointList) {
            index++;
            if (first) {
                leftPeakPoint = point;
                leftPeakPointIndex = index;
                first = false;
                continue;
            }
            if (leftPeakPoint.getYValue() < point.getYValue()) {
                leftPeakPoint = point;
                leftPeakPointIndex = index;
                rightPeakPoint = point;
            } else if (leftPeakPoint.getYValue() == point.getYValue()) {
                rightPeakPoint = point;
            } else {
                //determine if we are coming down off of a peak by looking two points behind the current point
                if (leftPeakPointIndex > 0) {
                    XYDataItem prev = pointList.get(leftPeakPointIndex - 1);
                    //if two points back has a Y value that is less than or equal to the left peak point
                    //then we have found the end of the peak and we can process as such
                    if (prev.getYValue() <= leftPeakPoint.getYValue()) {
                        double peakx = rightPeakPoint.getXValue() - ((rightPeakPoint.getXValue() - leftPeakPoint.getXValue()) / 2D);
                        lrpMaxPoints.add(peakx, leftPeakPoint.getY());
                    }
                }
                leftPeakPoint = point;
                leftPeakPointIndex = index;
                rightPeakPoint = point;
            }
        }

        return lrpMaxPoints;
    }

    public static XYSeries findMaxAndMins(XYSeries lrpSeries, String title) {
        XYSeries lrpMaxPoints = new XYSeries("");
        ((List<XYDataItem>) lrpSeries.getItems()).forEach(p -> {
            lrpMaxPoints.add(p.getXValue(), Math.abs(p.getYValue()));
        });
        return findMaximums(lrpMaxPoints, title);
    }

    public List<XYSeries> getFWHMForLRPPeaks(XYSeries lrpPeaks, XYSeries lrpSeries) {
        LinkedList<XYSeries> seriesList = new LinkedList<>();
        List<XYDataItem> pointList = (List<XYDataItem>) lrpSeries.getItems();
        List<XYDataItem> peakList = (List<XYDataItem>) lrpPeaks.getItems();
        //iterate through the peaks, process FWHM for each peak
        for (XYDataItem peak : peakList) {
            //grab index of the closest point to the peak
            int peakIndex = -1;
            for (XYDataItem pnt : pointList) {
                peakIndex++;
                if (Math.abs(pnt.getXValue() - peak.getXValue()) < 0.6D) {
                    break;
                }
            }
            //calculate point with Y value of valley to the left of peak
            XYDataItem leftValleyPoint = null;
            ListIterator<XYDataItem> it = pointList.listIterator(peakIndex);
            double prevY = peak.getYValue();
            while (it.hasPrevious()) {
                XYDataItem leftPoint = it.previous();
                if (leftPoint.getYValue() <= prevY) {
                    prevY = leftPoint.getYValue();
                    leftValleyPoint = leftPoint;
                } else {
                    break;
                }
            }
            //calculate point with Y value of valley to the right of peak
            XYDataItem rightValleyPoint = null;
            it = pointList.listIterator(peakIndex);
            prevY = peak.getYValue();
            while (it.hasNext()) {
                XYDataItem rightPoint = it.next();
                if (rightPoint.getYValue() <= prevY) {
                    prevY = rightPoint.getYValue();
                    rightValleyPoint = rightPoint;
                } else {
                    break;
                }
            }
            //determine half max Y value
            double halfMaxYValue;
            if (rightValleyPoint.getYValue() == leftValleyPoint.getYValue()) {
                halfMaxYValue = peak.getYValue() - ((peak.getYValue() - leftValleyPoint.getYValue()) / 2D);
            } else if (rightValleyPoint.getYValue() > leftValleyPoint.getYValue()) {
                halfMaxYValue = peak.getYValue() - ((peak.getYValue() - rightValleyPoint.getYValue()) / 2D);
            } else {
                halfMaxYValue = peak.getYValue() - ((peak.getYValue() - leftValleyPoint.getYValue()) / 2D);
            }
            //determine the X value on both sides of the peak that corresponds to the half max Y value
            double leftX = pointList.get(0).getXValue(), rightX = pointList.get(pointList.size() - 1).getXValue();
            XYDataItem prevPoint = pointList.get(peakIndex);
            it = pointList.listIterator(peakIndex);
            while (it.hasPrevious()) {
                XYDataItem leftPoint = it.previous();
                if (leftPoint.getYValue() == halfMaxYValue) {
                    leftX = leftPoint.getXValue();
                    break;
                } else {
                    if (leftPoint.getYValue() < halfMaxYValue) {
//                        System.out.println("Left X for peak (" + peak.getXValue() + "," + peak.getYValue() + "): ");
                        leftX = calculateXFromYForLineWithTwoPoints(leftPoint, prevPoint, halfMaxYValue);
//                        System.out.println("    Left X: (" + leftX + "," + halfMaxYValue + "): ");
                        break;
                    } else {
                        prevPoint = leftPoint;
                    }
                }
            }
            prevPoint = pointList.get(peakIndex);
            it = pointList.listIterator(peakIndex);
            while (it.hasNext()) {
                XYDataItem rightPoint = it.next();
                if (rightPoint.getYValue() == halfMaxYValue) {
                    rightX = rightPoint.getXValue();
                    break;
                } else {
                    if (rightPoint.getYValue() < halfMaxYValue) {
//                        System.out.println("Right X for peak (" + peak.getXValue() + "," + peak.getYValue() + "): ");
                        rightX = calculateXFromYForLineWithTwoPoints(rightPoint, prevPoint, halfMaxYValue);
//                        System.out.println("    Right X: (" + leftX + "," + halfMaxYValue + "): ");
                        break;
                    } else {
                        prevPoint = rightPoint;
                    }
                }
            }
            //store the two points for the half max full width line for this peak
            XYSeries peakSeries = new XYSeries("(" + peak.getXValue() + "," + peak.getYValue() + ")FWHM");
            peakSeries.add(leftX, halfMaxYValue);
            peakSeries.add(rightX, halfMaxYValue);
            seriesList.add(peakSeries);
        }
        return seriesList;
    }

    private static double calculateXFromYForLineWithTwoPoints(XYDataItem pt1, XYDataItem pt2, double y) {
//        System.out.println("    P1: (" + pt1.getXValue() + "," + pt1.getYValue() + ")");
//        System.out.println("    P2: (" + pt2.getXValue() + "," + pt2.getYValue() + ")");
        //calculate slope 
        double slope = (pt1.getYValue() - pt2.getYValue()) / (pt1.getXValue() - pt2.getXValue());
//        System.out.println("    Slope: " + slope);
        //calculate y value at y-intercept (aka b)
        double yint = pt1.getYValue() - (slope * pt1.getXValue());
//        System.out.println("    Y-int: " + yint);
        //return 
        return (y - yint) / slope;
    }

    /**
     * Get the local maximums from a collection of Points.
     *
     * @param lrpSeries
     * @param seriesTitle
     * @return
     */
    public static XYSeries getMaximumsWithHiddenPeaks(XYSeries lrpSeries, String seriesTitle) {
        XYSeries maxPoints = new XYSeries(seriesTitle);

        //convert to x and y coordinate arrays
        double[][] xyline = lrpSeries.toArray();

        //use a spline interpolator to converts points into an equation
        UnivariateInterpolator interpolator = new SplineInterpolator();
        UnivariateFunction function = interpolator.interpolate(xyline[0], xyline[1]);

        // create a differentiator using 5 points and 0.01 step
        FiniteDifferencesDifferentiator differentiator
                = new FiniteDifferencesDifferentiator(5, 0.01);

        // create a new function that computes both the value and the derivatives
        // using DerivativeStructure
        UnivariateDifferentiableFunction completeF = differentiator.differentiate(function);

        // now we can compute the value and its derivatives
        // here we decided to display up to second order derivatives,
        // because we feed completeF with order 2 DerivativeStructure instances
        //find local minima in second derivative, these indicate the peaks (and hidden peaks)
        //of the input
        for (double x = xyline[0][0] + 1; x < xyline[0][xyline[0].length - 1] - 1; x += 0.5) {
            DerivativeStructure xDSc = new DerivativeStructure(1, 2, 0, x);
            DerivativeStructure xDSl = new DerivativeStructure(1, 2, 0, x - 0.5);
            DerivativeStructure xDSr = new DerivativeStructure(1, 2, 0, x + 0.5);
            DerivativeStructure yDSc = completeF.value(xDSc);
            DerivativeStructure yDSl = completeF.value(xDSl);
            DerivativeStructure yDSr = completeF.value(xDSr);
            double c2d = yDSc.getPartialDerivative(2);
            if (c2d < yDSl.getPartialDerivative(2) && c2d < yDSr.getPartialDerivative(2)) {
                maxPoints.add((int) Math.round(x), yDSc.getValue());
            }
        }

        return maxPoints;
    }

    public LrpType getType() {
        return type;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public void drawOverlay(BufferedImage baseImg) {
        Graphics2D graphics = baseImg.createGraphics();
        graphics.setColor(Color.green);
        graphics.draw(this);
        System.out.println("Drawing selection on OCT...");
    }

    @Override
    public int getZValue() {
        return 1;
    }

    @Override
    public boolean display() {
        return display;
    }

    @Override
    public void setDisplay(boolean display) {
        this.display = display;
    }

    public int getLrpCenterXPosition() {
        return lrpCenterXPosition;
    }

    public List<XYPointerAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<XYPointerAnnotation> annotations) {
        this.annotations = annotations;
    }

}
