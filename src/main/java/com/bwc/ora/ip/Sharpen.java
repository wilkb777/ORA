/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.ip;

import java.awt.image.BufferedImage;

/**
 *
 * @author Brandon
 */
public class Sharpen implements FilterOperation {

    private double sharpenSigma = 0;
    private float sharpenWeight = 0;
    private final Blur blur;

    /**
     * Create sharpen operation for sharpening images.
     *
     * @param sharpenSigma "Radius (Sigma)" is the standard deviation (blur
     * radius) of the Gaussian blur that is subtracted.
     * @param sharpenWeight Mask Weight" determines the strength of filtering,
     * where "Mask Weight"=1 would be an infinite weight of the high-pass
     * filtered image that is added.
     */
    public Sharpen(double sharpenSigma, float sharpenWeight) {
        this.blur = new Blur(sharpenSigma);
        this.sharpenSigma = sharpenSigma;
        this.sharpenWeight = sharpenWeight;
    }

    @Override
    public BufferedImage performOperation(BufferedImage bi) {

        BufferedImage sharpBi = blur.performOperation(ImageUtils.deepCopy(bi));
        for (int y = 0; y < sharpBi.getHeight(); y++) {
            for (int x = 0; x < sharpBi.getWidth(); x++) {
                float oldPix = ImageUtils.calculateGrayScaleValue(bi.getRGB(x, y));
                float newPix = ImageUtils.calculateGrayScaleValue(sharpBi.getRGB(x, y));
                float sharpPix = (oldPix - sharpenWeight * newPix) / (1f - sharpenWeight);
                if (sharpPix > 255) {
                    sharpBi.setRGB(x, y, ImageUtils.calculateRGBValue(255));
                } else if (sharpPix < 0) {
                    sharpBi.setRGB(x, y, ImageUtils.calculateRGBValue(0));
                } else {
                    sharpBi.setRGB(x, y, ImageUtils.calculateRGBValue((int) sharpPix));
                }
            }
        }
        return sharpBi;
    }

}
