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

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

/**
 *
 * @author Jonathan Luo
 */
public class CheckTreeCellEditor extends DefaultTreeCellEditor {

    boolean clickOnLabel;
    
    public CheckTreeCellEditor(JTree tree) {
        super(tree, null, null);
    }

    public CheckTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer, null);
    }

    public CheckTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer,
				 TreeCellEditor editor) {
        super(tree, renderer, editor);
    }

    /**
     * Starts label editing only when click on label hotspot.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isClickOnLabel())
            super.actionPerformed(e);
    }

    public boolean isClickOnLabel() {
        return clickOnLabel;
    }

    public void setClickOnLabel(boolean clickOnLabel) {
        this.clickOnLabel = clickOnLabel;
    }

    /**
     * Returns true if <code>event</code> is a <code>MouseEvent</code>
     * and the click count is 2
     * @param event  the event being studied
     */
    @Override
    protected boolean shouldStartEditingTimer(EventObject event) {
	if((event instanceof MouseEvent) &&
	    SwingUtilities.isLeftMouseButton((MouseEvent)event)) {
	    MouseEvent        me = (MouseEvent)event;

	    return (me.getClickCount() == 2 &&  inHitRegion(me.getX(), me.getY()));
	}
	return false;
    }

    @Override
    protected void determineOffset(JTree tree, Object value,
				   boolean isSelected, boolean expanded,
				   boolean leaf, int row) {
        offset = 0;
    }    
}
