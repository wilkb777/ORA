/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;
import javax.swing.JLabel;

/**
 *
 * @author Brandon
 */
public class MousePositionListeningLabel extends JLabel implements MouseMotionListener {

    public MousePositionListeningLabel(String text) {
        super(text);
    }

    private Consumer<MouseEvent> mouseMovedEventHandler = (MouseEvent e) -> {
        setText("Mouse Position");
    };

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseMovedEventHandler.accept(e);
    }

    public void setMouseMovedEventHandler(Consumer<MouseEvent> mouseMovedEventHandler) {
        this.mouseMovedEventHandler = mouseMovedEventHandler;
    }

}
