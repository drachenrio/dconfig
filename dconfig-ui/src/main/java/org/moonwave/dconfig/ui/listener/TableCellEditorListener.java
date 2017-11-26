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

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import org.moonwave.dconfig.ui.*;

import org.moonwave.dconfig.ui.util.DialogUtil;
import org.moonwave.dconfig.ui.util.TableUtil;

/**
 *
 * Custom TableCellEditorListener.
 *
 * @author Jonthan Luo
 */
public class TableCellEditorListener implements CellEditorListener {

    public TableCellEditorListener() {
    }
    
    /**
     * This tells the listeners the editor has ended editing
     */
    public void editingStopped(ChangeEvent e) {
    	if (TableUtil.isAttributeDuplicate()) {
            String attributeName = TableUtil.getSelectedAttributeName();
            DialogUtil.showAttributeExistError(attributeName);
            TableUtil.restorePreviousAttributeName();
    	}
    }

    
    /** 
     * This tells the listeners the editor has canceled editing
     */
    public void editingCanceled(ChangeEvent e) {
    }
}
