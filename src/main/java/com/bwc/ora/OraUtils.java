/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora;

import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.LrpCollection;
import com.bwc.ora.io.ExportWriter;
import com.bwc.ora.io.TiffReader;
import com.bwc.ora.models.*;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.exception.LRPBoundaryViolationException;
import com.bwc.ora.views.OCTDisplayPanel;
import org.apache.commons.io.FileUtils;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class OraUtils {

    private static JFileChooser fc = new JFileChooser();
    private static final LrpCollection lrpCollection = Collections.getInstance().getLrpCollection();

    public static final ActionListener testAnalysisActionListener = evt -> {
        try {
            File tmpOct = File.createTempFile("test_oct_", ".tif");
            FileUtils.copyInputStreamToFile(OraUtils.class.getClassLoader().getResourceAsStream("test.tif"),
                    tmpOct);
            OraUtils.loadOctFromTiffFile(tmpOct, Oct.getInstance());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Image loading failed,"
                    + " reason: " + e.getMessage(), "Loading error!", JOptionPane.ERROR_MESSAGE
            );
        }
        Collections.getInstance().resetCollectionsForNewAnalysis();
        ModelsCollection.getInstance().resetSettingsToDefault();
    };

    public static final ActionListener newAnalysisActionListener = evt -> {
        try {
            //read in image and ready for analysis
            File tiffFile = OraUtils.selectFile(true, null, JFileChooser.FILES_ONLY, "Log OCT", "TIFF file", "tiff", "tif");
            if (tiffFile != null) {
                OraUtils.loadOctFromTiffFile(tiffFile, Oct.getInstance());
                Collections.getInstance().resetCollectionsForNewAnalysis();
                ModelsCollection.getInstance().resetSettingsToDefault();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Image loading failed,"
                    + " reason: " + ex.getMessage(), "Loading error!", JOptionPane.ERROR_MESSAGE);
        }
    };

    public static final ActionListener exportAnalysisActionListener = evt -> {
        try {
            //read in image and ready for analysis
            File exportDir = OraUtils.selectFile(true, null, JFileChooser.DIRECTORIES_ONLY, "Analysis export directory", null);
            if (exportDir != null) {
                ExportWriter.exportAnalysis(exportDir);
            }
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to export", ex);
            JOptionPane.showMessageDialog(null, "Analysis export failed,"
                    + " reason: " + ex.getMessage(), "Export error!", JOptionPane.ERROR_MESSAGE);
        }
    };

    public static void loadOctFromTiffFile(File octFile, Oct oct) throws IOException {
        oct.setFileName(octFile.getName());
        oct.setLogOctImage(TiffReader.readTiffImage(octFile));
    }

    public static File selectFile(boolean openDialog, Component parent, int selectorType, String selectDescription, String extensionDescription,
            String... extentions) {
        File prevLocation = fc.getSelectedFile() != null ? fc.getSelectedFile().getParentFile() : null;

        fc = new JFileChooser(prevLocation);
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(selectorType);
        if (selectorType == JFileChooser.FILES_ONLY) {
            fc.setFileFilter(new FileNameExtensionFilter(extensionDescription, extentions));
        }
        fc.setAcceptAllFileFilterUsed(false);
        fc.setApproveButtonText("Select");
        fc.setDialogTitle("Select " + selectDescription + "...");
        int returnVal = openDialog ? fc.showOpenDialog(parent) : fc.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (selectorType == JFileChooser.FILES_ONLY) {
                System.out.println("pwd: " + fc.getCurrentDirectory());
                return openDialog ? loadFile(fc.getSelectedFile()) : fc.getSelectedFile();
            } else if (selectorType == JFileChooser.DIRECTORIES_ONLY) {
                return loadDir(fc.getSelectedFile());
            } else {
                throw new IllegalArgumentException("Can't handle loading of files or directories at the same time!");
            }
        } else {
            return null;
        }
    }

    private static File loadFile(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Supplied input file does not exist @ " + file.getAbsolutePath() + ".");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("Supplied input file " + file.getAbsolutePath() + " is not a file!");
        }
        return file;
    }

    private static File loadDir(File file) {
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IllegalArgumentException("Supplied input " + file.getAbsolutePath() + " is not a directory!");
            }
        } else {
            throw new IllegalArgumentException("Supplied input directory does not exist @ " + file.getAbsolutePath() + ".");
        }
        return file;
    }

    /**
     * Create the anchor LRP. Does this by creating the LRP model and then adds
     * it to the LRP collections used by the application for storing LRPs for
     * analysis and display
     *
     * @param assisted use fovea finding algorithm or manual click to identify
     *                 fovea
     */
    public static void generateAnchorLrp(boolean assisted, JButton buttonToEnable) {
        OCTDisplayPanel lrpPanel = OCTDisplayPanel.getInstance();
        LrpSettings lrpSettings = ModelsCollection.getInstance().getLrpSettings();
        AnalysisSettings analysisSettings = ModelsCollection.getInstance().getAnalysisSettings();
        JOptionPane.showMessageDialog(null,
                "Click on the OCT where the " + (analysisSettings.getCurrentAnalysisMode() == AnalysisMode.FREE_FORM ? "" : "anchor ") + "LRP should go.\n"
                        + "Use the arrow keys to move the LRP.\n"
                        + "If any setting are adjusted while in this mode\n"
                        + "you'll have to click the mouse on the OCT to regain\n"
                        + "the ability to move the LRP with the arrow keys.", "Click anchor point", JOptionPane.INFORMATION_MESSAGE);
        if (assisted) {
            //todo add in assistive fovea finding
        } else {
            //listen for the location on the screen where the user clicks, create LRP at location
            lrpPanel.addMouseListener(new MouseInputAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint;
                    if ((clickPoint = lrpPanel.convertPanelPointToOctPoint(e.getPoint())) != null) {
                        Lrp newLrp;
                        try {
                            newLrp = new Lrp(analysisSettings.getCurrentAnalysisMode() == AnalysisMode.FREE_FORM ? "LRP" : "Fovea",
                                    clickPoint.x,
                                    clickPoint.y,
                                    lrpSettings.getLrpWidth(),
                                    lrpSettings.getLrpHeight(),
                                    LrpType.FOVEAL);
                            lrpCollection.setLrps(Arrays.asList(newLrp));
                            lrpPanel.removeMouseListener(this);
                            if (buttonToEnable != null) {
                                buttonToEnable.setEnabled(true);
                            }
                        } catch (LRPBoundaryViolationException e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage() + " Try again.", "LRP generation error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                    }
                }
            });

        }
    }

    public static void setLrpsForAnalysis() {
        Lrp fovealLrp = lrpCollection.getFovealLrp();
        if (fovealLrp == null) {
            return;
        }

        LrpSettings lrpSettings = ModelsCollection.getInstance().getLrpSettings();
        int octWidth = Oct.getInstance().getImageWidth();
        int distanceBetweenLrps = lrpSettings.getLrpSeperationDistanceInPixels();
        int numberOfLrpTotal = lrpSettings.getNumberOfLrp();
        int lrpPosToLeftOfFovea = fovealLrp.getLrpCenterXPosition();
        int lrpPosToRightOfFovea = fovealLrp.getLrpCenterXPosition();
        LinkedList<Lrp> lrps = new LinkedList<>();
        lrps.add(fovealLrp);
        while (lrps.size() < numberOfLrpTotal) {
            lrpPosToLeftOfFovea -= distanceBetweenLrps;
            lrps.push(new Lrp("Left " + ((lrps.size() + 1) / 2),
                    lrpPosToLeftOfFovea,
                    fovealLrp.getLrpCenterYPosition(),
                    lrpSettings.getLrpWidth(),
                    lrpSettings.getLrpHeight(),
                    LrpType.PERIPHERAL));

            lrpPosToRightOfFovea += distanceBetweenLrps;
            if (lrps.size() < numberOfLrpTotal) {
                lrps.add(new Lrp("Right " + (lrps.size() / 2),
                        lrpPosToRightOfFovea,
                        fovealLrp.getLrpCenterYPosition(),
                        lrpSettings.getLrpWidth(),
                        lrpSettings.getLrpHeight(),
                        LrpType.PERIPHERAL));
            }
        }

        boolean illegalPositionLrps = lrps.stream()
                                          .anyMatch(lrp -> lrp.getLrpCenterXPosition() - ((lrpSettings.getLrpWidth() - 1) / 2) < 0
                                                  || lrp.getLrpCenterXPosition() + ((lrpSettings.getLrpWidth() - 1) / 2) >= octWidth);

        if (illegalPositionLrps) {
            throw new IllegalArgumentException("Combination of LRP width, the number of LRPs and LRP " +
                    "separation distance cannot fit within the bounds of the OCT.");
        }
        lrpCollection.setLrps(lrps);
    }



}
