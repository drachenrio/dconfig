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

import java.awt.Color;
import javax.swing.JTextArea;
import org.moonwave.dconfig.ui.model.AppContext;

/**
 *
 * Custom status line.
 *
 * @author Jonathan Luo
 */
public class Status extends JTextArea {
    Color foregroundColor;
    Color backgroundColor;
    Color errorColor;
    
    /** Creates a new instance of Status */
    public Status() {
        super();
        this.setVisible(true);
        this.setEditable(false);
        this.setBackground(new Color(0xee, 0xee, 0xee)); // or Color.LIGHT_GRAY
        foregroundColor = this.getForeground();
        backgroundColor = this.getBackground();
        errorColor = new Color(0xff, 0x0, 0x0);
    }
    
    public void clear() {
        this.setForeground(foregroundColor);
        this.setBackground(backgroundColor);
        this.setText("");
    }
    
    public void showError(String message) {
        this.setForeground(errorColor);
        this.setBackground(backgroundColor);
        this.setText(message);
    }
    
    public void showMessage(String message) {
        this.setForeground(foregroundColor);
        this.setBackground(backgroundColor);
        this.setText(message);
    }
    public void showKey() {
        showMessage(AppContext.treeContext().getUIKey());
    }
}
