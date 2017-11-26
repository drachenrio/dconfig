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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.springfw.DataSourceManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 *
 * @author Jonathan Luo
 */
public class DbMetaDataUtil {

    private static final Log log = LogFactory.getLog(DbMetaDataUtil.class);
    private Connection conn = null;

    public DbMetaDataUtil() {
    }

    private DataSource dataSource;

    // ---------------------------------------------------------- Public Methods

    public DataSource getDataSource() {
    	if (dataSource == null)
            dataSource = DataSourceManager.getDataSource();
        return dataSource;
    }

    public void setDataSource(DataSource ds) {
    	dataSource = ds;
    }

    public void init() {
        try {
            conn = DataSourceUtils.getConnection(getDataSource());
        } catch (Exception e) {
            log.error(e, e);
        }
    }
    
    public void cleanUp() {
        DbUtils.closeQuietly(conn);
    }
        
    public void getColumnSize(String tableName) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement() ;
            rs = stmt.executeQuery( "SELECT * FROM " + tableName) ;
            ResultSetMetaData md = rs.getMetaData() ;

            // Get column size (precision)
            for( int i = 1; i <= md.getColumnCount(); i++ ) {
                if (tableName.equals(TableConst.DConfigKey.tableName)) {
                    if (md.getColumnName(i).equalsIgnoreCase(TableConst.DConfigKey.keyName)) {
                        TableConst.DConfigKey.keyNameSize = md.getPrecision(i);
                        break;
                    }
                } else if (tableName.equals(TableConst.DConfigAttribute.tableName)) {
                    if (md.getColumnName(i).equalsIgnoreCase(TableConst.DConfigAttribute.attributeName))
                        TableConst.DConfigAttribute.attributeNameSize = md.getPrecision(i);
                    else if (md.getColumnName(i).equalsIgnoreCase(TableConst.DConfigAttribute.attributeValue))
                        TableConst.DConfigAttribute.attributeValueSize = md.getPrecision(i);
                    else if (md.getColumnName(i).equalsIgnoreCase(TableConst.DConfigAttribute.comments))
                        TableConst.DConfigAttribute.commentsSize = md.getPrecision(i);
                } else if (tableName.equals(TableConst.DConfigSystem.tableName)) {
                    if (md.getColumnName(i).equalsIgnoreCase(TableConst.DConfigSystem.systemName))
                        TableConst.DConfigSystem.systemNameSize = md.getPrecision(i);
                    else if (md.getColumnName(i).equalsIgnoreCase(TableConst.DConfigSystem.systemValue))
                        TableConst.DConfigSystem.systemValueSize = md.getPrecision(i);
                }
            }
        }
        catch (SQLException se) {
            log.error("SQL Exception:");
            while (se != null) { // Loop through the SQL Exceptions
                log.error( "State: '" + se.getSQLState() +
                           "', Message: '" + se.getMessage() +
                           "', Error: '" + se.getErrorCode() + "'");
                se = se.getNextException();
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
        }
    }

    public static void main(String[] argv) {
        DbMetaDataUtil util = new DbMetaDataUtil();
    }
}
