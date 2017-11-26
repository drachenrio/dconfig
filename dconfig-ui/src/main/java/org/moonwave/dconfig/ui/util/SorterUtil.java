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

import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.moonwave.dconfig.util.*;

public class SorterUtil {

    public static void sortComboBox(JComboBox cb) {		
        String[] items = new String[cb.getItemCount()];

        // Get the model data.
        for (int i = 0; i < cb.getItemCount(); i++) {
            items[i] = (String) cb.getItemAt(i);
        }

        Arrays.sort(items);

        // Clear the model data.
        ((DefaultComboBoxModel)cb.getModel()).removeAllElements();

        // Re-add the sorted data to the model.
        for (int i = 0, size = items.length; i < size; i++) {
                cb.addItem(items[i]);
        }
    }
}
