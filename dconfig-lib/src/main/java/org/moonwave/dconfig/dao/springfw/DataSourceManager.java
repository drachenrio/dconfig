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

package org.moonwave.dconfig.dao.springfw;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.moonwave.dconfig.model.ConnectionInfo;
import org.moonwave.dconfig.util.DataSourceUtil;
import org.moonwave.dconfig.util.PropertiesReader;
import org.moonwave.dconfig.util.AppState;
import org.moonwave.dconfig.dao.LibProperties;
import org.moonwave.jdbc.datasource.DriverDataSource;

/**
 * Unified datasource manager for library, editor, and demo.
 *
 * @author Jonathan Luo
 */
public class DataSourceManager {

    private static final Log log = LogFactory.getLog(DataSourceManager.class);

    private static ConnectionInfo connectionInfo;
    private static Properties connProperties;

    /**
     * Creates a new <code>DataSource</code> from config file 
     * conf/dconfig_connection.properties or from <code>ConnectionInfo</code>
     * specified through editor ui.
     */
    public static DataSource getDataSource() {
        DataSource datasource = null;
        if (connectionInfo == null) {
            try {
                DriverManagerDataSourceEx ds = new DriverManagerDataSourceEx();
                Properties properties = getConnectionProperties();
                if (properties != null) {
                    String dataSourceName = LibProperties.getInstance().getProperty("connection.datasource");
                    if (dataSourceName != null) {
                        datasource = DataSourceUtil.getDatasource(dataSourceName);
                    } else {
                        String dsPrefix = "";
                        if (AppState.isLibMode()) {
                            dsPrefix = LibProperties.getInstance().getProperty("active.connection.prefix");
                        } else {
                            if (AppState.isDemoDerby())
                                dsPrefix = "derby.demo";
                            else if (AppState.isDemoHsqldb())
                                dsPrefix = "hsqldb.demo";
                            else if (AppState.isDemoH2())
                                dsPrefix = "h2.demo";
                        }
                        ds.setDriverClassName(properties.getProperty(dsPrefix + ".driverClassName"));
                        ds.setUrl(properties.getProperty(dsPrefix + ".url"));
                        ds.setUsername(properties.getProperty(dsPrefix + ".username"));
                        ds.setPassword(properties.getProperty(dsPrefix + ".password"));
                        //log.info(ds.toString());
                        datasource = ds;
                    }
                }
            } catch (Exception e) {
                log.error(e);
            }
        } else {
            DriverDataSource ds = new DriverDataSource(connectionInfo);
            datasource = ds;
        }
        return datasource;
    }

    protected static Properties getConnectionProperties() {
        if (connProperties == null) {
            String filename = null;
            int i = 0;
            while (true) {
                if (AppState.isLibMode()) {
                    filename = "dconfig_lib.properties";
                    if (i == 1)
                        filename = "conf/dconfig_lib.properties";
                }
                else
                    filename = "conf/dconfig_connection.properties";

                connProperties = PropertiesReader.getProperties(filename);
                if (connProperties != null) {
                    if (AppState.isVerbose())
                        System.out.println("load " + filename + " succeeded");
                    log.info("load " + filename + " succeeded");
                    break;
                } else {
                    if (AppState.isVerbose())
                        System.err.println("load " + filename + " failed");
                    log.error("load " + filename + " failed");
                }
                i++;
                if (i > 1)
                    break;
            }
            // write debug info
            if (log.isInfoEnabled() && AppState.isLibMode()) {
                String dsPrefix = LibProperties.getInstance().getProperty("active.connection.prefix");
                /*
                log.info("driverClassName: " + connProperties.getProperty(dsPrefix + ".driverClassName"));
                log.info("url: " + connProperties.getProperty(dsPrefix + ".url"));
                log.info("userName: " + connProperties.getProperty(dsPrefix + ".username"));
                log.info("password: " + connProperties.getProperty(dsPrefix + ".password"));
                 */
            }
        }
        return connProperties;
    }    

    /**
     * Gets current <code>ConnectionInfo</code>.
     */
    public static ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    /**
     * Sets <code>ConnectionInfo</code> for <code>DataSource</code>.
     */
    public static void setConnectionInfo(ConnectionInfo ci) {
        connectionInfo = ci;
    }
}
