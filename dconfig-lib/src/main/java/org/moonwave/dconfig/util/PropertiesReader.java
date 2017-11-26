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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jonathan Luo
 */
public class PropertiesReader {
    
    private static final Log log = LogFactory.getLog(PropertiesReader.class);
    
    private static final Map filename_properties_map = new HashMap();

    /**
     * Gets properties from jar resource.
     *
     * @param propertyFilename relative file path in a jar resource.
     * @return  populated <code>Properties</code> object; or null if failed.
     */
    public static Properties getProperties(String propertyFilename) {
        boolean loadLog4jFile = (propertyFilename.indexOf("log4j") >= 0) ? true : false;
    	Properties properties = null;
        try {
            properties = (Properties) filename_properties_map.get(propertyFilename);
            if (properties == null) {
                URL url = FileUtil.toURL(propertyFilename);
                if (AppState.isVerbose())
                    System.out.println(propertyFilename + "--> url: " + url);
                if (!loadLog4jFile)
                    log.info(propertyFilename + "--> url: " + url);
                if (url != null) {
                    properties = new Properties();
                    InputStream in = url.openStream();
                    properties.load(in);
                    in.close();            
                    url = null;
                    filename_properties_map.put(propertyFilename, properties);
                } else {
                    if (AppState.isVerbose())
                        System.err.println("warning: " + propertyFilename + " not found"); // in case logger not started
                    if (!loadLog4jFile)
                        log.error(propertyFilename + " not found");
                }                
                if (!loadLog4jFile)
                    log.info("load " + propertyFilename + " succeeded");
            }
        } catch (IOException e) {
            System.err.println("error reading " + propertyFilename + " " + e);
            if (!loadLog4jFile)
                log.error("error reading " + propertyFilename + " " + e);
        }
        return properties;
    }

    /**
     * Gets properties from local properties file in hard drive .
     *
     * @param localPropertiesFile local properties file resides on a local hard drive.
     * @return  populated <code>Properties</code> object; null if failed.
     */
    public static Properties getLocalProperties(String localPropertiesFile) {
    	Properties properties = null;
        try {
            properties = (Properties) filename_properties_map.get(localPropertiesFile);
            if (properties == null) {
                properties = new Properties();
                if (new File(localPropertiesFile).exists()) {
                    log.info("localPropertiesFile: " + localPropertiesFile + " exists");
                    FileInputStream in = new FileInputStream(localPropertiesFile);
                    properties.load(in);
                    in.close();
                } else {
                    log.error("cannot find " + localPropertiesFile);                    
                }
                filename_properties_map.put(localPropertiesFile, properties);
            }
        } catch (Exception e) {
            System.err.println("error reading " + localPropertiesFile + " " + e);
        }
        return properties;
    }
    
    /**
     * Helper method to dump a <code>Properties</code> contents.
     *
     * @param properties 
     */
    public static void dump(Properties properties) {
        log.info("begin - dump properties contents");
        for ( Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();
            String value = properties.getProperty( key );
            log.info(key + "=" + value);           
           //System.out.println( key + "=" + value );
       } // end for
        log.info("end - dump properties contents");
    }
}
