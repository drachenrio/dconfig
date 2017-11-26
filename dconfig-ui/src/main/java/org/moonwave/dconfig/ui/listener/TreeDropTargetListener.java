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

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.ui.action.MenuActionHandler;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.DConfigSelection;
import org.moonwave.dconfig.ui.util.TreeUtil;

/**
 *
 * @author Jonathan Luo
 *
 */

public class TreeDropTargetListener implements DropTargetListener {

    private static final Log log = LogFactory.getLog(TreeDropTargetListener.class);	

    DefaultMutableTreeNode draggedNode;
    DefaultMutableTreeNode targetNode;

    public TreeDropTargetListener() {
    }

    /**
     * Called while a drag operation is ongoing, when the mouse pointer enters
     * the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener. 
     * 
     * @param dtde the <code>DropTargetDragEvent</code> 
     */
    public void dragEnter(DropTargetDragEvent dtde) {
        JTree tree = (JTree)((DropTarget) dtde.getSource()).getComponent();
        draggedNode = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
        if (!AppContext.treeContext().isDragEnabled()) {
            return;
        }
    }

    /**
     * Called when a drag operation is ongoing, while the mouse pointer is still
     * over the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener.
     * 
     * @param dtde the <code>DropTargetDragEvent</code> 
     */
    public void dragOver(DropTargetDragEvent dtde) {
        JTree tree = (JTree)((DropTarget) dtde.getSource()).getComponent();
        draggedNode = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
        if (!AppContext.treeContext().isDragEnabled()) {
            return;
        }
    }

    /**
     * Called if the user has modified the current drop gesture.
     * <P>
     * @param dtde the <code>DropTargetDragEvent</code>
     */
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    /**
     * Called while a drag operation is ongoing, when the mouse pointer has
     * exited the operable part of the drop site for the
     * <code>DropTarget</code> registered with this listener.
     * 
     * @param dte the <code>DropTargetEvent</code> 
     */
    public void dragExit(DropTargetEvent dte) {
    }


    /**
     * Called when the drag operation has terminated with a drop on
     * the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener.  
     *
     * @param dtde the <code>DropTargetDropEvent</code> 
     */
    public void drop(DropTargetDropEvent dtde) {
        if (!AppContext.treeContext().isDragEnabled()) {
            dtde.rejectDrop();
            return;
        }
        Point pt = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent(); // or = (JTree)((DropTarget) dtde.getSource()).getComponent();
        TreePath targetNodePath = tree.getPathForLocation(pt.x, pt.y);
        if (targetNodePath == null) {
            dtde.rejectDrop();
            return;
        }
        targetNode = (DefaultMutableTreeNode) targetNodePath.getLastPathComponent();
        draggedNode = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
        TreePath draggedNodeTreePath = TreeUtil.toTreePath(draggedNode);
        DefaultMutableTreeNode draggedNodeParent = (DefaultMutableTreeNode) draggedNode.getParent();
        if (targetNode.isRoot()) {
            dtde.rejectDrop();
            return;
        }
        
        boolean actionNeeded = true;
        if (targetNodePath.equals(draggedNodeTreePath)) { // drags a node to itself
            actionNeeded = false;
        }
        else if (targetNode.isNodeChild(draggedNode)) { // draggedNode is the child of targetNode, i.e. drag a node to its own parent, no action is needed
            actionNeeded = false;
        }
        else if (targetNode.isNodeAncestor(draggedNode)){ // draggedNode is ancestor of targetNode, i.e. drag a node to its descendant, no action is needed
            actionNeeded = false;
        }
        if (actionNeeded) {
            try {
                dtde.acceptDrop(dtde.getDropAction());
                Transferable tr = dtde.getTransferable();
                if (tr.isDataFlavorSupported(DConfigSelection.dconfigFlavor)) {
                    DConfig draggedCfgObj = (DConfig) tr.getTransferData(DConfigSelection.dconfigFlavor);
                    DConfig draggedNodeParentCfgObj = (DConfig) draggedNodeParent.getUserObject();
                    DConfig targetCfgObj = (DConfig) targetNode.getUserObject();
                    TreeUtil.setSelectionPath(targetNode);                            
                    MenuActionHandler.doPaste(draggedCfgObj);
                    
                    if ((dtde.getDropAction() == DnDConstants.ACTION_MOVE)) {
                        //draggedNode, targetNode, draggedNodeParent are invalid after doPaste() call; Query them again before use
                        draggedNode = TreeUtil.findNode(draggedCfgObj.getKeyName());
                        TreeUtil.setSelectionPath(draggedNode);
                        TreeUtil.removeCurrentNodeAndChildren(true);
                        draggedNodeParent = TreeUtil.findNode(draggedNodeParentCfgObj.getKeyName());
                        TreeUtil.setSelectionPath(draggedNodeParent);
                        tree.requestFocusInWindow();
                    }
                    targetNode = TreeUtil.findNode(targetCfgObj.getKeyName());
                    TreeUtil.setSelectionPath(targetNode);
                    tree.requestFocusInWindow();
                    dtde.dropComplete(true);
                }
            } catch (Exception e) {
                log.error(e, e);
                dtde.dropComplete(false);
            }
        }
    }
}
