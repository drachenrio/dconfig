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

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.moonwave.dconfig.ui.*;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.TableContext;

/**
 *
 * Implementation of table column selection listener.
 *
 * @author Jonathan Luo
 */
public class TableColumnSelectionListener implements ListSelectionListener {

    /** 
    * Called whenever the value of the selection changes.
    * @param e the event that characterizes the change.
    */
    public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting()) return;

        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (lsm.isSelectionEmpty()) {
        } else {
            int selectedCol = lsm.getMinSelectionIndex();
            TableContext tableContext = AppContext.tableContext();
            tableContext.setSelectedViewCol(selectedCol);
        }    
    }
}
