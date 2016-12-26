package com.bwc.ora.models;

import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.views.AbstractOCTOverlay;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Brandon on 12/26/2016.
 */
public class ScaleBar extends AbstractOCTOverlay {

    public ScaleBar(String name, int zValue) {
        super(name, zValue);
    }

    @Override
    public void drawOverlay(BufferedImage baseImg) {
        final OctSettings settings = ModelsCollection.getInstance().getOctSettings();
        final DisplaySettings displaySettings = ModelsCollection.getInstance().getDisplaySettings();

        int pixInX = (int) Math.round((1D / settings.getxScale()) * 250D);
        int pixInY = (int) Math.round((1D / settings.getyScale()) * 250D);

        int width = baseImg.getWidth();
        int height = baseImg.getHeight();

        Graphics2D grfx = baseImg.createGraphics();
        grfx.setColor(Color.white);
        grfx.fillRect(width - (displaySettings.getScaleBarEdgeBufferWidth() + pixInX),
                height - (displaySettings.getScaleBarEdgeBufferWidth() + 3), pixInX, 3);
        grfx.fillRect(width - displaySettings.getScaleBarEdgeBufferWidth() - 3,
                height - (displaySettings.getScaleBarEdgeBufferWidth() + pixInY), 3, pixInY);
        grfx.drawString("250\u00B5m",
                width - (displaySettings.getScaleBarEdgeBufferWidth() + pixInX + 10),
                height - 5);
        AffineTransform orig = grfx.getTransform();
        grfx.rotate(-Math.PI / 2);
        grfx.drawString("250\u00B5m", -(height - (displaySettings.getScaleBarEdgeBufferWidth() - 3)), width - 5);
        grfx.setTransform(orig);
    }

    @Override
    public boolean display() {
        final DisplaySettings displaySettings = ModelsCollection.getInstance().getDisplaySettings();
        final OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();
        return displaySettings.isDisplayScaleBarsOnOct()
                && octSettings.getyScale() > 0D
                && octSettings.getxScale() > 0D;
    }

    @Override
    public void setDisplay(boolean display) {
        ModelsCollection.getInstance().getDisplaySettings().setDisplayScaleBarsOnOct(display);
    }
}
