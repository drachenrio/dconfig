/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * DConfig - Free Dynamic Configuration Toolkit
 * Copyright (C) 2006,2007 Jonathan Luo
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

package org.moonwave.dconfig.ui.model;

import javax.swing.AbstractButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import org.moonwave.dconfig.ui.*;

import org.moonwave.dconfig.ui.util.StateController;
import org.moonwave.dconfig.ui.util.ToolbarUtil;

/**
 * Saves shared objects or states of the application.
 *
 * @author Jonathan Luo
 */
public class AppContext {    
    static public enum Focus {none, tree, table};
    
    static AppContext instance = new AppContext();
    public CfgEditor cfgEditor;    
    Focus focusComponent = Focus.none;
    TreeContext treeContext = new TreeContext();
    TableContext tableContext = new TableContext();
    StateController stateController;
    boolean connected;
    boolean disconnectAction;
            
    public static AppContext appContext() {
        return instance;
    }
    public static TreeContext treeContext() {
        return appContext().treeContext;
    }
    public static TableContext tableContext() {
        return appContext().tableContext;
    }
    public static boolean isUpdateMode() {
    	AbstractButton btn = ToolbarUtil.findToolBarButton(MenuCreator.UPDATEMODE);
        return btn.isSelected();
    }
    private AppContext() {
    }    
    public void setCfgEditor(CfgEditor regEdit) {
        this.cfgEditor = regEdit;
    }
    public CfgEditor getCfgEditor() {
        return cfgEditor;
    }
    public static JTree getTree() {
        return appContext().cfgEditor.getTree();
    }    
    public static JTable getTable() {
        return appContext().cfgEditor.getTable();
    }
    public static void setConnected(boolean b) {
        appContext().connected = b;
    }
    public static boolean isConnected() {
        return appContext().connected;
    }
    public static void setDisconnectAction(boolean b) {
        appContext().disconnectAction = b;
    }
    public static boolean isDisconnectAction() {
        return appContext().disconnectAction;
    }
    public JSplitPane getSplitPane() {
        return cfgEditor.getSplitPane();
    }
    public JScrollPane getTablePane() {
        return cfgEditor.getTablePane();
    }
    public TableSorter getTableSorter() {
        return (TableSorter)cfgEditor.getTable().getModel();
    }
    public Status getStatus() {
        return cfgEditor.txtStatus;
    }    
    public StateController getStateController() {
    	if (stateController == null) {
    		stateController = new StateController();
    	}
		return stateController;
	}
	/**
     * Get focus component flag, either tree or table.
     */
    public Focus getFocusComponent() {
        return focusComponent;
    }
    /**
     * Set focus component flag, either tree or table.
     */
    public void setFocusComponent(Focus focused) {
        this.focusComponent = focused;
    }
    public JToolBar getToolBar() {
        return cfgEditor.getToolbar();
    }
}
