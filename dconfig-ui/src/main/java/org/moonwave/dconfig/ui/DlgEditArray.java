/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * DConfig - Free Dynamic Configuration Toolkit
 * Copyright (C) 2006, 2007 Jonathan Luo
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

package org.moonwave.dconfig.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang.StringUtils;
import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.util.Constants;

/**
 *
 * @author  jonathan
 */
public class DlgEditArray extends javax.swing.JDialog {
    protected String attributeName;
    protected String dataTypeAlias;
    protected JDialog dlgMain = null;
    protected boolean editable;
    protected String resultObj;
    
    /** Creates new form DlgArrayInput */
    public DlgEditArray(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.dlgMain = this;
    }
    
    public DlgEditArray(java.awt.Frame parent, String title, String attributeName, 
                        String text, String dataTypeAlias, boolean editable) {
        super(parent, title, true);
        this.editable = editable;
        this.attributeName = attributeName;
        this.dataTypeAlias = dataTypeAlias;
        this.dlgMain = this;
        // If there was a parent, set dialog position inside
        if (parent != null) {
            Dimension parentSize = parent.getSize();     // Parent size
            Point p = parent.getLocation();              // Parent position
            setLocation(p.x+parentSize.width/4,p.y+parentSize.height/4); 
        }
        initComponents();
        KeyStroke escape = KeyStroke.getKeyStroke("ESCAPE");
        Action escapeActionListener = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                btnCancel.doClick();
            }
        };
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(escape, "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", escapeActionListener);
        postInitialization(text);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        valueList = new javax.swing.JList();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnMoveUp = new javax.swing.JButton();
        btnMoveDown = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Edit Array Attribute");
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Array Datatype Attribute"));
        valueList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(valueList);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new16.gif")));
        btnAdd.setToolTipText("Add a new entry");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit16.gif")));
        btnEdit.setToolTipText("Edit selected entry");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete16.gif")));
        btnDelete.setToolTipText("Remove selected entry");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnMoveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/up16.png")));
        btnMoveUp.setToolTipText("Move entry up");
        btnMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveUpActionPerformed(evt);
            }
        });

        btnMoveDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/down16.png")));
        btnMoveDown.setToolTipText("Move entry down");
        btnMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveDownActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .add(16, 16, 16)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(btnMoveDown, 0, 0, Short.MAX_VALUE)
                    .add(btnMoveUp, 0, 0, Short.MAX_VALUE)
                    .add(btnDelete, 0, 0, Short.MAX_VALUE)
                    .add(btnEdit, 0, 0, Short.MAX_VALUE)
                    .add(btnAdd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(btnAdd)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnEdit)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnDelete)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnMoveUp)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnMoveDown))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addContainerGap())
        );

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnCancel.setMnemonic('n');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnClose)
                        .add(34, 34, 34)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(131, 131, 131))))
        );

        layout.linkSize(new java.awt.Component[] {btnCancel, btnClose}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(7, 7, 7)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnClose)
                    .add(btnCancel))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMoveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveDownActionPerformed
        int fromIdx = valueList.getSelectedIndex();
        int toIdx = fromIdx + 1;
        if (fromIdx != -1) {
            DefaultListModel model = (DefaultListModel)valueList.getModel();
            Object obj1 = model.get(fromIdx);
            Object obj2 = model.get(toIdx);
            model.set(toIdx, obj1);
            model.set(fromIdx, obj2);
            valueList.setSelectedIndex(toIdx);
        }
    }//GEN-LAST:event_btnMoveDownActionPerformed

    private void btnMoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveUpActionPerformed
        int fromIdx = valueList.getSelectedIndex();
        int toIdx = fromIdx - 1;
        if (fromIdx != -1) {
            DefaultListModel model = (DefaultListModel)valueList.getModel();
            Object obj1 = model.get(fromIdx);
            Object obj2 = model.get(toIdx);
            model.set(toIdx, obj1);
            model.set(fromIdx, obj2);
            valueList.setSelectedIndex(toIdx);
        }
    }//GEN-LAST:event_btnMoveUpActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int idx = valueList.getSelectedIndex();
        if (idx != -1) {
            DefaultListModel model = (DefaultListModel)valueList.getModel();
            model.remove(idx);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int idx = valueList.getSelectedIndex();
        Object sel = valueList.getSelectedValue();        

        JDialog dlg = null;
        if (dataTypeAlias.startsWith(DConfigDataType.aliasBoolean)) {
            dlg = new DlgEditBoolean(AppContext.appContext().getCfgEditor(),
                        "Edit value", attributeName, sel.toString(), dataTypeAlias, true);
        }
        else {
            dlg = new DlgEditValue(dlgMain,
                    "Edit value", attributeName, sel.toString(), dataTypeAlias, true);
        }
        
        dlg.setVisible(true);
        Object obj = null;
        if (dataTypeAlias.startsWith(DConfigDataType.aliasBoolean))
            obj = ((DlgEditBoolean)dlg).getResultObject();
        else
            obj = ((DlgEditValue)dlg).getResultObject();
        if (obj != null) {
            DefaultListModel model = (DefaultListModel)valueList.getModel();
            model.set(idx, obj);
            valueList.setSelectedIndex(idx);
        }
        dlg.dispose();
        dlg = null;
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        JDialog dlg = null;
        if (dataTypeAlias.startsWith(DConfigDataType.aliasBoolean)) {
            dlg = new DlgEditBoolean(AppContext.appContext().getCfgEditor(),
                        "Edit value", attributeName, "", dataTypeAlias, true);
        }
        else {
            dlg = new DlgEditValue(dlgMain,
                    "Edit value", attributeName, "", dataTypeAlias, true);
        }
        dlg.setVisible(true);
        Object obj = null;
        if (dataTypeAlias.startsWith(DConfigDataType.aliasBoolean))
            obj = ((DlgEditBoolean)dlg).getResultObject();
        else
            obj = ((DlgEditValue)dlg).getResultObject();
        
        if (obj != null) {
            DefaultListModel model = (DefaultListModel)valueList.getModel();
            model.addElement(obj);
            valueList.setSelectedIndex(model.getSize() -1);
        }
        dlg.dispose();
        dlg = null;
    }//GEN-LAST:event_btnAddActionPerformed

    public String getResultObject() {
        return resultObj;
    }
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // set result as null
        resultObj = null;
        setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // converted to Constants.delimiter separated String
        StringBuffer sb = new StringBuffer(500);
        DefaultListModel model = (DefaultListModel)valueList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (i != 0)
                sb.append(Constants.DELIMITER);
            sb.append(model.get(i));
        }
        resultObj = sb.toString();
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    class MyListSelectionListener implements ListSelectionListener {
        /** 
        * Called whenever the value of the selection changes.
        * @param e the event that characterizes the change.
        */
        public void valueChanged(ListSelectionEvent e) {
            // When the user release the mouse button and completes the selection,
            // getValueIsAdjusting() becomes false
            if (!e.getValueIsAdjusting()) {
                JList list = (JList)e.getSource();
                int count = list.getModel().getSize();
                int idx = list.getSelectedIndex();
                Object sel = list.getSelectedValue();
                
                if (!AppContext.isUpdateMode())
                    btnAdd.setEnabled(false);
                setButtonsEnabled(false);
                if (AppContext.isUpdateMode()) {
                    if (idx != -1) {
                        setButtonsEnabled(true);
                    }
                    if (idx == 0) { // first item
                        btnMoveUp.setEnabled(false);
                    }
                    if (idx == count - 1){ // last item
                        btnMoveDown.setEnabled(false);
                    }
            }
            }
        }
    }
    
    public void postInitialization(String arrayValue) {        

        //JPanel mainPanel = new JPanel();
        //mainPanel.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        valueList.setModel(new DefaultListModel());
        valueList.addListSelectionListener(new MyListSelectionListener());
        valueList.setAutoscrolls(true);

        // set list values
        DefaultListModel model = (DefaultListModel)valueList.getModel();
        String[] dataAr = StringUtils.split(arrayValue, Constants.DELIMITER);
        if (dataAr != null) {
            for (int i = 0; i < dataAr.length; i++) {
                model.addElement(dataAr[i]);
            }
        } else if (arrayValue.length() > 0) {
            model.addElement(arrayValue);
        }
        
        // set initial button states
        if (!AppContext.isUpdateMode())
            btnAdd.setEnabled(false);
        setButtonsEnabled(false);
        if (AppContext.isUpdateMode()) {
            btnAdd.setEnabled(true);
            if ((dataAr != null) && (dataAr.length > 0)) {
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
                if (dataAr.length >= 2) {
                    btnMoveDown.setEnabled(true);
                }
            }
        }
    }
    private void setButtonsEnabled(boolean b) {
        btnEdit.setEnabled(b);
        btnDelete.setEnabled(b);
        btnMoveUp.setEnabled(b);
        btnMoveDown.setEnabled(b);        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DlgEditArray(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnMoveDown;
    private javax.swing.JButton btnMoveUp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList valueList;
    // End of variables declaration//GEN-END:variables
    
}