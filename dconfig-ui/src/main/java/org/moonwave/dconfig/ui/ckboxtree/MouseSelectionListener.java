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

package org.moonwave.dconfig.ui.ckboxtree;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.TableModelImpl;
import org.moonwave.dconfig.ui.util.TableUtil;


/**
 *
 * Handles mouse selection event. 
 *
 * @author Jonathan Luo
 */
public class MouseSelectionListener extends MouseAdapter {

    JTree tree;
    JCheckBox checkbox = new JCheckBox();
    
    public MouseSelectionListener(JTree tree) {
      this.tree = tree;
    }
    
    /**
     * Checks whether (x,y) coordinates are on the Checkbox.
     * 
     * @param x client x-coordinate relative to JPanel
     * @param y client y-coordinate relative to JPanel
     * @return true if (x,y) coordinates are on the Checkbox; false otherwise.
     */
    boolean isOnCheckBox(int x, int y) {
        Rectangle rect = this.checkbox.getBounds();
        rect.width = this.checkbox.getPreferredSize().width;
        rect.height = this.checkbox.getPreferredSize().height;
	return rect.contains(x, y);
    }
    
    /**
     * Checks whether (x,y) coordinates are on the icon.
     * 
     * @param x client x-coordinate relative to JPanel
     * @param y client y-coordinate relative to JPanel
     * @return true if (x,y) coordinates are on the icon; false otherwise.
     */
    boolean isOnIcon(int x, int y) {
        Rectangle rect = this.checkbox.getBounds();
        rect.x += 16;
        rect.width = 16; // icon is 16x18 or 18x16 pixels
        rect.height = 16;
	return rect.contains(x, y);
    }
    
    public void mouseClicked(MouseEvent e) {
        CheckTreeCellEditor editor = (CheckTreeCellEditor) tree.getCellEditor();
        editor.setClickOnLabel(true);
        int x = e.getX();
        int y = e.getY();
        int row = tree.getRowForLocation(x, y);
        if (row == -1) // no selection
            return;
        TreePath path = tree.getPathForRow(row); //TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path==null) 
            return;
        Rectangle rect = tree.getPathBounds(path);// rect is the client top-left corner of the node selection from the view
        if (isOnIcon(x - rect.x, y - rect.y))
            editor.setClickOnLabel(false);
        if (isOnCheckBox(x - rect.x, y - rect.y) && AppContext.isUpdateMode()) { // handle check box click
            editor.setClickOnLabel(false);
            CheckTreeNode selectedNode = (CheckTreeNode) path.getLastPathComponent();
            selectedNode.setChecked(!selectedNode.isChecked());
            DConfig dcfgObj = (DConfig) selectedNode.getUserObject();
            dcfgObj.getKey().setInherited(selectedNode.isChecked() ? "Y" : "N");
            new DConfigKeyDao().update(dcfgObj.getKey());

            // update table attributes list
            TableModelImpl model = TableUtil.getCustomTableModel();
            model.populateTable(dcfgObj.getAttributesFromDb());
            
            ((DefaultTreeModel) tree.getModel()).nodeChanged(selectedNode);
            e.consume();
        }
    }
}
