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

import javax.swing.JOptionPane;

import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.util.*;

/**
 * Creates commonly used message dialogs.
 *
 * @author Jonathan Luo
 */
public class DialogUtil {
    
    public static int showConfirm(String title, String message) {
        return JOptionPane.showConfirmDialog(AppContext.appContext().getCfgEditor(), 
                message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    public static int showYesNoConfirm(String title, String message) {
        return JOptionPane.showConfirmDialog(AppContext.appContext().getCfgEditor(), 
                message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    public static void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(AppContext.appContext().getCfgEditor(),
            message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(String title, String message) {
        JOptionPane.showMessageDialog(AppContext.appContext().getCfgEditor(),
            message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showKeyExistError(String keyName) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("Cannot rename current key to '");
        sb.append(keyName);
        sb.append("'. The specified key name already exists. Pleaser try again.");
        JOptionPane.showMessageDialog(AppContext.appContext().getCfgEditor(),
            sb.toString(), "Error Renaming Key", JOptionPane.ERROR_MESSAGE);
    }

    public static void showKeyInvalidCharError(String keyName) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("'" + Constants.KEY_SEPARATOR + "' is not allowed in a key name. Please try again");
        JOptionPane.showMessageDialog(AppContext.appContext().getCfgEditor(),
            sb.toString(), "Error Renaming Key", JOptionPane.ERROR_MESSAGE);
    }

    public static void showAttributeExistError(String attributeName) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("The DConfig Editor cannot rename '");
        sb.append(attributeName);
        sb.append("'. The specified attribute name already exists. Please try again.");
        JOptionPane.showMessageDialog(AppContext.appContext().getCfgEditor(),
            sb.toString(), "Error Renaming Attribute", JOptionPane.ERROR_MESSAGE);
    }

    public static int showDeleteKeyDlg(String attributeName) {
        Object[] options = { "Yes", "No" };
        return JOptionPane.showOptionDialog(AppContext.appContext().getCfgEditor(), 
                "Are yoe sure you want to delete this key and all of its subkeys?",
                "Confirm Key Delete", JOptionPane.DEFAULT_OPTION, 
                JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    }

    public static int showDeleteAttributeDlg(String attributeName) {
        Object[] options = { "Yes", "No" };
        return JOptionPane.showOptionDialog(AppContext.appContext().getCfgEditor(), 
                "Are yoe sure you want to delete this attribute?",
                "Confirm Delete: '" + attributeName + "'", JOptionPane.DEFAULT_OPTION, 
                JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    }
}
