/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class OctDrawnPoint implements OCTOverlay {

    private final String title;
    private final Point pointToDraw;
    private boolean display = true;

    public OctDrawnPoint(String title, Point pointToDraw) {
        this.title = title;
        this.pointToDraw = pointToDraw;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public void drawOverlay(BufferedImage baseImg) {
        Graphics2D graphics = baseImg.createGraphics();
        graphics.setColor(Color.red);
        graphics.drawRect(pointToDraw.x - 1, pointToDraw.y - 1, 3, 3);
    }

    @Override
    public int getZValue() {
        return 1;
    }

    @Override
    public boolean display() {
        return display;
    }

    @Override
    public void setDisplay(boolean display) {
        this.display = display;
    }

}
