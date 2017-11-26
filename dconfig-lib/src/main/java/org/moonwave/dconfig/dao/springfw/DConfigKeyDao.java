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
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.DConfigReader;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.KeyUtil;
import org.moonwave.dconfig.util.TableConst;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * 
 * @author Jonathan Luo
 */
public class DConfigKeyDao {
    private static final Log log = LogFactory.getLog(DConfigKeyDao.class);

    private static String CREATE_KEY = "insert into " + TableConst.DConfigKey.tableName + " (key_name, inherited, date_created, date_modified) values (?, ?, ?, ?)";
    private static String UPDATE_KEY = "update " + TableConst.DConfigKey.tableName + " set key_name = ?, inherited = ?, date_modified = ? where id = ?";
    private static String DELETE_KEY = "delete from " + TableConst.DConfigKey.tableName + " where id = ?";
    private static String QUERY_ALL_KEYS = "select id, key_name, inherited from " + TableConst.DConfigKey.tableName + " order by key_name";
    private static String QUERY_BY_ID = "select id, key_name, inherited from " + TableConst.DConfigKey.tableName + " where id = ?";
    private static String QUERY_FOR_KEY = "select id, key_name, inherited from " + TableConst.DConfigKey.tableName + " where key_name = ?";
    private static String QUERY_FOR_ID = "select id from " + TableConst.DConfigKey.tableName + " where key_name = ?";
    private static String QUERY_KEYS_START_WITH = "select id, key_name, inherited from " + TableConst.DConfigKey.tableName + " where key_name = ? or key_name like ? order by key_name";

    private DataSource dataSource;
    private JdbcTemplate jt;

    // ---------------------------------------------------------- Public Methods
    
    public DConfigKeyDao() {
    }
    
    public DConfigKeyDao(DataSource ds) {
    	this.dataSource = ds;
    }
    
    public DataSource getDataSource() {
    	if (dataSource == null)
            dataSource = DataSourceManager.getDataSource();
        return dataSource;
    }
    
    /**
     * Set data source.
     * 
     * @param ds datasource to set.
     */
    public void setDataSource(DataSource ds) {
    	dataSource = ds;
    }

