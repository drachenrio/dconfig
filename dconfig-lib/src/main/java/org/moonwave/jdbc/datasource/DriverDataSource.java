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

package org.moonwave.jdbc.datasource;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.springfw.SmartDataSourceEx;
import org.moonwave.dconfig.model.ConnectionInfo;
import org.moonwave.dconfig.util.Timer;
import org.netbeans.modules.derby.DbURLClassLoader;
import org.springframework.jdbc.datasource.AbstractDataSource;

/**
 * <code>DataSource</code> constructed from <code>ConnecionInfo</code>.
 *
 * @author Jonathan Luo
 */
public class DriverDataSource extends AbstractDataSource implements SmartDataSourceEx {
    private static final Log log = LogFactory.getLog(DriverDataSource.class);

    private static Map jarFilePath_class = new HashMap();

    private ConnectionInfo connectionInfo;
    private String username;
    private String password;
    private java.sql.Connection con;
    private boolean connectionCanClose;
    
    public DriverDataSource(){
        con = null;
        connectionCanClose = true;
    }

    public DriverDataSource(ConnectionInfo connectionInfo){
        this.connectionInfo = connectionInfo;
        con = null;
        connectionCanClose = true;
    }

    /**
     * Loads and creates jdbc driver from jdbc driver jar file specified
     * by <code>ConnectionInfo</code> data members.
     */
    private java.sql.Driver loadJdbcDriver() 
                            throws MalformedURLException,
                                   ClassNotFoundException,
                                   InstantiationException,
                                   IllegalAccessException
    {  
        
        Class c = (Class) jarFilePath_class.get(connectionInfo.getJarFilePath());
        if (c == null) {
            java.net.URL[] driverURLs = new java.net.URL[1];
            driverURLs[0] = new File(this.connectionInfo.getJarFilePath()).toURI().toURL();
            DbURLClassLoader loader = new DbURLClassLoader(driverURLs);
            c = Class.forName(this.connectionInfo.getDriverClassName(), true, loader);
            jarFilePath_class.put(connectionInfo.getJarFilePath(), c);
        }
        return (java.sql.Driver)c.newInstance();
    }
    
    /**
     * Creates a new connection based on datasource information provided by 
     * <code>ConnectionInfo</code> data members. Or returns existing connection
     * from the same <code>DataSource</code> instance.
     * Throws exception if failed.
     * 
     */
    public java.sql.Connection getConnection() throws java.sql.SQLException {
        if (con == null) {
            try {
                java.sql.Driver driver = loadJdbcDriver();
                Properties properties = new Properties();
                if (this.username != null)
                    properties.put("user", this.username);
                else
                    properties.put("user", this.connectionInfo.getUsername());
                if (this.password != null)
                    properties.put("password", this.password);
                else
                    properties.put("password", this.connectionInfo.getPassword());
                con = driver.connect(this.connectionInfo.getDatabaseURL(), properties);
            } catch (Exception e) {
                log.error(e);
                throw new java.sql.SQLException();
            }
        }
        return con;        
    }
    
    /**
    * <p>Attempts to establish a connection with the data source that
    * this <code>DataSource</code> object represents.
    *
    * @param username the database user on whose behalf the connection is 
    *  being made
    * @param password the user's password
    * @return  a connection to the data source
    * @exception SQLException if a database access error occurs
    */
    public java.sql.Connection getConnection(String username, String password) 
                               throws SQLException {
        this.username = username;
        this.password = password;
        return getConnection();
    }
    
    /**
     * Creates a db connection based on datasource information provided
     * by <code>ConnectionInfo</code> data members. Throws exception if failed.
     */
    public java.sql.Connection getConnection(ConnectionInfo connectionInfo)
                               throws Exception {
        this.connectionInfo = connectionInfo;
        return getConnection();
    }
    
    /** 
     * Set whether the connection obtained from this DataSource can be close.
     * 
     * @param b true indicates connection can be closed; false connection should
     *          stay open. 
    */
    public void setConnectionClose(boolean b) {
        this.connectionCanClose = b;
    }

    /** 
     * Should we close this connection, obtained from this DataSource?
     * <p>Code that uses connections from a SmartDataSource should always
     * perform a check via this method before invoking <code>close()</code>.
     * <p>However, the JdbcTemplate class in the core package should take care of
     * closing JDBC connections, freeing application code of this responsibility.
     * @param con connection, which should have been obtained
     * from this data source, to check closure status of
     * @return whether the given connection should be closed
     */
    public boolean shouldClose(Connection con) {
        return connectionCanClose;
    } 
    
    /**
     * Implementation for Wrapper interface, in order to compile in JDK 6.0
     */
    public Object unwrap(java.lang.Class iface) throws java.sql.SQLException {
        throw new java.sql.SQLException();
    }

    /**
     * Implementation for Wrapper interface, in order to compile in JDK 6.0
     */
    public boolean isWrapperFor(java.lang.Class iface) throws java.sql.SQLException {
        return false;
    }    
}
