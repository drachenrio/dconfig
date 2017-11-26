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

package org.moonwave.dconfig.ui.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.springfw.DConfigDao;
import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;

import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.ui.*;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.AttributeSelection;
import org.moonwave.dconfig.ui.model.DConfigSelection;
import org.moonwave.dconfig.ui.model.TableContext;
import org.moonwave.dconfig.ui.model.TableModelImpl;
import org.moonwave.dconfig.ui.util.DialogUtil;
import org.moonwave.dconfig.ui.util.ImageUtil;
import org.moonwave.dconfig.util.KeyUtil;
import org.moonwave.dconfig.ui.util.TableUtil;
import org.moonwave.dconfig.ui.util.ToolbarUtil;
import org.moonwave.dconfig.ui.util.TreeUtil;

public class MenuActionHandler {

    private static final Log log = LogFactory.getLog(MenuActionHandler.class);

    /**
     * Adds a new key.
     */
    public static void newKey() {
        JTree tree = AppContext.getTree();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

        TreePath treePath = tree.getSelectionPath();//getLeadSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

        DefaultMutableTreeNode newNode = TreeUtil.addNode(selectedNode, TreeUtil.NEWKEY, "", null);
        if (newNode != null) {
            TreePath newTreePath = treePath.pathByAddingChild(newNode);

            DConfig userNode = (DConfig)selectedNode.getUserObject();
            AppContext.appContext().getStatus().setText("node: " + userNode.getLastKeyName() + " child count:" + selectedNode.getChildCount()) ;

            model.nodeStructureChanged(selectedNode);
            tree.makeVisible(newTreePath);
            tree.setSelectionPath(newTreePath);

            selectedNode = newNode;
            rename();
        } else {
            // does nothing
        }
    }

    /**
     * Ask user to confirm whether to save attribute changes if user select a different
     * key.
     */
    public static void newKeySelection() {
        if (AppContext.appContext().getFocusComponent() == AppContext.Focus.tree) {
            int iRet = JOptionPane.showConfirmDialog(AppContext.appContext().cfgEditor, 
                    "Are yoe sure you want to delete this key an all of its subkeys?",
                    "Confirm Key Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (iRet == JOptionPane.YES_OPTION) {
                AppContext.appContext().getStatus().showMessage("You selected YES");
            } else if (iRet == JOptionPane.NO_OPTION) {
                AppContext.appContext().getStatus().showError("You selected NO");
            }
        }
    }

    /**
     * Adds a new attribute.
     */
    public static void newAttribute() {
        TableContext tableContext = AppContext.tableContext();
        
        JTable table = AppContext.getTable();
        TableSorter tableSorter = AppContext.appContext().getTableSorter();
       
        TableModelImpl model = (TableModelImpl) tableSorter.getTableModel();
        tableSorter.cancelSorting();
        int rowCount = model.getRowCount();

        model.addNewRow("String");
        model.fireTableDataChanged();
        
        table.setRowSelectionInterval(rowCount , rowCount );  
        TableUtil.editSelectedAttributeName();
    }

    /**
     * Adds a new operation.
     */
    public static void newOperation() {
        TableContext tableContext = AppContext.tableContext();
        
        JTable table = AppContext.getTable();
        TableSorter tableSorter = AppContext.appContext().getTableSorter();
       
        TableModelImpl model = (TableModelImpl) tableSorter.getTableModel();
        tableSorter.cancelSorting();
        int rowCount = model.getRowCount();

        model.addNewRow("Operation");
        model.fireTableDataChanged();
        
        table.setRowSelectionInterval(rowCount , rowCount );  
        TableUtil.editSelectedAttributeName();
    }

    /**
     * Adds a new operation.
     */
    public static void newOperationGroup() {
        TableContext tableContext = AppContext.tableContext();
        
        JTable table = AppContext.getTable();
        TableSorter tableSorter = AppContext.appContext().getTableSorter();
       
        TableModelImpl model = (TableModelImpl) tableSorter.getTableModel();
        tableSorter.cancelSorting();
        int rowCount = model.getRowCount();

        model.addNewRow("Operation Group");
        model.fireTableDataChanged();
        
        table.setRowSelectionInterval(rowCount , rowCount );  
        TableUtil.editSelectedAttributeName();
    }

