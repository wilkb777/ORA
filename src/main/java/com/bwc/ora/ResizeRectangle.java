package com.bwc.ora;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

public class ResizeRectangle extends JPanel {
    private int SIZE = 8;
    private Rectangle2D[] points = { new Rectangle2D.Double(50, 50,SIZE, SIZE), new Rectangle2D.Double(150, 100,SIZE, SIZE) };
    Rectangle2D s = new Rectangle2D.Double();

    ShapeResizeHandler ada = new ShapeResizeHandler();

    public ResizeRectangle() {
        addMouseListener(ada);
        addMouseMotionListener(ada);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < points.length; i++) {
            g2.fill(points[i]);
        }
        s.setRect(points[0].getCenterX(), points[0].getCenterY(),
                Math.abs(points[1].getCenterX()-points[0].getCenterX()),
                Math.abs(points[1].getCenterY()- points[0].getCenterY()));

        g2.draw(s);
    }

    class ShapeResizeHandler extends MouseAdapter {
        Rectangle2D r = new Rectangle2D.Double(0,0,SIZE,SIZE);
        private int pos = -1;
        public void mousePressed(MouseEvent event) {
            Point p = event.getPoint();

            for (int i = 0; i < points.length; i++) {
                if (points[i].contains(p)) {
                    pos = i;
                    return;
                }
            }
        }

        public void mouseReleased(MouseEvent event) {
            pos = -1;
        }

        public void mouseDragged(MouseEvent event) {
            if (pos == -1)
                return;

            points[pos].setRect(event.getPoint().x,event.getPoint().y,points[pos].getWidth(),
                    points[pos].getHeight());
            repaint();
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Resize Rectangle");

        frame.add(new ResizeRectangle());
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
