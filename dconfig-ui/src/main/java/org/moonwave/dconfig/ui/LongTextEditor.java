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

/**
 * Custom cell editor for long text field like the comments column.
 *
 * @author Jonathan Luo
 */

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;
import javax.swing.JTable;
import java.awt.Component;
import java.util.EventObject;
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.TableContext;
import org.moonwave.dconfig.ui.model.TableModelImpl;

public class LongTextEditor extends AbstractCellEditor
                         implements TableCellEditor, FocusListener
{
    String currentColumnName;
    String attributeValue;
    JTextArea textArea;
    String attributeName;
    String dataTypeAlias;
    int modelColumn;
    
    public LongTextEditor() {
        textArea = new JTextArea();
        textArea.addFocusListener(this);
    }

    /**
     * Handles events from the editor button and from
     * the dialog's OK button.
     */
    //public void actionPerformed(ActionEvent e) {
    //}

    public Object getCellEditorValue() {
        return textArea.getText();
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        if (evt instanceof MouseEvent) {
            return ((MouseEvent)evt).getClickCount() >= 1;
        }
        return true;
    }
    
    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column)
    {
        TableSorter tableSorter = (TableSorter) table.getModel();
        modelColumn = table.convertColumnIndexToModel(column);
            
    	currentColumnName = tableSorter.getColumnName(modelColumn); 
        attributeValue = (String)value.toString();
        textArea.setText(attributeValue);
        attributeName = (String)tableSorter.getValueAt(row, TableModelImpl.IDX_ATTRIBUTE_NAME);
        String dataType = (String)tableSorter.getValueAt(row, TableModelImpl.IDX_DATA_TYPE);
        dataTypeAlias = DConfigDataTypeDao.getAliasByDataTypeName(dataType);
        
        TableContext tableContext = AppContext.tableContext();
        if ((attributeValue.length() <= 10) && !AppContext.isUpdateMode()) {
            textArea.setEditable(false);
        }
        return textArea;
    }

    // This method is called just before the cell value
    // is saved. If the value is not valid, false should be returned.
    @Override
    public boolean stopCellEditing() {
        String s = (String)getCellEditorValue();
        ///return false;
        //if (!isValid(s)) {
        //    // Should display an error message at this point
        //    return false;
        //}
        return super.stopCellEditing();
    }
    
    /**
     * Calls <code>fireEditingCanceled</code>.
     */
    @Override
    public void  cancelCellEditing() {
    	super.cancelCellEditing();
    }
    /**
     * Invoked as edit mode when a component gains the keyboard focus and
     * the length of the text > 10 or table column is update mode 
     */
    public void focusGained(FocusEvent e)
    {
        if ((attributeValue.length() > 10) || AppContext.isUpdateMode()) {
            if (modelColumn == TableModelImpl.IDX_COMMMENTS) {
                // don't show the dailog for a samll text and in view mode
                String title = AppContext.isUpdateMode() ? "Edit " : "View ";
                title += currentColumnName; 
                DlgEditLongString dlg = new DlgEditLongString(AppContext.appContext().getCfgEditor(),
                            title, attributeValue, AppContext.isUpdateMode());
                dlg.setVisible(true);
                textArea.setText(dlg.getText());// set new text
            } else {            
                if (dataTypeAlias.equals(DConfigDataType.aliasOperation)) {
                    DlgEditBoolean dlg = new DlgEditBoolean(AppContext.appContext().getCfgEditor(),
                                "Edit value", attributeName, attributeValue, dataTypeAlias, true);
                    dlg.setVisible(true);
                    if (dlg.getResultObject() != null) {
                        textArea.setText(dlg.getResultObject().toString());// set new text
                    }
                    dlg.dispose();
                    dlg = null;
                } else if (dataTypeAlias.equals(DConfigDataType.aliasOperationGroup)) {
                    DlgEditBoolean dlg = new DlgEditBoolean(AppContext.appContext().getCfgEditor(),
                                "Edit value", attributeName, attributeValue, dataTypeAlias, true);
                    dlg.setVisible(true);
                    if (dlg.getResultObject() != null) {
                        textArea.setText(dlg.getResultObject().toString());// set new text
                    }
                    dlg.dispose();
                    dlg = null;
                } else if (dataTypeAlias.equals(DConfigDataType.aliasBoolean)) {
                    DlgEditBoolean dlg = new DlgEditBoolean(AppContext.appContext().getCfgEditor(),
                                "Edit value", attributeName, attributeValue, dataTypeAlias, true);
                    dlg.setVisible(true);
                    if (dlg.getResultObject() != null) {
                        textArea.setText(dlg.getResultObject().toString());// set new text
                    }
                    dlg.dispose();
                    dlg = null;
                } else if (dataTypeAlias.equals(DConfigDataType.aliasString)) { // long string
                    // don't show the dailog for a samll text and in view mode
                    String title = AppContext.isUpdateMode() ? "Edit " : "View ";
                    title += currentColumnName; 
                    DlgEditLongString dlg = new DlgEditLongString(AppContext.appContext().getCfgEditor(),
                                title, attributeValue, AppContext.isUpdateMode());
                    dlg.setVisible(true);
                    textArea.setText(dlg.getText());// set new text
                } else if (dataTypeAlias.endsWith("ar")) {
                    String title = AppContext.isUpdateMode() ? "Edit " : "View ";
                    title += " Array Values"; 
                    DlgEditArray dlg = new DlgEditArray(AppContext.appContext().getCfgEditor(),
                            title, attributeName, attributeValue, dataTypeAlias, true);
                    dlg.setVisible(true);
                    if (dlg.getResultObject() != null) {
                        textArea.setText(dlg.getResultObject());// set new text
                    }
                    dlg.dispose();
                    dlg = null;
                } else { // other individual data type
                    String title = AppContext.isUpdateMode() ? "Edit " : "View ";
                    title += " Value"; 
                    DlgEditValue dlg = new DlgEditValue(AppContext.appContext().getCfgEditor(),
                                title, attributeName, attributeValue, dataTypeAlias, true);
                    dlg.setVisible(true);
                    if (dlg.getResultObject() != null) {
                        textArea.setText(dlg.getResultObject().toString());// set new text
                    }
                    dlg.dispose();
                    dlg = null;
                }
            }
            //Make the renderer reappear.
            fireEditingStopped();
            AppContext.appContext().getStateController().setTableSelectionStates();            
        }
    };

    /**
     * Invoked when a component loses the keyboard focus.
     */
    public void focusLost(FocusEvent e) {        
    };
}
