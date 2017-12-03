/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bwc.ora.collections;

import com.bwc.ora.models.Lrp;
import com.bwc.ora.models.LrpType;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;

/**
 * @author Brandon M. Wilk {@literal <}wilkb777@gmail.com{@literal >}
 */
public class LrpCollection extends JList<Lrp> {

    private final DefaultListModel<Lrp> dataModel;

    LrpCollection() {
        this.dataModel = new DefaultListModel<>();
        setModel(dataModel);
        this.setCellRenderer(new CustomeRenderer());
    }

    public boolean isEmpty(){
        return dataModel.isEmpty();
    }

    public void setLrps(List<Lrp> lrps) {
        clearLrps();
        lrps.forEach(dataModel::addElement);
        this.setSelectedIndex(0);
    }

    public void setLrp(Lrp lrp, int selectionIndex) {
        dataModel.setElementAt(lrp, selectionIndex);
        this.setSelectedIndex(selectionIndex);
    }

    public void addLrp(Lrp lrp) {
        dataModel.addElement(lrp);
        System.out.println("Num lrp = "+dataModel.size());
    }

    public void addListDataChangeListener(ListDataListener l){
        dataModel.addListDataListener(l);
    }

    public void clearLrps() {
        dataModel.clear();
    }

    public Stream<Lrp> stream() {
        Enumeration<Lrp> lrps = dataModel.elements();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<Lrp>() {

                            @Override
                            public boolean hasNext() {
                                return lrps.hasMoreElements();
                            }

                            @Override
                            public Lrp next() {
                                return lrps.nextElement();
                            }
                        },
                        Spliterator.ORDERED),
                false);
    }

    public Stream<Lrp> streamSelected() {
        Lrp selected = getSelectedValue();
        if (selected == null) {
            return Stream.empty();
        } else {
            selected.setDisplay(true);
            return Stream.of(selected);
        }
    }

    public Lrp getFovealLrp() {
        Lrp fovealLrp = null;
        Enumeration<Lrp> lrps = dataModel.elements();
        while (lrps.hasMoreElements()) {
            Lrp lrp = lrps.nextElement();
            if (lrp.getType() == LrpType.FOVEAL) {
                fovealLrp = lrp;
                break;
            }
        }
        return fovealLrp;
    }

    @Override
    public void setSelectedIndex(int index) {
        if (getSelectedIndex() > -1) {
            getSelectedValue().setDisplay(false);
        }
        super.setSelectedIndex(index);
        getSelectedValue().setDisplay(true);
    }

    public void selectNextLrp() {
        int curSelectionIndex = getSelectedIndex();
        if (getModel().getSize() > 0 && curSelectionIndex < 0) {
            setSelectedIndex(0);
        } else if (getModel().getSize() > 0 && curSelectionIndex < getModel().getSize()) {
            setSelectedIndex(curSelectionIndex + 1);
        }
    }

    public void selectPrevLrp() {
        int curSelectionIndex = getSelectedIndex();
        if (getModel().getSize() > 0 && curSelectionIndex > 0) {
            setSelectedIndex(curSelectionIndex - 1);
        }
    }

    private class CustomeRenderer extends JLabel implements ListCellRenderer<Lrp> {

        public CustomeRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Lrp> list,
                Lrp value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            //Set the icon and text.
            setText(" ["+value.getName()+"] ");
            return this;
        }

    }
}
