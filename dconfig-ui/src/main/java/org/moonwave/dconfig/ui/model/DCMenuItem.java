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

package org.moonwave.dconfig.ui.model;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.moonwave.dconfig.ui.*;
import org.moonwave.dconfig.ui.action.MenuAction;

import org.moonwave.dconfig.ui.util.ImageUtil;

public class DCMenuItem extends JMenuItem {
    public static ImageIcon deleteIcon = ImageUtil.createImageIcon("images/delete16m.png", "");
    public static ImageIcon selectIcon = ImageUtil.createImageIcon("images/select16.png", "");
    public static ImageIcon blankIcon = ImageUtil.createImageIcon("images/blank16.png", "");
    static DCMenuChangeListener menuChangeListener = new DCMenuChangeListener();

    MenuAction menuAction = new MenuAction();

    /**
     * Creates a <code>JMenuItem</code> with no set text or icon.
     */
    public DCMenuItem() {
    	super();
    	this.setIcon(blankIcon);
        this.addChangeListener(menuChangeListener);
        this.addActionListener(menuAction);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified icon.
     *
     * @param icon the icon of the <code>JMenuItem</code>
     */
    public DCMenuItem(Icon icon) {
    	super(icon);
    	this.addChangeListener(menuChangeListener);
        this.addActionListener(menuAction);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified text.
     *
     * @param text the text of the <code>JMenuItem</code>
     */
    public DCMenuItem(String text) {
    	super(text);
    	setIcon(blankIcon);
    	this.addChangeListener(menuChangeListener);
        this.addActionListener(menuAction);
    }
    
    /**
     * Creates a menu item whose properties are taken from the 
     * specified <code>Action</code>.
     *
     * @param a the action of the <code>JMenuItem</code>
     * @since 1.3
     */
    public DCMenuItem(Action a) {
    	super(a);
    	setIcon(blankIcon);
    	this.addChangeListener(menuChangeListener);
        this.addActionListener(menuAction);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified text and icon.
     *
     * @param text the text of the <code>JMenuItem</code>
     * @param icon the icon of the <code>JMenuItem</code>
     */
    public DCMenuItem(String text, Icon icon) {
    	super(text, icon);
    	this.addChangeListener(menuChangeListener);
        this.addActionListener(menuAction);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified text and
     * keyboard mnemonic.
     *
     * @param text the text of the <code>JMenuItem</code>
     * @param mnemonic the keyboard mnemonic for the <code>JMenuItem</code>
     */
    public DCMenuItem(String text, int mnemonic) {
    	super(text, mnemonic);
    	setIcon(blankIcon);
    	this.addChangeListener(menuChangeListener);
        this.addActionListener(menuAction);
    }

    /**
     * Creates a <code>JMenuItem</code> with the specified text and
     * keyboard mnemonic.
     *
     * @param text the text of the <code>JMenuItem</code>
     * @param actionCommand action command text.
     */
    public DCMenuItem(String text, String actionCommand) {
    	super(text);
    	setIcon(blankIcon);
        this.setActionCommand(actionCommand);
        this.addActionListener(menuAction);
    }
    
    /**
     * Creates a <code>JMenuItem</code> with the specified text and
     * keyboard mnemonic.
     *
     * @param text the text of the <code>JMenuItem</code>
     * @param mnemonic the keyboard mnemonic for the <code>JMenuItem</code>
     */
    public DCMenuItem(String text, String actionCommand, int mnemonic) {
    	super(text, mnemonic);
    	setIcon(blankIcon);
        this.setActionCommand(actionCommand);
    	this.addChangeListener(menuChangeListener);
        this.addActionListener(menuAction);
    }

    static class DCMenuChangeListener implements ChangeListener {
        /**
         * Invoked when the target of the listener has changed its state.
         *
         * @param e  a ChangeEvent object.
         */
        public void stateChanged(ChangeEvent e) {
            Object obj = e.getSource();
            if (obj instanceof DCMenuItem) {
                DCMenuItem menuItem = (DCMenuItem) obj;
                if (menuItem.isSelected()) {
                    if (menuItem.isArmed())
                        menuItem.setIcon(deleteIcon);
                    else
                        menuItem.setIcon(selectIcon);
                }
            }
        }
    }
}
