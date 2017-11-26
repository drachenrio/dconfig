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

import java.util.List;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.Cursors;
import org.moonwave.dconfig.ui.model.TreeContext;

/**
 * Helper methods which perform the actual search. 
 *
 * @author Jonathan Luo
 */
public class FindUtil {
    static String valueToFind;
    static boolean searchKey;
    static boolean searchAttribute;
    static boolean searchData;
    static boolean searchComments;
    static boolean caseSensitive;
    static boolean searchBackward;
    static boolean includeInheritedAttribute;

    public static void find(String valueToFind,
                            boolean searchKey,
                            boolean searchAttribute,
                            boolean searchData,
                            boolean searchComments,
                            boolean caseSensitive,
                            boolean searchBackward,
                            boolean includeInheritedAttribute)
    {
        FindUtil.valueToFind = valueToFind;
        FindUtil.searchKey = searchKey;
        FindUtil.searchAttribute = searchAttribute;
        FindUtil.searchData = searchData;
        FindUtil.searchComments = searchComments;
        FindUtil.caseSensitive = caseSensitive;
        FindUtil.searchBackward = searchBackward;
        FindUtil.includeInheritedAttribute = includeInheritedAttribute;
        if (searchBackward)
            findPrevious();
        else
            findNext();        
    }
    
