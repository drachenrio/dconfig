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

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.moonwave.dconfig.dao.springfw.DConfigAttributeDao;
import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.ui.*;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.TableModelImpl;
import org.moonwave.dconfig.ui.model.TreeContext;
import org.moonwave.dconfig.ui.util.DialogUtil;
import org.moonwave.dconfig.ui.util.TableUtil;
import org.moonwave.dconfig.ui.util.TreeUtil;

/**
 * Custom <code>TreeSelectionListener</code> which handles tree node selection change.
 *
 * @author Jonathan Luo
 */
public class TreeSelectionActionListener implements TreeSelectionListener                
{
    boolean canceled = false;
    
    public TreeSelectionActionListener() {
        
    }

    public void valueChanged(TreeSelectionEvent e) {
        TableUtil.cancelCellEditing();

        TreeContext treeContext = AppContext.treeContext();

        TreePath currTreePath = e.getPath();
        TreePath newLeadingPath = e.getNewLeadSelectionPath();
        TreePath prevTreePath = e.getOldLeadSelectionPath();
        
        if (canceled) {
            canceled = false;
            return;
        }
        
        // check if there is any changed for the previous key or attributes
        TableModelImpl model = TableUtil.getCustomTableModel();
        DefaultMutableTreeNode currentNode = TreeUtil.getTreeNode(currTreePath);
        DConfig cfgObj = TreeUtil.getDConfigObject(currTreePath);
        DConfig prevCfgObj = TreeUtil.getDConfigObject(prevTreePath);
        if ((cfgObj == null) || (prevCfgObj == null)) {
            return;
        }        
        if (model.hasChanged()) {
            int iRet = 0;
            if (AppContext.isDisconnectAction())
                iRet = DialogUtil.showYesNoConfirm("Confirm Save", "Attribute values have been changed, do you want to save the changes?");
            else
                iRet = DialogUtil.showConfirm("Confirm Save", "Attribute values have been changed, do you want to save the changes?");                
            if (iRet == JOptionPane.YES_OPTION) {
                List<DConfigAttribute> attributes = model.getAllAttributes();
                if (prevCfgObj.getKey().hasChanged() && prevCfgObj.getKey().isNew()) {
                    if (new DConfigKeyDao().save(prevCfgObj.getKey())) {
                        for (int i = 0; i < attributes.size(); i++) {
                            DConfigAttribute attribute = attributes.get(i);
                            attribute.setKeyId(prevCfgObj.getKey().getId());
                        }
                    }
                }
                if (new DConfigAttributeDao().save(prevCfgObj, attributes)) {
                    prevCfgObj.setAttributes(attributes);
                }
                else {
                    DialogUtil.showError("Save Attributes Failed", "Sorry, an error occurred while saving attributes to database. Please try again later.");
                    // go back to the previous tree node selection
                    AppContext.getTree().setSelectionPath(prevTreePath);
                }
                model.populateTable(cfgObj.getAttributesFromDb());
            } else if (iRet == JOptionPane.NO_OPTION) {
                List<DConfigAttribute> attributes = model.getAllAttributes();
                for (int i = 0; i < attributes.size(); i++) {
                    ((DConfigAttribute)attributes.get(i)).discardChanges();
                }
                model.populateTable(cfgObj.getAttributesFromDb());
            	// do nothing               
            } else if (iRet == JOptionPane.CANCEL_OPTION) {
                // go back to the previous tree node selection
                canceled = true;
                AppContext.getTree().setSelectionPath(prevTreePath);
            }
        } else {
            model.populateTable(cfgObj.getAttributesFromDb());
        }
        //show current key
        AppContext.appContext().getStatus().showKey();
        AppContext.appContext().getStateController().setTreeSelectionStates();
    }
}