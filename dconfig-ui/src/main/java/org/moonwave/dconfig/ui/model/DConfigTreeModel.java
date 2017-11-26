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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.moonwave.dconfig.dao.springfw.DConfigDao;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.ui.util.DialogUtil;
import org.moonwave.dconfig.util.KeyUtil;
import org.moonwave.dconfig.ui.util.TreeUtil;
import org.moonwave.dconfig.util.Constants;

/**
 *
 * @author Jonathan Luo
 */
public class DConfigTreeModel extends DefaultTreeModel {
        
    /**
      * Creates a tree in which any node can have children.
      *
      * @param root a TreeNode object that is the root of the tree
      */
     public DConfigTreeModel(TreeNode root) {
        super(root);
    }

    /**
      * Creates a tree specifying whether any node can have children,
      * or whether only certain nodes can have children.
      *
      * @param root a TreeNode object that is the root of the tree
      * @param asksAllowsChildren a boolean, false if any node can
      *        have children, true if each node is asked to see if
      *        it can have children
      */
    public DConfigTreeModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
    }
    
    /**
      * This sets the user object of the TreeNode identified by path
      * and posts a node changed.  If you use custom user objects in
      * the TreeModel you're going to need to subclass this and
      * set the user object of the changed node to something meaningful.
      */
    public void valueForPathChanged(TreePath path, Object newValue) {
    	DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)path.getLastPathComponent();
        Object userObject = currentNode.getUserObject();

        JTree tree = AppContext.getTree();
        String newKeyname = (String)newValue;
        if (newKeyname.indexOf(Constants.KEY_SEPARATOR) != -1) {
            DialogUtil.showKeyInvalidCharError(newKeyname);
        } else if (TreeUtil.isKeyDuplicate(newKeyname)) {
            DialogUtil.showKeyExistError(newKeyname);
        } else {
            DConfig dcfg = (DConfig) userObject;
            String currentKeyname = dcfg.getLastKeyName();
            if (!currentKeyname.equals(newKeyname)) {
                List newList = new ArrayList();
                //String leadKeyName = dcfg.getKey().getLeadKeyName();
                String keyname = dcfg.getKey().getKeyName();
                String[] keys = KeyUtil.toArray(keyname);
                int newKeynameIdx = keys.length - 1;
                //dcfg.getKey().setKeyName((leadKeyName.length() > 0) ? leadKeyName + "." + lastKeyname : lastKeyname);
                List nodeList = TreeUtil.getNodesDepthFirst(currentNode);
                for (int keyIndex = 0; keyIndex < nodeList.size(); keyIndex++) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodeList.get(keyIndex);
                    DConfig dcfgItem = (DConfig) node.getUserObject();
                    DConfigKey regKey = dcfgItem.getKey();
                    keys = KeyUtil.toArray(regKey.getKeyName());
                    keys[newKeynameIdx] = newKeyname;
                    keyname = KeyUtil.toKeyname(keys);
                    regKey.setKeyName(keyname);
                    newList.add(dcfgItem);
                    //dcfgItem.getAttributes(); // load attribute from db if need
                }
                if (new DConfigDao().save(newList)) {
                    nodeChanged(currentNode);
                    // sort the nodes by name
                    TreePath parentPath = path;
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) currentNode.getParent();

                    TreeUtil.sortChildNodes(parentNode);

                    TreePath newTreePath = parentPath.pathByAddingChild(currentNode);
                    ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(currentNode);
                    // find the node and set the selection to it
                    TreePath treePath = TreeUtil.toTreePath(currentNode);
                    tree.makeVisible(path);
                    tree.setSelectionPath(path);
                } else {
                    DialogUtil.showError("Save Data Failed", "Sorry, an error occurred while saving new key name to database. Please try again later.");
                    // restore old value
                    // revert changes
                    for (int keyIndex = 0; keyIndex < nodeList.size(); keyIndex++) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodeList.get(keyIndex);
                        DConfig dcfgItem = (DConfig) node.getUserObject();
                        DConfigKey regKey = dcfgItem.getKey();
                        regKey.setKeyName(regKey.getKeyNameOld());                        
                        /*
                        keys = KeyUtil.toArray(regKey.getKeyName());
                        keys[newKeynameIdx] = currentKeyname;
                        keyname = KeyUtil.toKeyname(keys);
                        regKey.setKeyName(keyname);
                         */
                    }
                }
            }
        }
    }
}
