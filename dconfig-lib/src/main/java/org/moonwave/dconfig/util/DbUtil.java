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
import org.moonwave.dconfig.dao.springfw.DataSourceManager;
import org.moonwave.jdbc.datasource.MetaDataUtil;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 *
 * @author Jonathan Luo
 */
public class DbUtil {
    
    private static final Log log = LogFactory.getLog(DbUtil.class);

    public static boolean isDbConfigured() {
        boolean bRet = false;
        try {
            DataSource dataSource = DataSourceManager.getDataSource();
            Connection conn = DataSourceUtils.getConnection(dataSource);
            if (MetaDataUtil.tableExists(conn, "dconfig_datatype")) {
                dataSource = null;
                DbUtils.closeQuietly(conn);
                bRet = true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return bRet;
    }
}
