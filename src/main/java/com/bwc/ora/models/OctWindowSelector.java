package com.bwc.ora.models;

import com.bwc.ora.views.OCTOverlay;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OctWindowSelector extends Rectangle implements OCTOverlay {

    private boolean dispaly = false;
    final static float dash1[] = { 5.0f };
    final static BasicStroke dashed = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

    @Override public String getName() {
        return "window selector";
    }

    @Override public void drawOverlay(BufferedImage baseImg) {
        Graphics2D grfx = baseImg.createGraphics();
        grfx.setColor(Color.MAGENTA);
        grfx.setStroke(dashed);
        grfx.draw(this);
    }

    @Override public int getZValue() {
        return 10000;
    }

    @Override public boolean display() {
        return dispaly;
    }

    @Override public void setDisplay(boolean display) {
        this.dispaly = display;
    }
}
