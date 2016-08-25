/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

import java.awt.image.BufferedImage;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public interface OCTOverlay {

    public String getName();

    public void drawOverlay(BufferedImage baseImg);

    public int getZValue();

    /**
     * A method indicating whether the overlay should be displayed or not.
     *
     * @return true if it should be displayed on the OCT false if it should not
     * be drawn.
     */
    public boolean display();
    
    /**
     * Sets whether or not the overlay should be displayed
     * @param display 
     */
    public void setDisplay(boolean display);
}
