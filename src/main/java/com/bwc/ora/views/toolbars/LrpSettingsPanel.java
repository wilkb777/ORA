/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views.toolbars;

import com.bwc.ora.OraUtils;
import com.bwc.ora.models.LrpSettings;
import com.bwc.ora.collections.ModelsCollection;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class LrpSettingsPanel extends JPanel {

    private final JComboBox<Integer> lrpWidthField;
    private final JFormattedTextField lrpSmoothingFactorField;
    private final JFormattedTextField numLrpField;
    private final JFormattedTextField lrpSeparationDistanceField;
    private final JRadioButton lrpSeparationDistanceInMicrons;
    private final JRadioButton lrpSeparationDistanceInPixels;
    private final ButtonGroup lrpSeparationDistanceUnitsButtonGroup;
    private final ToolbarTextField lrpHeightTextField;
    private final JButton anchorLrpButton;
    private final JButton runAnalysisButton;
    private final JSlider lrpSmoothingSlider;
    private final String title;

    public LrpSettingsPanel() {
        super();
        LrpSettings settings = ModelsCollection.getInstance().getLrpSettings();
        this.lrpHeightTextField = new ToolbarTextField(settings.getLrpHeight());
        this.lrpSeparationDistanceUnitsButtonGroup = new ButtonGroup();
        this.lrpSeparationDistanceInPixels = new JRadioButton("pixels", settings.isDistanceUnitsInPixels());
        this.lrpSeparationDistanceInMicrons = new JRadioButton("microns", !settings.isDistanceUnitsInPixels());
        this.lrpSeparationDistanceField = new ToolbarTextField(settings.getLrpSeperationDistance());
        this.numLrpField = new ToolbarTextField(settings.getNumberOfLrp());
        this.lrpSmoothingFactorField = new ToolbarTextField(settings.getLrpSmoothingFactor());
        int[] range = settings.getLrpSmoothingRange();
        this.lrpSmoothingSlider = new JSlider(SwingConstants.HORIZONTAL, range[0], range[1], (int) settings.getLrpSmoothingFactor());
        this.lrpWidthField = new JComboBox<>(settings.getLrpWidthOptions());
        this.lrpWidthField.setSelectedItem(settings.getLrpWidth());
        this.runAnalysisButton = new JButton("Run Analysis");
        this.anchorLrpButton = new JButton("<html><center>Generate</center><center>Anchor LRP</center></html>");
        init();
        connectToModel();
        title = "LRP Settings";
    }

    private void init() {
        //set layout manager
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        //Lay out the text controls and the labels for the left column
        GridBagLayout gridbag = new GridBagLayout();
        JPanel leftColumnPanel = new JPanel();
        leftColumnPanel.setLayout(gridbag);

        ArrayList<Component> labelsList = new ArrayList<>();
        ArrayList<Component> componentsList = new ArrayList<>();

        labelsList.add(new JLabel());
        componentsList.add(anchorLrpButton);

        JLabel l = new JLabel("LRP Width ");
        labelsList.add(l);
        l.setLabelFor(lrpWidthField);
        componentsList.add(lrpWidthField);
        ((JLabel) lrpWidthField.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        l = new JLabel("LRP Height ");
        labelsList.add(l);
        l.setLabelFor(lrpHeightTextField);
        componentsList.add(lrpHeightTextField);

        l = new JLabel("# LRP ");
        labelsList.add(l);
        l.setLabelFor(numLrpField);
        componentsList.add(numLrpField);

        //add labels and inputs to left column
        ToolbarUtilities.addLabelTextRows(labelsList, componentsList, gridbag, leftColumnPanel);

        //Lay out the text controls and the labels for the right column
        gridbag = new GridBagLayout();
        JPanel rightColumnPanel = new JPanel();
        rightColumnPanel.setLayout(gridbag);

        labelsList = new ArrayList<>();
        componentsList = new ArrayList<>();

        JPanel radioBtnPanel = new JPanel();
        BoxLayout layout = new BoxLayout(radioBtnPanel, BoxLayout.LINE_AXIS);
        radioBtnPanel.setLayout(layout);
        l = new JLabel("LRP spacing ");
        labelsList.add(l);
        l.setLabelFor(lrpSeparationDistanceField);
        lrpSeparationDistanceField.setMaximumSize(new Dimension(50, lrpSeparationDistanceField.getMaximumSize().height));
        radioBtnPanel.add(lrpSeparationDistanceField);
        radioBtnPanel.add(Box.createHorizontalStrut(7));
        radioBtnPanel.add(lrpSeparationDistanceInMicrons);
//        radioBtnPanel.add(Box.createHorizontalStrut(3));
        radioBtnPanel.add(lrpSeparationDistanceInPixels);
        lrpSeparationDistanceUnitsButtonGroup.add(lrpSeparationDistanceInMicrons);
        lrpSeparationDistanceUnitsButtonGroup.add(lrpSeparationDistanceInPixels);
        l.setLabelFor(radioBtnPanel);
        componentsList.add(radioBtnPanel);

        l = new JLabel("LRP Smoothing ");
        labelsList.add(l);
        l.setLabelFor(lrpSmoothingFactorField);
        componentsList.add(ToolbarUtilities.getPanelWithSliderAndTextField(lrpSmoothingFactorField, lrpSmoothingSlider));

        labelsList.add(new JLabel());
        runAnalysisButton.setEnabled(false);
        componentsList.add(runAnalysisButton);

        //add labels and inputs to left column
        ToolbarUtilities.addLabelTextRows(labelsList, componentsList, gridbag, rightColumnPanel);

        //add columns to panel
        add(Box.createHorizontalStrut(ToolbarUtilities.TOOLBAR_HORIZONTAL_STRUT_WIDTH));
        add(leftColumnPanel);
        add(Box.createHorizontalStrut(ToolbarUtilities.TOOLBAR_HORIZONTAL_STRUT_WIDTH));
        add(new JSeparator(SwingConstants.VERTICAL));
        add(Box.createHorizontalStrut(ToolbarUtilities.TOOLBAR_HORIZONTAL_STRUT_WIDTH));
        add(rightColumnPanel);

        //set font size of subcomponents
        ToolbarUtilities.setSubcomponentFontSize(ToolbarUtilities.TOOLBAR_SUBCOMPONENT_FONT_SIZE, leftColumnPanel);
        ToolbarUtilities.setSubcomponentFontSize(ToolbarUtilities.TOOLBAR_SUBCOMPONENT_FONT_SIZE, rightColumnPanel);
    }

    public String getTitle() {
        return title;
    }

    private void connectToModel() {
        LrpSettings settings = ModelsCollection.getInstance().getLrpSettings();

        lrpWidthField.addActionListener(evt -> settings.setLrpWidth((int) ((JComboBox) evt.getSource()).getSelectedItem()));
        settings.addPropertyChangeListener(evt -> {
            if (LrpSettings.PROP_LRP_WIDTH.equals(evt.getPropertyName())) {
                lrpWidthField.setSelectedItem(evt.getNewValue());
            }
        });

        lrpHeightTextField.addPropertyChangeListener("value", evt -> settings.setLrpHeight((int) evt.getNewValue()));
        settings.addPropertyChangeListener(evt -> {
            if (LrpSettings.PROP_LRP_HEIGHT.equals(evt.getPropertyName())) {
                lrpHeightTextField.setValue(evt.getNewValue());
            }
        });

        numLrpField.addPropertyChangeListener("value", evt -> settings.setNumberOfLrp((int) evt.getNewValue()));
        settings.addPropertyChangeListener(evt -> {
            if (LrpSettings.PROP_NUMBER_OF_LRP.equals(evt.getPropertyName())) {
                numLrpField.setValue(evt.getNewValue());
            }
        });

        lrpSmoothingFactorField.addPropertyChangeListener("value", evt -> settings.setLrpSmoothingFactor((int) evt.getNewValue()));
        lrpSmoothingSlider.addChangeListener(evt -> {
            JSlider source = (JSlider) evt.getSource();
            settings.setLrpSmoothingFactor((int) source.getValue());
        });
        settings.addPropertyChangeListener(evt -> {
            if (LrpSettings.PROP_LRP_SMOOTHING_FACTOR.equals(evt.getPropertyName())) {
                lrpSmoothingFactorField.setValue(evt.getNewValue());
                lrpSmoothingSlider.setValue((int) evt.getNewValue());
            }
        });

        lrpSeparationDistanceField.addPropertyChangeListener("value", evt -> settings.setLrpSeperationDistance((double) evt.getNewValue()));
        settings.addPropertyChangeListener(evt -> {
            if (LrpSettings.PROP_LRP_SEPERATION_DISTANCE.equals(evt.getPropertyName())) {
                lrpSeparationDistanceField.setValue(evt.getNewValue());
            }
        });

        lrpSeparationDistanceInMicrons.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                settings.setDistanceUnitsInPixels(false);
            }
        });
        settings.addPropertyChangeListener(evt -> {
            if (LrpSettings.PROP_DISTANCE_UNITS_IN_PIXELS.equals(evt.getPropertyName())) {
                lrpSeparationDistanceInMicrons.setSelected(!((boolean) evt.getNewValue()));
            }
        });

        lrpSeparationDistanceInPixels.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                settings.setDistanceUnitsInPixels(true);
            }
        });
        settings.addPropertyChangeListener(evt -> {
            if (LrpSettings.PROP_DISTANCE_UNITS_IN_PIXELS.equals(evt.getPropertyName())) {
                lrpSeparationDistanceInPixels.setSelected((boolean) evt.getNewValue());
            }
        });

        anchorLrpButton.addActionListener(evt -> {
            int option = JOptionPane.showOptionDialog(null, "Anchor method to use?",
                    "Anchoring", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null,
                    new Object[]{"Assisted Fovea Finding", "Manual"}, "Manual");
            switch (option) {
                case JOptionPane.YES_OPTION:
                    break;
                case JOptionPane.NO_OPTION:
                    OraUtils.generateAnchorLrp(false);
                    runAnalysisButton.setEnabled(true);
                    break;
                default:
                    break;
            }
        });

        runAnalysisButton.addActionListener(evt -> {
            try {
                OraUtils.runAnalysis();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "An error was encountered:\n" + e.getMessage()
                        + "\nRevise settings and try to run the analysis again.", "Settings Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public void disableRunAnalysisButton() {
        runAnalysisButton.setEnabled(false);
    }
}
