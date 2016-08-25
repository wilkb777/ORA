 /*
 * Copyright (C) 2014 Robert F Cooper <robert.f.cooper@marquette.edu>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bwc.ora.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

/**
 *
 * This class performs all of the tiff reading functions that you could require
 * in reading tiff images. NOTE: This program requires the JAI imageIO library,
 * or it will not function correctly!
 *
 * Version 1.0: Reads tiff images and tiff stacks. Version 1.1: Reads tiff
 * stacks to an ArrayList instead of an array of BufferedImages
 *
 */
public class TiffReader {

    public static BufferedImage readTiffImage(File file) throws IOException {

        Iterator tiffImReader = ImageIO.getImageReadersByFormatName("tiff");
        ImageReader tiffStkReader = (ImageReader) tiffImReader.next();
        BufferedImage tiffImage;
        int numPages = 0;

        try {

            tiffStkReader.setInput(ImageIO.createImageInputStream(file));
            numPages = tiffStkReader.getNumImages(true); // Allows the program to scan ahead to determine
            // the amount of images. Disable if slowdowns experienced.

        } catch (IOException ex) {
            System.out.println("Error opening file!");
            return null;
        }

        if (numPages > 1) {
            System.out.println("More than one image detected in this stack! Loading first image...");
        }

        try {
            tiffImage = tiffStkReader.read(0);
        } catch (IOException ex) {
            System.out.println("Error loading frame 0!");
            throw ex;
        } catch (NullPointerException ex) {
            System.out.println("Null Pointer Exception!");
            throw ex;
        }

        return tiffImage;
    }

    public static ArrayList<BufferedImage> readTiffStack(File file) throws IOException {

        Iterator tiffImReader = ImageIO.getImageReadersByFormatName("tiff");
        ImageReader tiffStkReader = (ImageReader) tiffImReader.next();
        ArrayList<BufferedImage> imageStk = new ArrayList<>();
        int numPages = 0;
        int i = 0;

        try {

            tiffStkReader.setInput(ImageIO.createImageInputStream(file));
            //numPages=tiffStkReader.getNumImages(true); // Allows the program to scan ahead to determine
            // the amount of images. Disable if slowdowns experienced.

        } catch (IOException ex) {
            System.out.println("Error opening file!");
            return null;
        }

        try {

            // Run until there is an index out of bounds exception thrown
            while (imageStk.add(tiffStkReader.read(i))) {
                i++;
            }

        } catch (NullPointerException ex) {
            System.out.println("NullPointerException during load of frame number " + i + "!");
            throw ex;
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Reached end of file... read " + i + " frames.");
            throw ex;
        }

        return imageStk;
    }

    public static BufferedImage readTiffImage(byte[] imgBytes) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imgBytes));
    }
}
