/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora;

import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.ViewsCollection;
import com.bwc.ora.io.TiffReader;
import com.bwc.ora.models.DisplaySettings;
import com.bwc.ora.models.Models;
import com.bwc.ora.views.MousePositionListeningLabel;
import com.bwc.ora.views.toolbars.LrpSettingsPanel;
import com.bwc.ora.views.OCTDisplayPanel;
import com.bwc.ora.views.OraMenuBar;
import com.bwc.ora.views.toolbars.OctSettingsPanel;
import com.bwc.ora.models.Oct;
import com.bwc.ora.views.LrpDisplayFrame;
import com.bwc.ora.views.toolbars.AnalysisPanel;
import com.bwc.ora.views.toolbars.ToolbarUtilities;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class Ora extends JFrame {

    private OraMenuBar menuBar;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //set the look and feel of the application (use system L&F if possible)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            //fail silently and allow application to use default
        }

        //bring up UI
        SwingUtilities.invokeLater(() -> {
            Ora ui = new Ora("OCT Reflectivity Analytics");
            ui.setVisible(true);
            LrpDisplayFrame lrpDisp = LrpDisplayFrame.getInstance();
        });
        
    }

    int cntr = 0;

    public Ora(String title) throws HeadlessException {
        super(title);

        //config container properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(300, 50));
        setIconImage(new ImageIcon(getClass().getResource("/logo.png")).getImage());
        setLocationByPlatform(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        OCTDisplayPanel disp = OCTDisplayPanel.getInstance();

        //set up the menu bar
        menuBar = new OraMenuBar();
        setJMenuBar(menuBar);

        //set up info panel
        JPanel infoPanel = new JPanel();
        BoxLayout infoPanelLayout = new BoxLayout(infoPanel, BoxLayout.LINE_AXIS);
        infoPanel.setLayout(infoPanelLayout);

        //set up mouse position listener on info panel
        JPanel mousePositionPanel = new JPanel();
        mousePositionPanel.setBorder(new TitledBorder(new EtchedBorder(), "Position"));
        MousePositionListeningLabel positionLabel = new MousePositionListeningLabel("(0,0)");
        positionLabel.setFont(positionLabel.getFont().deriveFont(ToolbarUtilities.TOOLBAR_SUBCOMPONENT_FONT_SIZE));
        mousePositionPanel.add(positionLabel);
        disp.addMouseMotionListener(positionLabel);
        positionLabel.setMouseMovedEventHandler(e -> {
            Point octPoint = disp.convertPanelPointToOctPoint(e.getPoint());
            positionLabel.setText(octPoint == null ? "(0,0)" : "(" + octPoint.x + "," + octPoint.y + ")");
        });
        infoPanel.add(mousePositionPanel);
        infoPanel.add(Box.createHorizontalStrut(5));

        //set up mouse position distance to fovea listener on info panel
        JPanel distanceToFoveaPanel = new JPanel();
        distanceToFoveaPanel.setBorder(new TitledBorder(new EtchedBorder(), "To Fovea"));
        MousePositionListeningLabel distanceLabel = new MousePositionListeningLabel("N/A");
        distanceLabel.setFont(distanceLabel.getFont().deriveFont(ToolbarUtilities.TOOLBAR_SUBCOMPONENT_FONT_SIZE));
        distanceToFoveaPanel.add(distanceLabel);
        disp.addMouseMotionListener(distanceLabel);
        distanceLabel.setMouseMovedEventHandler(e -> {
            Point octPoint = disp.convertPanelPointToOctPoint(e.getPoint());
            distanceLabel.setText(octPoint == null ? "N/A" : "N/A");
        });
        infoPanel.add(distanceToFoveaPanel);
        infoPanel.add(Box.createHorizontalStrut(5));

        //set up the file name display, connect to listen for file name changes
        JPanel currentOctNamePanel = new JPanel();
        currentOctNamePanel.setBorder(new TitledBorder(new EtchedBorder(), "Current OCT"));
        JLabel fileNameLabel = new JLabel("N/A");
        currentOctNamePanel.add(fileNameLabel);
        fileNameLabel.setFont(fileNameLabel.getFont().deriveFont(ToolbarUtilities.TOOLBAR_SUBCOMPONENT_FONT_SIZE));
        Oct oct = Oct.getInstance();
        oct.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if (evt.getPropertyName().equals(Oct.PROP_LOG_OCT)) {
                fileNameLabel.setText(oct.getFileName());
            }
        });
        infoPanel.add(currentOctNamePanel);
        infoPanel.add(Box.createHorizontalGlue());

        //set the file name display to change display based on boolean in settings model
        DisplaySettings settings = Models.getInstance().getDisplaySettings();
        settings.addPropertyChangeListener(evt -> {
            if (DisplaySettings.PROP_DISPLAY_FILE_NAME.equals(evt.getPropertyName())) {
                currentOctNamePanel.setVisible(settings.isDisplayFileName());
            }
        });

        add(infoPanel);
        add(Box.createVerticalStrut(4));

        //set up the image display
        disp.addChangeListener((ChangeEvent e) -> {
            pack();
        });
        add(disp);

        //add settings tab pane
        JTabbedPane settingsTabPane = new JTabbedPane(JTabbedPane.LEFT);
        settingsTabPane.setFont(settingsTabPane.getFont().deriveFont(13F));

        //add tool bars
        LrpSettingsPanel lrpSettingsPanel = new LrpSettingsPanel();
        OctSettingsPanel octSettingsPanel = new OctSettingsPanel();
        AnalysisPanel controlsPanel = new AnalysisPanel();
        settingsTabPane.addTab(octSettingsPanel.getTitle(), octSettingsPanel);
        settingsTabPane.addTab(lrpSettingsPanel.getTitle(), lrpSettingsPanel);
        settingsTabPane.addTab(controlsPanel.getTitle(), controlsPanel);
        int maxToolBarWidth = Stream.of(lrpSettingsPanel, octSettingsPanel)
                .mapToInt(p -> (int) Math.round(p.getPreferredSize().getWidth()))
                .max()
                .getAsInt();
        settingsTabPane.setPreferredSize(new Dimension(maxToolBarWidth, (int) (settingsTabPane.getPreferredSize().height * 1.2D)));
        add(settingsTabPane);
        
        //add toolbars to managed views for interacting with elsewhere in the application
        ViewsCollection viewsCollection = Collections.getInstance().getViewsCollection();
        viewsCollection.add(lrpSettingsPanel);
        viewsCollection.add(octSettingsPanel);

        //ready for display of the window.
        pack();
    }

    final void loadImage(File tiffFile) {
        BufferedImage img = null;
        try {
            //read in image and keep track of the image for later use
            img = TiffReader.readTiffImage(tiffFile);
            Oct.getInstance().setLogOctImage(img);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Image loading failed for " + tiffFile.getAbsolutePath()
                    + ", reason: " + ex.getMessage(), "Loading error!", JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
