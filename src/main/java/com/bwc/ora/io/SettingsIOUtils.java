package com.bwc.ora.io;

import com.bwc.ora.collections.ModelsCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class SettingsIOUtils {

    public static final String SETTINGS_FILE_EXTENSION = "oraconf";

    public static void saveCurrentSettings(File analysisSettingsFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(analysisSettingsFile)))) {
            Gson jsonFormatter = new GsonBuilder().setPrettyPrinting().create();
            pw.print(jsonFormatter.toJson(ModelsCollection.getInstance()));
        }
    }
}
