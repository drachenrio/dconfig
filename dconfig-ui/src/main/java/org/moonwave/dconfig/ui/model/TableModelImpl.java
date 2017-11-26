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
 * This library is distributed in the hop120e that it will be useful,
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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.ui.util.TableUtil;
import org.moonwave.dconfig.util.AppState;

/**
 *
 * @author Jonathan Luo
 *
 */
public class TableModelImpl extends AbstractTableModel {

    public static int IDX_INHERITANCE       = 0; 
    public static int IDX_ATTRIBUTE_NAME    = 1; 
    public static int IDX_DATA_TYPE         = 2; 
    public static int IDX_ATTRIBUTE_VALUE   = 3; 
    public static int IDX_COMMMENTS         = 4;
    public static String[] columnNames = {"#", "Attribute Name", "Data Type / Action ", "Value", "Comments"};
    
    private List dataRowList = new ArrayList();
    private int dataRowListSizeOld;
    
    private int nextAttributeCount;
    public TableModelImpl() {
    }

    /**
     * Clears all table data.
     */
    public void clear() {
    	dataRowList.clear();
        dataRowListSizeOld = 0;
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Allows all cells editable if in update mode.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        if (AppContext.isUpdateMode())
            return true;
        else {
            if ((col == IDX_ATTRIBUTE_VALUE) || (col == IDX_COMMMENTS))
                return true; // allow open LongText window
            else
                return false;
        }
    }

    /**
     * Returns a list of <code>DConfigAttribute</code> objects from table model
     * data, inlcuding attributes marked as deleted.
     */
    public List<DConfigAttribute> getAllAttributes() {
        List<DConfigAttribute> attributeList = new ArrayList<DConfigAttribute>();
        for (int i = 0; i < dataRowList.size(); i++) {
            DataRow dataRow = (DataRow) dataRowList.get(i);
            attributeList.add(dataRow.getAttribute());
        }
    	return attributeList;
    }

    /**
     * Returns a list of <code>DConfigAttribute</code> objects from table model
     * data, not include deleted attributes.
     */
    public List<DConfigAttribute> getAttributes() {
        List<DConfigAttribute> attributeList = new ArrayList<DConfigAttribute>();
        for (int i = 0; i < dataRowList.size(); i++) {
            DataRow dataRow = (DataRow) dataRowList.get(i);
            if (!dataRow.isDelete())
                attributeList.add(dataRow.getAttribute());
        }
    	return attributeList;
    }
    
    /**
     * Populates table data list with data from db.
     *
     * @param attributeList attribute list used to populate table.
     */
    public void populateTable(List<DConfigAttribute> attributeList) {
        // set values in table
    	nextAttributeCount = 1;
        this.clear();
        for (int i = 0; i < attributeList.size(); i++) {
            DConfigAttribute attribute = (DConfigAttribute)attributeList.get(i);
            attribute.makeAValueCopy();
            DataRow dataRow = new DataRow(attribute);
            dataRowList.add(dataRow);
        }
        dataRowListSizeOld = attributeList.size();
        fireTableDataChanged();
    }

    public void addNewRow(String defaultType) {
    	DConfigAttribute attribute = new DConfigAttribute();
    	String key = AppContext.treeContext().getCurrentKey();
        DConfig dcfg = AppContext.treeContext().getSelectedUserObject();
        if (dcfg != null) {        
            attribute.setKeyId(dcfg.getKey().getId());
            int nextAttributeId = TableUtil.getNextNewAttributeNumber();
            attribute.setAttributeName(TableUtil.newAttribute + nextAttributeId);
            attribute.setDataTypeName(defaultType);
            attribute.setAttributeValue("");
            attribute.setComments("");
            dataRowList.add(new DataRow(attribute));
        }
    }