    /**
     * Returns a list of all keys from database.
     *
     */
    public List getAllKeys() {
    	List retList = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_ALL_KEYS);
            query.compile();
            query.setRowMapper(new DConfigKeyMapper());
            retList = query.execute();
    	} catch (Exception e) {
            log.error(e, e);
            retList = Constants.EMPTY_ARRAYLIST;
    	}
    	return retList;
    }

    /**
     * Create a new key for a given key name.
     *
     * @param keyName key name.
     * @return number of rows affected.
     */
    public int create(String keyName, String inherited) {
        int iRet = 0;
        //try {
            //jt = new JdbcTemplate(getDataSource());
            
        UpdateTemplate ut = new UpdateTemplate(getDataSource());
        ut.setSql(CREATE_KEY);
        ut.declareParameter(new SqlParameter("keyName", Types.VARCHAR));
        ut.declareParameter(new SqlParameter("inherited", Types.VARCHAR));
        ut.declareParameter(new SqlParameter("dataCreated", Types.TIMESTAMP));
        ut.declareParameter(new SqlParameter("dataModified", Types.TIMESTAMP));
        ut.compile();

        String[] currKeys = StringUtils.split(keyName, Constants.KEY_SEPARATOR);
        // create series of keys
        for (int j = 0; j < currKeys.length; j++) {
            String currentKeyname = KeyUtil.toKey(currKeys, j);
            if (isKeyExist(currentKeyname))
                continue;                
            iRet = ut.update(new Object[] { currentKeyname, inherited,
                                            Calendar.getInstance().getTime(), 
                                            Calendar.getInstance().getTime() });
        }
    	//} catch (Exception e) {
        //    log.error(e, e);
    	//}
        new DConfigSystemDao().setNewDataChangeTimeStamp();
    	return iRet;
    }
        
    /**
     * Commits any <code>DConfigKey</code> changes to database. Starts its own transaction.
     *
     * @param dconfigKey the current <code>DConfigKey</code> object.
     * @return true on success; false otherwise.
     */
    public boolean save(DConfigKey dconfigKey) {
    	if (dconfigKey == null)
            return true;
    	boolean bRet = false;
        Connection conn = null;
    	try {
            conn = DataSourceUtils.getConnection(getDataSource());
            conn.setAutoCommit(false);
            ((SmartDataSourceEx) dataSource).setConnectionClose(false);            

            if (dconfigKey.isNew())
                create(dconfigKey);
            else if (dconfigKey.isDelete())
                delete(dconfigKey);
            else if (dconfigKey.hasChanged())
                update(dconfigKey);
            conn.commit();
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
            dconfigKey.clearChangeFlag();
            bRet = true;
    	} catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1)
            {
            }
            log.error(e, e);
    	}
    	return bRet;
    }

    /**
     * Creates a new key. Transaction started by caller.
     *
     * @param key key to be updated.
     * @return number of rows updated
     */
    public int create(DConfigKey key) {    	
    	int iRet = 0;
    	if (key != null) {
            iRet = create(key.getKeyName(), key.getInherited());

            jt = new JdbcTemplate(getDataSource());
            int id = jt.queryForInt(QUERY_FOR_ID, new Object[] {key.getKeyName()});
            key.setId(Integer.valueOf(id));
            key.clearChangeFlag();
    	}
    	return iRet;
    }

    /**
     * Creates a new key. Transaction started by caller.
     *
     * @param keyName key to be updated.
     * @return number of rows updated
     */
    public boolean isKeyExist(String keyName) {    	
    	boolean bRet = false;
        try {
            jt = new JdbcTemplate(getDataSource());
            int id = jt.queryForInt(QUERY_FOR_ID, new Object[] {keyName});
            if (id > 0) 
                bRet = true;
        } catch (IncorrectResultSizeDataAccessException e) {
            // ignore
        } catch (Exception e) {
            log.error(e);
        }
        return bRet;
    }
    
    /**
     * Updates a specified key. Transaction started by caller. // move clear flag out to transaction method
     *
     * @param key key to be updated.
     */
    public int update(DConfigKey key) {
    	int iRet = 0;
        jt = new JdbcTemplate(getDataSource());
        iRet = jt.update(UPDATE_KEY, new Object[] { key.getKeyName(), key.getInherited(),
                         Calendar.getInstance().getTime(), key.getId() });
        key.clearChangeFlag();
        new DConfigSystemDao().setNewDataChangeTimeStamp();
    	return iRet;
    }

    /**
     * Deletes a specified key. Transaction started by caller.
     *
     * @param key key to be deleted.
     * @return number of rows affected.
     */
    public int delete(DConfigKey key) {
    	int iRet = 0;
        jt = new JdbcTemplate(getDataSource());
        iRet = jt.update(DELETE_KEY, new Object[] {key.getId()});
        new DConfigSystemDao().setNewDataChangeTimeStamp();
    	return iRet;
    }    

    /**
     * Retrieves <code>DConfigKey</code> by id.
     *
     * @param id key id to search for.
     * @return <code>DConfigKey</code> object if found; null otherwise.
     */
    public DConfigKey getById(Integer id) {
    	DConfigKey key = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_BY_ID);
            query.declareParameter(new SqlParameter("id", Types.INTEGER));
            query.compile();
            query.setRowMapper(new DConfigKeyMapper());
            List resultList = query.execute(new Object[] {id});
            if (resultList.size() > 0)
                key = (DConfigKey) resultList.get(0);
    	} catch (Exception e) {
            log.error(e, e);
    	}
    	return key;
    }
    
    /**
     * Retrieves <code>DConfigKey</code> by key name.
     *
     * @param keyName key name to search for.
     * @return <code>DConfigKey</code> object if found; null otherwise.
     */
    public DConfigKey getByKey(String keyName) {
        DConfigKey key = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_FOR_KEY);
            query.declareParameter(new SqlParameter("keyNmae", Types.VARCHAR));
            query.compile();
            query.setRowMapper(new DConfigKeyMapper());            
            List resultList = query.execute(new Object[] {keyName});
            if (resultList.size() > 0)
                key = (DConfigKey) resultList.get(0);            
    	} catch (Exception e) {
            log.error(e, e);
    	}
    	return key;
    }

    /**
     * Queries the id for a given key name.
     *
     * @param keyName key name to search.
     * @return key id on success; -1 on error.
     */
    public int getIdForKeyname(String keyName) {    	
    	int iRet = 0;
    	if (keyName != null) {
            try {
                jt = new JdbcTemplate(getDataSource());
                iRet = jt.queryForInt(QUERY_FOR_ID, new Object[] {keyName});
            } catch (Exception e) {
                iRet = -1;
            }
    	}
    	return iRet;
    }

    /**
     * Gets a list of <code>DConfigKey</code> start with given whole key name. 
     * Example:
     *  keyStartWith("con") yields query string
     *    select id, key_name, inherited from dconfig_key where key_name = 'con' 
     *    or key_name like 'con.%' order by key_name
     *  This may return null depends on actual key data.
     *
     * Note: This method is used for builing tree in the ui.
     *
     * @param keynameStartWith key name to start with.
     */
    public List keyStartWith(String keynameStartWith) {
        List resultList;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_KEYS_START_WITH);
            query.declareParameter(new SqlParameter("keys1", Types.VARCHAR));
            query.declareParameter(new SqlParameter("keys2", Types.VARCHAR));
            query.compile();
            query.setRowMapper(new DConfigKeyMapper());
            resultList = query.execute(new Object[] {keynameStartWith, keynameStartWith + ".%"});
    	} catch (Exception e) {
            log.error(e, e);
            resultList = new ArrayList(); // return empty list
    	}
    	return resultList;
    }

    /**
     * Gets a list of <code>DConfigKey</code> start with given key token.
     * Example:
     *  keyStartWith("con") yields query string
     *    select id, key_name, inherited from dconfig_key where key_name = 'con' 
     *    or key_name like 'con%' order by key_name
     *  This may return some data depends on actual key data.
     *
     * Note: This method is used for ajax query.
     *
     * @param keynameStartWith key name to start with.
     */
    public List getKeyStartWith(String keynameStartWith) {
        List resultList;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_KEYS_START_WITH);
            query.declareParameter(new SqlParameter("keys1", Types.VARCHAR));
            query.declareParameter(new SqlParameter("keys2", Types.VARCHAR));
            query.compile();
            query.setRowMapper(new DConfigKeyMapper());
            resultList = query.execute(new Object[] {keynameStartWith, keynameStartWith + "%"});
    	} catch (Exception e) {
            log.error(e, e);
            resultList = new ArrayList(); // return empty list
    	}
    	return resultList;
    }

    /**
     * Imports a key name to db. 
     *
     * @param key <code>DConfigKey</code>
     * @return number of rows affected.
     */
    public int importToDb(DConfigKey key) {
        int iRet = 0;

        String keyName = key.getKeyName();
        jt = new JdbcTemplate(getDataSource());

        UpdateTemplate ut = new UpdateTemplate(getDataSource());
        ut.setSql(CREATE_KEY);
        ut.declareParameter(new SqlParameter("keyName", Types.VARCHAR));
        ut.declareParameter(new SqlParameter("inherited", Types.VARCHAR));
        ut.declareParameter(new SqlParameter("dataCreated", Types.TIMESTAMP));
        ut.declareParameter(new SqlParameter("dataModified", Types.TIMESTAMP));
        ut.compile();

        String[] currKeys = StringUtils.split(keyName, Constants.KEY_SEPARATOR);
        // create ancestor keys
        for (int j = 0; j < currKeys.length; j++) {
            String currentKeyname = KeyUtil.toKey(currKeys, j);
            if (DConfigReader.isKeyExist(currentKeyname))
                continue;
            iRet = ut.update(new Object[] { currentKeyname, key.getInherited(),
                                            Calendar.getInstance().getTime(), 
                                            Calendar.getInstance().getTime() });
            DConfigKey keyItem = new DConfigKey();
            keyItem.setKeyName(currentKeyname);
            keyItem.setInherited(key.getInherited());
            int id = jt.queryForInt(QUERY_FOR_ID, new Object[] {keyItem.getKeyName()});
            keyItem.setId(Integer.valueOf(id));
            keyItem.clearChangeFlag();
            // update caches
            DConfigReader.getKeyId_KeyMap().put(Integer.valueOf(id), keyItem);
            DConfigReader.getKeyName_KeyMap().put(currentKeyname, keyItem);
        }
        // update key id
        DConfigKey keyItem = (DConfigKey) DConfigReader.getKeyName_KeyMap().get(key.getKeyName());
        key.setId(keyItem.getId());
    	return iRet;
    }

    public static void main(String[] argv) {
        // create sample data
        DConfigKeyDao dao = new DConfigKeyDao();

        int iRet = dao.create("config.system", "N");
        iRet += dao.create("config.cache", "N");
        iRet += dao.create("config.view", "N");
        iRet += dao.create("config.datasource.epm", "N");
        iRet += dao.create("config.datasource.mysql", "N");
        iRet += dao.create("config.datasource.ems", "N");
        iRet += dao.create("config.datasource.ems", "N");
        //iRet = dao.update(3, "config.view.ui.regEditor");

        List list = dao.keyStartWith("config.datasource");
        System.out.println(list);
    }
}
