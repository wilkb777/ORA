package com.bwc.ora.analysis;

import javax.swing.*;

public class FreeFormAnalysis implements Analysis {
    @Override
    public void run() {
        //enable LRP to be moved by the arrow keys
        JOptionPane.showMessageDialog(null, "Use the arrow keys to move the LRP.", "Move LRP", JOptionPane.INFORMATION_MESSAGE);


        //run analysis as normal
        new PreformattedAnalysis().run();
    }
}
