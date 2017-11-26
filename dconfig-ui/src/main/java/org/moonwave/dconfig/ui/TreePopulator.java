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
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;

import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.ui.ckboxtree.CheckTreeNode;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.TableModelImpl;
import org.moonwave.dconfig.ui.util.AppUtil;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.ui.util.TreeUtil;
import org.moonwave.dconfig.util.KeyUtil;

/**
 *
 * Implementation of initial tree population.
 *
 * @author Jonathan Luo
 *
 */
public class TreePopulator {

    public static DefaultMutableTreeNode createTree() {
        DefaultMutableTreeNode root =  new CheckTreeNode(new DConfig(DConfigKey.root));
        return root;
    }

    /**
     * Creates a subtree for a <code>DConfigKey</code> list under a given parent
     * node.
     *
     * @param root root node.
     * @param parent parent node.
     * @param keyList a list of <code>DConfigKey</code> objects.
     * @return the first child node that was added.
     */
    public static DefaultMutableTreeNode createSubTree(DefaultMutableTreeNode root,
                                     DefaultMutableTreeNode parent,
                                     List keyList) 
    {
        DefaultMutableTreeNode firstNodeToAdd = null;
        DefaultMutableTreeNode parentNode = root;
        DefaultMutableTreeNode currentNode = null;
        DConfigKey newKey = null;

        for (int keyIndex = 0; keyIndex < keyList.size(); keyIndex++) {
            parentNode = root;
            DConfigKey regKey = (DConfigKey) keyList.get(keyIndex);
            String leadingKeyName = KeyUtil.getNextLeadingKeyName("", regKey.getKeyName());
            if (!leadingKeyName.equals(regKey.getKeyName())) { // not a first level node
                while (!leadingKeyName.equals(Constants.EMPTY_STRING)) {
                    currentNode = TreeUtil.findNode(root, leadingKeyName);
                    if (currentNode == null) { // create 'missing' parent nodes along the way                        
                        newKey = new DConfigKey(leadingKeyName); // mark 'missing' parent node as new by default
                        newKey.setChangeFlag();
                        currentNode = new CheckTreeNode(new DConfig(newKey));
                        parentNode.add(currentNode);
                    }
                    parentNode = currentNode;
                    leadingKeyName = KeyUtil.getNextLeadingKeyName(leadingKeyName, regKey.getKeyName());
                    if (leadingKeyName.equals(regKey.getKeyName())) // the same node to add
                        break;
                }
            }
            DefaultMutableTreeNode newNode = new CheckTreeNode(new DConfig(regKey));
            parentNode.add(newNode);
            //parentNode = newNode;
            if (keyIndex == 0)
                firstNodeToAdd = newNode;
        }
        return firstNodeToAdd;
    }


    /**
     * Removes existing tree nodes and populates the tree with data through
     * new datasource.
     */
    public static boolean populateTree() {
        // remove all subtrees for new connection
        DefaultMutableTreeNode root =  TreeUtil.getRoot(AppContext.getTree());
        TreeUtil.removeAllChildren(root);

        DConfigDataTypeDao.load();
        
        DConfigKeyDao dao = new DConfigKeyDao();
        List keyList = dao.getAllKeys();
        createSubTree(root, root, keyList);
        ((DefaultTreeModel)AppContext.getTree().getModel()).nodeStructureChanged(root);
        TreeUtil.setSelectionPath(root);
        // addtional table initialization
        TableColumnModel tcm = AppContext.getTable().getColumnModel();
        TableColumn tc = tcm.getColumn(TableModelImpl.IDX_DATA_TYPE);

        String[] dataTypes = AppUtil.getDataTypeArray();
        if (dataTypes.length > 0)
            tc.setCellEditor(new TableDataTypeEditor(dataTypes));
        
        AppContext.getTree().requestFocusInWindow();
        return true;
    }
}
