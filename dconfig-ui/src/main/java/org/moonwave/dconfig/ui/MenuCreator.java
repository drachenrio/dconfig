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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.MenuListener;
import org.moonwave.dconfig.ui.action.MenuAction;
import org.moonwave.dconfig.ui.listener.MainMenuListener;
import org.moonwave.dconfig.ui.listener.ToolBarActionListener;
import org.moonwave.dconfig.ui.listener.PopupListener;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.DCMenuItem;

import org.moonwave.dconfig.ui.util.ImageUtil;

/**
* Methods to create toolbar, menu, and popup menu.
* 
* @author Jonathan Luo
*/
public class MenuCreator {
    static MenuAction menuAction = new MenuAction();
    public static JToolBar toolbar;
    public static JMenuBar menuBar;
    public static JPopupMenu treePopup;
    public static JPopupMenu tablePopup;
    public static JPopupMenu tablePanePopup;

    static final public String SAVE 		= "save";
    static final public String IMPORT 		= "import";
    static final public String EXPORT 		= "export";
    static final public String EXIT 		= "exit";
    
    static final public String NEWKEY 		= "newKey";
    static final public String NEWATTR 		= "newAttrib";
    static final public String OPERATION	= "operation";
    static final public String OPERATIONGRP	= "operationGrp";
    static final public String DELETE 		= "delete";
    static final public String RENAME 		= "rename";
    
    static final public String CUT              = "cut";
    static final public String COPY 		= "copy";
    static final public String PASTE 		= "paste";
    
    static final public String COPYKEYNAME	= "copyKeyName";
    static final public String STRONGTYPEDEDITOR= "strongTypedEditor";
    static final public String FIND 		= "find";
    static final public String FINDNEXT 	= "findNext";
    static final public String FINDPREVIOUS     = "findPrevious";
    
    static final public String READONLY 	= "readonly";
    static final public String RRFRESH 		= "refresh";

    static final public String DBCONNECTION 	= "dbConnection";
    static final public String DRIVERMANAGER	= "driverManager";

    static final public String CONNECT          = "connect";
    static final public String DISCONNECT	= "disconnect";

    static final public String UPDATEMODE       =  "updateMode";
    static final public String ABOUT 		= "about";
    
    // popup menu
    static final public String EXPAND_COLLAPSE_ALL = "expandCollapseAll";
    static final public String EXPAND_COLLAPSE = "expandCollapse";
    
    /**
     * Creates menu bar.
     *
     * @return a <code>JMenuBar</code> instance.
     */
    public static JMenuBar createMenuBar() {
        JMenu menuFile, menuEdit, menuView, menuTools, menuHelp, submenu;
        DCMenuItem menuItem;
        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build File menu
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menuFile);

        menuItem = new DCMenuItem("Connect", CONNECT, KeyEvent.VK_C);
        menuFile.add(menuItem);
        
        menuItem = new DCMenuItem("Disconnect", DISCONNECT, KeyEvent.VK_D);
        menuFile.add(menuItem);

        menuFile.addSeparator();

        menuItem = new DCMenuItem("Save", SAVE, KeyEvent.VK_S);
        menuItem.setIcon(ImageUtil.createImageIcon("images/save16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/save16d.gif", ""));
        menuFile.add(menuItem);
        
        menuFile.addSeparator();
        
        menuItem = new DCMenuItem("Import...", IMPORT, KeyEvent.VK_I);
        menuItem.setIcon(ImageUtil.createImageIcon("images/import16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/import16d.gif", ""));
        menuFile.add(menuItem);

        menuItem = new DCMenuItem("Export...", EXPORT, KeyEvent.VK_E);
        menuItem.setIcon(ImageUtil.createImageIcon("images/export16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/export16d.gif", ""));
        menuFile.add(menuItem);
        
        menuFile.addSeparator();
        menuItem = new DCMenuItem("Exit", EXIT, KeyEvent.VK_X);
        menuFile.add(menuItem);

        //Build Edit menu
        MenuListener listener = new MainMenuListener();
        menuEdit = new JMenu("Edit");
        menuEdit.addMenuListener(listener);
        menuEdit.setMnemonic(KeyEvent.VK_E);
        menuBar.add(menuEdit);        

