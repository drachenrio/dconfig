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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.moonwave.dconfig.dao.springfw.DConfigDao;
import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.ui.DConfigComparator;
import org.moonwave.dconfig.ui.TreePopulator;
import org.moonwave.dconfig.ui.ckboxtree.CheckTreeNode;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.util.AppState;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.KeyUtil;

/**
 *
 * @author Jonathan Luo
 */
public class TreeUtil {
    public static final String NEWKEY = "new key #";
    public static final int NEWKEYLEN = NEWKEY.length(); 
    
    public TreeUtil() {
    }
    
    /**
     * Adds a new node under given parent node.
     * @param parent parent tree node.
     * @param nodeName node name.
     * @param nodeInfo node info.
     * @return the new tree node added on success; null otherwise
     */
    public static DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent, 
                                                 String nodeName, 
                                                 String nodeInfo,
                                                 Integer id) {
        int nextKeyId = TreeUtil.getNextNewKeyNumber();
        String keyPrefix = ((DConfig) parent.getUserObject()).getKeyName();
        DConfigKey key = null;
        if (keyPrefix.equals("/"))
            key = new DConfigKey(nodeName + nextKeyId);
        else
            key = new DConfigKey(keyPrefix + Constants.KEY_SEPARATOR + nodeName + nextKeyId);
        DConfig cfg = new DConfig(key);
        // create primary attribute
        List<DConfigAttribute>  attributes = new ArrayList<DConfigAttribute>();
        DConfigAttribute attribute = new DConfigAttribute();
        attribute.setAttributeName(Constants.PRIMARY_ATTRIBUTE);
        attribute.setDataTypeAlias("str");        
        attributes.add(attribute);
        cfg.setAttributes(attributes);

        CheckTreeNode treeNode = null;
        if (new DConfigDao().save(cfg)) {
            treeNode = new CheckTreeNode(cfg);
            parent.add(treeNode);
        } else {
            DialogUtil.showError("Save New Key Failed", "Sorry, an error occurred while saving new key to database. Please try again later.");
        }
        return treeNode;
    }

    /**
     * Finds the first tree node that matches given keyname prefix under specified
     * start node. 
     *
     * @param startNode the node started to search for.
     * @param nodeNamePrefix dot-seperated node name prefix.
     * @return tree node if found; null if could not found.
     */
    public static DefaultMutableTreeNode findNodeBreadthFirst(DefaultMutableTreeNode startNode,
                                                              String nodeNamePrefix)
    {
    	DefaultMutableTreeNode nodeFound = null;
    	DefaultMutableTreeNode item = startNode;
    	Enumeration em = startNode.breadthFirstEnumeration();
    	while (em.hasMoreElements()) {
            item = (DefaultMutableTreeNode) em.nextElement();
            DConfig cfg = (DConfig)item.getUserObject();
            if (cfg.getKeyName().equals(nodeNamePrefix)) {
                nodeFound = item;
                break;
            }
    	}
        return nodeFound;
    }

        /**
     * Finds the first tree node that matches the key name.
     *
     * @param keyName dot-separate key name.
     */
    public static DefaultMutableTreeNode findNode(String keyName) {        
    	return TreeUtil.findNodeBreadthFirst(AppContext.treeContext().getRoot(), keyName);
    }

    /**
     * Finds the first tree node that matches the key name starts from root node.
     *
     * @param root root node
     * @param keyName dot-separate key name.
     */
    public static DefaultMutableTreeNode findNode(DefaultMutableTreeNode root, String keyName) {
    	return TreeUtil.findNodeBreadthFirst(root, keyName);
    }

    public static DefaultMutableTreeNode findNode(DefaultMutableTreeNode root, String[] nodeNameList, int j) {
    	return TreeUtil.findNodeBreadthFirst(root, KeyUtil.toKey(nodeNameList, j));
    }

    /**
     * Returns its parent's dot-separted key for given node.
     * 
     * @param node current tree node.
     * @return parent key name on success; null otherwise.
     */
    public static String getParentKey(DefaultMutableTreeNode node) {
        node = (DefaultMutableTreeNode) node.getParent();
        return (node != null) ? ((DConfig)node.getUserObject()).getKeyName() : null;
    }

    public static DefaultMutableTreeNode getTreeNode(TreePath treePath) {
        return (treePath != null) ? (DefaultMutableTreeNode) treePath.getLastPathComponent() : null;
    }
    
    public static DConfig getDConfigObject(TreePath treePath) {
        DefaultMutableTreeNode treeNode = getTreeNode(treePath);
        return (DConfig) ((treeNode != null) ? treeNode.getUserObject() : null);
    }
    
    public static String getKey(TreePath treePath) {
        String key = "";
        if (treePath != null) {
            Object[] nodes = treePath.getPath();
            for (int i = 1; i < nodes.length; i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes[i];
                if (i > 1)
                    key += Constants.KEY_SEPARATOR;
                key += node.getUserObject().toString();
            }
        }
        return key;
    }
    
    public static DefaultMutableTreeNode getRoot(JTree tree) {
        return (DefaultMutableTreeNode) tree.getModel().getRoot();
    }

    /**
     * Returns a TreePath containing the specified node.
     * 
     * @param node curent node.
     * @return a TreePath containing the specified node.
     */
    public static TreePath toTreePath(TreeNode node) {
        List list = new ArrayList();
    
        // Add all nodes to list
        while (node != null) {
            list.add(node);
            node = node.getParent();
        }
        Collections.reverse(list);
    
        // Convert array of nodes to TreePath
        return (list.size() > 0) ? (new TreePath(list.toArray())) : null;
    }
    
    public static DefaultMutableTreeNode getLastTreeNode(TreePath treePath) {
        return (DefaultMutableTreeNode) treePath.getLastPathComponent();    		        
    }
    
    public static void setSelectionPath(TreeNode node) {
        TreePath treePath = toTreePath(node);
        setSelectionPath(treePath);
    	AppContext.getTree().makeVisible(treePath);
    }

    public static void setSelectionPath(TreePath treePath) {
    	AppContext.getTree().getSelectionModel().setSelectionPath(treePath);
    }
    
    public static int getNextNewKeyNumber() {
    	int iMax = 0;
    	DefaultMutableTreeNode currentNode = AppContext.treeContext().getSelectedNode();
    	if ((currentNode != null) && (currentNode.getChildCount() > 0)) {
            for (Enumeration e = currentNode.children(); e.hasMoreElements(); ) {
            	DefaultMutableTreeNode n = (DefaultMutableTreeNode)e.nextElement();
            	DConfig reg = (DConfig) n.getUserObject();
            	String keyname = reg.getLastKeyName();
            	int idx = keyname.indexOf(NEWKEY);
            	if (idx != 0)
            		continue;
            	String num = keyname.substring(idx + NEWKEYLEN, keyname.length()).trim();
            	try {
            		int iNum = Integer.parseInt(num);
            		if (iNum > iMax)
            			iMax = iNum;
            	} catch (Exception ex) {
            	}
            }
    	}
    	return iMax + 1;
    }
    
    public static int getNextKeyNumber(String keynameToCheck) {
    	int iMax = 0;
        int keynameToCheckLen = keynameToCheck.length();
        boolean found = false;
    	DefaultMutableTreeNode currentNode = AppContext.treeContext().getSelectedNode();
    	if ((currentNode != null) && (currentNode.getChildCount() > 0)) {
            for (Enumeration e = currentNode.children(); e.hasMoreElements(); ) {
            	DefaultMutableTreeNode n = (DefaultMutableTreeNode)e.nextElement();
            	DConfig reg = (DConfig) n.getUserObject();
            	String keyname = reg.getLastKeyName();
            	int idx = keyname.indexOf(keynameToCheck);
            	if (idx != 0)
            		continue;
                found = true;
            	String num = keyname.substring(idx + keynameToCheckLen, keyname.length()).trim();
            	try {
            		int iNum = Integer.parseInt(num);
            		if (iNum > iMax)
            			iMax = iNum;
            	} catch (Exception ex) {
            	}
            }
    	}
        if (found)
            iMax++;
        return iMax;
    }
    

    /**
     * Removes all children for specified tree node from tree view without
     * removing from database.
     *
     * @param currentNode current node.
     *
     */
    public static void removeAllChildren(DefaultMutableTreeNode currentNode) {
        currentNode.removeAllChildren();
    }
    
    /**
     * Removes selected node and all its children from tree view.
     *
     */
    public static void removeCurrentNodeAndChildren(boolean removeFromDb) {
    	DefaultMutableTreeNode currentNode = AppContext.treeContext().getSelectedNode();
    	if (currentNode != null)
            removeAllNodes(currentNode, removeFromDb);
    }
    
    /**
     * Removes a specified node from tree.
     *
     * @param nodeToRemove the tree node to be removed from tree.
     */
    public static void removeNode(DefaultMutableTreeNode nodeToRemove, boolean removeFromDb) {
    	DConfig cfgObj = (DConfig) nodeToRemove.getUserObject();
        if (removeFromDb) {
            cfgObj.setDelete(true);
            new DConfigDao().save(cfgObj);
        }
    	DefaultMutableTreeNode parent = (DefaultMutableTreeNode)(nodeToRemove.getParent());
        if (parent != null) {
            if (AppState.isVerbose())
                System.out.println("remove " + cfgObj.getKeyName() + " from parent " 
                                + ((DConfig)parent.getUserObject()).getKeyName());

            Object obj = AppContext.getTree().getModel();
            if (obj instanceof DefaultTreeModel) {
                nodeToRemove.setUserObject(null);
                ((DefaultTreeModel)obj).removeNodeFromParent(nodeToRemove);
            }
            return;
        }
    }
    
    /**
     * Removed all child nodes and node itself.
     * 
     * @param node start node
     * @param removeFromDb true to remove from DB; Not remove from db if false.
     */
    protected static void removeAllNodes(DefaultMutableTreeNode node, boolean removeFromDb) {
        List nodeList = getNodesDepthFirst(node);
        for (int i = 0; i < nodeList.size(); i++) {
            DefaultMutableTreeNode nodeToRemove = (DefaultMutableTreeNode) nodeList.get(i);
            removeNode(nodeToRemove, removeFromDb);
        }
        return;
    }
        
    /**
     * Checks whether a key is duplicated.
     * @param newKey new key name.
     * @return true if key is duplicate; false otherwise.
     */
    public static boolean isKeyDuplicate(String newKey) {
    	boolean bRet = false;    	
        TreePath currentSelection = AppContext.getTree().getSelectionPath();
        if (currentSelection != null) {        	
            DefaultMutableTreeNode currentNode = AppContext.treeContext().getSelectedNode();
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
            	int count = parent.getChildCount();
            	Enumeration children = parent.children();
            	while (children.hasMoreElements()) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                    if (node.equals(currentNode))
                        continue;
                    if (node.getUserObject().toString().equals(newKey)) {
                        bRet = true;
                        break;
                    }
            	}
            }
        }
        return bRet;
    }

    /**
     * Sorts child nodes for a given node.
     *
     * @param parentNode the parent node.
     */
    public static void sortChildNodes(DefaultMutableTreeNode parentNode) {
        if (parentNode != null) {
            List childList = new ArrayList();
            Enumeration children = parentNode.children();
            while (children.hasMoreElements()) {
                childList.add(children.nextElement());
            }
            if (childList.size() >= 2) {
                // sort
                DConfigComparator comparator = new DConfigComparator();
                Collections.sort(childList, comparator);

                parentNode.removeAllChildren();
                for (int i = 0; i < childList.size(); i++) {
                    parentNode.add((DefaultMutableTreeNode)childList.get(i));
                }
                DefaultTreeModel model = (DefaultTreeModel) AppContext.getTree().getModel();
                model.nodeStructureChanged(parentNode);
            }
        }
    }
    
    public static void startEditingAtSelectionPath() {
        JTree tree = AppContext.getTree();        
        tree.requestFocusInWindow();
        BasicTreeUI treeUI = (BasicTreeUI) tree.getUI();
        TreePath treePath = tree.getSelectionPath();
        treeUI.startEditingAtPath(tree, treePath);
        tree.scrollPathToVisible(treePath);
        // now sort the tree node by name    	
    }
    
    
    /*  If expand is true, expands all nodes in the tree.
     *  Otherwise, collapses all nodes in the tree.
     **/
    protected static void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
    
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    
    public static void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }    
    // Traverse all nodes in tree
    public void visitAllNodes(JTree tree) {
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
        visitAllNodes(root);
    }
    
    /**
     * Visits all nodes using deepth first search.
     * 
     * @param node
     */
    public void visitAllNodes(DefaultMutableTreeNode node) {
        // node is visited exactly once
        if (AppState.isVerbose())
            System.out.println( ((DConfig)node.getUserObject()).getKeyName());
    
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            	DefaultMutableTreeNode n = (DefaultMutableTreeNode)e.nextElement();
                visitAllNodes(n);
            }
        }
        return;
    }
    
    
    /**
     * Refreshes selected node by reloading child nodes from database. 
     *
     */
    public static void refreshSelectedNode() {
        DefaultMutableTreeNode node = AppContext.treeContext().getSelectedNode();
        if (node == null)
            return;
        DefaultMutableTreeNode root = AppContext.treeContext().getRoot();
        String currentKeyname = getKeyname(node);
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
        if (node == root)
            parent = node;
        TreePath treePath = toTreePath(parent);
        // remove current node and its children
        removeCurrentNodeAndChildren(false);

        // get the current keyname and go to db to find the node itself plus all its children
        DConfigKeyDao dao = new DConfigKeyDao();
        List keyList = null;
        if (root == node)
            keyList = dao.getAllKeys();
        else
            keyList = dao.keyStartWith(currentKeyname);
        DefaultMutableTreeNode firstNodeAdded = TreePopulator.createSubTree(root, parent, keyList);

        JTree tree = AppContext.getTree();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

        TreeUtil.sortChildNodes(parent);
        
        model.nodeStructureChanged(parent);
        
        if (node == root) {
            tree.makeVisible(treePath);
            tree.setSelectionPath(treePath);
        } else {
            if (firstNodeAdded != null) {
                TreePath newTreePath = treePath.pathByAddingChild(firstNodeAdded);
                tree.makeVisible(newTreePath);
                tree.setSelectionPath(treePath);
                tree.setSelectionPath(newTreePath);
                //TreeUtil.expandAll(tree, newTreePath, true);
            } else { // the last selected node has been removed from db, set selection to its parent
                tree.makeVisible(treePath);
                tree.setSelectionPath(treePath);
                //TreeUtil.expandAll(tree, treePath, true);
            }            
        }
    }
    
    // Traverse all expanded nodes in tree
    public void visitAllExpandedNodes(JTree tree) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        visitAllExpandedNodes(tree, new TreePath(root));
    }

    public void visitAllExpandedNodes(JTree tree, TreePath parent) {
        // Return if node is not expanded
        if (!tree.isVisible(parent)) {
            return;
        }
    
        // node is visible and is visited exactly once
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        //process(node);

        // Visit all children
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                visitAllExpandedNodes(tree, path);
            }
        }
    }
        
    /**
     * Gets a list of nodes in preorder from root node.
     * 
     * @return  list of node by preorder started from root.
     */
    public static List getNodesByPreorder() {
        return getNodesByPreorder(AppContext.treeContext().getRoot());
    }
    
    /**
     * Gets a list of nodes in preorder from start node. The start node is included
     * in the result list.
     * 
     * @param startNode start node.
     * @return list of nodes in preorder.
     */
    public static List getNodesByPreorder(DefaultMutableTreeNode startNode) {
        List list = new ArrayList();
        if (startNode != null) {
            Enumeration em = startNode.preorderEnumeration();
            while (em.hasMoreElements()) {
                list.add ((DefaultMutableTreeNode) em.nextElement());
            }
        }
        return list;
    }
    
    /**
     * Gets a list of nodes in depth first order from start node. The start 
     * node is included in the result list.
     * 
     * @param startNode start node.
     * @return list of nodes in preorder.
     */
    public static List getNodesDepthFirst(DefaultMutableTreeNode startNode) {
        List list = new ArrayList();
        if (startNode != null) {
            Enumeration em = startNode.depthFirstEnumeration();
            while (em.hasMoreElements()) {
                list.add ((DefaultMutableTreeNode) em.nextElement());
            }
        }
        return list;
    }
    
    /**
     * Gets a list of nodes in breadth first order from start node. The start 
     * node is included in the result list.
     * 
     * @param startNode start node.
     * @return list of nodes in breadth first order.
     */
    public static List getNodesBreadthFirst(DefaultMutableTreeNode startNode) {
        List list = new ArrayList();
        if (startNode != null) {
            Enumeration em = startNode.breadthFirstEnumeration();
            while (em.hasMoreElements()) {
                list.add ((DefaultMutableTreeNode) em.nextElement());
            }
        }
        return list;
    }

    public static String getKeyname(DefaultMutableTreeNode node) {
        if (node != null)
            return ((DConfig)node.getUserObject()).getKeyName();
        return null;
    }
    
    /**
     * Visits the tree nodes in post order order from a start node.
     * 
     * @param startNode the node started to search for.
     */
    public static void visitNodePostOrder(DefaultMutableTreeNode startNode)
    {
    	DefaultMutableTreeNode item = startNode;
    	Enumeration em = startNode.postorderEnumeration();
    	while (em.hasMoreElements()) {
            item = (DefaultMutableTreeNode) em.nextElement();
            DConfig reg = (DConfig)item.getUserObject();
            if (AppState.isVerbose())
                System.out.println(reg.getKeyName());
        }
    }
    
    /**
     * Visits the tree nodes from a start node's ancestor.
     * 
     * @param startNode the node started to search for.
     */
    public static void visitNodePathFromAncestor(DefaultMutableTreeNode startNode)
    {
    	DefaultMutableTreeNode item = startNode;
    	Enumeration em = startNode.pathFromAncestorEnumeration(AppContext.treeContext().getRoot());
    	while (em.hasMoreElements()) {
            item = (DefaultMutableTreeNode) em.nextElement();
            DConfig cfg = (DConfig)item.getUserObject();
            if (AppState.isVerbose())
                System.out.println(cfg.getKeyName());
        }
    }
    
    /**
     * Visits the tree nodes in breadth first order from a start node.
     * 
     * @param startNode the node started to search for.
     */
    public static void visitNodeBreadthFirst(DefaultMutableTreeNode startNode)
    {
    	DefaultMutableTreeNode item = startNode;
    	Enumeration em = startNode.breadthFirstEnumeration();
    	while (em.hasMoreElements()) {
            item = (DefaultMutableTreeNode) em.nextElement();
            DConfig cfg = (DConfig)item.getUserObject();
            if (AppState.isVerbose())
                System.out.println(cfg.getKeyName());
        }
    }
    
    
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = null;// = tree.getSelectionPath();

        if (parentPath == null) {
            //There's no selection. Default to the root node.
            //parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child,
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode =  new DefaultMutableTreeNode(child);
        if (parent == null) {
            //parent = rootNode;
        }	
        //treeModel.insertNodeInto(childNode, parent, parent.getChildCount()); // add to the last position in the child list

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            //tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }        
}
    