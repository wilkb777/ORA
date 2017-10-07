package com.bwc.ora.io;

import com.bwc.ora.OraUtils;
import com.bwc.ora.collections.Collections;
import com.bwc.ora.collections.ModelsCollection;
import com.bwc.ora.models.Oct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.*;

public class SettingsIOUtils {

    public static final String ORA_ANALYSIS_SETTINGS_FILE_EXTENSION = "oasf";

    public static final ActionListener loadAnalysisSettingsActionListener = evt -> {
        try {
            //read in settings file for analysis
            File osfFile = OraUtils.selectFile(true, null, JFileChooser.FILES_ONLY, "ORA Analysis Settings", ORA_ANALYSIS_SETTINGS_FILE_EXTENSION + " file",
                    ORA_ANALYSIS_SETTINGS_FILE_EXTENSION);
            if (osfFile != null) {
                SettingsIOUtils.loadSettings(osfFile);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "ORA Analysis Setting loading failed,"
                    + " reason: " + ex.getMessage(), "Loading error!", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException npe) {
            JOptionPane.showMessageDialog(null, "ORA Analysis Setting loading failed,"
                    + " reason: OCT not loaded, settings can't be loaded without an OCT loaded", "Loading error!", JOptionPane.ERROR_MESSAGE);
        }
    };

    public static final ActionListener saveAnalysisSettingsActionListener = evt -> {
        try {
            //read in settings file for analysis
            File osfFile = OraUtils.selectFile(false, null, JFileChooser.FILES_ONLY, "Save Analysis Settings", ORA_ANALYSIS_SETTINGS_FILE_EXTENSION + " file",
                    ORA_ANALYSIS_SETTINGS_FILE_EXTENSION);
            if (osfFile != null) {
                if (!osfFile.getName().endsWith(ORA_ANALYSIS_SETTINGS_FILE_EXTENSION)) {
                    osfFile = new File(osfFile.getParentFile(), osfFile.getName() + "." + ORA_ANALYSIS_SETTINGS_FILE_EXTENSION);
                }
                SettingsIOUtils.saveCurrentSettings(osfFile);
            } else {
                throw new IOException("No file entered");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "ORA Analysis Setting saving failed,"
                    + " reason: " + ex.getMessage(), "Save error!", JOptionPane.ERROR_MESSAGE);
        }
    };

    public static void saveCurrentSettings(File analysisSettingsFile) throws IOException {
        System.out.println("Saving to " + analysisSettingsFile.getAbsolutePath());
        try (PrintWriter pw = new PrintWriter(analysisSettingsFile)) {
            Gson jsonFormatter = new GsonBuilder().setPrettyPrinting().create();
            String json = jsonFormatter.toJson(ModelsCollection.getInstance());
            System.out.println("Settings JSON:\n" + json);
            pw.println(json);
        }
    }

    public static void loadSettings(File analysisSettingsFile) throws FileNotFoundException {
        ModelsCollection loadedSettings = (new Gson()).fromJson(new FileReader(analysisSettingsFile), ModelsCollection.class);
        ModelsCollection.getInstance().loadSettings(loadedSettings);
    }
}
