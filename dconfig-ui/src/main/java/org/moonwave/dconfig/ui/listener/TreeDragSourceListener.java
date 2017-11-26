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

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import org.moonwave.dconfig.ui.model.AppContext;

/**
 *
 * @author Jonathan Luo
 *
 */

public class TreeDragSourceListener implements DragSourceListener {

    TransferableTreeNode transferable;

    public TreeDragSourceListener() {
    }

    /**
     * Drag Event Handlers
     */
    public void dragEnter(DragSourceDragEvent e) {
        DragSourceContext context = e.getDragSourceContext();
        if (!AppContext.treeContext().isDragEnabled()) {
            context.setCursor(DragSource.DefaultMoveNoDrop);
            return;
        }
        int dropAction = e.getDropAction();
        if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
            context.setCursor(DragSource.DefaultCopyDrop);
        } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
            context.setCursor(DragSource.DefaultMoveDrop);
        } else {
            context.setCursor(DragSource.DefaultCopyNoDrop);
        }
    }


    /**
     * Drag Event Handlers
     */
    public void dragOver(DragSourceDragEvent e) {
        DragSourceContext context = e.getDragSourceContext();
        if (!AppContext.treeContext().isDragEnabled()) {
            context.setCursor(DragSource.DefaultMoveNoDrop);
            return;
        }
        int dropAction = e.getDropAction();
        if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
            context.setCursor(DragSource.DefaultCopyDrop);
        } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
            context.setCursor(DragSource.DefaultMoveDrop);
        } else {
            context.setCursor(DragSource.DefaultCopyNoDrop);
        }
    }

    /**
     * Called when the user has modified the drop gesture.
     * This method is invoked when the state of the input
     * device(s) that the user is interacting with changes.
     * Such devices are typically the mouse buttons or keyboard
     * modifiers that the user is interacting with.
     * 
     * @param dsde the <code>DragSourceDragEvent</code>
     */
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    /**
     * Called as the cursor's hotspot exits a platform-dependent drop site.
     * 
     * @param dse the <code>DragSourceEvent</code>
     */
    public void dragExit(DragSourceEvent dse) {
        if (!AppContext.treeContext().isDragEnabled()) {
            return;
        }
        DragSourceContext context = dse.getDragSourceContext();
        context.setCursor(DragSource.DefaultMoveNoDrop);
    }


    /**
     * This method is invoked to signify that the Drag and Drop
     * operation is complete. The getDropSuccess() method of
     * the <code>DragSourceDropEvent</code> can be used to 
     * determine the termination state. The getDropAction() method
     * returns the operation that the drop site selected 
     * to apply to the Drop operation. Once this method is complete, the
     * current <code>DragSourceContext</code> and
     * associated resources become invalid.
     * 
     * @param dsde the <code>DragSourceDropEvent</code>
     */
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }
}
