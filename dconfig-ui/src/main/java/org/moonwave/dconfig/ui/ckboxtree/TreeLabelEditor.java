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

package org.moonwave.dconfig.ui.ckboxtree;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.CellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.ColorUIResource;
import org.moonwave.dconfig.ui.util.ImageUtil;


/**
 * Implementation of <code>TreeLabelEditor</code>. A <code>TreeLabelEditor</code> 
 * includes a disabled checkbox, a disabled icon label, and an active text field.
 *
 * @author Jonathan Luo
 */
public class TreeLabelEditor extends JPanel implements CellEditor {
 
    static Color defaultBackground   = UIManager.getColor("Tree.textBackground");
    static ImageIcon keyExpandedIcon = ImageUtil.createImageIcon("images/keyV.png", "");
    static ImageIcon keyCollapseIcon = ImageUtil.createImageIcon("images/keyH.png", "");

    static ImageIcon readonly_inheritedIcon   = ImageUtil.createImageIcon("images/ro_checked_checkbox.png", "");
    static ImageIcon readonly_defaultCheckbox = ImageUtil.createImageIcon("images/ro_blank_checkbox.png", "");
    
    Vector listeners = new Vector();

    protected TreeCheckBox  checkbox;
    protected JLabel        iconLabel;
    protected JTextField    txtField;
  
    public TreeLabelEditor() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEADING);
        layout.setHgap(0);
        layout.setVgap(2);
        this.setLayout(layout);

        checkbox = new TreeCheckBox();
        checkbox.setEnabled(false);

        iconLabel = new JLabel();
        iconLabel.setIcon(keyExpandedIcon);
        iconLabel.setEnabled(false);

        txtField = new JTextField();
        txtField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (stopCellEditing()) {
                    fireEditingStopped();
                }
            }
        });

        this.add(checkbox);
        this.add(iconLabel);
        this.add(txtField);
    }

    public void initialize(JTree tree, Object value, boolean isSelected,
                           boolean expanded, boolean leaf, int row) {

        // update check box
        CheckTreeNode node = (CheckTreeNode) value;
        if (node.isRoot() || ((CheckTreeNode)node.getParent()).isRoot()) {
            // don't show check box for root and first level nodes
            checkbox.setVisible(false);
        }
        else {
            checkbox.setVisible(true);
        }
        checkbox.setSelected(node.isChecked());
        checkbox.setBackground(defaultBackground);
        if (checkbox.isSelected()) {
            checkbox.setIcon(readonly_inheritedIcon);
            checkbox.setPressedIcon(readonly_inheritedIcon);
            checkbox.setDisabledIcon(readonly_inheritedIcon);
        } else {
            checkbox.setIcon(readonly_defaultCheckbox);
            checkbox.setPressedIcon(readonly_defaultCheckbox);
            checkbox.setDisabledIcon(readonly_defaultCheckbox);
        }
        // update icon
        if (expanded)
            iconLabel.setIcon(keyExpandedIcon);
        else 
            iconLabel.setIcon(keyCollapseIcon);

        // update tree label
        String labelText = tree.convertValueToText(value, isSelected, expanded, leaf, row, true);
        txtField.setFont(tree.getFont());
        this.setText(labelText); // or setText(value.toString());
    }

    public void setText(String s) {
        txtField.setText(s);
        txtField.setColumns(s.length());
    }

    public String getText() {
        return txtField.getText();
    }

    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
        color = null;
        super.setBackground(color);
    }
  
    public void cancelCellEditing() {
        txtField.setText("");
    }

    public boolean stopCellEditing() {
        // Value validation goes here; return false for invalid value.
        // Checking whether '.' is entered is done by a separate event handler,
        // so always return true
        return true;
    }

    public Object getCellEditorValue() {
        return txtField.getText();    
    }

    public boolean shouldSelectCell(EventObject eo) {
        return true;
    }

    public boolean isCellEditable(EventObject eo) {
        return true;
    }

    /** 
     * Add support for listeners.
     */
    public void addCellEditorListener(CellEditorListener cel) {
        listeners.addElement(cel);
    }

    public void removeCellEditorListener(CellEditorListener cel) {
        listeners.removeElement(cel);
    }

    protected void fireEditingStopped() {
        if (listeners.size() > 0) {
            ChangeEvent ce = new ChangeEvent(this);
            for (int i = listeners.size() - 1; i >= 0; i--) {
                ((CellEditorListener) listeners.elementAt(i)).editingStopped(ce);
            }
        }
    }
}
