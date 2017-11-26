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

import java.sql.Connection;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author Jonathan Luo
 */
public class DriverManagerDataSourceEx extends DriverManagerDataSource implements SmartDataSourceEx {

    private boolean connectionCanClose;
    
    /** Creates a new instance of DriverManagerDataSourceEx */
    public DriverManagerDataSourceEx() {
    }

    /** 
	 * Set whether the connection obtained from this DataSource can be close.
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
        
    public String toString() {
        StringBuffer sb = new StringBuffer(100);
        sb.append("driverClassName: ").append(this.getDriverClassName());
        sb.append(", url: ").append(this.getUrl());
        sb.append(", username: ").append(getUsername());
        sb.append(", password: ").append(this.getPassword());
        return sb.toString();        
    }
}
