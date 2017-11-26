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

package org.moonwave.dconfig.ui;

import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.model.DConfigDataType;

/**
 *
 * Custom Data Type column cell renderer
 *
 * @author Jonathan Luo
 */
public class DataTypeRenderer extends JComboBox {
    
    public DataTypeRenderer(JTable table, TableColumn dataTypeColumn) {
        super();
        List list = DConfigDataTypeDao.getDataTypeList();
        for (int i = 0; i < list.size(); i++) {
        	DConfigDataType dataType = (DConfigDataType) list.get(i);
            addItem(dataType.getDataTypeName());
        }
        dataTypeColumn.setCellEditor(new DefaultCellEditor(this));
        
        //should show as combo box in view mode
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        dataTypeColumn.setCellRenderer(renderer);
    }
}
