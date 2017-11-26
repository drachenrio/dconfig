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

package org.moonwave.dconfig.model;

import java.io.Serializable;

/**
 *
 * @author Jonathan Luo
 */
public class ConnectionInfo implements Serializable {
    String alias;
    String jdbcDriver; // jdbc driver name
    String driverClassName; // jdbc driver name
    String jarFilePath; // jdbc driver name
    String databaseURL;
    String username;
    String password;
    Boolean savePassword;
    Boolean defaultConnection;
    transient boolean saveRequested = false;
    
    public ConnectionInfo() {
        alias = "";
        jdbcDriver = "";
        databaseURL = "";
        username = "";
        password = "";
        savePassword = Boolean.FALSE;
        defaultConnection = Boolean.FALSE;
        saveRequested = false;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public String getJdbcDriver() {
        return jdbcDriver;
    }
    
    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }
        
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
    
    public void setJarFilePath(String jarFilePath) {
        this.jarFilePath = jarFilePath;
    }

    public String getJarFilePath() {
        return jarFilePath;
    }

    public String getDatabaseURL() {
        return databaseURL;
    }
    
    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isSavePassword() {
        return savePassword.booleanValue();
    }
    
    public void setSavePassword(boolean b) {
        this.savePassword = Boolean.valueOf(b);
    }
    
    public boolean isDefaultConnection() {
        return defaultConnection.booleanValue();
    }
    
    public void setDefaultConnection(boolean b) {
        this.defaultConnection = Boolean.valueOf(b);
    }
    
    public boolean isSaveRequested() {
        return saveRequested;
    }
    
    public void setSaveRequested(boolean b) {
        this.saveRequested = b;
    }
    
    public boolean isConfigDone() {
        return ((this.alias.length() > 0) && (this.databaseURL.length() > 0) &&
                (this.jdbcDriver.length() > 0) && (this.username.length() > 0) &&
                (this.password.length() > 0));
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionInfo)) return false;

        final ConnectionInfo other = (ConnectionInfo) o;

        if ((alias != null) && !alias.equals(other.alias)) return false;
        if ((jdbcDriver != null) && !jdbcDriver.equals(other.jdbcDriver)) return false;
        if ((driverClassName != null) && !driverClassName.equals(other.driverClassName)) return false;
        if ((jarFilePath != null) && !jarFilePath.equals(other.jarFilePath)) return false;
        if ((databaseURL != null) && !databaseURL.equals(other.databaseURL)) return false;
        if ((username != null) && !username.equals(other.username)) return false;

        return true;
    }

    public int hashCode() {
        return alias.hashCode();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("alias: ").append(this.alias);
        sb.append(", jdbcDriver: ").append(this.jdbcDriver);
        sb.append(", driverClassName: ").append(this.driverClassName);
        sb.append(", jarFilePath: ").append(this.jarFilePath);
        sb.append(", databaseURL: ").append(this.databaseURL);
        sb.append(", username: ").append(this.username);
        sb.append(", password: ").append(this.password.length() > 0 ? "***" : "");
        sb.append(", savePassword: ").append(savePassword);
        sb.append(", defaultConnection: ").append(defaultConnection);
        return sb.toString();
    }
}