    /**
     * Update update view mode toolbar state.
     */
    public static void setUpdateViewMode() {
        AbstractButton toolbarUpdate = ToolbarUtil.findToolBarButton(MenuCreator.UPDATEMODE);
        if (toolbarUpdate.isSelected()) {
            toolbarUpdate.setIcon(ImageUtil.createImageIcon("images/updateMode.png", ""));
            toolbarUpdate.setToolTipText("Update mode. Click button or press Ctrl-U for view mode.");
            AppContext.treeContext().setDragEnabled(true);
            //AppContext.getTree().setDragEnabled(true); // dynamic setDragEnabled(true) causes drag failure on JRE 6.0
        } else {
            toolbarUpdate.setIcon(ImageUtil.createImageIcon("images/viewMode.png", ""));
            toolbarUpdate.setToolTipText("View mode. Click button or press Ctrl-U for update mode");
            AppContext.treeContext().setDragEnabled(false);
            //AppContext.getTree().setDragEnabled(false);
        }
        AppContext.getTree().revalidate();
        AppContext.getTree().repaint();
    }

    /**
     * Performs a tree node or a table row deletion.
     */
    public static void performDelete() {
        if (AppContext.appContext().getFocusComponent() == AppContext.Focus.tree) {
            //ImageIcon leafIcon = ImageUtil.createImageIcon("images/key.png", "");
            //toolkit.beep();
            int sel = DialogUtil.showDeleteKeyDlg(null);
            if (sel == JOptionPane.YES_OPTION) {
                TreeUtil.removeCurrentNodeAndChildren(true);
            }            
        } else if (AppContext.appContext().getFocusComponent() == AppContext.Focus.table) {
            String selectedAttrName = TableUtil.getSelectedAttributeName();
            TableModelImpl model = TableUtil.getCustomTableModel();
            model.deleteRowByAttributeName(selectedAttrName);
            model.fireTableDataChanged();
            AppContext.appContext().getStateController().setTableSelectionStates();
        }
    }

    /**
     * Renames a key name or an attribute.
     */
    public static void rename() {
        if (AppContext.appContext().getFocusComponent() == AppContext.Focus.tree)  {
            TreeUtil.startEditingAtSelectionPath();
        } else if (AppContext.appContext().getFocusComponent() == AppContext.Focus.table) {
            TableUtil.editSelectedAttributeName();
        }
    }

    /**
     * Performs cut.
     */
    public static void performCut() {
        AppContext.treeContext().setCut(false);
        if (AppContext.appContext().getFocusComponent() == AppContext.Focus.tree)
            AppContext.treeContext().setCut(true);
        performCopy();
        if (AppContext.appContext().getFocusComponent() == AppContext.Focus.table)
            performDelete();
    }

    /**
     * Performs copy.
     */
    public static void performCopy() {
        if (AppContext.appContext().getFocusComponent() == AppContext.Focus.tree)  {
            JTree tree = AppContext.getTree();
            DConfig dcfgOrig = AppContext.treeContext().getSelectedUserObject();            
            // make a copy
            DConfig dcfg = new DConfig(dcfgOrig);
            dcfg.clearChangeFlag();
            dcfg.setNew();            
            DConfigSelection dcfgSelection = new DConfigSelection(dcfg);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(dcfgSelection, dcfgSelection);
        } else if (AppContext.appContext().getFocusComponent() == AppContext.Focus.table) {
            JTable table = AppContext.getTable();
            TableModelImpl model = TableUtil.getCustomTableModel();
            String attributeName = TableUtil.getSelectedAttributeName();
            DConfigAttribute attributeOrig = model.getAttributeByAttributeName(attributeName);
            // make a copy
            DConfigAttribute attribute = new DConfigAttribute(attributeOrig);
            attribute.clearChangeFlag();
            AttributeSelection attrSelection = new AttributeSelection(attribute);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(attrSelection, attrSelection);
        }
    }
    
