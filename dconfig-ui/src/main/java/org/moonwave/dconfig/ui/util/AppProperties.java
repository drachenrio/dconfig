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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.model.DriverInfo;
import org.moonwave.dconfig.util.*;

public class AppProperties {
    private static final Log log = LogFactory.getLog(AppProperties.class);	    
    private static String propertiesFile = Constants.DCFGHOME + "/dconfig_app.properties";
    private static String DRIVERCLASS = ".driverClass";
    private static String DBURL = ".dburl";
    private static String DRIVERFILEPATH = ".driverFilePath";
    private static String CONFIGOK = ".ok";
            
    private Properties properties;

    static AppProperties appProperties;
    
    public static AppProperties getInstance() {
        if (appProperties == null)
            appProperties = new AppProperties();
        return appProperties;
    }
    
    protected AppProperties() {
    }
    
    /**
     * Delegate to searche for the property with the specified key in this property list.
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
        properties = PropertiesReader.getLocalProperties(propertiesFile);
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

    /**
     * Saves application properties into local hard drive.
     *
     */
    public synchronized void store() {
        try {
            FileOutputStream out = new FileOutputStream(propertiesFile);
            properties.store(out, null);
            out.close();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public String[] getDriverSupported() {
        String jdbcDrivers = getProperty("JDBCDriver.supported");
        return StringUtils.split(jdbcDrivers, ",");        
    }
    public String getDriverClass(String driverName) {
        return getProperty(toLookupKey(driverName) + DRIVERCLASS);
    }
    public void setDriverClass(String driverName, String driverClassName) {
        setProperty(toLookupKey(driverName) + DRIVERCLASS, driverClassName);
    }
    public String getDbUrl(String driverName) {
        return getProperty(toLookupKey(driverName) + DBURL);
    }
    public void setDbUrl(String driverName, String dburl) {
        setProperty(toLookupKey(driverName) + DBURL, dburl);
    }
    public String getDriverFilePath(String driverName) {
        return getProperty(toLookupKey(driverName) + DRIVERFILEPATH);
    }
    
    
    /**
     * Sets jdbc driver file path for a given 
     *
     * @param driverName drive name
     * @param driverFilePath the jdbc drive jar file path.
     */    
    public void setDriverFilePath(String driverName, String driverFilePath) {
        setProperty(toLookupKey(driverName) + DRIVERFILEPATH, driverFilePath);
    }
    
    /**
     * Retrieve driver OK status for a given driver name.
     *
     * @param driverName drive name
     * @return driver OK status.
     */    
    public String getDriverOkStatus(String driverName) {
        return getProperty(toLookupKey(driverName) + CONFIGOK);
    }
    
    /**
     * Sets driver OK status.
     *
     * @param driverName drive name
     * @param status "true" or "false"
     */
    public void setDriverOkStatus(String driverName, String status) {
        setProperty(toLookupKey(driverName) + CONFIGOK, status);
    }

    public List getConfiguredDriverList() {
        List list = new ArrayList();
        String[] drivers = getDriverSupported();
        for (int i = 0; i < drivers.length; i++) {
            String driverName = drivers[i];
            if ((getDriverOkStatus(driverName) != null) && (getDriverOkStatus(driverName).equals("true"))) {
                DriverInfo info = new DriverInfo();
                info.setDriverName(driverName);
                info.setDriverClass(getDriverClass(driverName));
                info.setUrlFormat(getDbUrl(driverName));
                info.setDriverFilePath(getDriverFilePath(driverName));
                list.add(info);
            }
        }
        return list;
    }

    /**
     * Converts a driver name to look up key foramt by replacing '/' and ' ' with
     * '_'.
     * 
     * @param driverName drive name to be converted.
     *
     */
    public static String toLookupKey(String driverName) {
        String retValue = driverName.replaceAll("/", "_");
        retValue = retValue.replaceAll(" ", "_");
        return retValue;
    }
}
