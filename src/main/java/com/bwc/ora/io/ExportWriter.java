package com.bwc.ora.io;

import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.LrpCollection;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.Lrp;
import com.bwc.ora.models.LrpSettings;
import com.bwc.ora.models.Oct;
import com.bwc.ora.models.OctSettings;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Brandon on 9/7/2016.
 */
public class ExportWriter {

    private static final DecimalFormat df = new DecimalFormat("#.######");

    private static void writeSettingsCsv(File outdir) throws FileNotFoundException {
        LrpSettings lrpSettings = ModelsCollection.getInstance().getLrpSettings();
        OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();
        LrpCollection lrps = Collections.getInstance().getLrpCollection();

        try (PrintWriter pw = new PrintWriter(new File(outdir, "settings_" + Oct.getInstance().getFileNameWithoutExtension() + ".csv"))) {
            //add lrp properties
            pw.println(LrpSettings.PROP_LRP_HEIGHT + "," + String.valueOf(lrpSettings.getLrpHeight()));
            pw.println(LrpSettings.PROP_LRP_WIDTH + "," + String.valueOf(lrpSettings.getLrpWidth()));
            pw.println(LrpSettings.PROP_LRP_SEPERATION_DISTANCE + "," + df.format(lrpSettings.getLrpSeperationDistance()));
            pw.println("distance unit" + "," + (lrpSettings.isDistanceUnitsInPixels() ? "pixels" : "microns"));
            pw.println(LrpSettings.PROP_LRP_SMOOTHING_FACTOR + "," + df.format(lrpSettings.getLrpSmoothingFactor()));
            pw.println(LrpSettings.PROP_NUMBER_OF_LRP + "," + String.valueOf(lrpSettings.getNumberOfLrp()));
            pw.println("LRP center X positions,"
                    + lrps.stream().map(Lrp::getLrpCenterXPosition).map(String::valueOf).collect(Collectors.joining(",")));
            pw.println("LRP center Y positions,"
                    + lrps.stream().map(Lrp::getLrpCenterYPosition).map(String::valueOf).collect(Collectors.joining(",")));

            //add oct properties
            pw.println(OctSettings.PROP_X_SCALE + "," + df.format(octSettings.getxScale()));
            pw.println(OctSettings.PROP_Y_SCALE + "," + df.format(octSettings.getyScale()));
            pw.println(OctSettings.PROP_SHARPEN_WEIGHT + "," + df.format(octSettings.getSharpenWeight()));
            pw.println(OctSettings.PROP_SHARPEN_KERNEL_RADIUS + "," + df.format(octSettings.getSharpenKernelRadius()));
            pw.println(OctSettings.PROP_SMOOTHING_FACTOR + "," + df.format(octSettings.getSmoothingFactor()));
            pw.println("contrast adjusted" + "," + (octSettings.isApplyContrastAdjustment() ? "yes" : "no"));
            pw.println("noise reduction" + "," + (octSettings.isApplyNoiseReduction() ? "yes" : "no"));
            pw.println("log or linear OCT" + "," + (octSettings.isDisplayLogOct() ? "log" : "linear"));
        }
    }

    private static void writePeaksCsvs(File outdir) {
        LrpCollection lrps = Collections.getInstance().getLrpCollection();

        lrps.stream().forEach(lrp -> {
            try (PrintWriter pw = new PrintWriter(new File(outdir, "peaks_lrp-" + lrp.getName().replaceAll("\\s", "-") + "_" + Oct.getInstance().getFileNameWithoutExtension() + ".csv"))) {
                lrp.getAnnotations().forEach(peak -> pw.println(peak.getText() + "," + df.format(peak.getX()) + "," + df.format(peak.getY())));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Error in writing peaks file", e);
            }
        });

    }

    private static void writeLrpsCsvs(File outdir) {
        LrpCollection lrps = Collections.getInstance().getLrpCollection();

        lrps.stream().forEach(lrp -> {
            try (PrintWriter pw = new PrintWriter(new File(outdir, "lrp-" + lrp.getName().replaceAll("\\s", "-") + "_" + Oct.getInstance().getFileNameWithoutExtension() + ".csv"))) {
                ((List<XYDataItem>) lrp.getLrpDataSeries().getItems())
                        .forEach(point -> pw.println(df.format(point.getX()) + "," + df.format(point.getY())));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Error in writing peaks file", e);
            }
        });

    }

    private static void writePeak2PeakCsvs(File outdir) {
        LrpCollection lrps = Collections.getInstance().getLrpCollection();
        OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();

        lrps.stream().forEach(lrp -> {
            try (PrintWriter pw = new PrintWriter(new File(outdir, "peak2peak_lrp-" + lrp.getName().replaceAll("\\s", "-") + "_" + Oct.getInstance().getFileNameWithoutExtension() + ".csv"))) {
                LinkedList<XYPointerAnnotation> peaks = new LinkedList<>(lrp.getAnnotations());
                while (peaks.size() > 1) {
                    XYPointerAnnotation peak1 = peaks.removeFirst();
                    peaks.forEach(peak2 -> pw.println(peak1.getText() + "," + peak2.getText() + "," + df.format(Math.abs(peak1.getX() - peak2.getX()) * octSettings.getyScale())));
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Error in writing peaks file", e);
            }
        });

    }

    private static void writeFwhmCsvs(File outdir) {
        LrpCollection lrps = Collections.getInstance().getLrpCollection();
        OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();

        lrps.stream().forEach(lrp -> {
            try (PrintWriter pw = new PrintWriter(new File(outdir, "fwhm_lrp-" + lrp.getName().replaceAll("\\s", "-") + "_" + Oct.getInstance().getFileNameWithoutExtension() + ".csv"))) {
                List<XYSeries> fwhmSeries = lrp.getAllSeriesData().getFwhmSeries();
                lrp.getAnnotations().forEach(peak -> {
                    for (XYSeries fwhm : fwhmSeries) {
                        if (peak.getX() > fwhm.getMinX() && peak.getX() < fwhm.getMaxX()) {
                            pw.println(peak.getText() + "," + df.format(Math.abs(fwhm.getMinX() - fwhm.getMaxX()) * octSettings.getyScale()));
                            break;
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Error in writing peaks file", e);
            }
        });
    }

    private static void writeOct(File outdir) {
        Oct oct = Oct.getInstance();
        OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();
        String octType = octSettings.isDisplayLogOct() ? "log" : "linear";
        //save capture of OCT as displayed to user
        File screenFile = new File(outdir, oct.getFileNameWithoutExtension() + "_" + octType + "_analysis_oct.png");
        try {
            ImageIO.write(oct.updateTransformedOct(), "png", screenFile);
        } catch (IOException e) {
            throw new RuntimeException("failed to write modified oct");
        }
    }

    public static void exportAnalysis(File outputLocation) throws FileNotFoundException {
        File analysisOutdir = new File(outputLocation, "analysis_" + Oct.getInstance().getFileNameWithoutExtension());
        if (!analysisOutdir.exists()) {
            if (!analysisOutdir.mkdir()) {
                throw new IllegalArgumentException("Can't create output in specified location");
            }
        }

        writeFwhmCsvs(analysisOutdir);
        writeLrpsCsvs(analysisOutdir);
        writePeak2PeakCsvs(analysisOutdir);
        writePeaksCsvs(analysisOutdir);
        writeSettingsCsv(analysisOutdir);
        writeOct(analysisOutdir);
    }
}
