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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

/**
 *
 * @author Robert F Cooper
 */
public class TiffWriter {

    public static void writeTiffImage(File file, BufferedImage tiffImage) {

        Iterator tiffWriterFormat = ImageIO.getImageWritersByFormatName("tiff");
        ImageWriter tiffImWriter = (ImageWriter) tiffWriterFormat.next();

        int numPages = 0;

        try {
            tiffImWriter.setOutput(ImageIO.createImageOutputStream(file));

        } catch (IOException ex) {
            System.out.println("Error opening file!");

        }

        if (numPages > 1) {
            System.out.println("More than one image detected in this stack! Loading first image...");
        }

        try {
            tiffImWriter.write(tiffImage);
        } catch (IOException ex) {
            System.out.println("Error writing frame number " + 0 + "!");

        } catch (NullPointerException ex) {
            System.out.println("Null Pointer Exception!");

        }

    }

    public static byte[] writeTiffImageToByteArray(BufferedImage tiffImage) {
        byte[] imageInByte = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(tiffImage, "tiff", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
        } catch (IOException ie) {

        }
        return imageInByte;
    }

}
