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

package org.moonwave.dconfig.ui.util;

import javax.swing.AbstractButton;

import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.MenuCreator;
import org.moonwave.dconfig.ui.model.DataRow;
import org.moonwave.dconfig.ui.model.TableModelImpl;

/**
 *
 * @author Jonathan Luo
 */
public class StateController {

    AbstractButton btnUpdateMode = ToolbarUtil.findToolBarButton(MenuCreator.UPDATEMODE);
    
    public void setInitialStates() {
    	MenuUtil.setItemEnable(MenuCreator.FINDNEXT, false);
    	MenuUtil.setItemEnable(MenuCreator.FINDPREVIOUS, false);
        MenuUtil.setEnable(MenuCreator.DISCONNECT, false);
    }
    
    public void updateButtonStates() {
        if (AppContext.appContext().getFocusComponent() == AppContext.Focus.tree)  {
            setTreeSelectionStates();
        } else {
            setTableSelectionStates();
        }
    }

    public void setTreeSelectionStates() {
    	disableAll();
    	if (AppContext.appContext().isUpdateMode()) {
            MenuUtil.setEnable(MenuCreator.NEWKEY, true);
            DConfig cfg = (DConfig) AppContext.treeContext().getSelectedUserObject();
            if (cfg != null) {
                TableModelImpl model = TableUtil.getCustomTableModel();
                if (cfg.hasChanged() || model.hasChanged())
                    MenuUtil.setEnable(MenuCreator.SAVE, true);
                if (!cfg.getKey().isRoot()) {
                    MenuUtil.setEnable(MenuCreator.NEWATTR, true);
                    MenuUtil.setEnable(MenuCreator.OPERATION, true);
                    MenuUtil.setEnable(MenuCreator.OPERATIONGRP, true);
                    MenuUtil.setEnable(MenuCreator.DELETE, true);
                    MenuUtil.setEnable(MenuCreator.RENAME, true);
                }
            }
    	}
    }

    public void setTableSelectionStates() {
    	disableAll();
        DConfig cfg = (DConfig) AppContext.treeContext().getSelectedUserObject();
    	if (btnUpdateMode.isSelected()) {
            TableModelImpl model = TableUtil.getCustomTableModel();
            if (((cfg != null) && cfg.hasChanged()) || model.hasChanged())
                MenuUtil.setEnable(MenuCreator.SAVE, true);
            MenuUtil.setEnable(MenuCreator.NEWATTR, true);
            MenuUtil.setEnable(MenuCreator.OPERATION, true);
            MenuUtil.setEnable(MenuCreator.OPERATIONGRP, true);
            DataRow dataRow = TableUtil.getSelectedDataRow();
            if ((dataRow != null) && !dataRow.isInherited()) {
                MenuUtil.setEnable(MenuCreator.DELETE, true);
                MenuUtil.setEnable(MenuCreator.RENAME, true);
            } else {
                MenuUtil.setEnable(MenuCreator.DELETE, false);
                MenuUtil.setEnable(MenuCreator.RENAME, false);                
            }
    	}
    }
    
    protected void disableAll() {
        MenuUtil.setEnable(MenuCreator.NEWKEY, false);
        MenuUtil.setEnable(MenuCreator.NEWATTR, false);
        MenuUtil.setEnable(MenuCreator.OPERATION, false);
        MenuUtil.setEnable(MenuCreator.OPERATIONGRP, false);
        MenuUtil.setEnable(MenuCreator.DELETE, false);
        MenuUtil.setEnable(MenuCreator.RENAME, false);
        MenuUtil.setEnable(MenuCreator.SAVE, false);
    }
    
    protected void enableAll() {
        MenuUtil.setEnable(MenuCreator.NEWKEY, true);
        MenuUtil.setEnable(MenuCreator.NEWATTR, true);
        MenuUtil.setEnable(MenuCreator.OPERATION, true);
        MenuUtil.setEnable(MenuCreator.OPERATIONGRP, true);
        MenuUtil.setEnable(MenuCreator.DELETE, true);
        MenuUtil.setEnable(MenuCreator.RENAME, true);
        MenuUtil.setEnable(MenuCreator.SAVE, true);
    }    
}
