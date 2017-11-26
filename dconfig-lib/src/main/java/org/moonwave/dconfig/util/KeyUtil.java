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

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jonathan Luo
 */
public class KeyUtil {
    
    /**
     * Converts an array of tokens to a dot-separated keyname.
     */
    public static String toKeyname(String[] keys) {
        StringBuffer keyname = new StringBuffer(100);
        for (int i = 0; i < keys.length; i++) {
            if (i != 0)
                keyname.append(".");
            keyname.append(keys[i]);
        }
        return keyname.toString();
    }
    
    /**
     * Gets the lead portion of a dot-separated keyname, excludes the last keyname. 
     * e.g. for a key com.sun.studio, com.sun is returned.
     * for a key like config, empty string is returned.
     * 
     * @param keyName dot-separated key name
     * @return lead key name; empty string if current node is the first level node.
     */
    public static String getLeadKeyName(String keyName) {
    	if (keyName.equals("/")) // root
            return Constants.EMPTY_STRING;
    	int lastDotIdx = keyName.lastIndexOf(Constants.KEY_SEPARATOR);
    	if (lastDotIdx < 0)
            return Constants.EMPTY_STRING;
    	else
            return keyName.substring(0, lastDotIdx);
    }

    /**
     * Gets the parent key name.
     *
     * @param keyName dot-separated key name
     * @return parent key name; empty string if parent is root.
     */
    public static String getParentKeyName(String keyName) {
        return getLeadKeyName(keyName);
    }

    /**
     * Gets the first token of a dot-separated keyname.
     * e.g. for a key 'com.sun.studio', 'com' is returned.
     * for a key like 'config', 'config' is returned.
     * 
     * @param keyName dot-separated key name
     * @return first token of the key name; empty string if current node is the first level node.
     */
    public static String getFirstKeyName(String keyName) {
    	if (keyName.equals("/")) // root
            return Constants.EMPTY_STRING;
    	int firstDotIdx = keyName.indexOf(Constants.KEY_SEPARATOR);
    	if (firstDotIdx < 0) {
            if (keyName.length() > 0)
                return keyName;
            else
                return Constants.EMPTY_STRING;
        }
    	else
            return keyName.substring(0, firstDotIdx);
    }
    

    /**
     * Gets the next leading key name.
     * e.g. getNextLeadingKeyName("", "com.sun.studio") returns "com"
     * e.g. getNextLeadingKeyName(null, "com.sun.studio") returns "com"
     * e.g. getNextLeadingKeyName("com", "com.sun.studio") returns "com.sun"
     * e.g. getNextLeadingKeyName("com.sun", "com.sun.studio") returns "com.sun.studio"
     * e.g. getNextLeadingKeyName("com.sun.studio", "com.sun.studio") returns ""
     * 
     * @param leadingKeyName dot-separated leading key name; null is allow.
     * @param keyName a dot-separated key name in full.
     * @return remaining key name; empty string if current node is the first level node.
     */
    public static String getNextLeadingKeyName(String leadingKeyName, String keyName) {
        
        StringBuffer sb = new StringBuffer();
        if (leadingKeyName == null)
            leadingKeyName = "";
        if (keyName.equals("/")) // root
            return Constants.EMPTY_STRING;
        if (leadingKeyName.length() == keyName.length())
            return Constants.EMPTY_STRING;
        if (leadingKeyName.length() > 0)
            sb.append(leadingKeyName).append(Constants.KEY_SEPARATOR);
        int startIdx = (leadingKeyName.length() > 0) ? leadingKeyName.length() + 1 : 0;
        int endIdx = keyName.indexOf(Constants.KEY_SEPARATOR, startIdx);
        endIdx = (endIdx == -1) ? keyName.length() : endIdx;
        sb.append(keyName.substring(startIdx, endIdx));
        return sb.toString();
    }
    
    /**
     * Converts a dot-separated key name to and array by splitting the 'dot'.
     *
     */
    public static String[] toArray(String keyname) {
        return StringUtils.split(keyname, Constants.KEY_SEPARATOR);
    }

    /**
     * Convertes a node list, up to given index, to key.
     * For example, toKey({"java", "sun", "com"}, 1) returns "java.sun" string.
     *
     * @param nodeNameList
     * @param uptoIndex 
     */
    public static String toKey(String[] nodeNameList, int uptoIndex) {
    	String nodeName = "";
    	for (int i = 0; i <= uptoIndex; i++) {
            if (i > 0)
                nodeName += Constants.KEY_SEPARATOR;
            nodeName += nodeNameList[i];
    	}
    	return nodeName;
    }    
}
