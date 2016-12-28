/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.ip;

import ij.process.ByteProcessor;

import java.awt.image.BufferedImage;

/**
 *
 * @author Brandon
 */
public class Blur implements FilterOperation {

    private double blurFactor;

    public Blur(double blurFactor) {
        this.blurFactor = blurFactor;
    }

    public double getBlurFactor() {
        return blurFactor;
    }

    public void setBlurFactor(double blurFactor) {
        this.blurFactor = blurFactor;
    }

    @Override
    public BufferedImage performOperation(BufferedImage bi) {
        ByteProcessor bp = new ByteProcessor(bi);
        bp.blurGaussian(blurFactor);
        return bp.getBufferedImage();
    }

}
