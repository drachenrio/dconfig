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

import java.net.URL;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;


/** 
 * This class implements some log4j initialization and configuration
 * utility methods.
 *
 */

public class LoggerUtil {

    // --------------------------------------------------------- Public Methods

    /**
     * Initializes loggers and set the default logging level.
     *
     * @param configFilename log4j property file in full path.
     */
    public static void load(String configFilename) {
        try {
            PropertyConfigurator.configure(configFilename);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Initializes loggers and set the default logging level.
     *
     * @param configURL log4j property file URL address.
     */
    public static void load(URL configURL) {
        try {
            PropertyConfigurator.configure(configURL);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Initializes loggers and set the default logging level from argument <code>
     * Properties</code>
     *
     * @param properties properties to do the initialization.
     */
    public static void load(Properties properties) {
        try {
            PropertyConfigurator.configure(properties);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }    
}
