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

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Jonathan Luo
 *
 */


class AutoScrollingJTree extends JTree implements Autoscroll {

    private int margin = 12;
    
    public AutoScrollingJTree() {
      super();
    }
    
    public AutoScrollingJTree(TreeNode root) {
        super(root);
    }

    public void autoscroll(Point p) {
        int row = getRowForLocation(p.x, p.y);
        int rowCount = getRowCount();
        Rectangle outer = getBounds();
        row = ((p.y + outer.y <= margin) ? (row < 1 ? 0 : row - 1) : (row < rowCount - 1 ? row + 1 : row));
        scrollRowToVisible(row);
    }

    public Insets getAutoscrollInsets() {
      Rectangle outer = getBounds();
      Rectangle inner = getParent().getBounds();
      return new Insets(inner.y - outer.y + margin, inner.x - outer.x
          + margin, outer.height - inner.height - inner.y + outer.y
          + margin, outer.width - inner.width - inner.x + outer.x
          + margin);
    }
}
