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

package org.moonwave.dconfig.dao;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.util.*;

/**
 * Manage library properties.
 *
 * @author Jonathan Luo
 */
public class LibProperties {
    private static final Log log = LogFactory.getLog(LibProperties.class);	    
    private static String propertiesFile = "dconfig_lib.properties";
            
    private Properties properties;

    static LibProperties libProperties;
    
    public static LibProperties getInstance() {
        if (libProperties == null)
            libProperties = new LibProperties();
        return libProperties;
    }
    
    protected LibProperties() {
        load();
    }
    
    /**
     * Delegate to search for the property with the specified key in this property list.
     *
     * @param key
     * @see   #setProperty
     */
    public String getProperty(String key) {
        return (properties.getProperty(key) != null) ? properties.getProperty(key).trim() : "";
    }

    /**
     * Delegate to searche for the property with the specified key in this property list.
     * The method returns the default value argument if the property is not found.
     *
     * @param   key            the hashtable key.
     * @param   defaultValue   a default value.
     *
     * @return  the value in this property list with the specified key value.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
        
    /**
     * Loads application properties into memory.
     *
     */
    public synchronized void load() {
        properties = PropertiesReader.getProperties(propertiesFile);
        if (properties == null) {
            properties = PropertiesReader.getProperties("conf/" + propertiesFile);            
        }
    }

    /**
     * Releases application properties resource.
     *
     */
    public void unload() {
        if (properties != null)
            properties.clear();
        properties = null;
    }

}
