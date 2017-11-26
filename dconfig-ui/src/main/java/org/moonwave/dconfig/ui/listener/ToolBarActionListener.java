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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import org.moonwave.dconfig.ui.*;

import org.moonwave.dconfig.ui.util.MenuUtil;

/**
 *
 * Toolbar action handler.
 *
 * @author Jonathan Luo
 */
public class ToolBarActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = null;
        if (e.getActionCommand().equals(MenuCreator.UPDATEMODE))
            menuItem = MenuUtil.findMenuItem(MenuCreator.menuBar, MenuCreator.READONLY);
        else
            menuItem = MenuUtil.findMenuItem(MenuCreator.menuBar, e.getActionCommand());
        if (menuItem != null)
            menuItem.doClick();
    }
}
