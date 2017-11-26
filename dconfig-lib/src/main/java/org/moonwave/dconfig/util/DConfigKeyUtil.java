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

import org.apache.commons.lang.StringUtils;

import org.moonwave.dconfig.model.DConfigKey;

/**
 *
 * @author Jonathan Luo
 */
public class DConfigKeyUtil {
    
    public DConfigKeyUtil() {
    }
    
    /**
     * 
     * @param keyList
     * @param level 0-indexed, level 0 is the root level.
     * @return array of key names for matched level.
     */
    public static String[] getKeyNameByLevel(List keyList, int level) {
        String[] prevKeys;// = new String[10]; // max tree node level
    	String[] keys = new String[10];
    	List resultList = new ArrayList();
    	if (level > 0) {
            for (int i = 0; i < keyList.size(); i++) {
                DConfigKey regKey = (DConfigKey) keyList.get(0);
                prevKeys = StringUtils.split(regKey.getKeyName(), ".");
                String key = prevKeys[level - 1];
                if (!resultList.contains(key))
                    resultList.add(key);
            }
    	}
    	return (String[])resultList.toArray();
    }
}
