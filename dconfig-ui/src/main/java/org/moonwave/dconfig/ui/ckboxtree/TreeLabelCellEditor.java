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

import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;

/**
 * Wrapper class for <code>TreeLabelEditor</code>. Further customization should
 * be done inside <code>TreeLabelEditor</code>.
 *
 * @author Jonathan Luo
 */
public class TreeLabelCellEditor implements TreeCellEditor {

    TreeLabelEditor labelEditor = new TreeLabelEditor();

    public TreeLabelCellEditor() {
    }

    public Component getTreeCellEditorComponent(JTree tree, 
                                                Object value,
                                                boolean isSelected,
                                                boolean expanded, 
                                                boolean leaf, 
                                                int row) {
        labelEditor.initialize(tree, value, isSelected, expanded, leaf, row);
        labelEditor.requestFocusInWindow();
        return (Component) labelEditor;
    }

    public Object getCellEditorValue() {
        return labelEditor.getCellEditorValue();
    }

    public boolean isCellEditable(EventObject event) {
        return true;
    }

    public boolean shouldSelectCell(EventObject event) {
        return labelEditor.shouldSelectCell(event);
    }

    public boolean stopCellEditing() {
        return labelEditor.stopCellEditing();
    }

    public void cancelCellEditing() {
        labelEditor.cancelCellEditing();
    }

    public void addCellEditorListener(CellEditorListener l) {
        labelEditor.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        labelEditor.removeCellEditorListener(l);
    }
}