        //add a submenu
        submenu = new JMenu("New");
        submenu.setIcon(ImageUtil.createImageIcon("images/blank16.png", ""));
        submenu.setMnemonic(KeyEvent.VK_N);

        menuItem = new DCMenuItem("Key", NEWKEY, KeyEvent.VK_K);
        ImageIcon leafIcon = ImageUtil.createImageIcon("images/key.png", "");
        menuItem.setIcon(leafIcon);
       
        submenu.add(menuItem);

        menuItem = new DCMenuItem("Attribute", NEWATTR, KeyEvent.VK_A);
        submenu.add(menuItem);
        menuEdit.add(submenu);

        menuItem = new DCMenuItem("Operation", OPERATION, KeyEvent.VK_O);
        submenu.add(menuItem);
        menuEdit.add(submenu);

        menuItem = new DCMenuItem("Operation Group", OPERATIONGRP, KeyEvent.VK_G);
        submenu.add(menuItem);
        menuEdit.add(submenu);
        
        menuItem = new DCMenuItem("Delete", DELETE, KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        menuItem.setIcon(ImageUtil.createImageIcon("images/delete16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/delete16d.gif", ""));
        menuEdit.add(menuItem);
        
        menuItem = new DCMenuItem("Rename", RENAME, KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        menuEdit.add(menuItem);

        menuEdit.addSeparator();

        menuItem = new DCMenuItem("Cut", CUT, KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuEdit.add(menuItem);
        menuItem = new DCMenuItem("Copy", COPY, KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuEdit.add(menuItem);
        menuItem = new DCMenuItem("Paste", PASTE, KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuEdit.add(menuItem);
        
        menuEdit.addSeparator();
        
        menuItem = new DCMenuItem("Copy Key Name", COPYKEYNAME, KeyEvent.VK_K);
        menuItem.setIcon(ImageUtil.createImageIcon("images/copy16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/copy16d.gif", ""));
        menuEdit.add(menuItem);
        
        /*
        menuItem = new DCMenuItem("Use Strong Typed Editors", STRONGTYPEDEDITOR, KeyEvent.VK_U);
        menuItem.setSelected(true);
        menuItem.setIcon(DCMenuItem.selectIcon);
        menuEdit.add(menuItem);
        */
        
        menuEdit.addSeparator();
        menuItem = new DCMenuItem("Find...", FIND, KeyEvent.VK_F);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        menuItem.setIcon(ImageUtil.createImageIcon("images/find16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/find16d.gif", ""));
        menuEdit.add(menuItem);
        
        menuItem = new DCMenuItem("Find Next", FINDNEXT, KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("F3"));
        menuEdit.add(menuItem);
        
        menuItem = new DCMenuItem("Find Previous", FINDPREVIOUS, KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, ActionEvent.SHIFT_MASK));
        menuEdit.add(menuItem);
        
        //Build View menu
        menuView = new JMenu("View");
        menuView.setMnemonic(KeyEvent.VK_V);
        menuBar.add(menuView);

        menuItem = new DCMenuItem("Read Only", READONLY, KeyEvent.VK_O);
        menuItem.setSelected(true);
        menuItem.setIcon(DCMenuItem.selectIcon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
        menuView.add(menuItem);
        
        menuView.addSeparator();
        
        menuItem = new DCMenuItem("Refresh", RRFRESH, KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
        menuItem.setIcon(ImageUtil.createImageIcon("images/refresh16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/refresh16d.gif", ""));
        menuView.add(menuItem);
        
        //Build View menu
        menuTools = new JMenu("Tools");
        menuTools.setMnemonic(KeyEvent.VK_T);
        menuBar.add(menuTools);

        menuItem = new DCMenuItem("DB Connection...", DBCONNECTION, KeyEvent.VK_D);
        //menuItem.setIcon(DCMenuItem.selectIcon);
        //menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
        menuTools.add(menuItem);
                
        menuItem = new DCMenuItem("Driver Manager...", DRIVERMANAGER, KeyEvent.VK_M);
        menuTools.add(menuItem);

        //Build Help menu
        menuHelp = new JMenu("Help");
        menuHelp.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menuHelp);
        
        menuItem = new DCMenuItem("About", ABOUT, KeyEvent.VK_A);
        menuHelp.add(menuItem);
                
        return menuBar;
    }
    
    public static void createTreePopupMenu() {
        treePopup = new JPopupMenu();
        DCMenuItem menuItem;
        
        menuItem = new DCMenuItem("Expand", EXPAND_COLLAPSE, KeyEvent.VK_X);
        menuItem.setIcon(ImageUtil.createImageIcon("images/blank16.png", ""));            	
        treePopup.add(menuItem);
        menuItem = new DCMenuItem("Expand All", EXPAND_COLLAPSE_ALL, KeyEvent.VK_A);
        menuItem.setIcon(ImageUtil.createImageIcon("images/expandall.gif", ""));               	
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/expandall.gif", ""));               	
        treePopup.add(menuItem);
        treePopup.addSeparator();
        
        //add a submenu
        JMenu submenu = new JMenu("New");
        submenu.setIcon(ImageUtil.createImageIcon("images/blank16.png", ""));
        submenu.setMnemonic(KeyEvent.VK_N);

        menuItem = new DCMenuItem("Key", NEWKEY, KeyEvent.VK_K);
        menuItem.setIcon(ImageUtil.createImageIcon("images/key.png", ""));
       
        submenu.add(menuItem);

        menuItem = new DCMenuItem("Attribute", NEWATTR, KeyEvent.VK_A);
        submenu.add(menuItem);
        treePopup.add(submenu);

        menuItem = new DCMenuItem("Operation", OPERATION, KeyEvent.VK_O);
        submenu.add(menuItem);
        treePopup.add(submenu);
        
        menuItem = new DCMenuItem("Operation Group", OPERATIONGRP, KeyEvent.VK_G);
        submenu.add(menuItem);
        treePopup.add(submenu);
        
        menuItem = new DCMenuItem("Delete", DELETE, KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        menuItem.setIcon(ImageUtil.createImageIcon("images/delete16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/delete16d.gif", ""));
        treePopup.add(menuItem);
        
        menuItem = new DCMenuItem("Rename", RENAME, KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        treePopup.add(menuItem);

        treePopup.addSeparator();
        menuItem = new DCMenuItem("Cut", CUT, KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        treePopup.add(menuItem);
        menuItem = new DCMenuItem("Copy", COPY, KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        treePopup.add(menuItem);
        menuItem = new DCMenuItem("Paste", PASTE, KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        treePopup.add(menuItem);
        
        treePopup.addSeparator();

        menuItem = new DCMenuItem("Copy Key Name", COPYKEYNAME, KeyEvent.VK_K);
        menuItem.setIcon(ImageUtil.createImageIcon("images/copy16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/copy16d.gif", ""));
        treePopup.add(menuItem);       
        treePopup.addSeparator();

        menuItem = new DCMenuItem("Find...", FIND, KeyEvent.VK_F);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        menuItem.setIcon(ImageUtil.createImageIcon("images/find16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/find16d.gif", ""));
        treePopup.add(menuItem);

        menuItem = new DCMenuItem("Export...", EXPORT, KeyEvent.VK_E);
        menuItem.setIcon(ImageUtil.createImageIcon("images/export16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/export16d.gif", ""));
        treePopup.add(menuItem);
        

        //Add listener
        MouseListener popupListener = new PopupListener(treePopup);
        AppContext.getTree().addMouseListener(popupListener);
    }

    public static void createTablePopupMenu() {
        JMenuItem menuItem;
        
        //Create the popup menu.
        tablePopup = new JPopupMenu();
        //add a submenu
        menuItem = new DCMenuItem("Delete", DELETE, KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        menuItem.setIcon(ImageUtil.createImageIcon("images/delete16.gif", ""));
        menuItem.setDisabledIcon(ImageUtil.createImageIcon("images/delete16d.gif", ""));
        tablePopup.add(menuItem);
        
        menuItem = new DCMenuItem("Rename", RENAME, KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        tablePopup.add(menuItem);

        tablePopup.addSeparator();
        menuItem = new DCMenuItem("Cut", CUT, KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        tablePopup.add(menuItem);
        menuItem = new DCMenuItem("Copy", COPY, KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        tablePopup.add(menuItem);
        menuItem = new DCMenuItem("Paste", PASTE, KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        tablePopup.add(menuItem);

        MouseListener popupListener = new PopupListener(tablePopup);
        AppContext.getTable().addMouseListener(popupListener);
        
        // table pane
        tablePanePopup = new JPopupMenu();
        JMenu submenu = new JMenu("New");
        submenu.setIcon(ImageUtil.createImageIcon("images/blank16.png", ""));
        submenu.setMnemonic(KeyEvent.VK_N);

        menuItem = new DCMenuItem("Key", NEWKEY, KeyEvent.VK_K);
        ImageIcon leafIcon = ImageUtil.createImageIcon("images/key.png", "");
        menuItem.setIcon(leafIcon);
       
        submenu.add(menuItem);

        menuItem = new DCMenuItem("Attribute", NEWATTR, KeyEvent.VK_A);
        submenu.add(menuItem);
        tablePanePopup.add(submenu);

        menuItem = new DCMenuItem("Operation", OPERATION, KeyEvent.VK_O);
        submenu.add(menuItem);
        tablePanePopup.add(submenu);        
        
        menuItem = new DCMenuItem("Operation Group", OPERATIONGRP, KeyEvent.VK_G);
        submenu.add(menuItem);
        tablePanePopup.add(submenu);
        
        tablePanePopup.addSeparator();
        menuItem = new DCMenuItem("Paste", PASTE, KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        tablePanePopup.add(menuItem);

        popupListener = new PopupListener(tablePanePopup);
        AppContext.appContext().getTablePane().addMouseListener(popupListener);
    }
    
    /**
     * Creates tool bar.
     *
     * @return a <code>JToolBar</code> instance.
     */
    public static JToolBar createToolBar() {
        toolbar = new JToolBar("EPM Toolbar");
        toolbar.setRollover(true);

        ToolBarActionListener toolBarAction = new ToolBarActionListener();
        AbstractButton button = null;
        
        //TODO save all toolbar button in the AppContext, so their's states, enable, toggle,etc. can be easily controled.
        //check.getSource()
        
        button = makeImageButton(new JButton(), "newKey", NEWKEY, "Create a new key", "New key", toolBarAction);
        toolbar.add(button);

        button = makeImageButton(new JButton(), "save", SAVE, "Save changes to DB", "Save", toolBarAction);
        toolbar.add(button);
        
        button = makeImageButton(new JButton(), "newAttrib", NEWATTR, "New attribute", "New Attribute", toolBarAction);
        toolbar.add(button);
                
        toolbar.addSeparator();
        
        button = makeImageButton(new JButton(), "rename", RENAME, "Rename key / attribute", "Rename", toolBarAction);
        toolbar.add(button);
        
        button = makeImageButton(new JButton(), "delete", DELETE, "Delete a key or an attribute", "Delete", toolBarAction);
        toolbar.add(button);

        toolbar.addSeparator();
        
        button = makeImageButton(new JButton(), "connect", CONNECT, "Connect to default database or open connection wizard", "Connect", toolBarAction);
        toolbar.add(button);
        
        button = makeImageButton(new JButton(), "disconnect", DISCONNECT, "Disconnect from database", "Disconnect", toolBarAction);
        toolbar.add(button);

        toolbar.addSeparator();
        
        button = makeImageButton(new JToggleButton(), "viewMode", UPDATEMODE, 
        		"View mode. Click button or press Ctrl-U for update mode", "Update / View", toolBarAction);
        button.setSelected(false);
        toolbar.add(button);
        
        return toolbar;
    }
    
    /**
     * Creates an image button.
     *
     * @param imageName image name.
     * @param actionCommand action command name.
     * @param toolTipText tool tip text.
     * @param altText alternate text.
     * @param toolBarAction tool bar action.
     * @return an <code>AbstractButton</code> created.
     */
    protected static AbstractButton makeImageButton(AbstractButton button,
                                                    String imageName,
                                                    String actionCommand,
                                                    String toolTipText,
                                                    String altText,
                                                    ActionListener toolBarAction)
    {
        //Look for the image.
        String imgLocation = "images/" + imageName + ".png";

        //Create and initialize the button.
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(toolBarAction);
        ImageIcon icon = ImageUtil.createImageIcon(imgLocation, altText);
        if (icon != null) {
            button.setIcon(icon);
        } else { //no image found
            button.setText(altText);
        }
        return button;
    }
}
