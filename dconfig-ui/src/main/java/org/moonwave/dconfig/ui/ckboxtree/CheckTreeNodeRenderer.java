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
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.util.ImageUtil;

/**
 *
 * @author Jonathan Luo
 */

/**
 * Implementation of custom tree cell renderer. A viewable tree node consists of
 * a check box, an icon label, and a tree label. For root node and its child nodes, 
 * the check boxes are invisible. All three controls are placed inside a panel.
 *
 * @author Jonathan Luo
 */

public class CheckTreeNodeRenderer extends DefaultTreeCellRenderer {
    
    static Color defaultSelectionBackground   = UIManager.getColor("Tree.selectionBackground");
    static Color defaultBackground            = UIManager.getColor("Tree.textBackground");
    static Color defaultSelectionBorderColor  = UIManager.getColor("Tree.selectionBorderColor");
    
    static ImageIcon keyExpandedIcon = ImageUtil.createImageIcon("images/keyV.png", "");
    static ImageIcon keyCollapseIcon = ImageUtil.createImageIcon("images/keyH.png", "");

    static ImageIcon readonly_inheritedIcon   = ImageUtil.createImageIcon("images/ro_checked_checkbox.png", "");
    static ImageIcon readonly_defaultCheckbox = ImageUtil.createImageIcon("images/ro_blank_checkbox.png", "");
    static ImageIcon update_inheritedIcon     = ImageUtil.createImageIcon("images/rw_checked_checkbox.png", "");
    static ImageIcon update_defaultCheckbox   = ImageUtil.createImageIcon("images/rw_blank_checkbox.png", "");
        
    protected TreePanel     panel;
    protected TreeCheckBox  checkbox;
    protected JLabel        iconLabel;
    protected TreeLabel     label;
    protected Color         customLabelSelectionBackground; // selected, lost focus tree node background color

    public CheckTreeNodeRenderer() {
        panel = new TreePanel();
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEADING);
        layout.setHgap(0);
        layout.setVgap(2);
        panel.setLayout(layout);
        iconLabel = new JLabel() {
            public void setBackground(Color color) {
                if (color instanceof ColorUIResource)
                    color = null;
                super.setBackground(color);
                }
            };
        checkbox = new TreeCheckBox();
        label = new TreeLabel();
        panel.add(checkbox);
        panel.add(iconLabel);
        panel.add(label);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                        boolean isSelected, boolean expanded, boolean leaf, int row,
                        boolean hasFocus)
    {
        setEnabled(tree.isEnabled());
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
        checkbox.setEnabled(AppContext.isUpdateMode());
        if (checkbox.isEnabled()) {
            checkbox.setToolTipText("Check to enable Attribute Inheritance");
            if (checkbox.isSelected()) {
                checkbox.setIcon(update_inheritedIcon);
                checkbox.setPressedIcon(update_inheritedIcon);
                checkbox.setDisabledIcon(update_inheritedIcon);
            } else {
                checkbox.setIcon(update_defaultCheckbox);
                checkbox.setPressedIcon(update_defaultCheckbox);
                checkbox.setDisabledIcon(update_defaultCheckbox);
            }
        } else {
            if (checkbox.isSelected()) {
                checkbox.setIcon(readonly_inheritedIcon);
                checkbox.setPressedIcon(readonly_inheritedIcon);
                checkbox.setDisabledIcon(readonly_inheritedIcon);
            } else {
                checkbox.setIcon(readonly_defaultCheckbox);//or the original
                checkbox.setPressedIcon(readonly_defaultCheckbox);
                checkbox.setDisabledIcon(readonly_defaultCheckbox);
            }
        }
        // update icon
        if (expanded)
            iconLabel.setIcon(keyExpandedIcon);
        else 
            iconLabel.setIcon(keyCollapseIcon);

        // update tree label
        String labelText = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
        label.setFont(tree.getFont());
        label.setText(labelText);
        label.setSelect(isSelected);
        label.setFocus(hasFocus);
            
        return panel;
  }

    public Dimension getPreferredSize() {
        Dimension iconD = iconLabel.getPreferredSize();
        Dimension textD = label.getPreferredSize();
        int height = iconD.height < textD.height ? textD.height : iconD.height;
        return new Dimension(iconD.width + textD.width, height);
    }

    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
          color = null;
        super.setBackground(color);
    }
    
    public void setCustomLabelSelectionBackground(Color color) {
        customLabelSelectionBackground = color;
    }
  
    /**
     * TreePanel
     */ 
    class TreePanel extends JPanel {
        Dimension preferredSize;
        public TreePanel() {
            super();
        }

        public void setBackground(Color color) {
            if (color instanceof ColorUIResource)
            color = null;
            super.setBackground(color);
        }

        public void setPreferredSize(Dimension d) {
            if (d != null) {
            preferredSize = d;
            }
        }

        public Dimension getPreferredSize() {
            preferredSize = super.getPreferredSize();
            preferredSize.height = 25; //23
            return preferredSize;
        }
    }
  
    /**
     * TreeLabel
     */ 
    class TreeLabel extends JLabel {
        Dimension preferredSize;

        TreeLabel() {
            setOpaque(true);
        }

        public void setBackground(Color color) {
            if (color instanceof ColorUIResource)
                color = null;
            super.setBackground(color);
        }

        public void setPreferredSize(Dimension d) {
            if (d != null)
                preferredSize = d;
        }

        public Dimension getPreferredSize() {
            return (preferredSize != null) ? preferredSize : super.getPreferredSize();
        }

        public void setText(String str) {
            super.setText(str);
        }

        void setSelect(boolean isSelected) {
            Color bColor;
            if (isSelected) {
                if (customLabelSelectionBackground != null)
                    bColor = customLabelSelectionBackground;
                else
                    bColor = defaultSelectionBackground;
            }
            else
                bColor = defaultBackground;
            super.setBackground(bColor);
        }

        void setFocus(boolean hasFocus) {
            if (hasFocus) {
                Color lineColor = defaultSelectionBorderColor;
                setBorder(BorderFactory.createLineBorder(lineColor));
            } else {
                setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        }
    }
}
