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

package org.moonwave.dconfig.ui.listener;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.ui.*;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.AttributeSelection;
import org.moonwave.dconfig.ui.model.DConfigSelection;
import org.moonwave.dconfig.ui.model.DataRow;

import org.moonwave.dconfig.ui.util.ImageUtil;
import org.moonwave.dconfig.ui.util.MenuUtil;
import org.moonwave.dconfig.ui.util.TableUtil;

/**
 * Implementation of Popup Listener.
 *
 * @author Jonathan Luo
 */
public class PopupListener extends MouseAdapter {
    JPopupMenu popup;

    public PopupListener(JPopupMenu popupMenu) {
        popup = popupMenu;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            checkPopup(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            checkPopup(e);
        }
    }
    
    private void disableAllTreePopupMenus() {
        MenuUtil.setEnable(popup, MenuCreator.NEWKEY, false);
        MenuUtil.setEnable(popup, MenuCreator.NEWATTR, false);
        MenuUtil.setEnable(popup, MenuCreator.OPERATION, false);      
        MenuUtil.setEnable(popup, MenuCreator.OPERATIONGRP, false);
        MenuUtil.setEnable(popup, MenuCreator.DELETE, false);
        MenuUtil.setEnable(popup, MenuCreator.RENAME, false);
        MenuUtil.setEnable(popup, MenuCreator.CUT, false);
        MenuUtil.setEnable(popup, MenuCreator.COPY, false);
        MenuUtil.setEnable(popup, MenuCreator.PASTE, false);
    }

    private void checkPopup(MouseEvent e) {
        Object obj = e.getComponent();
        obj = e.getSource();
        if (obj instanceof JTree) {
            disableAllTreePopupMenus();
            JTree tree = (JTree) obj;
            tree.requestFocusInWindow();
            TreePath treePath = tree.getClosestPathForLocation(e.getX(), e.getY());
            if (treePath != null)
                tree.setSelectionPath(treePath);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            // set up popup menu states
            JMenuItem menuItem = MenuUtil.findMenuItem(popup, MenuCreator.EXPAND_COLLAPSE);
            if (menuItem != null)
                menuItem.setEnabled(true);
            if (node.isLeaf()) {
                if (menuItem != null)
                    menuItem.setEnabled(false);
                    menuItem.setDisabledIcon(null);
            } else {                	
                if (tree.isCollapsed(treePath)) {
                    menuItem.setText("Expand");
                    menuItem.setMnemonic(KeyEvent.VK_X);
                } else {
                    menuItem.setText("Collapse");
                    menuItem.setMnemonic(KeyEvent.VK_O);
                }                	
            }
            menuItem = MenuUtil.findMenuItem(popup, MenuCreator.EXPAND_COLLAPSE_ALL);
            if (menuItem != null)
                menuItem.setEnabled(true);
            if (node.isLeaf()) {
                if (menuItem != null)
                    menuItem.setEnabled(false);
                    menuItem.setDisabledIcon(null);
            } else {                	
                if (tree.isCollapsed(treePath)) {
                    menuItem.setText("Expand All");
                    menuItem.setMnemonic(KeyEvent.VK_A);
                    menuItem.setIcon(ImageUtil.createImageIcon("images/expandall.gif", ""));               	
                    menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/expandall.gif", ""));               	
                } else {
                    menuItem.setText("Collapse All");
                    menuItem.setMnemonic(KeyEvent.VK_A);
                    menuItem.setIcon(ImageUtil.createImageIcon("images/collapseall16.gif", ""));
                    menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/collapseall16d.gif", ""));
                }                	
            }
            JMenuItem readOnly = MenuUtil.findMenuItem(MenuCreator.menuBar, MenuCreator.READONLY);
            boolean enable = ((readOnly != null) && readOnly.isSelected()) ? false : true;
            MenuUtil.setEnable(popup, MenuCreator.NEWKEY, enable);
            MenuUtil.setEnable(popup, MenuCreator.CUT, enable);
            MenuUtil.setEnable(popup, MenuCreator.COPY, enable);
            //AppContext.getTree().setEditable(false);
            DConfig cfg = (DConfig) AppContext.treeContext().getSelectedUserObject();
            if (AppContext.appContext().isUpdateMode()) {
                AppContext.getTree().setEditable(true);
                if (cfg != null) {
                    if (!cfg.getKey().isRoot()) {
                        MenuUtil.setEnable(popup, MenuCreator.NEWKEY, enable);
                        MenuUtil.setEnable(popup, MenuCreator.NEWATTR, enable);
                        MenuUtil.setEnable(popup, MenuCreator.OPERATION, enable);
                        MenuUtil.setEnable(popup, MenuCreator.OPERATIONGRP, enable);
                        MenuUtil.setEnable(popup, MenuCreator.DELETE, enable);
                        MenuUtil.setEnable(popup, MenuCreator.RENAME, enable);
                    }
                }
            }
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = cb.getContents(AppContext.appContext().cfgEditor);
            enable &= ((t != null) && (t.isDataFlavorSupported(AttributeSelection.dconfigAttributeFlavor) || 
                                       t.isDataFlavorSupported(DConfigSelection.dconfigFlavor)));
            MenuUtil.setEnable(popup, MenuCreator.PASTE, enable);
        } else if (obj instanceof JTable) {
            JTable table = (JTable) obj;
            table.requestFocusInWindow();
            int row = table.rowAtPoint(e.getPoint());
            table.setRowSelectionInterval(row, row);
            table.setColumnSelectionInterval(0, table.getColumnCount() - 1);
            // set up popup menu states
            JMenuItem readOnly = MenuUtil.findMenuItem(MenuCreator.menuBar, MenuCreator.READONLY);
            boolean enable = ((readOnly != null) && readOnly.isSelected()) ? false : true;
            DataRow dataRow = TableUtil.getSelectedDataRow();
            if ((dataRow != null) && dataRow.isInherited())
                enable = false;
            MenuUtil.setEnable(popup, MenuCreator.DELETE, enable);
            MenuUtil.setEnable(popup, MenuCreator.RENAME, enable);
            MenuUtil.setEnable(popup, MenuCreator.CUT, enable);
            MenuUtil.setEnable(popup, MenuCreator.COPY, enable);

            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = cb.getContents(AppContext.appContext().cfgEditor);
            enable = ((readOnly != null) && readOnly.isSelected()) ? false : true;
            enable &= ((t != null) && t.isDataFlavorSupported(AttributeSelection.dconfigAttributeFlavor));
            MenuUtil.setEnable(popup, MenuCreator.PASTE, enable);
        } else if (obj instanceof JScrollPane) {
            AppContext.getTable().requestFocusInWindow();
            JMenuItem readOnly = MenuUtil.findMenuItem(MenuCreator.menuBar, MenuCreator.READONLY);
            boolean enable = ((readOnly != null) && readOnly.isSelected()) ? false : true;
            MenuUtil.setEnable(popup, MenuCreator.NEWKEY, enable);
            MenuUtil.setEnable(popup, MenuCreator.NEWATTR, enable);
            MenuUtil.setEnable(popup, MenuCreator.OPERATION, enable);
            MenuUtil.setEnable(popup, MenuCreator.OPERATIONGRP, enable);

            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = cb.getContents(AppContext.appContext().cfgEditor);
            enable &= ((t != null) && t.isDataFlavorSupported(AttributeSelection.dconfigAttributeFlavor));
            MenuUtil.setEnable(popup, MenuCreator.PASTE, enable);
        }
        popup.show(e.getComponent(), e.getX(), e.getY());
    }
}
