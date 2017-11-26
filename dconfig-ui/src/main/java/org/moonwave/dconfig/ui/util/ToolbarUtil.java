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

import java.awt.Component;

import javax.swing.AbstractButton;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.util.*;

public class ToolbarUtil {

	public static AbstractButton findToolBarButton(String actionCommand) {
		AbstractButton button = null;
        Component[] components = AppContext.appContext().getCfgEditor().getToolbar().getComponents();
        for (int i = 0; i < components.length; i++) {
        	if (components[i] instanceof AbstractButton) {
        		AbstractButton btn = (AbstractButton) components[i]; 
        		if (btn.getActionCommand().equals(actionCommand)) {
        			button = btn;
        			break;
        		}
        	}
        }
        return button;
	}
    
	public static void setEnable(String actionCommand, boolean b) {
        AbstractButton menuButton = findToolBarButton(actionCommand);
        if (menuButton != null)
            menuButton.setEnabled(b);
    }
    
    public static void setSelected(String actionCommand, boolean b) {
        AbstractButton menuButton = findToolBarButton(actionCommand);
        if (menuButton != null)
            menuButton.setSelected(b);
    }
}
