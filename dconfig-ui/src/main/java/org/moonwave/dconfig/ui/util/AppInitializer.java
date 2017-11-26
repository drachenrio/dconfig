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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.moonwave.dconfig.model.DriverInfo;
import org.moonwave.dconfig.ui.DlgAbout;
import org.moonwave.dconfig.ui.model.PropertyItem;
import org.moonwave.dconfig.util.AppState;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.LoggerUtil;
import org.moonwave.dconfig.util.PropertiesReader;

/**
 *
 * @author Jonathan Luo
 */
public class AppInitializer {
    
    /**
     * Performs application initializations.
     */
    public static boolean initialize(String[] args) {
        AppState.setLibMode(false);
        for (int i = 0;  i < args.length; i++) {
            if (args[i].equalsIgnoreCase("demo"))
                AppState.setDemo(true);
            else if (args[i].equalsIgnoreCase("derby"))
                AppState.setDerby(true);
            else if (args[i].equalsIgnoreCase("hsqldb"))
                AppState.setHsqldb(true);
            else if (args[i].equalsIgnoreCase("h2"))
                AppState.setH2(true);
            else if (args[i].equalsIgnoreCase("verbose"))
                AppState.setVerbose(true);            
        }
        
        // create local working dir
        boolean success = true;
        if (!new File(Constants.DCFGHOME).exists())
            success = (new File(Constants.DCFGHOME)).mkdir();
        
        // initialize logger
        Properties propreties = PropertiesReader.getProperties("conf/log4j.properties");
        String logFile = propreties.getProperty("log4j.appender.dconfig.File");
        if (logFile.indexOf("USER_HOME") >=0 )
            logFile = logFile.replaceFirst("USER_HOME", Constants.DCFGHOME);
        else
            logFile = Constants.DCFGHOME + "/" + logFile;
        System.out.println(logFile);
        
        List propList = DlgAbout.getPropertyList();
        propList.add(new PropertyItem("app.logfile", logFile));
        
        propreties.setProperty("log4j.appender.dconfig.File", logFile);
        LoggerUtil.load(propreties);
        
        // initialize application properties
        try {
            String remotePropFile = "conf/dconfig_app.properties";
            String localPropFile = Constants.DCFGHOME + "/dconfig_app.properties";
            Properties remote = PropertiesReader.getProperties(remotePropFile);
            // update local app info
            Properties local = new Properties();
            if (new File(localPropFile).exists()) {
                FileInputStream in = new FileInputStream(localPropFile);
                local.load(in);
                in.close();
            }
            // add any new entries from remote to local
            Enumeration en = remote.keys();
            while (en.hasMoreElements()) {
                String key = (String)en.nextElement();
                // only skip JDBC related keys
                boolean isJdbcKey = (key.indexOf("driverClass") >= 0) || 
                                    (key.indexOf("dburl") >= 0) ||
                                    (key.indexOf("JDBCDriver.supported") >= 0);
                if (isJdbcKey) // skip any *driverClass, *dburl properties
                    continue;
                local.setProperty(key, remote.getProperty(key));
                if (AppState.isVerbose())
                    System.out.println(key);
            }
           
            // save changes
            FileOutputStream out = new FileOutputStream(localPropFile);
            local.store(out, null);
            out.close();          
            
            // update local driver info
            // load local driver info
            DriverInfoUtil.load();
            List driverList = DriverInfoUtil.getDriverInfoList();

            boolean hasChanged = false;
            String jdbcDriverSupported = remote.getProperty("JDBCDriver.supported");
            String[] jdbcDrivers = StringUtils.split(jdbcDriverSupported, ",");
            for (int i = 0; i < jdbcDrivers.length; i++) {
                String driverName = jdbcDrivers[i];
                if ((driverName == null) || DriverInfoUtil.existDriverName(driverName))
                    continue;
                // create a new entry
                String driverClass = remote.getProperty(AppProperties.toLookupKey(driverName + ".driverClass"));
                String dburl = remote.getProperty(AppProperties.toLookupKey(driverName + ".dburl"));
                DriverInfo driverInfo = new DriverInfo();
                driverInfo.setDriverName(driverName);
                driverInfo.setDriverClass(driverClass);
                driverInfo.setSupportedDriverClass(driverClass);
                driverInfo.setSaveRequested(true);
                driverInfo.setUrlFormat(dburl);
                driverInfo.setSaveRequested(true);
                driverList.add(driverInfo);
                //
                hasChanged = true;
            }
            // save changes
            if (hasChanged) {
                DriverInfoUtil.store();
            }            
        } catch (Exception e) {
            // show a dialog saying cannot start the application and quit
            System.err.println(e);
            success = false;
        }
        
        AppProperties.getInstance().load();
        return success;
     }
}
