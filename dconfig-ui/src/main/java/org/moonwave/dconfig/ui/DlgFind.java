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

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import org.moonwave.dconfig.ui.util.FindUtil;
import org.moonwave.dconfig.ui.util.MenuUtil;

import org.moonwave.dconfig.ui.util.SorterUtil;

/**
 *
 * @author Jonathan Luo
 */
public class DlgFind extends JDialog implements ActionListener {    
    static JComboBox cbFind = new JComboBox();
    static DlgFind dialog;
    public static JCheckBox ckKeys;
    public static JCheckBox ckAttributeNames;
    public static JCheckBox ckData;
    public static JCheckBox ckComments;
    public static JCheckBox ckCaseSensitive;
    public static JCheckBox ckSearchBackwards;
    public static JCheckBox ckInheritedAttribute;

    static {
        ckKeys = new JCheckBox("Keys");
        ckKeys.setMnemonic(KeyEvent.VK_K);
        ckKeys.setSelected(true);

        ckData = new JCheckBox("Data Values");
        ckData.setSelected(true);
        ckData.setMnemonic(KeyEvent.VK_V);

        ckAttributeNames = new JCheckBox("Attributes Names");
        ckAttributeNames.setMnemonic(KeyEvent.VK_A);
        ckAttributeNames.setSelected(true);

        ckComments = new JCheckBox("Comments");
        ckComments.setMnemonic(KeyEvent.VK_M);

        ckCaseSensitive = new JCheckBox("Case Sensitive");
        ckCaseSensitive.setMnemonic(KeyEvent.VK_S);
        //ckCaseSensitive.setDisplayedMnemonicIndex(5);
        
        ckSearchBackwards = new JCheckBox("Search Backwards");
        ckSearchBackwards.setMnemonic(KeyEvent.VK_B);
        
        ckInheritedAttribute = new JCheckBox("Include Inherited Attributes");
        ckInheritedAttribute.setMnemonic(KeyEvent.VK_I);
    }    
    
    JLabel lblFindWhat;
    FindWhatItemListener findWhatItemListener = new FindWhatItemListener();
    JButton btnFind;
    JButton btnClose;


    public DlgFind(Frame parent, String title) {
        super(parent, title, true);
        this.setResizable(false);
        
    	cbFind.addItemListener(findWhatItemListener);
    	dialog = this;
        Container cp = getContentPane();

        // set dialog position inside its parent
        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation(); // Parent position
            setLocation(p.x+parentSize.width/4, p.y+parentSize.height/4); 
        }
        cp.setLayout(new BorderLayout());

        Box mainPane = Box.createHorizontalBox();
        mainPane.setName("main pane");
        
        // left panel
        Box leftMain = Box.createVerticalBox();
        Box firstRow = Box.createHorizontalBox();

        cbFind.setEditable(true);        
        lblFindWhat = new JLabel("Find What:");
        lblFindWhat.setDisplayedMnemonic('F');
        lblFindWhat.setLabelFor(cbFind);
        

        Action actionListener = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                btnFind.doClick();
            }
        };
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(enter, "ENTER");
        getRootPane().getActionMap().put("ENTER", actionListener);
        
        KeyStroke escape = KeyStroke.getKeyStroke("ESCAPE");
        Action escapeActionListener = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                btnClose.doClick();
            }
        };
        inputMap.put(escape, "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", escapeActionListener);
        
        firstRow.add(lblFindWhat);
        firstRow.add(Box.createHorizontalStrut(10));
        firstRow.add(cbFind);
        leftMain.add(firstRow);
                               
        // right panel
        Box rightMain = Box.createVerticalBox();
        btnFind = new JButton(" Find ");
        btnFind.setActionCommand("Find");
        btnFind.setMnemonic(KeyEvent.VK_D);
        btnFind.addActionListener(this);
        btnFind.setSelected(false);

        btnClose = new JButton("Close");
        btnClose.setActionCommand("Close");
        btnClose.setMnemonic(KeyEvent.VK_C);
        btnClose.addActionListener(this);
        
        rightMain.add(btnFind);
        rightMain.add(Box.createVerticalStrut(10));
        rightMain.add(btnClose);
        rightMain.add(Box.createVerticalGlue());        

        // options panel
        JPanel panelOptions = new JPanel();
        TitledBorder border = new TitledBorder("Options");
        panelOptions.setBorder(border);
        panelOptions.setLayout(new GridLayout(4,2));

        panelOptions.add(ckKeys);
        panelOptions.add(ckCaseSensitive);
        panelOptions.add(ckAttributeNames);
        panelOptions.add(ckSearchBackwards);
        panelOptions.add(ckData);
        panelOptions.add(ckInheritedAttribute);
        panelOptions.add(ckComments);

        leftMain.add(createHorizontalBox(10));
        leftMain.add(panelOptions);
        leftMain.add(Box.createVerticalGlue());
        
        cp.add(BorderLayout.EAST, createVerticalBox(10));
        cp.add(BorderLayout.SOUTH, createHorizontalBox(10));
        cp.add(BorderLayout.WEST, createVerticalBox(10));
        cp.add(BorderLayout.NORTH, createHorizontalBox(10));
        
        mainPane.add(leftMain);
        mainPane.add(createVerticalBox(10));
        mainPane.add(rightMain);
        cp.add(mainPane, BorderLayout.CENTER);

        //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    protected Box createVerticalBox(int strut) {
        Box bv = Box.createVerticalBox();
        bv.add(Box.createHorizontalStrut(strut));
        return bv;
    }
    protected Box createHorizontalBox(int strut) {
        Box bh = Box.createHorizontalBox();
        bh.add(Box.createVerticalStrut(strut));
        return bh;
    }
    
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        Object obj = e.getSource();
        String objName = "";
        if ((cmd != null) && cmd.equals("Close")) {
            dialog = null;
            cbFind.removeItemListener(findWhatItemListener);
            setVisible(false);
        }
        else if ((cmd != null) && cmd.equals("Find")) {
            FindUtil.find((String)cbFind.getSelectedItem(), 
                            ckKeys.isSelected(),
                            ckAttributeNames.isSelected(),
                            ckData.isSelected(),
                            ckComments.isSelected(),
                            ckCaseSensitive.isSelected(),
                            ckSearchBackwards.isSelected(),
                            ckInheritedAttribute.isSelected());
            btnFind.setSelected(true);
            MenuUtil.setItemEnable(MenuCreator.FINDNEXT, true);
            MenuUtil.setItemEnable(MenuCreator.FINDPREVIOUS, true);            
        }
    }

    class FindWhatItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            int typeChange = e.getStateChange();
            if (typeChange == ItemEvent.SELECTED) {
                String selectedItem = (String) e.getItem();
                int count = cbFind.getItemCount();
                int index = cbFind.getSelectedIndex();
                if (index < 0) {
                    boolean exist = false;
                    for (int i = 0; i < count; i++) {
                        Object item = cbFind.getItemAt(i);
                        if (item instanceof String) {
                            String itemStr = (String) item;
                            if (selectedItem.equals(itemStr)) {
                                exist = true;
                                break;
                            }
                        }
                    }
                    if (!exist) {
                        cbFind.addItem(selectedItem);
                        cbFind.removeItemListener(findWhatItemListener);
                        SorterUtil.sortComboBox(cbFind);
                        cbFind.setSelectedItem(selectedItem);
                        cbFind.addItemListener(findWhatItemListener);
                    }
                }
            }
        }
    }
}
