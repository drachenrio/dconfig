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
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.springfw.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Helper class to startup or shutdown demo database.
 *
 * @author Jonathan Luo
 */
public class DemoDbManager {
    private static final Log log = LogFactory.getLog(DemoDbManager.class);
    
    private static Connection conn;
    private static DataSource datasource;
    
    /**
     * Starts up embedded demo database.
     *
     */
    public static boolean startup() {
        boolean success = false;
        try {
            datasource = DataSourceManager.getDataSource();
            conn = DataSourceUtils.getConnection(datasource);
            success = true;
            log.info("starting up demo db " + conn.toString() + " successful");
    	} catch (Exception e) {
            log.info("starting up demo db failed");
            log.error(e, e);
        }
        return success;
    }

    /**
     * Shuts down embedded demo database.
     *
     */
    public static void shutdown() {
        try {
            if (AppState.isDemoHsqldb()) {
                JdbcTemplate jt = new JdbcTemplate(datasource);
                jt.execute("SHUTDOWN");
            }
            DbUtils.closeQuietly(conn);
            conn = null;
            log.info("shutdown demo db successful");
    	} catch (Exception e) {
            log.info("shuting down demo db failed");
            log.error(e, e);
        }
    }
}
