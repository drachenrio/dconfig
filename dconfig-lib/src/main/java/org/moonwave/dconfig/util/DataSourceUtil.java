package org.moonwave.dconfig.util;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author Jonathan Luo
 */
public class DataSourceUtil {

    private static final Log log = LogFactory.getLog(DataSourceUtil.class);

    public static java.sql.Connection getPostgresConnection() {
        java.sql.Connection conn = null;
        try {
            InitialContext initialContext = new InitialContext();
            DataSource ds = (DataSource) initialContext.lookup("java:PostgresDS");
            conn = ds.getConnection();
        } catch (Exception e) {
            log.error(e, e);
        }
        return conn;
    }

    public static DataSource getPostgresDatasource() {
        DataSource ds = null;
        try {
            InitialContext initialContext = new InitialContext();
            ds = (DataSource) initialContext.lookup("java:PostgresDS");
        } catch (Exception e) {
            log.error(e, e);
        }
        return ds;
    }

    public static DataSource getDatasource(String dataSourceName) {
        DataSource ds = null;
        try {
            InitialContext initialContext = new InitialContext();
            ds = (DataSource) initialContext.lookup(dataSourceName);
            if (ds == null)
                ds = (DataSource) initialContext.lookup("java:" + dataSourceName);
        } catch (Exception e) {
            log.error(e, e);
        }
        return ds;
    }
}
