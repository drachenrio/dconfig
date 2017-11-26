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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.util.DateUtil;

/**
 *
 * @author Jonathan Luo
 */
public class DlgEditValue extends javax.swing.JDialog {
    private static final Log log = LogFactory.getLog(DlgEditValue.class);

    private String dataTypeAlias;
    private boolean editable;
    private Object resultObject;

    private JComboBox cbTrueFalse;
    /** Creates new form DlgSingleInput */
    public DlgEditValue(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public DlgEditValue(java.awt.Frame parent, String title, String attributeName,
                          String attributeValue, String dataTypeAlias, 
                          boolean editable) {
        super(parent, title, true);
        this.editable = editable;
        this.dataTypeAlias = dataTypeAlias;

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
        postInitialization(attributeName, attributeValue);
    }

    public DlgEditValue(java.awt.Dialog parent, String title, String attributeName,
                          String attributeValue, String dataTypeAlias, 
                          boolean editable) {
        super(parent, title, true);
        this.editable = editable;
        this.dataTypeAlias = dataTypeAlias;

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
        postInitialization(attributeName, attributeValue);
    }
        
    private void postInitialization(String attributeName, String attributeValue) {
        txtField.setEditable(AppContext.isUpdateMode());
        
        this.lblNameValue.setText(attributeName);
        String dataTypeName = (new DConfigDataTypeDao()).getDataTypeNameByAlias(dataTypeAlias);
        dataTypeName = StringUtils.chomp(dataTypeName, "Array");
        this.lblDatatypeValue.setText(dataTypeName);
        if (dataTypeAlias.startsWith(DConfigDataType.aliasInteger) ||
                dataTypeAlias.startsWith(DConfigDataType.aliasLong)) {      
            txtField.setFormatterFactory(new DefaultFormatterFactory(
                    new NumberFormatter(NumberFormat.getIntegerInstance())));
            try {
                txtField.setValue(Integer.valueOf(attributeValue));
            } catch (Exception e) {
                txtField.setValue(new Integer(0));
            }
        }
        else if (dataTypeAlias.startsWith(DConfigDataType.aliasFloat) ||
                 dataTypeAlias.startsWith(DConfigDataType.aliasDouble)) {
            DefaultFormatter fmt = new NumberFormatter(new DecimalFormat("#0.0###############"));
            DefaultFormatterFactory fmtFactory = new DefaultFormatterFactory(fmt, fmt, fmt);
            txtField.setFormatterFactory(fmtFactory);
            try {
                txtField.setValue(new BigDecimal(attributeValue));
            } catch (Exception e) {
                txtField.setValue(new BigDecimal("0.0"));
            }
        }
        else if (dataTypeAlias.startsWith(DConfigDataType.aliasDatetime)) {
            //txtField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));
            SimpleDateFormat fmt = new SimpleDateFormat(DateUtil.DEFAULT_DISPLAY_FORMAT);
            txtField.setFormatterFactory(new DefaultFormatterFactory(
                    new DateFormatter(fmt)));
            Date date = null;
            try {
                date = fmt.parse(attributeValue);
            } catch (Exception e) {
                date = DateUtil.clearHHMMSSmm(new Date());
            }
            txtField.setValue(date);
        } else { // String            
            DefaultFormatter fomatter = new DefaultFormatter();
            fomatter.setOverwriteMode(false);
            txtField.setFormatterFactory(new DefaultFormatterFactory(fomatter));
            txtField.setValue(attributeValue);
        }
        if (AppContext.isUpdateMode()) {
            txtField.selectAll();
            //String vaule = txtField.getText();
            //txtField.setSelectionEnd((txtField.getText() != null) ? txtField.getText().length() - 1 : 0);
        }
        //JPanel mainPanel = new JPanel();
        //mainPanel.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // right box
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblNameValue = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblDatatypeValue = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtField = new javax.swing.JFormattedTextField();
        btnClose = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Value");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Attribute"));

        jLabel1.setText("Name:");

        lblNameValue.setText("jLabel2");

        jLabel3.setText("Data Type:");

        lblDatatypeValue.setText("jLabel4");

        jLabel2.setText("Value:");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel2)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblNameValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblDatatypeValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 183, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, txtField))
                .add(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(lblNameValue))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(lblDatatypeValue))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(txtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
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
                        .add(btnClose, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(23, 23, 23)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(87, 87, 87))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnClose)
                    .add(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public Object getResultObject() {
        return resultObject;
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        resultObject = null;
        setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        log.info("before commitEdit() - txtField.getValue(): " + txtField.getValue());
        try {
            txtField.commitEdit();
        } catch (Exception e) {
            log.error(e);            
        }
        log.info("after commitEdit() - txtField.getValue(): " + txtField.getValue());
        resultObject = txtField.getValue();
        if (resultObject instanceof Date) {
            resultObject = DateUtil.toDisplayFormat((Date)resultObject);
        }
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DlgEditValue(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDatatypeValue;
    private javax.swing.JLabel lblNameValue;
    private javax.swing.JFormattedTextField txtField;
    // End of variables declaration//GEN-END:variables
    
}
