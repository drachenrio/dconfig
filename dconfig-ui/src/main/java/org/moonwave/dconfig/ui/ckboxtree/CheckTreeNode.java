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

import javax.swing.tree.DefaultMutableTreeNode;
import org.moonwave.dconfig.model.DConfig;

/**
 *
 * Implementation of tree node with check field.
 *
 * @author Jonathan Luo
 */
public class CheckTreeNode extends DefaultMutableTreeNode {
    
    private boolean checked;

    public CheckTreeNode(Object userObject) {
        super(userObject, true);
        this.checked = ((DConfig)userObject).getKey().isInherited();
    }

    public void setChecked(boolean isSelected) {
        this.checked = isSelected;
    }

    public boolean isChecked() {
        return checked;
    }
}