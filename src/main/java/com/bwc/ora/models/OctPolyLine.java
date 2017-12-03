package com.bwc.ora.models;

import com.bwc.ora.views.OCTOverlay;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class OctPolyLine extends LinkedList<Point> implements OCTOverlay {

    private final String name;
    private final int zvalue;
    private boolean disp = true;

    public OctPolyLine(String name, int zvalue) {
        this.name = name;
        this.zvalue = zvalue;
    }

    @Override public String getName() {
        return name;
    }

    @Override public void drawOverlay(BufferedImage baseImg) {
        if (!disp) {
            return;
        }
        Graphics2D graphics = baseImg.createGraphics();
        graphics.setColor(Color.MAGENTA);
        graphics.drawPolyline(
                this.stream().mapToInt(p -> p.x).toArray(),
                this.stream().mapToInt(p -> p.y).toArray(),
                this.size()
        );
    }

    @Override public int getZValue() {
        return zvalue;
    }

    @Override public boolean display() {
        return disp;
    }

    @Override public void setDisplay(boolean display) {
        disp = display;
    }

}
