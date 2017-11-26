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

package org.moonwave.dconfig.ui.model;

import java.util.List;

import org.moonwave.dconfig.ui.action.MenuActionHandler;

/**
 *
 * Saves states of table objects.
 *
 * @author Jonathan Luo
 */
public class TableContext {

    List<DataRow> dataRowOrig; // saves data before any changes
    List<DataRow> dataRow; //
    String selectedAttributeName;
    int selectedViewRow;
    int selectedViewCol;
    boolean tooltips; // tooltips mode if true
    
    public TableContext() {
    }
    
    public List<DataRow> getTableRowsOrig() {
        return this.dataRowOrig;
    }
    public void setTableRowsOrig(List<DataRow> dataRowOrig) {
        this.dataRowOrig = dataRowOrig;
    }
    public List<DataRow> getTableRows() {
        return this.dataRow;
    }
    public void setTableRows(List<DataRow> dataRow) {
        this.dataRow = dataRow;
    }
    public void toggleUpdateViewMode() {
        MenuActionHandler.setUpdateViewMode();        
    }
    public int getSelectedRow() {    	
    	return AppContext.getTable().getSelectedRow();
    }
    public int getSelectedColumn() {
    	return AppContext.getTable().getSelectedColumn();
    }
    public int getSelectedViewRow() {
    	return selectedViewRow;
    }
    public void setSelectedViewRow(int row) {
    	selectedViewRow = row;
    }
    public int getSelectedViewCol() {
    	return selectedViewCol;
    }
    public void setSelectedViewCol(int col) {
    	selectedViewCol = col;
    }
    public String getSelectedAttributeName() {
        return selectedAttributeName;
    }
    public void setSelectedAttributeName(String selectedAttributeName) {
        this.selectedAttributeName = selectedAttributeName;
    }
    public boolean isTooltips() {
    	return tooltips;
    }
    public void setTooltips(boolean isTooltips) {
    	this.tooltips = isTooltips;
    }
}
