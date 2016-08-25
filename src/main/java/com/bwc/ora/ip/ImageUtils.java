/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.ip;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class ImageUtils {

    /**
     * Determine the gray scale value of a pixel based on its RGB value.
     *
     * @param rgb
     * @return
     */
    public static int calculateGrayScaleValue(int rgb) {
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        return (r + g + b) / 3;
    }
    
    /**
     * Determine the RGB value of a pixel based on its gray scale value.
     *
     * @param gsv
     * @return
     */
    public static int calculateRGBValue(int gsv) {
        return (gsv << 16) | (gsv << 8) | gsv;
    }

    /**
     * Given a set of RGB values calculate the average grayscale value
     *
     * @param rgbValues
     * @return
     */
    public static int getAvgGrayScaleValue(int[] rgbValues) {
        return (int) Math.round(
                Arrays.stream(rgbValues)
                .map(ImageUtils::calculateGrayScaleValue)
                .average()
                .orElse(0)
        );
    }
    
    /**
     * Convert the supplied image to a 2D pixel array such that an (X,Y) value
     * indexes as array[x][y].
     *
     * Credit for this method goes to Stack Overflow user Mota and their post
     * here:
     * http://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
     * for this implementation.
     *
     * This method will return the red, green and blue values directly for each
     * pixel, and if there is an alpha channel it will add the alpha value.
     * Using this method is harder in terms of calculating indices, but is much
     * faster than using getRGB to build this same array.
     *
     * @param image
     * @return
     */
    public static int[][] convertTo2D(BufferedImage image) {

        final int width = image.getWidth();
        final int height = image.getHeight();

        int[][] result = new int[width][height];
        for (int x = 0; x < result.length; x++) {
            for (int y = 0; y < result[x].length; y++) {
                result[x][y] = image.getRGB(x, y);
            }
        }

        return result;
    }

    /**
     * Create a deep copy of the supplied buffered image
     *
     * @param bi
     * @return
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
