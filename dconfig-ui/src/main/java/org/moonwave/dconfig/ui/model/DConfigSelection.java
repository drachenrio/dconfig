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


import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import org.moonwave.dconfig.model.DConfig;

/**
 * A <code>Transferable</code> which implements the capability required
 * to transfer a <code>DConfig</code> object.
 *
 * This <code>Transferable</code> properly supports 
 * <code>dconfigFlavor</code>.
 * No other <code>DataFlavor</code>s are supported.
 *
 * @author Jonathan Luo
 */
public class DConfigSelection implements Transferable, ClipboardOwner {

    private static final int DCONFIG = 0;

    private DConfig          dcfg;
			
    public final static DataFlavor dconfigFlavor =
            new DataFlavor(DConfig.class, "DConfig Data");
    
    
    private static final DataFlavor[] flavors = {
        dconfigFlavor
    };
    
    /**
     * Creates a <code>Transferable</code> capable of transferring
     * the specified <code>DConfig</code>.
     */
    public DConfigSelection(DConfig data) {
        this.dcfg = data;
    }

    /**
     * Returns an array of flavors in which this <code>Transferable</code>
     * can provide the data. <code>dconfigFlavor</code>.
     * is properly supported.
     *
     * @return an array of length one with element <code>dconfigFlavor</code>.
     */
    public DataFlavor[] getTransferDataFlavors() {
        // returning flavors itself would allow client code to modify
        // our internal behavior
    	return (DataFlavor[])flavors.clone();
    }

    /**
     * Returns whether the requested flavor is supported by this
     * <code>Transferable</code>.
     *
     * @param flavor the requested flavor for the data
     * @return true if <code>flavor</code> is equal to
     *   <code>dconfigFlavor</code>; false if <code>flavor</code>
     *   is not one of the above flavors
     * @throws NullPointerException if flavor is <code>null</code>
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        // if 'flavor' is null, throw NPE
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the <code>Transferable</code>'s data in the requested
     * <code>DataFlavor</code> if possible. If the desired flavor is
     * If the desired flavor is <code>dconfigFlavor</code>, 
     * a <code>DConfig</code> is returned.
     *
     * @param flavor the requested flavor for the data
     * @return the data in the requested flavor, as outlined above
     * @throws UnsupportedFlavorException if the requested data flavor is
     *         not equivalent to <code>dconfigFlavor</code>
     * @throws IOException if an IOException occurs while retrieving the data.
     *         By default, AttributeSelection never throws this exception, but a
     *         subclass may.
     * @throws NullPointerException if flavor is <code>null</code>
     */
    public Object getTransferData(DataFlavor flavor)
                  throws UnsupportedFlavorException, IOException
    {
        // if 'flavor' is null, throw NPE
        if (flavor.equals(flavors[DCONFIG])) {
            return (Object) dcfg;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
