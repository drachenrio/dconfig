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

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTable;
import javax.swing.JTree;
import org.moonwave.dconfig.ui.ckboxtree.CheckTreeNodeRenderer;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.TableContext;

/**
 *
 * @author Jonathan Luo
 */
public class TableFocusListener implements FocusListener {
    Color selectionForeground;
    Color selectionBackground;
    Color lostFocusSelectionForeground;
    Color lostFocusSelectionBackground;
    
    public TableFocusListener() {
        selectionForeground = AppContext.getTable().getSelectionForeground();
        selectionBackground = AppContext.getTable().getSelectionBackground();

        lostFocusSelectionForeground = Color.WHITE;
        lostFocusSelectionBackground = new Color(248, 193, 171);//Color.LIGHT_GRAY;RGB ..LIGHT_GRAY;
    }

    /**
     * Invoked when a component gains the keyboard focus.
     */
    public void focusGained(FocusEvent e) {
        //set tree as focus lost color
        TableContext tableContext = AppContext.tableContext();
        JTree tree = AppContext.getTree();
        JTable table = AppContext.getTable();

        table.setSelectionForeground(selectionForeground);
        table.setSelectionBackground(selectionBackground);
        AppContext.appContext().setFocusComponent(AppContext.Focus.table);
        //cancel tree cell editing
        if (tree.getCellEditor() != null)
            tree.getCellEditor().cancelCellEditing();

        CheckTreeNodeRenderer renderer = (CheckTreeNodeRenderer) tree.getCellRenderer();
        renderer.setForeground(selectionForeground);
        renderer.setCustomLabelSelectionBackground(lostFocusSelectionBackground);
        renderer.setBackground(lostFocusSelectionBackground);
               
        //AppContext.appContext().getStatus().showKey();        
        AppContext.appContext().getStateController().setTableSelectionStates();
    };

    /**
     * Invoked when a component loses the keyboard focus.
     */
    public void focusLost(FocusEvent e) {
        AppContext.getTable().setSelectionForeground(lostFocusSelectionForeground);
    };
}
