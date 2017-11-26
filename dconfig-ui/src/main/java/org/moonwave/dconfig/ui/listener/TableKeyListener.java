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

import java.awt.Component;
//import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.moonwave.dconfig.ui.*;

import org.moonwave.dconfig.ui.util.TableUtil;

/**
 *
 * Implementation of table key listener.
 *
 * @author Jonathan Luo
 */
public class TableKeyListener implements KeyListener {
    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of 
     * a key typed event.
     */
    public TableKeyListener() {
    }

    
    public void keyTyped(KeyEvent e) {
        Object source = e.getSource();
        char keyChar = e.getKeyChar();
        int keyCode = e.getKeyCode();
        Component obj = e.getComponent();
        int loc = e.getKeyLocation();
        int mask = e.getModifiers();
        int maskEx = e.getModifiersEx();
        boolean b = e.isMetaDown();
        if (keyChar == KeyEvent.VK_TAB) {
            // set focus to JTree
        }
        if (keyCode == KeyEvent.VK_TAB) {
            // set focus to JTree
        } 
    }

    /**
     * Invoked when a key has been pressed. 
     * See the class description for {@link KeyEvent} for a definition of 
     * a key pressed event.
     */
    public void keyPressed(KeyEvent e) {
        Object source = e.getSource();
        char keyChar = e.getKeyChar();
        int keyCode = e.getKeyCode();
        if (keyChar == KeyEvent.VK_TAB) {
            // set focus to JTree
        } 
        if (keyCode == KeyEvent.VK_F2) {
        	TableUtil.saveSelectedAttributeName();
        }
        else {
        	int F2 = 1;        	
        }        	
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of 
     * a key released event.
     */
    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        char keyChar = e.getKeyChar();
        int keyCode = e.getKeyCode();
        if (keyChar == KeyEvent.VK_TAB) {
            // set focus to JTree
        }        
    }
}
