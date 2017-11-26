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

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.moonwave.dconfig.model.DConfig;

/**
 *
 * @author Jonathan Luo
 */
public class TreeDragGestureListener implements DragGestureListener {
    
    TreeDragSourceListener dragSourceListener;

    public TreeDragGestureListener(TreeDragSourceListener dragSourceListener) {
        this.dragSourceListener = dragSourceListener;
    }
    
    public void dragGestureRecognized(DragGestureEvent dge) {
        JTree tree = (JTree) dge.getComponent();
        TreePath path = tree.getSelectionPath();
        if ((path == null) || (path.getPathCount() <= 1)) {
          // Ignore empty selection
          return;
        }
        DefaultMutableTreeNode draggedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (draggedNode.isRoot()) // not allow moving the root
            return;

        TransferableTreeNode transferable = new TransferableTreeNode((DConfig)draggedNode.getUserObject());
        try {
            dge.startDrag(DragSource.DefaultMoveDrop, transferable, dragSourceListener);
        } catch (Exception e) {
            System.out.println("   <<!--- exception DragSource.dragGestureRecognized() - startDrag " + e.getMessage());
        }
    }
}
