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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.ui.MenuCreator;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.AttributeSelection;
import org.moonwave.dconfig.ui.model.DConfigSelection;
import org.moonwave.dconfig.ui.model.DataRow;
import org.moonwave.dconfig.ui.util.MenuUtil;
import org.moonwave.dconfig.ui.util.TableUtil;

/**
 * Setup submenu states before menu is shown.
 *
 * @author Jonathan Luo
 */
public class MainMenuListener implements MenuListener {
    
    public MainMenuListener() {
    }

    public void menuCanceled(MenuEvent e) {
    }

    public void menuDeselected(MenuEvent e) {
    }

    public void menuSelected(MenuEvent e) {
        setMenuStates(e);
    }

    private void setMenuStates(MenuEvent e) {
        JMenu menu = (JMenu) e.getSource();
        if (menu.getActionCommand().equals("Edit")) {
            if (AppContext.appContext().getFocusComponent() == AppContext.Focus.tree) {
                // similar to logic in PopupListener.checkPopup()
                disableAllSubMenusForTree(menu);
                JMenuItem readOnly = MenuUtil.findMenuItem(MenuCreator.menuBar, MenuCreator.READONLY);
                boolean enable = ((readOnly != null) && readOnly.isSelected()) ? false : true;
                MenuUtil.setEnable(menu, MenuCreator.NEWKEY, enable);
                MenuUtil.setEnable(menu, MenuCreator.CUT, enable);
                MenuUtil.setEnable(menu, MenuCreator.COPY, enable);
                //AppContext.getTree().setEditable(false);
                DConfig cfg = (DConfig) AppContext.treeContext().getSelectedUserObject();
                if (AppContext.appContext().isUpdateMode()) {
                    AppContext.getTree().setEditable(true);
                    if (cfg != null) {
                        if (!cfg.getKey().isRoot()) {
                            MenuUtil.setEnable(menu, MenuCreator.NEWKEY, enable);
                            MenuUtil.setEnable(menu, MenuCreator.NEWATTR, enable);
                            MenuUtil.setEnable(menu, MenuCreator.OPERATION, enable);
                            MenuUtil.setEnable(menu, MenuCreator.OPERATIONGRP, enable);
                            MenuUtil.setEnable(menu, MenuCreator.DELETE, enable);
                            MenuUtil.setEnable(menu, MenuCreator.RENAME, enable);
                        }
                    }
                }
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable t = cb.getContents(AppContext.appContext().cfgEditor);
                enable &= ((t != null) && (t.isDataFlavorSupported(AttributeSelection.dconfigAttributeFlavor) || 
                                           t.isDataFlavorSupported(DConfigSelection.dconfigFlavor)));
                MenuUtil.setEnable(menu, MenuCreator.PASTE, enable);
            } else if (AppContext.appContext().getFocusComponent() == AppContext.Focus.table) {
                // similar to logic in PopupListener.checkPopup()
                disableAllSubMenus(menu);
                JMenuItem readOnly = MenuUtil.findMenuItem(MenuCreator.menuBar, MenuCreator.READONLY);
                boolean enable = ((readOnly != null) && readOnly.isSelected()) ? false : true;
                DataRow dataRow = TableUtil.getSelectedDataRow();
                if (dataRow == null)
                    enable = false;                    
                else if (dataRow.isInherited())
                    enable = false;                
                MenuUtil.setEnable(menu, MenuCreator.DELETE, enable);
                MenuUtil.setEnable(menu, MenuCreator.RENAME, enable);
                MenuUtil.setEnable(menu, MenuCreator.CUT, enable);
                MenuUtil.setEnable(menu, MenuCreator.COPY, enable);

                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable t = cb.getContents(AppContext.appContext().cfgEditor);
                enable = ((readOnly != null) && readOnly.isSelected()) ? false : true;
                enable &= ((t != null) && t.isDataFlavorSupported(AttributeSelection.dconfigAttributeFlavor));
                MenuUtil.setEnable(menu, MenuCreator.PASTE, enable);
            }
        }
    }

    private void disableAllSubMenus(MenuElement menuElement) {
        MenuUtil.setEnable(menuElement, MenuCreator.DELETE, false);
        MenuUtil.setEnable(menuElement, MenuCreator.RENAME, false);
        MenuUtil.setEnable(menuElement, MenuCreator.CUT, false);
        MenuUtil.setEnable(menuElement, MenuCreator.COPY, false);
        MenuUtil.setEnable(menuElement, MenuCreator.PASTE, false);
    }

    private void disableAllSubMenusForTree(MenuElement menuElement) {
        MenuUtil.setEnable(menuElement, MenuCreator.NEWKEY, false);
        MenuUtil.setEnable(menuElement, MenuCreator.NEWATTR, false);
        MenuUtil.setEnable(menuElement, MenuCreator.OPERATION, false);
        MenuUtil.setEnable(menuElement, MenuCreator.OPERATIONGRP, false);
        disableAllSubMenus(menuElement);
    }
}