    /**
     * Performs paste.
     */
    public static void performPaste() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = cb.getContents(AppContext.appContext().cfgEditor);
        if (t == null)
            return;
        try {
            if (t.isDataFlavorSupported(AttributeSelection.dconfigAttributeFlavor)) {
                DConfigAttribute attributeOrig = (DConfigAttribute) cb.getData(AttributeSelection.dconfigAttributeFlavor);
                DConfigAttribute attribute = new DConfigAttribute(attributeOrig);
                attribute.setNew();
                // now do the paste
                TableContext tableContext = AppContext.tableContext();

                JTable table = AppContext.getTable();
                TableSorter tableSorter = AppContext.appContext().getTableSorter();
                TableModelImpl model = (TableModelImpl) tableSorter.getTableModel();
                tableSorter.cancelSorting();
                int rowCount = model.getRowCount();

                model.addNewRow(attribute);
                model.fireTableDataChanged();

                table.setRowSelectionInterval(rowCount , rowCount );
            } else if (t.isDataFlavorSupported(DConfigSelection.dconfigFlavor)) {
                DConfig dcfgOrig = (DConfig) cb.getData(DConfigSelection.dconfigFlavor);
                DConfig dcfgSelected = AppContext.treeContext().getSelectedUserObject();
                doPaste(dcfgOrig);                
                if (AppContext.treeContext().isCut()) {
                    DefaultMutableTreeNode nodeToRemove = TreeUtil.findNode(dcfgOrig.getKeyName());
                    TreeUtil.setSelectionPath(nodeToRemove);
                    TreeUtil.removeCurrentNodeAndChildren(true);
                    DefaultMutableTreeNode selectedNode = TreeUtil.findNode(dcfgSelected.getKeyName());
                    TreeUtil.setSelectionPath(selectedNode);
                    AppContext.getTree().requestFocusInWindow();
                }
                MenuAction.setClipboard(""); // allow paste only once
            }
        } catch (Exception ex) {
            log.error(ex, ex);
            System.err.println("UnsupportedFlavorException occurred");
        }
    }

    /**
     * Performs paste.
     */
    public static void doPaste(DConfig dcfgOrig) {
        DConfig dcfgToPaste = new DConfig(dcfgOrig);
        String origLastKeyname = dcfgToPaste.getLastKeyName();                
        String[] origkeys = KeyUtil.toArray(dcfgToPaste.getKeyName());
        int lastKeynameIdx = origkeys.length - 1;
        dcfgToPaste.setNew();

        // now do the paste
        TableContext tableContext = AppContext.tableContext();
        JTree tree = AppContext.getTree();
        DConfig dcfgCurrent = AppContext.treeContext().getSelectedUserObject();
        
        String parentKeyname = dcfgCurrent.getKeyName();

        DConfigKeyDao dao = new DConfigKeyDao();
        List keyList = dao.keyStartWith(dcfgToPaste.getKeyName());
        String leadKeyname = dcfgToPaste.getKey().getLeadKeyName();

        String adjustedKeyname = "";
        boolean renamed = false;
        List newList = new ArrayList();
        for (int keyIndex = 0; keyIndex < keyList.size(); keyIndex++) {
            DConfigKey regKey = (DConfigKey) keyList.get(keyIndex);
            DConfig dcfgItem = new DConfig(regKey);
            dcfgItem.getAttributes(); // load attributes from db if need

            String lastKeyname = dcfgItem.getLastKeyName();
            // replace leadKeyname with parentkey name + the rest ending names                    
            String keyname = regKey.getKeyName();
            String[] keys = KeyUtil.toArray(keyname);
            if (keyIndex == 0) { // search duplicate key name for the first one
                //keyname = keyname + "#" + "?";
                adjustedKeyname = lastKeyname;
                int nextKeyId = TreeUtil.getNextKeyNumber(lastKeyname);
                if (nextKeyId > 0) {
                    renamed = true;
                    adjustedKeyname += nextKeyId;
                    keyname += nextKeyId;
                }
            } else {
                if (renamed) {
                    keys[lastKeynameIdx] = adjustedKeyname;
                    keyname = KeyUtil.toKeyname(keys);
                }
            }                        
            if (leadKeyname.length() == 0) {
                if (!parentKeyname.equals("/"))
                    keyname = parentKeyname + "." + keyname;
            }
            else {
                if (parentKeyname.equals("/"))
                    keyname = keyname.replaceFirst(leadKeyname + ".", "");
                else
                    keyname = keyname.replaceFirst(leadKeyname, parentKeyname);
            }
            regKey.setKeyName(keyname);
            dcfgItem.setNew();
            newList.add(dcfgItem);
        }

        // make it as a single transaction
        if (new DConfigDao().save(newList)) {
        } else {
            DialogUtil.showError("Paste Key Failed", "Sorry, an error occurred while saving data to database. Please try again later.");
        }

        TreeUtil.refreshSelectedNode();
        //TreeUtil.expandAll(tree, newTreePath, true);
    }
}
