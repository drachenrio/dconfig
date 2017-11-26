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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.ui.*;
import org.moonwave.dconfig.ui.util.TreeUtil;
import org.moonwave.dconfig.util.Constants;

/**
 *
 * Saves states of tree objects.
 *
 * @author Jonathan Luo
 */

public class TreeContext {    

    private boolean dragEnabled;
    private boolean cut;
    
    public TreeContext() {
    }
    
    /**
     * Turns on or off drag handling.
     *
     * @param b whether or not to enable drag handling
     */
    public void setDragEnabled(boolean b) {
	dragEnabled = b;
    }

    /**
     * Returns whether or not drag is enabled.
     */
    public boolean isDragEnabled() {
	return dragEnabled;
    }

    /**
     * Sets cut operation flag.
     *
     * @param b cut operation flag.
     */
    public void setCut(boolean b) {
	cut = b;
    }

    /**
     * Returns whether or not it is in cut operation state..
     */
    public boolean isCut() {
	return cut;
    }
    
    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) AppContext.getTree().getModel().getRoot();
    }

    public TreePath getSelectionPath() {
    	return AppContext.getTree().getSelectionPath();
    }
    
    public DefaultMutableTreeNode getSelectedNode() {
    	if (AppContext.getTree().getSelectionPath() != null) {
            return (DefaultMutableTreeNode) AppContext.getTree().getSelectionPath().getLastPathComponent();
    	} else {
            return null;
    	}
    }

    public DConfig getSelectedUserObject() {
        DefaultMutableTreeNode node = getSelectedNode();
        return ((node != null) ? (DConfig)node.getUserObject() : null);
    }
    
    /**
     * Gets the current (dot-separated) key representation. 
     *
     * @return current key.
     */
    public String getCurrentKey() {
    	return TreeUtil.getKey(getSelectionPath());
    }
    
    /**
     * Gets the UI (dot-separated) key representation. 
     *
     * @return UI (dot-separated) key representation. 
     */
    public String getUIKey() {
        String key = "";
        TreePath currentTreePath = getSelectionPath();
        if (currentTreePath != null) {
            Object[] nodes = currentTreePath.getPath();
            for (int i = 1; i < nodes.length; i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes[i];
                if (i > 1) // nodes[0] is the root with nodename '/'
                    key += Constants.KEY_SEPARATOR;
                if (node.getUserObject() instanceof DConfig)
                    key += ((DConfig)node.getUserObject()).getLastKeyName();
                else
                    key += node.getUserObject();
            }
        }
        return key;
    }
}
