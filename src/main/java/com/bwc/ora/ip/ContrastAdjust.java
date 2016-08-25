/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.ip;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ContrastAdjust implements FilterOperation {

    @Override
    public BufferedImage performOperation(BufferedImage bi) {
        //first set up scaling parameters
        double A = (double) findMinGrayScaleValue(bi);
        double B = (double) findMaxGrayScaleValue(bi);
        System.out.println("Min Gray scale value: " + A + ", max Gray scale value: " + B);
        double a = 0D;
        double b = 255D;
        //normalize pixels to new range
        int[][] pixels = ImageUtils.convertTo2D(bi);
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                int pixel = ImageUtils.calculateGrayScaleValue(pixels[x][y]);
                int norm = (int) Math.round(a + (((double) pixel - A) * (b - a) / (B - A)));
                bi.setRGB(x, y, ImageUtils.calculateRGBValue(norm));
            }
        }
        return bi;
    }

    private int findMaxGrayScaleValue(BufferedImage bi) {
        return Arrays.stream(bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), null, 0, bi.getWidth()))
                .map(ImageUtils::calculateGrayScaleValue)
                .max().getAsInt();
    }

    private int findMinGrayScaleValue(BufferedImage bi) {
        return Arrays.stream(bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), null, 0, bi.getWidth()))
                .map(ImageUtils::calculateGrayScaleValue)
                .min().getAsInt();
    }

}
