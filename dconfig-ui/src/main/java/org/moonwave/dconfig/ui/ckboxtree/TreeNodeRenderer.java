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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.util.ImageUtil;

/**
 *
 * Implementation of tree cell renderer. Not being used.
 * Replaced by CheckTreeNodeRenderer for in-place tree cell editing need.
 *
 * @author Jonathan Luo
 */
@Deprecated
public class TreeNodeRenderer extends JPanel implements TreeCellRenderer {

    static Color defaultBackground   = UIManager.getColor("Tree.textBackground");
    static ImageIcon keyExpandedIcon = ImageUtil.createImageIcon("images/keyV.png", "");
    static ImageIcon keyCollapseIcon = ImageUtil.createImageIcon("images/keyH.png", "");
    static Dimension invisibleCheckBoxSize = new Dimension(0, 21);
    static Dimension defaultCheckBoxSize  = new Dimension(21, 21);

    protected JCheckBox checkbox;
    protected TreeLabel treeLabel;

    public TreeNodeRenderer() {
        setLayout(null);
        checkbox = new JCheckBox();
        treeLabel = new TreeLabel();
        add(checkbox);
        add(treeLabel);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                        boolean isSelected, boolean expanded, boolean leaf, int row,
                        boolean hasFocus) {
        setEnabled(false);
        //enableInputMethods(false);
        // update check box
        CheckTreeNode node = (CheckTreeNode) value;
        if (node.isRoot() || ((CheckTreeNode)node.getParent()).isRoot()) {
            // don't show check box for root and first level nodes
            checkbox.setPreferredSize(invisibleCheckBoxSize);
        }
        else
            checkbox.setPreferredSize(defaultCheckBoxSize);
        checkbox.setSelected(node.isChecked());
        checkbox.setBackground(defaultBackground);
        checkbox.setEnabled(AppContext.isUpdateMode());

        // update tree label
        if (expanded)
            treeLabel.setIcon(keyExpandedIcon);
        else 
            treeLabel.setIcon(keyCollapseIcon);
        String labelText = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
        treeLabel.setFont(tree.getFont());
        treeLabel.setText(labelText);
        treeLabel.setSelected(isSelected);
        treeLabel.setFocus(hasFocus);
        //panel.setPreferredSize(getPreferredSize());
        //treeLabel.setEnabled(AppContext.isUpdateMode());
        treeLabel.enableInputMethods(false);
        return this;
    }

    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
            color = null;
        if (color != null) {
            System.out.println("color: " + color);
        }
        super.setBackground(color);
    }

    public Dimension getPreferredSize() {
        Dimension checkboxSize = checkbox.getPreferredSize();
        Dimension labelSize = treeLabel.getPreferredSize();
        return new Dimension(checkboxSize.width + labelSize.width, Math.max(checkboxSize.height, labelSize.height));
    }
    
    public void doLayout() {
        Dimension checkboxSize  = checkbox.getPreferredSize();
        Dimension labelSize = treeLabel.getPreferredSize();
        int checkboxY = 0;
        int labelY = 0;

        if (checkboxSize.height < labelSize.height)
            checkboxY = (labelSize.height - checkboxSize.height) / 2;
        else
            labelY = (checkboxSize.height - labelSize.height) / 2;

        checkbox.setBounds(0, checkboxY, checkboxSize.width, checkboxSize.height);
        checkbox.setLocation(0, checkboxY);

        treeLabel.setBounds(checkboxSize.width, labelY, labelSize.width, labelSize.height);
        treeLabel.setLocation(checkboxSize.width, labelY);
    }
}

class TreeLabel extends JLabel {

    static Color lostFocusSelectionBackground = new Color(248, 193, 171); // Color.LIGHT_GRAY;RGB ..LIGHT_GRAY; 
    static Color focusSelectionBackground = UIManager.getColor("Tree.selectionBackground");
    static Color defaultBackground = UIManager.getColor("Tree.textBackground");
    static Color focusBorderColor = UIManager.getColor("Tree.selectionBorderColor");
    
    private boolean selected;
    private boolean hasFocus;

    public TreeLabel() {
    }

    public void setBackground(Color color) {
      if (color instanceof ColorUIResource)
        color = null;
      super.setBackground(color);
    }

    public void paint(Graphics g) {
        String labelText = getText();
        if ((labelText != null) && (labelText.length() > 0)) {
            if (selected) {
                if (hasFocus)
                    g.setColor(focusSelectionBackground);
                else {
                    if (AppContext.appContext().getFocusComponent() == AppContext.Focus.tree)
                        g.setColor(focusSelectionBackground);
                    else
                        g.setColor(lostFocusSelectionBackground);
                }
            } else {
                g.setColor(defaultBackground);
            }
            int textStartX = getIcon().getIconWidth() + Math.max(0, getIconTextGap() - 2);
            Dimension d = getPreferredSize();
            g.fillRect(textStartX, 0, d.width - textStartX - 1, d.height);
            if (hasFocus) {
                g.setColor(focusBorderColor);
                g.drawRect(textStartX, 0, d.width - 1 - textStartX, d.height - 1);
            }
        }
        super.paint(g);
    }
    
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize(); // include icon if applicable
        if (d != null)
            d = new Dimension(d.width + 3, d.height);
        return d;
    }

    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    public void setFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
}

