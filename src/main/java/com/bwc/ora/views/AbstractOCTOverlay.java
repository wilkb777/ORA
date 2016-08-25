/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public abstract class AbstractOCTOverlay implements OCTOverlay{
    private final String name;
    private final int zValue;

    public AbstractOCTOverlay(String name, int zValue) {
        this.name = name;
        this.zValue = zValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getZValue() {
        return zValue;
    }
    
}
