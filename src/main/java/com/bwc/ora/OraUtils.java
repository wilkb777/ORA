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
import com.bwc.ora.ip.segmentation.ILMsegmenter;
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
import java.util.Comparator;
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
        OCTDisplayPanel octDisplayPanel = OCTDisplayPanel.getInstance();
        LrpSettings lrpSettings = ModelsCollection.getInstance().getLrpSettings();
        DisplaySettings displaySettings = ModelsCollection.getInstance().getDisplaySettings();

        if (assisted) {
            JOptionPane.showMessageDialog(null,
                    "Click and drag a window on the OCT which\n"
                            + "contains the foveal pit, some of the vitrious,\n"
                            + "and some of the Bruch's membrane and choroid.\n"
                            + "ORA will the place a LRP at what it believes is\n"
                            + "the center of the foveal pit.\n"
                            + "\n"
                            + "Use the arrow keys to move the LRP if desired after placement.\n"
                            + "If any setting are adjusted while in this mode\n"
                            + "you'll have to click the mouse on the OCT to regain\n"
                            + "the ability to move the LRP with the arrow keys.", "Draw foveal pit window", JOptionPane.INFORMATION_MESSAGE);

            //allow the user to select region on screen where fovea should be found
            OctWindowSelector octWindowSelector = ModelsCollection.getInstance().getOctWindowSelector();

            MouseInputAdapter selectorMouseListener = new MouseInputAdapter() {
                Point firstPoint = null;
                Point secondPoint = null;
                Point lastWindowPoint = null;

                @Override
                public void mousePressed(MouseEvent e) {
                    Collections.getInstance().getOctDrawnLineCollection().clear();
                    Collections.getInstance().getLrpCollection().clearLrps();
                    if ((firstPoint = octDisplayPanel.convertPanelPointToOctPoint(e.getPoint())) != null) {
                        octWindowSelector.setDisplay(true);
                        displaySettings.setDisplaySelectorWindow(true);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    secondPoint = octDisplayPanel.convertPanelPointToOctPoint(e.getPoint());
                    octWindowSelector.setDisplay(false);
                    displaySettings.setDisplaySelectorWindow(false);
                    octDisplayPanel.removeMouseListener(this);
                    octDisplayPanel.removeMouseMotionListener(this);
                    OctPolyLine ilm = ILMsegmenter.segmentILM(firstPoint, secondPoint == null ? lastWindowPoint : secondPoint);
                    Collections.getInstance().getOctDrawnLineCollection().add(ilm);
                    //use ILM segmented line to find local minima and place LRP
                    Point maxYPoint = ilm.stream()
                                         .max(Comparator.comparingInt(p -> p.y))
                                         .orElse(ilm.get(0));
                    int fovealCenterX = (int) Math.round(ilm.stream()
                                                            .filter(p -> p.y == maxYPoint.y)
                                                            .mapToInt(p -> p.x)
                                                            .average()
                                                            .getAsDouble());

                    Lrp newLrp;
                    try {
                        newLrp = new Lrp("Fovea",
                                fovealCenterX,
                                Oct.getInstance().getImageHeight() / 2,
                                lrpSettings.getLrpWidth(),
                                lrpSettings.getLrpHeight(),
                                LrpType.FOVEAL);
                        lrpCollection.setLrps(Arrays.asList(newLrp));
                        octDisplayPanel.removeMouseListener(this);
                        if (buttonToEnable != null) {
                            buttonToEnable.setEnabled(true);
                        }
                    } catch (LRPBoundaryViolationException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage() + " Try again.", "LRP generation error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    Point dragPoint;
                    if ((dragPoint = octDisplayPanel.convertPanelPointToOctPoint(e.getPoint())) != null) {
                        lastWindowPoint = dragPoint;
                        int minX = Math.min(firstPoint.x, dragPoint.x);
                        int minY = Math.min(firstPoint.y, dragPoint.y);
                        int width = Math.max(firstPoint.x, dragPoint.x) - minX;
                        int height = Math.max(firstPoint.y, dragPoint.y) - minY;
                        octWindowSelector.setRect(minX, minY, width, height);
                        displaySettings.setDisplaySelectorWindow(false);
                        displaySettings.setDisplaySelectorWindow(true);
                    }
                }
            };

            octDisplayPanel.addMouseListener(selectorMouseListener);
            octDisplayPanel.addMouseMotionListener(selectorMouseListener);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Click on the OCT where the anchor LRP should go.\n"
                            + "Use the arrow keys to move the LRP.\n"
                            + "If any setting are adjusted while in this mode\n"
                            + "you'll have to click the mouse on the OCT to regain\n"
                            + "the ability to move the LRP with the arrow keys.", "Click anchor point", JOptionPane.INFORMATION_MESSAGE);
            //listen for the location on the screen where the user clicks, create LRP at location
            octDisplayPanel.addMouseListener(new MouseInputAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint;
                    if ((clickPoint = octDisplayPanel.convertPanelPointToOctPoint(e.getPoint())) != null) {
                        Lrp newLrp;
                        try {
                            newLrp = new Lrp("Fovea",
                                    clickPoint.x,
                                    clickPoint.y,
                                    lrpSettings.getLrpWidth(),
                                    lrpSettings.getLrpHeight(),
                                    LrpType.FOVEAL);
                            lrpCollection.setLrps(Arrays.asList(newLrp));
                            octDisplayPanel.removeMouseListener(this);
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