    /**
     * Fine next matched item forward.
     */
    public static void findNext()
    {
    	AppContext.appContext().getCfgEditor().setCursor(Cursors.WAIT_CURSOR);

        JTree tree = AppContext.getTree();
        TreeContext treeContext = AppContext.appContext().treeContext();        
        
        if (!caseSensitive)
            valueToFind = valueToFind.toUpperCase();
        
        DefaultMutableTreeNode root = TreeUtil.getRoot(tree);
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) AppContext.getTree().getLastSelectedPathComponent();
        if (currentNode == null)
            currentNode = root;
        List nodeList = TreeUtil.getNodesByPreorder();
        // goto the current node first
        int startIdx = 0;
        for (int i = 0; i < nodeList.size(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeList.get(i);
            if (node == currentNode) {
                startIdx = i;
                break;
            }
        }
        if (startIdx == 0)
            startIdx++; // skip root

        DConfig cfg = (DConfig) currentNode.getUserObject();
        List attributeList = cfg.getAttributes(); // TODO - getOwnAttributes()
        ListSelectionModel model = AppContext.getTable().getSelectionModel();
        int attributeIdx = model.getMinSelectionIndex();

        boolean found = false;
        for (int i = startIdx; i < nodeList.size(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeList.get(i);
            cfg = (DConfig) node.getUserObject();
            if (searchKey && (currentNode != node)) {
                String lastKey = cfg.getLastKeyName();
                if (!caseSensitive) {
                    lastKey = lastKey.toUpperCase();
                }
                if (lastKey.indexOf(valueToFind) >= 0) {
                    TreePath newPath = TreeUtil.toTreePath(node);
                    AppContext.getTree().setSelectionPath(newPath);
                    AppContext.getTree().makeVisible(newPath);
                    AppContext.getTree().scrollPathToVisible(newPath);
                    break;
                }
            }
            found = false;
            if (!found && (searchAttribute || searchData || searchComments)) { // attribute name
                List attributes = cfg.getAttributes();
                if (attributeList != attributes) // start a new search
                    attributeIdx = 0;
                else
                    attributeIdx++; // continue previous search                
                for (int j = attributeIdx; j < attributes.size(); j++) {                    
                    DConfigAttribute attribute = (DConfigAttribute) attributes.get(j);
                    if (!includeInheritedAttribute) {
                        if (attribute.isInherited())
                            continue;
                    }
                    if (searchAttribute) {
                        String attributeName = attribute.getAttributeName();
                        if (!caseSensitive)
                            attributeName = attributeName.toUpperCase();
                        if (attributeName.indexOf(valueToFind) >= 0) {
                            //attributeList = attributes;
                            attributeIdx = j;
                            found = true;
                            break;
                        }
                    }
                    if (searchData) {
                        String attributeValue = attribute.getAttributeValue();
                        if (attributeValue != null) {
                            if (!caseSensitive)
                                attributeValue = attributeValue.toUpperCase();
                            if (attributeValue.indexOf(valueToFind) >= 0) {
                                //attributeList = attributes;
                                attributeIdx = j;
                                found = true;
                                break;
                            }
                        }
                    }
                    if (searchComments) {
                        String comments = attribute.getComments();
                        if (comments != null) {
                            if (!caseSensitive)
                                comments = comments.toUpperCase();
                            if (comments.indexOf(valueToFind) >= 0) {
                                //attributeList = attributes;
                                attributeIdx = j;
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (found) {
                TreePath newPath = TreeUtil.toTreePath(node);
                AppContext.getTree().setSelectionPath(newPath);
                AppContext.getTree().makeVisible(newPath);
                AppContext.getTree().scrollPathToVisible(newPath);
                //
                model = AppContext.getTable().getSelectionModel();
                model.setSelectionInterval(attributeIdx, attributeIdx);
                break;
            }
        }
    	
    	AppContext.appContext().getCfgEditor().setCursor(Cursors.DEFAULT_CURSOR);
    }
    
    /**
     * Fine next matched item backward.
     */
    public static void findPrevious()
    {        
        
    	AppContext.appContext().getCfgEditor().setCursor(Cursors.WAIT_CURSOR);

        JTree tree = AppContext.getTree();
        TreeContext treeContext = AppContext.appContext().treeContext();        
        
        if (!caseSensitive)
            valueToFind = valueToFind.toUpperCase();
        
        TreePath treePath = AppContext.getTree().getSelectionPath();
        DefaultMutableTreeNode currentNode = TreeUtil.getLastTreeNode(treePath);
        DefaultMutableTreeNode root = TreeUtil.getRoot(tree);
        if (currentNode == null)
            return;
        List nodeList = TreeUtil.getNodesByPreorder();
        // goto the current node first
        int startIdx = 0;
        for (int i = 0; i < nodeList.size(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeList.get(i);
            if (node == currentNode) {
                startIdx = i;
                break;
            }                
        }
        DConfig cfg = (DConfig) currentNode.getUserObject();
        List attributeList = cfg.getAttributes();
        ListSelectionModel model = AppContext.getTable().getSelectionModel();
        int attributeIdx = model.getMinSelectionIndex();
        boolean found = false;
        for (int i = startIdx; i >= 0; i--) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeList.get(i);
            cfg = (DConfig) node.getUserObject();
            found = false;
            if (!found && (searchAttribute || searchData || searchComments)) { // attribute name
                List attributes = cfg.getAttributes();
                if (attributeList != attributes) // start a new search
                    attributeIdx = attributes.size() - 1;
                else
                    attributeIdx--; // continue previous search                    
                for (int j = attributeIdx; j >= 0; j--) {
                    DConfigAttribute attribute = (DConfigAttribute) attributes.get(j);
                    if (!includeInheritedAttribute) {
                        if (attribute.isInherited())
                            continue;
                    }
                    if (searchAttribute) {
                        String attributeName = attribute.getAttributeName();
                        if (!caseSensitive)
                            attributeName = attributeName.toUpperCase();
                        if (attributeName.indexOf(valueToFind) >= 0) {
                            attributeIdx = j;
                            found = true;
                            break;
                        }
                    }
                    if (searchData) {
                        String attributeValue = attribute.getAttributeValue();
                        if (attributeValue != null) {
                            if (!caseSensitive)
                                attributeValue = attributeValue.toUpperCase();
                            if (attributeValue.indexOf(valueToFind) >= 0) {
                                attributeIdx = j;
                                found = true;
                                break;
                            }
                        }
                    }
                    if (searchComments) {
                        String comments = attribute.getComments();
                        if (comments != null) {
                            if (!caseSensitive)
                                comments = comments.toUpperCase();
                            if (comments.indexOf(valueToFind) >= 0) {
                                attributeIdx = j;
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (found) {
                TreePath newPath = TreeUtil.toTreePath(node);
                AppContext.getTree().setSelectionPath(newPath);
                AppContext.getTree().makeVisible(newPath);
                AppContext.getTree().scrollPathToVisible(newPath);
                //
                model = AppContext.getTable().getSelectionModel();
                model.setSelectionInterval(attributeIdx, attributeIdx);
                break;
            }
            if (searchKey && (currentNode != node)) {
                String lastKey = cfg.getLastKeyName();
                if (!caseSensitive) {
                    lastKey = lastKey.toUpperCase();
                }
                if (lastKey.indexOf(valueToFind) >= 0) {
                    TreePath newPath = TreeUtil.toTreePath(node);
                    AppContext.getTree().setSelectionPath(newPath);
                    AppContext.getTree().makeVisible(newPath);
                    AppContext.getTree().scrollPathToVisible(newPath);

                    model = AppContext.getTable().getSelectionModel();
                    model.setSelectionInterval(-1, -1); // clear table selection
                    break;
                }
            }
        }
    	
    	AppContext.appContext().getCfgEditor().setCursor(Cursors.DEFAULT_CURSOR);
    }
}
