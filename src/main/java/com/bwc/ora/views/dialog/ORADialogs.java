package com.bwc.ora.views.dialog;

import javax.swing.*;
import java.awt.*;

public class ORADialogs {

    /**
     * Bring up a warning dialog asking user if they want to change analysis modes.
     * @param parent
     * @return the choice the user made, options are ints from {@link JOptionPane}
     */
    public static int bringUpAnalysisModeChangeWarning(Component parent) {
        return JOptionPane.showConfirmDialog(parent,
                "You are about to change analysis modes.\n"
                        + "All unsaved/exported work will be lost.\n"
                        + "Do you want to continue?",
                "WARNING: Analysis Mode Change",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
    }
}
