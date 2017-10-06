/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views.toolbars;

import com.bwc.ora.analysis.AnalysisUtils;
import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.LrpCollection;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.AnalysisMode;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class AnalysisPanel extends JPanel {

    private final JButton nextLrpButton = new JButton("Next LRP");
    private final JButton prevLrpButton = new JButton("Previous LRP");
    private final JButton restartButton = new JButton("Restart Analysis");
    private final JScrollPane analysisNavPane = new JScrollPane(Collections.getInstance().getLrpCollection());
    private final String title = "Analysis Controls";

    public AnalysisPanel() {
        super();
        //set layout for the analysis panel
        setLayout(new BorderLayout(5, 5));

        //set up panel containing navigation buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(prevLrpButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(nextLrpButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(restartButton);

        //add componenets to the panel for display
        add(buttonPanel, BorderLayout.PAGE_START);
        add(analysisNavPane, BorderLayout.CENTER);

        connectToModel();
    }

    private void connectToModel() {
        LrpCollection lrpCollection = Collections.getInstance().getLrpCollection();

        //connect next and previous buttons for navigation during analysis
        nextLrpButton.addActionListener(evt -> lrpCollection.selectNextLrp());
        prevLrpButton.addActionListener(evt -> lrpCollection.selectPrevLrp());

        //setup restart analysis button
        restartButton.addActionListener(evt -> {
            Collections.getInstance().resetCollectionsForNewAnalysis();
//            AnalysisUtils.runAnalysis(ModelsCollection.getInstance().getAnalysisSettings().getCurrentAnalysisMode());
        });

    }

    public String getTitle() {
        return title;
    }

}
