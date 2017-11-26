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

package org.moonwave.dconfig.ui.util;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import org.moonwave.dconfig.model.DConfigAttribute;

import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.DataRow;
import org.moonwave.dconfig.ui.model.TableContext;
import org.moonwave.dconfig.ui.model.TableModelImpl;
import org.moonwave.dconfig.ui.TableSorter;
import org.moonwave.dconfig.util.*;

/**
 *
 * @author Jonathan Luo
 */
public class TableUtil {
    public static final String newAttribute = "new attribute #";
    public static final int newAttributeLen = newAttribute.length(); 
    public TableUtil() {
    }
    
    
    /**
     * Checks whether current attribute is duplicated.
     * 
     * @return true if attribute is duplicate; false otherwise.
     */
    public static boolean isAttributeDuplicate() {
    	boolean bRet = false;    	
        TableContext tableContext = AppContext.tableContext();
        JTable table = AppContext.getTable();       
        TableSorter tableSorter = TableUtil.getTableModel();
        TableModelImpl model = TableUtil.getCustomTableModel();
        int row = tableContext.getSelectedViewRow();
                
        String newAttributeName = (String) getTableModel().getValueAt(row, TableModelImpl.IDX_ATTRIBUTE_NAME);
        
        int modelRow = tableSorter.modelIndex(row);
        bRet = model.isAttributeDuplicate(newAttributeName, modelRow);
        return bRet;
    }
    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     */
    public static void cancelCellEditing() {
        JTable table = AppContext.getTable();
        TableCellEditor cellEditor = table.getCellEditor();
        if (cellEditor != null) {
        	cellEditor.cancelCellEditing();
        }
    }
    /**
     * Tells the editor to stop editing and accept any partially edited
     * value as the value of the editor.  The editor returns false if
     * editing was not stopped; this is useful for editors that validate
     * and can not accept invalid entries.
     */
    public static void stopCellEditing() {
        JTable table = AppContext.getTable();
        TableCellEditor cellEditor = table.getCellEditor();
        if (cellEditor != null) {
        	cellEditor.stopCellEditing();
        }
    }
    /**
     * Starts to edit attribute name of the selected row.
     *
     */
    public static void editSelectedAttributeName() {
        TableContext tableContext = AppContext.tableContext();
        JTable table = AppContext.getTable();
        table.requestFocusInWindow();
        
        int attributeNameViewCol = table.convertColumnIndexToView(TableModelImpl.IDX_ATTRIBUTE_NAME);        
        table.editCellAt(table.getSelectedRow() , attributeNameViewCol);
        
        if (table.getEditorComponent() instanceof JTextField) {
        	JTextField c = (JTextField) table.getEditorComponent();
	        c.getCaret().setVisible(true);
	        c.setVisible(true);
	        c.setFocusable(true);
	        c.setSelectionStart(0);
	        c.setSelectionEnd(c.getText().trim().length());
	        c.requestFocusInWindow();
        }
    }
    
    public static int getNextNewAttributeNumber() {
    	int iMax = 0;
    	TableModelImpl impl = getCustomTableModel();
    	int count = impl.getRowCount();
    	for (int i = 0; i < count; i++) {
            String attributeName = (String) impl.getValueAt(i, TableModelImpl.IDX_ATTRIBUTE_NAME);
            int idx = attributeName.indexOf(newAttribute);
            if (idx != 0) // not starts with
                continue;
            String num = attributeName.substring(idx + newAttributeLen, attributeName.length());
            try {
                int iNum = Integer.parseInt(num);
                if (iNum > iMax)
                        iMax = iNum;
            } catch (Exception ex) {
            }
    	}
    	return iMax + 1;
    }
    
    public static int getNextNewAttributeNumber(String attributeNameToCheck) {
    	int iMax = 0;
        int attributeNameToCheckLen = attributeNameToCheck.length();
    	TableModelImpl impl = getCustomTableModel();
        boolean found = false;
        int count = impl.getRowCount();
    	for (int i = 0; i < count; i++) {
            String attributeName = (String) impl.getValueAt(i, TableModelImpl.IDX_ATTRIBUTE_NAME);
            int idx = attributeName.indexOf(attributeNameToCheck);
            if (idx != 0) // not starts with
                    continue;
            found = true;
            String num = attributeName.substring(idx + attributeNameToCheckLen, attributeName.length());
            try {
                    int iNum = Integer.parseInt(num);
                    if (iNum > iMax)
                            iMax = iNum;
            } catch (Exception ex) {
            }
    	}
        if (found)
            iMax++;
        return iMax;
    }

    public static String getSelectedAttributeName() {
        String attributeName = "";
        JTable table = AppContext.getTable();

        int row = AppContext.tableContext().getSelectedViewRow();
        int col = AppContext.tableContext().getSelectedViewCol();
        int rowCount = getTableModel().getRowCount();
        if ((row != -1) && (row < rowCount)) {
            attributeName = (String) getTableModel().getValueAt(row, TableModelImpl.IDX_ATTRIBUTE_NAME);
        }
    	return attributeName;
    }

    public static DConfigAttribute getSelectedAttribute() {
        TableModelImpl model = TableUtil.getCustomTableModel();
        String attributeName = TableUtil.getSelectedAttributeName();
        return model.getAttributeByAttributeName(attributeName);
    }

    public static DataRow getSelectedDataRow() {
        TableModelImpl model = TableUtil.getCustomTableModel();
        String attributeName = TableUtil.getSelectedAttributeName();
        return model.getDataRowByAttributeName(attributeName);
    }

    public static void saveSelectedAttributeName() {
    	String selectedAttributeName = getSelectedAttributeName();
    	AppContext.tableContext().setSelectedAttributeName(selectedAttributeName);
    }

    public static void restorePreviousAttributeName() {
        JTable table = AppContext.getTable();
        int row = AppContext.tableContext().getSelectedViewRow();
        int col = AppContext.tableContext().getSelectedViewCol();        
        if (row != -1) {
            table.setValueAt(AppContext.tableContext().getSelectedAttributeName(), row, col);
        }
    }

    public static TableModelImpl getCustomTableModel() {
        JTable table = AppContext.getTable();
        TableSorter tableSorter = (TableSorter) table.getModel();
        TableModelImpl impl = (TableModelImpl) tableSorter.getTableModel();
    	return impl;
    }

    public static TableSorter getTableModel() {
        JTable table = AppContext.getTable();
        return (TableSorter) table.getModel();
    }
}
