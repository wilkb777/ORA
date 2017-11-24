package com.bwc.ora.models;

import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.ip.FilterOperation;
import com.bwc.ora.ip.ImageUtils;
import ij.ImagePlus;
import ij.process.ImageConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author Brandon
 */
public class Oct {

    private BufferedImage logOctImage;
    private String fileName;
    private BufferedImage linearOctImage;
    private transient static final double logScale = 255D / Math.log(255D);
    private transient static final OctSettings settings = ModelsCollection.getInstance().getOctSettings();

    public transient static final String PROP_LOG_OCT = "oct";

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private BufferedImage transformedOct = null;

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Creates a simple data structure for caching the log and linear versions
     * of an OCT image.
     */
    private Oct() {
        logOctImage = null;
        linearOctImage = null;
        fileName = null;
    }

    public static Oct getInstance() {
        return OCTHolder.INSTANCE;
    }

    private static class OCTHolder {

        private static final Oct INSTANCE = new Oct();
    }

    public void setLogOctImage(BufferedImage logOctImage) {
        //segmentation as well as some image operations can only be done on 
        //8-bit gray scale images, ensure image is in useable format for application
        System.out.println("type:" + logOctImage.getType());
        ImagePlus ip = new ImagePlus("", logOctImage);
        if (ip.getBitDepth() != 8) {
            ImageConverter ic = new ImageConverter(ip);
            ic.convertToGray8();
        }
        //store log Oct image
        BufferedImage oldoct = this.logOctImage;
        this.logOctImage = ip.getBufferedImage();
        //store liner scale version of Oct
        linearOctImage = getLinearOCT(this.logOctImage);
        propertyChangeSupport.firePropertyChange(PROP_LOG_OCT, oldoct, this.logOctImage);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the logrithmic version of the Oct
     *
     * @return
     */
    public BufferedImage getLogOctImage() {
        return logOctImage;
    }

    /**
     * Get the linear version of the Oct
     *
     * @return
     */
    public BufferedImage getLinearOctImage() {
        return linearOctImage;
    }

    /**
     * Get the OCT as transformed based on the OCT settings set in {@link OctSettings}. Make sure to call
     * {@link Oct#updateTransformedOct()} before calling this method in cases where it is known that the OCT settings
     * may have been changed but the transformed OCT hasn't been updated yet.
     *
     * @return
     */
    public BufferedImage getTransformedOct() {
        return transformedOct;
    }

    public BufferedImage updateTransformedOct() {
        if (logOctImage == null) {
            return null;
        }
        transformedOct = manualTransformOct(settings.isDisplayLogOct(),
                settings.getActiveOperations().toArray(new FilterOperation[] {}));
        return transformedOct;
    }

    public BufferedImage manualTransformOct(boolean log, FilterOperation... filters) {
        //grab the approriate OCT to transform and make a deep copy
        BufferedImage octCopy = log ? ImageUtils.deepCopy(logOctImage) : ImageUtils.deepCopy(linearOctImage);
        //apply image filter operations
        for (FilterOperation op : filters) {
            octCopy = op.performOperation(octCopy);
        }
        return octCopy;
    }

    /**
     * Get the width of the Oct image
     *
     * @return
     */
    public int getImageWidth() {
        return logOctImage == null ? 0 : logOctImage.getWidth();
    }

    /**
     * Get the height of the Oct image
     *
     * @return
     */
    public int getImageHeight() {
        return logOctImage == null ? 0 : logOctImage.getHeight();
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileNameWithoutExtension() {
        return FilenameUtils.removeExtension(fileName);
    }

    /**
     * Get a deep copy of the supplied image after each pixel has been converted
     * to a linear value.
     *
     * @param logOCT
     * @return
     */
    private BufferedImage getLinearOCT(BufferedImage logOCT) {
        BufferedImage retImg = ImageUtils.deepCopy(logOCT);
        int w = retImg.getWidth();
        int h = retImg.getHeight();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = retImg.getRGB(j, i);
                retImg.setRGB(j, i, exp(pixel));
            }
        }
        return retImg;
    }

    /**
     * Calculate the exponential of an RGB value to obtain the original linear
     * value.
     *
     * @param rgb
     * @return
     */
    private int exp(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);
        int ret = (rgb & 0xFF000000) | (((int) Math.exp((double) r / logScale)) << 16) | (((int) Math.exp((double) g / logScale)) << 8) | ((int) Math.exp(
                (double) b / logScale));
        return ret;
    }

    /**
     * Calculate the logrithmic value of an assumed linear RGB value.
     *
     * @param rgb
     * @return
     */
    private int ln(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);
        int ret = (rgb & 0xFF000000) | (((int) (Math.log(r) * logScale)) << 16) | (((int) (Math.log(g) * logScale)) << 8) | ((int) (Math.log(b) * logScale));
        return ret;
    }

}