    public void addNewRow(DConfigAttribute attribute) {
    	String key = AppContext.treeContext().getCurrentKey();
        DConfig dcfg = AppContext.treeContext().getSelectedUserObject();
        if (dcfg != null) {        
            attribute.setKeyId(dcfg.getKey().getId());
            int nextAttributeId = TableUtil.getNextNewAttributeNumber(attribute.getAttributeName());
            if (nextAttributeId > 0)
                attribute.setAttributeName(attribute.getAttributeName() + nextAttributeId);
            dataRowList.add(new DataRow(attribute));
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns active row counts.
     */
    public int getRowCount() {
    	int count = 0;
    	for (int i = 0; i < dataRowList.size(); i++) {
            DataRow dataRow = (DataRow) dataRowList.get(i);
            if (!dataRow.isDelete())
                count++;
    	}
        return count;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int rowIn, int colIn) {
        Object obj = "";
        int row = 0;
        for (int i = 0;  i < dataRowList.size(); i++) {
            DataRow dataRow = (DataRow) dataRowList.get(i);
            if (dataRow.isDelete())
                continue;
            if (row == rowIn) {
                if (colIn == IDX_INHERITANCE) {
                    if (AppContext.tableContext().isTooltips()) {
                        obj = dataRow.getInheritedFrom();
                    } else
                        obj = dataRow.getInherited();
                }
                else if (colIn == IDX_ATTRIBUTE_NAME)
                    obj = dataRow.getAttributeName();
                else if (colIn == IDX_DATA_TYPE)
                    obj = dataRow.getDataTypeName();
                else if (colIn == IDX_ATTRIBUTE_VALUE)
                    obj = dataRow.getValueText();
                else if (colIn == IDX_COMMMENTS)
                    obj = dataRow.getCommentsText();
                break;
            }
            row++;
        }
        return obj;
    }
    
    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int rowin, int col) {        
        String valueToset = null;
        if (value instanceof LongText)
            valueToset = ((LongText)value).getText();
        else
            valueToset = (String) value;
        int row = 0;
        for (int i = 0;  i < dataRowList.size(); i++) {
            DataRow dataRow = (DataRow) dataRowList.get(i);
            if (dataRow.isDelete())
                continue;
            if (row == rowin) {
                if (col == IDX_INHERITANCE)
                    dataRow.setInherited((String)value);
                else if (col == IDX_ATTRIBUTE_NAME)
                    dataRow.setAttributeName((String)value);
                else if (col == IDX_DATA_TYPE)
                    dataRow.setDataTypeName((String)value);
                else if (col == IDX_ATTRIBUTE_VALUE)
                    dataRow.setValueText(valueToset);
                else if (col == IDX_COMMMENTS)
                    dataRow.setComments(valueToset);
                break;
            }
            row++;
        }

        fireTableCellUpdated(row, col);
        if (col == IDX_ATTRIBUTE_NAME) {
            if (isAttributeDuplicate((String)value, row)) {
                AppContext.appContext().getStatus().setText("Attribute is not unique, try again");
                if (AppState.isVerbose())
                    System.out.println("Attribute is not unique, try again");
                if (AppContext.getTable().isCellEditable(row, col)) {
                    int i = 0;
                }
            }
        }
    }

    public boolean isAttributeDuplicate(String attributeName, int rowIn) {
    	boolean bRet = false;
    	int row = 0;
    	for (int i = 0; i < dataRowList.size(); i++) {
            DataRow dataRow = (DataRow) dataRowList.get(i);
            if (dataRow.isDelete())
                continue;
            if ((row != rowIn) && dataRow.getAttributeName().equals(attributeName)) {
                bRet = true;
                break;
            }
            row++;
    	}
    	return bRet;
    }
    
    public boolean hasChanged() {
    	boolean bRet = false;
        if (this.getRowCount() != dataRowListSizeOld)
            return true;
        for (int i = 0; i < dataRowList.size(); i++) {
            DataRow dataRow = (DataRow)dataRowList.get(i);
            if (dataRow.hasChanged()) {
                bRet = true;
                break;
            }
        }
    	return bRet;
    }

    public DataRow getDataRowByAttributeName(String attributeName) {
    	DataRow dataRow = null;
    	for (int i = 0; i < dataRowList.size(); i++) {
            DataRow item = (DataRow) dataRowList.get(i);
            if (item.isDelete())
                continue;
            if (item.getAttributeName().equals(attributeName)) {
                dataRow = item;
                break;
            }
    	}
    	return dataRow;
    }

    public DConfigAttribute getAttributeByAttributeName(String attributeName) {
    	DataRow dataRow = getDataRowByAttributeName(attributeName);
        DConfigAttribute attribute = null;
        if (dataRow != null) {
            attribute = dataRow.getAttribute();
        }        
        return attribute;
    }

    public void deleteRowByAttributeName(String attributeName) {
    	DataRow dataRow = getDataRowByAttributeName(attributeName);
    	if (dataRow != null) {
            dataRow.setDelete(true);
    	}
    }

    /**
     * Adds a new row into table data list. Makes a copy to the original data list as well.  
     * @param dataRow
     */
    protected void createRow(DataRow dataRow) {    	
        dataRowList.add(dataRow);
    }
    
    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        if (AppState.isVerbose()) {
            for (int row=0; row < numRows; row++) {
                System.out.print("    row " + row + ":");
                for (int col=0; col < numCols; col++) {
                    System.out.print(" col: " + col + " " + getValueAt(row, col));
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }
}
