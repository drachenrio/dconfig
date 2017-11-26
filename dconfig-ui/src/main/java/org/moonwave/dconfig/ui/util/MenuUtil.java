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

import javax.swing.JMenuItem;
import javax.swing.MenuElement;

import org.moonwave.dconfig.ui.MenuCreator;
import org.moonwave.dconfig.util.*;

/**
 *
 * @author Jonathan Luo
 */
public class MenuUtil {
    
    public static void setEnable(MenuElement menuElement, String actionCommand, boolean b) {
        JMenuItem item = findMenuItem(menuElement, actionCommand);
        if (item != null)
            item.setEnabled(b);
    }

    /**
     * Enables or disables a menu item and a toolbar item for a matched action
     * command.
     *
     * @param actionCommand
     * @param b true to enable; false to disable
     */
    public static void setEnable(String actionCommand, boolean b) {
    	MenuUtil.setItemEnable(actionCommand, b);
    	ToolbarUtil.setEnable(actionCommand, b);
    }
    
    /**
     * Enables or disables a menu item for a matched action command.
     *
     * @param actionCommand
     * @param b true to enable; false to disable
     */
    public static void setItemEnable(String actionCommand, boolean b) {
        JMenuItem item = findMenuItem(MenuCreator.menuBar, actionCommand);
        if (item != null)
            item.setEnabled(b);
    }
    
    public static void setSelected(String actionCommand, boolean b) {
        JMenuItem item = findMenuItem(MenuCreator.menuBar, actionCommand);
        if (item != null)
            item.setSelected(b);
    }

    public static JMenuItem findMenuItem(MenuElement menuElement, String actionCommand) {
        if (menuElement instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) menuElement;
            if (menuItem.getActionCommand().equals(actionCommand))
                return menuItem;
        }
        JMenuItem menuItem = null;
        MenuElement[] subs = menuElement.getSubElements();
        for (int i = 0; i < subs.length; i++) {
            menuItem = findMenuItem(subs[i], actionCommand);
            if (menuItem != null)
                break;
        }
        return menuItem;
    }

    //------------------------------------------------------ Public Help Methods
    /**
     * Prints all sub menu items for a given parent menu. 
     *
     */
    public static void visitAll(MenuElement menuElement) {
        System.out.println(menuElement);
        MenuElement[] subs = menuElement.getSubElements();
        for (int i = 0; i < subs.length; i++) {
            visitAll(subs[i]);
        }
    }
}
