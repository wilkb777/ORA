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
public interface FilterOperation {

    /**
     * Performs the specific image operation on the image represented by the
     * supplied float processor.
     *
     * @param bi the image to apply the given operation to
     * @return the modified image
     */
    public BufferedImage performOperation(BufferedImage bi);
}
