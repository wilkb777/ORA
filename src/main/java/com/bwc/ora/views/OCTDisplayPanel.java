/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

import com.bwc.ora.collections.*;
import com.bwc.ora.ip.ImageUtils;
import com.bwc.ora.models.*;
import com.bwc.ora.models.exception.LRPBoundaryViolationException;
import com.bwc.ora.util.ChangeSupport;
import ij.ImagePlus;
import ij.process.ImageConverter;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MouseInputAdapter;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class OCTDisplayPanel extends JLabel {

    private final Oct oct = Oct.getInstance();
    private final DisplaySettings dispSettings = ModelsCollection.getInstance().getDisplaySettings();
    private final OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();
    private LrpCollection lrps = Collections.getInstance().getLrpCollection();
    private OctDrawnPointCollection drawnPointCollection = Collections.getInstance().getOctDrawnPointCollection();
    private OctDrawnLinesCollection octDrawnLinesCollection = Collections.getInstance().getOctDrawnLineCollection();
    private final LrpSettings lrpSettings = ModelsCollection.getInstance().getLrpSettings();
    private final Collections collections = Collections.getInstance();
    private BufferedImage cachedOct = null;

    private transient final ChangeSupport changeSupport = new ChangeSupport(this);

    private OCTDisplayPanel() {
        setAlignmentX(CENTER_ALIGNMENT);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        setFocusable(true);

        //register listener for changes to Oct for auto update on change
        oct.addPropertyChangeListener(evt -> updateDisplay(false, new ChangeEvent(evt)));

        //register listeners for changes to display settings for auto update on change
        dispSettings.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
            case DisplaySettings.PROP_DISPLAY_SCALE_BARS_ON_OCT:
            case DisplaySettings.PROP_SHOW_LINES_ON_OCT:
            case DisplaySettings.PROP_DISPLAY_SELECTOR_WINDOW:
            case DisplaySettings.PROP_SCALE_BAR_EDGE_BUFFER_WIDTH:
                updateDisplay(true, new ChangeEvent(evt));
            default:
                break;
            }
        });

        //register listeners for changes to oct settings for auto update on change
        octSettings.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
            case OctSettings.PROP_APPLY_CONTRAST_ADJUSTMENT:
            case OctSettings.PROP_APPLY_NOISE_REDUCTION:
            case OctSettings.PROP_DISPLAY_LOG_OCT:
            case OctSettings.PROP_SHARPEN_KERNEL_RADIUS:
            case OctSettings.PROP_SHARPEN_WEIGHT:
            case OctSettings.PROP_SMOOTHING_FACTOR:
                updateDisplay(false, new ChangeEvent(evt));
                break;
            case OctSettings.PROP_X_SCALE:
            case OctSettings.PROP_Y_SCALE:
                updateDisplay(true, new ChangeEvent(evt));
            default:
                break;
            }
        });

        //add listener to check to see if LRP selection should be displayed 
        lrps.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                updateDisplay(true, new ChangeEvent(e));
            }
        });

        //add listener to check to see if Drawn Points have changed
        drawnPointCollection.addCollectionEventListener(e -> {
            updateDisplay(true, new ChangeEvent(e));
        });

        //add listener to check to see if Drawn Points have changed
        octDrawnLinesCollection.addCollectionEventListener(e -> {
            System.out.println("Line added!");
            updateDisplay(true, new ChangeEvent(e));
        });

        //add listener to check for updates to lrp settings to change lrp
        lrpSettings.addPropertyChangeListener(e -> {
            if (lrps.getSelectedIndex() > -1) {
                switch (e.getPropertyName()) {
                case LrpSettings.PROP_LRP_HEIGHT:
                case LrpSettings.PROP_LRP_WIDTH:
                    updateDisplay(true, new ChangeEvent(e));
                default:
                    break;
                }
            }
        });

        //add click monitor for ensuring focus is gained when user clicks on OCT
        addMouseListener(new MouseInputAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });

        //listen for key events to move the LRP
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (Collections.getInstance().getLrpCollection().isEmpty()
                        || !hasFocus()) {
                    return;
                }
                int centerXPosition = Collections.getInstance().getLrpCollection().getSelectedValue().getLrpCenterXPosition();
                int centerYPosition = Collections.getInstance().getLrpCollection().getSelectedValue().getLrpCenterYPosition();
                switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    centerYPosition--;
                    break;
                case KeyEvent.VK_DOWN:
                    centerYPosition++;
                    break;
                case KeyEvent.VK_LEFT:
                    centerXPosition--;
                    break;
                case KeyEvent.VK_RIGHT:
                    centerXPosition++;
                    break;
                default:
                    break;
                }
                if (centerXPosition != Collections.getInstance().getLrpCollection().getSelectedValue().getLrpCenterXPosition()
                        || centerYPosition != Collections.getInstance().getLrpCollection().getSelectedValue().getLrpCenterYPosition()) {
                    Lrp newLrp;
                    try {
                        newLrp = new Lrp(
                                Collections.getInstance().getLrpCollection().getSelectedValue().getName(),
                                centerXPosition,
                                centerYPosition,
                                ModelsCollection.getInstance().getLrpSettings().getLrpWidth(),
                                ModelsCollection.getInstance().getLrpSettings().getLrpHeight(),
                                Collections.getInstance().getLrpCollection().getSelectedValue().getType());
                    } catch (LRPBoundaryViolationException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage() + " Try again.", "LRP generation error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Collections.getInstance().getLrpCollection().setLrp(newLrp, Collections.getInstance().getLrpCollection().getSelectedIndex());
                    updateDisplay(true, new ChangeEvent(e));
                }
            }
        });

    }

    public static OCTDisplayPanel getInstance() {

        return OCTDisplayPanelHolder.INSTANCE;
    }

    private static class OCTDisplayPanelHolder {

        private static final OCTDisplayPanel INSTANCE = new OCTDisplayPanel();
    }

    private void updateDisplay(boolean useCachedOct, ChangeEvent evt) {
        BufferedImage copyoct;
        if (useCachedOct) {
            copyoct = ImageUtils.deepCopy(cachedOct);
        } else {
            copyoct = oct.updateTransformedOct();
            cachedOct = copyoct;
        }
        if (copyoct == null) {
            return;
        }
        //create colorable BI for drawing to
        BufferedImage octBase = new BufferedImage(oct.getImageWidth(), oct.getImageHeight(), BufferedImage.TYPE_INT_ARGB);
        ImagePlus ip = new ImagePlus("", copyoct);
        ImageConverter ic = new ImageConverter(ip);
        ic.convertToRGB();
        copyoct = ip.getBufferedImage();
        copyoct.copyData(octBase.getRaster());

        //order overlay layers and draw to image accordingly
        collections.getOverlaysStream()
                   .filter(OCTOverlay::display)
                   .sorted(Comparator.comparingInt(OCTOverlay::getZValue))
                   .forEach((OCTOverlay overlay) -> overlay.drawOverlay(octBase));
        //finally set image to be drawn to the screen
        setIcon(new ImageIcon(octBase));
        //notify listeners that the Panel has updated the image
        changeSupport.fireStateChanged(evt);
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    /**
     * Determine if the supplied coordinate overlaps with the area of this panel
     * that displays the Oct image
     *
     * @param x
     * @param y
     * @param imageOffsetX
     * @param imageOffsetY
     * @return true if the coordinate is within the bounds of the displayed Oct,
     * false if it isn't or if the Oct image isn't displayed already
     */
    public boolean coordinateOverlapsOCT(int x, int y, int imageOffsetX, int imageOffsetY) {
        if (oct.getLogOctImage() != null) {
            boolean withinX = ((imageOffsetX + oct.getImageWidth()) - x) * (x - imageOffsetX) > -1;
            boolean withinY = ((imageOffsetY + oct.getImageHeight()) - y) * (y - imageOffsetY) > -1;
            return withinX && withinY;
        } else {
            return false;
        }
    }

    /**
     * Utility method used to translate a point (i.e. the location of an event,
     * like a mouse click) in the coordinate space of this panel to the
     * coordinate space of the Oct being displayed in the panel.
     *
     * @param p The point to be translated
     * @return translated point from Panel coordinates -> Oct coordinates, or
     * null if the Oct isn't present or the coordinate is outside of the Oct
     */
    public Point convertPanelPointToOctPoint(Point p) {
        int imageWidth = oct.getImageWidth();
        int panelWidth = this.getWidth();
        int imageOffsetX = 0;
        if (panelWidth > imageWidth) {
            imageOffsetX = (panelWidth - imageWidth) / 2;
        }

        int imageHeight = oct.getImageHeight();
        int panelHeight = this.getHeight();
        int imageOffsetY = 0;
        if (panelHeight > imageHeight) {
            imageOffsetY = (panelHeight - imageHeight) / 2;
        }

        if (!coordinateOverlapsOCT(p.x, p.y, imageOffsetX, imageOffsetY)) {
            return null;
        }

        return new Point(p.x - imageOffsetX, p.y - imageOffsetY);
    }
}
