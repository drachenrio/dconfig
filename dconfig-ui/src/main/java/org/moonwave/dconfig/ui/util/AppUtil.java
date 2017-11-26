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
import javax.swing.tree.DefaultTreeModel;

import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.util.*;

/**
 * Application specific utility methods.
 *
 * @author Jonathan Luo
 */
public class AppUtil {
    
    public static String[] getDataTypeArray() {
        List list = DConfigDataTypeDao.getDataTypeList();
    	String[] dataTypes = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
        	DConfigDataType dataType = (DConfigDataType) list.get(i);
        	dataTypes[i] = dataType.getDataTypeName();
        }
    	return dataTypes;
    }
    
    /**
     * Sets addtional title for main window.
     *
     * @param additionalTitle additional Title after the primary title.
     */
    public static void setTitle(String additionalTitle) {
        String title = AppProperties.getInstance().getProperty("app.name") + " - " + additionalTitle;
        AppContext.appContext().getCfgEditor().setTitle(title);        
    }
    
    /**
     * Disconnects from current <code>DataSource</code>.
     */
    public static void disconnect() {
        AppContext.treeContext().getRoot().removeAllChildren();
        ((DefaultTreeModel)AppContext.getTree().getModel()).nodeStructureChanged(AppContext.treeContext().getRoot());
        setTitle("disconnected");
        // set the mode as readonly; not allow to add new node on the tree; this should be in appcontext
    }
}
