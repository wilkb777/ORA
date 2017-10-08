/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.views.toolbars;

import com.bwc.ora.models.LrpSettings;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.OctSettings;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
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
public class OctSettingsPanel extends JPanel {

    private final String title;
    private JCheckBox octNoiseReductionCheckBox;
    private JCheckBox octContrastAdjustCheckBox;
    private JRadioButton octLinearDisplay;
    private JRadioButton octLogDisplay;
    private ButtonGroup octLogLinearDisplayButtonGroup;
    private JFormattedTextField octSmoothingFactorField;
    private JSlider octSmoothingFactorSlider;
    private JFormattedTextField octSharpenWeightField;
    private JSlider octSharpenWeightSlider;
    private JFormattedTextField octSharpenKernelRadiusField;
    private JSlider octSharpenKernelSlider;
    private JFormattedTextField octXScaleField;
    private JFormattedTextField octYScaleField;

    public OctSettingsPanel() {
        super();
        OctSettings settings = ModelsCollection.getInstance().getOctSettings();
        this.octNoiseReductionCheckBox = new JCheckBox();
        this.octContrastAdjustCheckBox = new JCheckBox();
        this.octLinearDisplay = new JRadioButton("Linear");
        this.octLogDisplay = new JRadioButton("Logarithmic", true);
        this.octLogLinearDisplayButtonGroup = new ButtonGroup();
        this.octSmoothingFactorField = new ToolbarTextField(settings.getSmoothingFactor());
        int[] range = OctSettings.getSmoothingFactorSliderRange();
        this.octSmoothingFactorSlider = new JSlider(SwingConstants.HORIZONTAL, range[0], range[1], (int) Math.round(settings.getSmoothingFactor() / OctSettings.getSmoothingFactorSliderMultiplier()));
        this.octSharpenWeightField = new ToolbarTextField(settings.getSharpenWeight());
        range = OctSettings.getSharpenWeightSliderRange();
        this.octSharpenWeightSlider = new JSlider(SwingConstants.HORIZONTAL, range[0], range[1], Math.round(settings.getSharpenWeight() / OctSettings.getSharpenWeightSliderMultiplier()));
        this.octSharpenKernelRadiusField = new ToolbarTextField(0D);
        range = OctSettings.getSharpenKernelRadiusSliderRange();
        this.octSharpenKernelSlider = new JSlider(SwingConstants.HORIZONTAL, range[0], range[1], (int) Math.round(settings.getSharpenKernelRadius() / OctSettings.getSharpenKernelRadiusSliderMultiplier()));
        this.octXScaleField = new ToolbarTextField(0D);
        this.octYScaleField = new ToolbarTextField(0D);
        init();
        connectToModel();
        title = "OCT Settings";
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

        JLabel l = new JLabel("OCT format: ");
        labelsList.add(l);
        JPanel radioBtnPanel = new JPanel();
        BoxLayout layout = new BoxLayout(radioBtnPanel, BoxLayout.LINE_AXIS);
        radioBtnPanel.setLayout(layout);
        radioBtnPanel.add(octLinearDisplay);
        radioBtnPanel.add(Box.createHorizontalStrut(ToolbarUtilities.TOOLBAR_HORIZONTAL_STRUT_WIDTH));
        radioBtnPanel.add(octLogDisplay);
        octLogLinearDisplayButtonGroup.add(octLogDisplay);
        octLogLinearDisplayButtonGroup.add(octLinearDisplay);
        l.setLabelFor(radioBtnPanel);
        componentsList.add(radioBtnPanel);

        JPanel scalePanel = new JPanel();
        l = new JLabel("Contrast Adjust");
        scalePanel.add(l);
        l.setLabelFor(octContrastAdjustCheckBox);
        scalePanel.add(octContrastAdjustCheckBox);
        labelsList.add(scalePanel);

        scalePanel = new JPanel();
        l = new JLabel("Noise Reduction");
        scalePanel.add(l);
        l.setLabelFor(octNoiseReductionCheckBox);
        scalePanel.add(octNoiseReductionCheckBox);
        componentsList.add(scalePanel);

        scalePanel = new JPanel();
        l = new JLabel("X Scale");
        scalePanel.add(l);
        octXScaleField.setColumns(6);
        l.setLabelFor(octXScaleField);
        scalePanel.add(octXScaleField);
        labelsList.add(scalePanel);

        scalePanel = new JPanel();
        l = new JLabel("Y Scale");
        scalePanel.add(l);
        octYScaleField.setColumns(6);
        l.setLabelFor(octYScaleField);
        scalePanel.add(octYScaleField);
        componentsList.add(scalePanel);

        //add labels and inputs to left column
        ToolbarUtilities.addLabelTextRows(labelsList, componentsList, gridbag, leftColumnPanel);

        //Lay out the text controls and the labels for the right column
        gridbag = new GridBagLayout();
        JPanel rightColumnPanel = new JPanel();
        rightColumnPanel.setLayout(gridbag);

        labelsList = new ArrayList<>();
        componentsList = new ArrayList<>();

        l = new JLabel("Smoothing ");
        labelsList.add(l);
        l.setLabelFor(octSmoothingFactorField);
        componentsList.add(ToolbarUtilities.getPanelWithSliderAndTextField(octSmoothingFactorField, octSmoothingFactorSlider));

        l = new JLabel("Sharpen Weight ");
        labelsList.add(l);
        l.setLabelFor(octSharpenWeightField);
        componentsList.add(ToolbarUtilities.getPanelWithSliderAndTextField(octSharpenWeightField, octSharpenWeightSlider));

        l = new JLabel("Sharpen Radius ");
        labelsList.add(l);
        l.setLabelFor(octSharpenKernelRadiusField);
        componentsList.add(ToolbarUtilities.getPanelWithSliderAndTextField(octSharpenKernelRadiusField, octSharpenKernelSlider));

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
        OctSettings settings = ModelsCollection.getInstance().getOctSettings();

        octContrastAdjustCheckBox.addItemListener(evt -> settings.setApplyContrastAdjustment(evt.getStateChange() == ItemEvent.SELECTED));
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_APPLY_CONTRAST_ADJUSTMENT.equals(evt.getPropertyName())) {
                octContrastAdjustCheckBox.setSelected((boolean) evt.getNewValue());
            }
        });

        octNoiseReductionCheckBox.addItemListener(evt -> settings.setApplyNoiseReduction(evt.getStateChange() == ItemEvent.SELECTED));
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_APPLY_NOISE_REDUCTION.equals(evt.getPropertyName())) {
                octNoiseReductionCheckBox.setSelected((boolean) evt.getNewValue());
            }
        });

        octSharpenKernelRadiusField.addPropertyChangeListener("value", evt -> settings.setSharpenKernelRadius((double) evt.getNewValue()));
        octSharpenKernelSlider.addChangeListener(evt -> {
            JSlider source = (JSlider) evt.getSource();
            settings.setSharpenKernelRadius(source.getValue() * OctSettings.getSharpenKernelRadiusSliderMultiplier());
        });
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_SHARPEN_KERNEL_RADIUS.equals(evt.getPropertyName())) {
                octSharpenKernelRadiusField.setValue(evt.getNewValue());
                octSharpenKernelSlider.setValue((int) Math.round(settings.getSharpenKernelRadius() / OctSettings.getSharpenKernelRadiusSliderMultiplier()));
            }
        });

        octSharpenWeightField.addPropertyChangeListener("value", evt -> settings.setSharpenWeight((float) evt.getNewValue()));
        octSharpenWeightSlider.addChangeListener(evt -> {
            JSlider source = (JSlider) evt.getSource();
            settings.setSharpenWeight(source.getValue() * OctSettings.getSharpenWeightSliderMultiplier());
        });
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_SHARPEN_WEIGHT.equals(evt.getPropertyName())) {
                octSharpenWeightField.setValue(evt.getNewValue());
                octSharpenWeightSlider.setValue((int) Math.round(settings.getSharpenWeight() / OctSettings.getSharpenWeightSliderMultiplier()));
            }
        });

        octSmoothingFactorField.addPropertyChangeListener("value", evt -> settings.setSmoothingFactor((double) evt.getNewValue()));
        octSmoothingFactorSlider.addChangeListener(evt -> {
            JSlider source = (JSlider) evt.getSource();
            settings.setSmoothingFactor(source.getValue() * OctSettings.getSmoothingFactorSliderMultiplier());
        });
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_SMOOTHING_FACTOR.equals(evt.getPropertyName())) {
                octSmoothingFactorField.setValue(evt.getNewValue());
                octSmoothingFactorSlider.setValue((int) Math.round(settings.getSmoothingFactor() / OctSettings.getSmoothingFactorSliderMultiplier()));
            }
        });

        octXScaleField.addPropertyChangeListener("value", evt -> settings.setxScale((double) evt.getNewValue()));
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_X_SCALE.equals(evt.getPropertyName())) {
                octXScaleField.setValue(evt.getNewValue());
            }
        });

        octYScaleField.addPropertyChangeListener("value", evt -> settings.setyScale((double) evt.getNewValue()));
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_Y_SCALE.equals(evt.getPropertyName())) {
                octYScaleField.setValue(evt.getNewValue());
            }
        });

        octLogDisplay.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                settings.setDisplayLogOct(true);
            }
        });
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_DISPLAY_LOG_OCT.equals(evt.getPropertyName())) {
                octLogDisplay.setSelected((boolean) evt.getNewValue());
            }
        });

        octLinearDisplay.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                settings.setDisplayLogOct(false);
            }
        });
        settings.addPropertyChangeListener(evt -> {
            if (OctSettings.PROP_DISPLAY_LOG_OCT.equals(evt.getPropertyName())) {
                octLinearDisplay.setSelected(!(boolean) evt.getNewValue());
            }
        });
    }
}
