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

import java.io.InputStreamReader;
import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.springfw.*;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.object.SqlUpdate;

/**
 *
 * @author Jonathan Luo
 */
public class DemoDataPopulator {
    private static final Log log = LogFactory.getLog(DemoDataPopulator.class);
    
    /** 
     * Creates a new instance of DemoDataPopulatorDao
     */
    public DemoDataPopulator() {
    }
    
    public boolean populate() {
        boolean bRet = false;
        if (DbUtil.isDbConfigured())
            return true;
            
        DataSource dataSource = DataSourceManager.getDataSource();
        Connection conn = DataSourceUtils.getConnection(dataSource);        
        try {
            conn.setAutoCommit(false);
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
        
            SqlReader in = null;
            String filename = "";
            
            if (AppState.isLibMode()) {
                if (AppState.isDemoDerby())
                    filename = "derby_10.1.3.1.sql";
                else if (AppState.isDemoHsqldb())
                    filename = "hsqldb_1.7.3.3.sql";
                else
                    filename = "h2_1.0.sql";
            } else {
                if (AppState.isDemoDerby()) // desktop
                    filename = "sql.1.2.complete/derby_10.1.3.1.sql";
                else if (AppState.isDemoHsqldb())
                    filename = "sql.1.2.complete/hsqldb_1.7.3.3.sql";
                else
                    filename = "sql.1.2.complete/h2_1.0.sql";
            }
            java.net.URL url = FileUtil.toURL(filename);
            log.info(filename + " --> url: " + url);
                        
            in = new SqlReader(new InputStreamReader(url.openStream()));
            
            String sql;
            int rowAffected;
            SqlUpdate sqlUpdate = null;
            //sqlUpdate.setDataSource(dataSource);
            while (true) {
                sql = in.nextSql();
                if ((sql == null) || (sql.length() == 0))
                    break;
                sqlUpdate = new SqlUpdate(dataSource, sql);
                rowAffected = sqlUpdate.update();
                if (AppState.isVerbose())
                    System.out.println("execute - " + sql);
                log.info("execute - " + sql);
            }
            sqlUpdate = null;
            conn.commit();
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
            in.close();
            bRet = true;
    	} catch (Exception e) {
            System.out.println(e);
            log.error(e, e);
            try {
                log.info("conn.rollback()");
                conn.rollback();
            } catch (Exception e1)
            {
            }
    	} finally {
            DbUtils.closeQuietly(conn);
        }
        return bRet;
    }
}
