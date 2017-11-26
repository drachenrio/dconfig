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

package org.moonwave.dconfig.ui.model;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.moonwave.dconfig.util.Constants;

/**
 *
 * @author Jonathan Luo
 */
public class DConfigTable extends JTable {
    
    public DConfigTable() {
    }
    
    public DConfigTable(TableModel model) {
        super(model);
    }
            
    /** 
     * Implements table cell tool tips.
     */
    @Override
    public String getToolTipText(MouseEvent e) {
        String tip = "";
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        int actualColumnIndex = convertColumnIndexToModel(colIndex);
        AppContext.tableContext().setTooltips(true);
        if (actualColumnIndex == TableModelImpl.IDX_INHERITANCE) {
            TableModel model = getModel();
            tip = (String) model.getValueAt(rowIndex, TableModelImpl.IDX_INHERITANCE);
            if (!tip.equalsIgnoreCase(Constants.EMPTY_STRING))
                tip = "inherited from " + tip;
        } else {
            tip = super.getToolTipText(e);
        }
        AppContext.tableContext().setTooltips(false);
        return tip;
    }
}
