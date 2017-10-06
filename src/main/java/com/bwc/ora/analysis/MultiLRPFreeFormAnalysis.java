package com.bwc.ora.analysis;

import com.bwc.ora.OraUtils;
import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.*;
import com.bwc.ora.models.exception.LRPBoundaryViolationException;
import com.bwc.ora.views.OCTDisplayPanel;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiLRPFreeFormAnalysis implements Analysis {
    @Override
    public void run() {
        OctSettings octSettings = ModelsCollection.getInstance().getOctSettings();
        if (!(octSettings.getyScale() > 0 && octSettings.getxScale() > 0)) {
            throw new AnalysisConditionsNotMetException("X and Y scale must be positive, non-zero decimal numbers.");
        }

        OCTDisplayPanel lrpPanel = OCTDisplayPanel.getInstance();
        LrpSettings lrpSettings = ModelsCollection.getInstance().getLrpSettings();
        JOptionPane.showMessageDialog(null,
                "Click on the OCT where each of the LRPs should go.\n"
                        + "Once generated use the arrow keys to move each LRP.\n"
                        + "If any setting are adjusted while in this mode\n"
                        + "you'll have to click the mouse on the OCT to regain\n"
                        + "the ability to move the LRP with the arrow keys.", "Click anchor point", JOptionPane.INFORMATION_MESSAGE);
        AtomicInteger clickCounter = new AtomicInteger(0);
        //listen for the location on the screen where the user clicks, create LRP at location
        lrpPanel.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint;
                if ((clickPoint = lrpPanel.convertPanelPointToOctPoint(e.getPoint())) != null) {
                    clickCounter.incrementAndGet();
                    Lrp newLrp;
                    try {
                        newLrp = new Lrp("LRP " + clickCounter.get() + " ",
                                clickPoint.x,
                                clickPoint.y,
                                lrpSettings.getLrpWidth(),
                                lrpSettings.getLrpHeight(),
                                LrpType.PERIPHERAL);
                        Collections.getInstance().getLrpCollection().addLrp(newLrp);
                        Collections.getInstance().getLrpCollection().setSelectedIndex(clickCounter.get()-1);
                        if (clickCounter.get() >= lrpSettings.getNumberOfLrp()) {
                            lrpPanel.removeMouseListener(this);
                        }
                    } catch (LRPBoundaryViolationException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage() + " Try again.", "LRP generation error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                }
            }
        });

        //move to analysis tab
        Collections.getInstance().getViewsCollection().setAnalysisTabAsSelectedTab();
    }
}
