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

package org.moonwave.dconfig.util;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String KEY_SEPARATOR = ".";
    public static final List   EMPTY_ARRAYLIST = new ArrayList();
    public static final String EMPTY_STRING = "";
    public static final String DELIMITER = "&^#;"; // delimiter for array items
    public static String USERHOME = System.getProperty("user.home");
    public static final String WORKINGDIR = System.getProperty("user.dir");
    public static final String PRIMARY_ATTRIBUTE = "(primary)";
    public static String DCFGHOME = USERHOME + "/.dconfig";


    static {
        if (System.getProperty("os.name").indexOf("Windows") >= 0) {
            USERHOME = USERHOME.replace('\\', '/');
            DCFGHOME = USERHOME + "/.dconfig";
        }
    }
}
