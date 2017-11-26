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

import java.text.SimpleDateFormat;
import java.util.*;

public class DataConverter {

    /**
     * Converts a date string in stored format to a date.
     *
     * @param dateString date string in stored format: yyyy-MM-dd HH:mm:ss.
     * @return Converted date on success; null otherwise.
     */
    public static Date toDate(String dateString) {
        SimpleDateFormat fmt = null;
        if (dateString.indexOf("/") >=0 )
            fmt = new SimpleDateFormat(DateUtil.DEFAULT_DISPLAY_FORMAT);
        else
            fmt = new SimpleDateFormat(DateUtil.STORED_FORMAT);
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (Exception e) {
        }
        return date;
    }
    
    public static Double toDouble(String dateString) {
        return null;
    }
    
}
