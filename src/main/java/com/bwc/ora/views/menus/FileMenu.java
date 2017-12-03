package com.bwc.ora.views.menus;

import com.bwc.ora.OraUtils;

import javax.swing.*;

/**
 * Created by Brandon on 1/16/2017.
 */
public class FileMenu extends JMenu {

    private JMenuItem newAnalysis = new JMenuItem();
    private JMenuItem testAnalysis = new JMenuItem();
//    private JMenuItem open = new JMenuItem();
    private JMenuItem export = new JMenuItem();
//    private JMenuItem save = new JMenuItem();
    private JMenuItem exit = new JMenuItem();

    public FileMenu() {
        setText("File");
        init();
    }

    public FileMenu(String s) {
        super(s);
        init();
    }
    
    private void init(){
        newAnalysis.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newAnalysis.setText("New Analysis");
        add(newAnalysis);

        testAnalysis.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        testAnalysis.setText("Test Analysis");
        add(testAnalysis);

//        open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
//        open.setText("Open Analysis");
//        add(open);

        export.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        export.setText("Export Analysis Results");
        add(export);

//        save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
//        save.setText("Save Analysis");
//        save.setEnabled(false);
//        add(save);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exit.setText("Quit");
        add(exit);

        connectFileOperations();
    }

    private void connectFileOperations() {
        newAnalysis.addActionListener(OraUtils.newAnalysisActionListener);
        testAnalysis.addActionListener(OraUtils.testAnalysisActionListener);
        export.addActionListener(OraUtils.exportAnalysisActionListener);
        exit.addActionListener(e -> System.exit(0));
    }
}
