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

package org.moonwave.dconfig.ui.listener;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTable;
import javax.swing.JTree;
import org.moonwave.dconfig.ui.ckboxtree.CheckTreeNodeRenderer;
import org.moonwave.dconfig.ui.model.AppContext;

/**
 * Custom FocusListener for tree.
 *
 * @author Jonathan Luo
 */
public class TreeFocusListener implements FocusListener {
    Color selectionForeground;
    Color selectionBackground;
    Color lostFocusSelectionForeground;
    Color lostFocusSelectionBackground;
    
    public TreeFocusListener() {
        JTree tree = AppContext.getTree();
        CheckTreeNodeRenderer renderer = (CheckTreeNodeRenderer) tree.getCellRenderer();
        selectionForeground = renderer.getForeground();
        selectionBackground = renderer.getBackgroundSelectionColor();
        selectionBackground = renderer.getBackground();
                
        lostFocusSelectionForeground = renderer.getForeground();//Color.WHITE;
        lostFocusSelectionBackground = new Color(248, 193, 171); // Color.LIGHT_GRAY;RGB ..LIGHT_GRAY; 
    }
    
    /**
     * Invoked when a component gains the keyboard focus.
     */
    public void focusGained(FocusEvent e) {        
        JTree tree = AppContext.getTree();
        JTable table = AppContext.getTable();
        CheckTreeNodeRenderer renderer = (CheckTreeNodeRenderer) tree.getCellRenderer();
        renderer.setForeground(selectionForeground);
        renderer.setBackgroundSelectionColor(selectionBackground);
        renderer.setBackground(selectionBackground);
        renderer.setCustomLabelSelectionBackground(null);
        
        AppContext.appContext().setFocusComponent(AppContext.Focus.tree);
        
        //set table as focus color
        table.setSelectionForeground(lostFocusSelectionForeground);
        table.setSelectionBackground(lostFocusSelectionBackground);
        
        //cancel table cell editing
        if (table.getCellEditor() != null)
            table.getCellEditor().cancelCellEditing();
        //show current key
        AppContext.appContext().getStatus().showKey();
        AppContext.appContext().getStateController().setTreeSelectionStates();
    };

    /**
     * Invoked when a component loses the keyboard focus.
     */
    public void focusLost(FocusEvent e) {
    };    
}
