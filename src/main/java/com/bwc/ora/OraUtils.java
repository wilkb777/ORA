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
import com.bwc.ora.models.Lrp;
import com.bwc.ora.models.LrpSettings;
import com.bwc.ora.models.LrpType;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.Oct;
import com.bwc.ora.views.LrpDisplayFrame;
import com.bwc.ora.views.OCTDisplayPanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
            //read in image and ready for analysis
            OraUtils.loadOctFromTiffFile(new File(OraUtils.class.getClassLoader().getResource("KS_10238_OS_L_7_90_05_529disp_reg_fr1-25_AL21p35.tif").toURI()), Oct.getInstance());
        } catch (IOException | URISyntaxException ex) {
            JOptionPane.showMessageDialog(null, "Image loading failed,"
                    + " reason: " + ex.getMessage(), "Loading error!", JOptionPane.ERROR_MESSAGE
            );
        }
    };

    public static final ActionListener newAnalysisActionListener = evt -> {
        try {
            //read in image and ready for analysis
            File tiffFile = OraUtils.selectFile(null, JFileChooser.FILES_ONLY, "Log OCT", "TIFF file", "tiff", "tif");
            if (tiffFile != null) {
                OraUtils.loadOctFromTiffFile(tiffFile, Oct.getInstance());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Image loading failed,"
                    + " reason: " + ex.getMessage(), "Loading error!", JOptionPane.ERROR_MESSAGE);
        }
    };

    public static final ActionListener exportAnalysisActionListener = evt -> {
        try {
            //read in image and ready for analysis
            File exportDir = OraUtils.selectFile(null, JFileChooser.DIRECTORIES_ONLY, "Analysis export directory", null);
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

    public static File selectFile(Component parent, int selectorType, String selectDescription, String extensionDescription, String... extentions) {
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
        if (fc.getSelectedFile() != null && fc.getSelectedFile().isFile()) {
            fc.setCurrentDirectory(fc.getSelectedFile().getParentFile());
        }
        int returnVal = fc.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (selectorType == JFileChooser.FILES_ONLY) {
                return loadFile(fc.getSelectedFile());
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
        if (file.exists()) {
            if (!file.isFile()) {
                throw new IllegalArgumentException("Supplied input file " + file.getAbsolutePath() + " is not a file!");
            }
        } else {
            throw new IllegalArgumentException("Supplied input file does not exist @ " + file.getAbsolutePath() + ".");
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
    public static void generateAnchorLrp(boolean assisted) {
        OCTDisplayPanel lrpPanel = OCTDisplayPanel.getInstance();
        LrpSettings lrpSettings = ModelsCollection.getInstance().getLrpSettings();
        if (assisted) {
            //todo add in assistive fovea finding
        } else {
            //listen for the location on the screen where the user clicks, create LRP at location
            lrpPanel.addMouseListener(new MouseInputAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint;
                    if ((clickPoint = lrpPanel.convertPanelPointToOctPoint(e.getPoint())) != null) {
                        lrpCollection.setLrps(Arrays.asList(
                                new Lrp("Fovea",
                                        clickPoint.x,
                                        lrpSettings.getLrpWidth(),
                                        lrpSettings.getLrpHeight(),
                                        LrpType.FOVEAL)
                        ));
                        lrpCollection.setSelectedIndex(0);
                        lrpPanel.removeMouseListener(this);
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
            if (lrpPosToLeftOfFovea >= (lrpSettings.getLrpWidth() - 1) / 2) {
                lrps.push(new Lrp("Left " + ((lrps.size() + 1) / 2),
                        lrpPosToLeftOfFovea,
                        lrpSettings.getLrpWidth(),
                        lrpSettings.getLrpHeight(),
                        LrpType.PERIPHERAL));
            }

            lrpPosToRightOfFovea += distanceBetweenLrps;
            if (lrps.size() < numberOfLrpTotal
                    && lrpPosToRightOfFovea < octWidth - ((lrpSettings.getLrpWidth() - 1) / 2)) {
                lrps.add(new Lrp("Right " + (lrps.size() / 2),
                        lrpPosToRightOfFovea,
                        lrpSettings.getLrpWidth(),
                        lrpSettings.getLrpHeight(),
                        LrpType.PERIPHERAL));
            }
        }
        System.out.println("Number of lrps: " + lrps.size());
        lrpCollection.setLrps(lrps);
    }

    /**
     * Recursively enable or disable the specified container and all of its
     * children components
     *
     * @param component
     * @param enabled
     */
    public static void setEnabled(Component component, boolean enabled) {
        component.setEnabled(enabled);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setEnabled(child, enabled);
            }
        }
    }

    public static void runAnalysis() {
        System.out.println("Disabling panels...");
        //disable settings panels so no changes to the settings can be made
        Collections.getInstance().getViewsCollection().disableViewsInputs();

        /*
         Based on the current settings generate the approriate number of LRPs for the user to analyze
         */
        System.out.println("Setting Lrps...");
        setLrpsForAnalysis();
    }
}
