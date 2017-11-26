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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.tree.TreePath;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.ui.model.DConfigSelection;

/**
 *
 * @author Jonathan Luo
 *
 */

class TransferableTreeNode implements Transferable {

    public final static DataFlavor treePathFlavor = new DataFlavor(TreePath.class, "Tree Path");
    
    DataFlavor flavors[] = { DConfigSelection.dconfigFlavor, treePathFlavor };
    DConfig  dcfgObj;
    TreePath treePath;

    public TransferableTreeNode(DConfig dcfgObj) {
       this.dcfgObj = dcfgObj;
    }

    public TransferableTreeNode(TreePath treePath) {
        this.treePath = treePath;
    }

    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }

    public synchronized Object getTransferData(DataFlavor flavor)
                               throws UnsupportedFlavorException, IOException {
        if (flavor.getRepresentationClass() == DConfig.class) {
            return (Object) dcfgObj;
        } else if (flavor.getRepresentationClass() == TreePath.class) {
            return (Object) treePath;            
        }
        else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
