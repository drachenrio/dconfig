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
import java.util.List;

import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;

/**
 * 
 * @author Jonathan Luo
 */
public class DConfigDao {
    
    private static final Log log = LogFactory.getLog(DConfigDao.class);

    private DataSource dataSource;
    
    private JdbcTemplate jt;
    
    // ---------------------------------------------------------- Public Methods
    public DConfigDao() {
    }
    
    public DConfigDao(DataSource ds) {
    	this.dataSource = ds;
    }
    
    public DataSource getDataSource() {
    	if (dataSource == null)
            dataSource = DataSourceManager.getDataSource();
        return dataSource;
    }
    
    public void setDataSource(DataSource ds) {
    	dataSource = ds;
    }
    
    /**
     * Commit changes to database. Starts its own transaction.
     * @return true if changes committed successfully; false otherwise.  
     */
    public boolean save(DConfig cfgObj) {
    	boolean bRet = false;    	
        Connection conn = null;
                
    	try {
            conn = DataSourceUtils.getConnection(getDataSource());
            conn.setAutoCommit(false);
            ((SmartDataSourceEx) dataSource).setConnectionClose(false);
            
            DConfigKeyDao keyDao = new DConfigKeyDao(getDataSource());
            DConfigAttributeDao attributeDao = new DConfigAttributeDao(getDataSource());
            if (cfgObj.isNew()) {
                keyDao.create(cfgObj.getKey());
                //update keyid
                List attributes = cfgObj.getAttributes();
                if (attributes != null) {
                    for (int i = 0; i < attributes.size(); i++) {   
                        DConfigAttribute attribute = (DConfigAttribute)attributes.get(i);
                        attribute.setKeyId(cfgObj.getKey().getId());
                    }
                }
                bRet = attributeDao.saveToDb(cfgObj, cfgObj.getAttributes());
            }
            else if (cfgObj.isDelete()) {
                // delete all attributes first
                attributeDao.deleteAttributes(cfgObj.getKey());
                keyDao.delete(cfgObj.getKey());
            }
            else { // update mode
                bRet = attributeDao.saveToDb(cfgObj, cfgObj.getAttributes());
                if (cfgObj.getKey().hasChanged())
                    keyDao.update(cfgObj.getKey());
            }

            conn.commit();
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
            new DConfigSystemDao().setNewDataChangeTimeStamp();
            bRet = true;
    	} catch (Exception e) {
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
    
    /**
     * Commit changes to database. Starts its own transaction.
     * @param dcfgList list of <code>DConfig</code> object.
     * @return true if changes committed successfully; false otherwise.  
     */
    public boolean save(List dcfgList) {
    	boolean bRet = false;    	
        Connection conn = null;
                
    	try {
            conn = DataSourceUtils.getConnection(getDataSource());
            conn.setAutoCommit(false);
            ((SmartDataSourceEx) dataSource).setConnectionClose(false);
            
            DConfigKeyDao keyDao = new DConfigKeyDao(getDataSource());
            DConfigAttributeDao attributeDao = new DConfigAttributeDao(getDataSource());
            DConfig cfgObj = null;
            for (int i = 0; i < dcfgList.size(); i++) {
                cfgObj = (DConfig) dcfgList.get(i);
                if (cfgObj.isNew()) {
                    keyDao.create(cfgObj.getKey());
                    //update keyid
                    List attributes = cfgObj.getAttributes();
                    if (attributes != null) {
                        for (int attr = 0; attr < attributes.size(); attr++) {
                            DConfigAttribute attribute = (DConfigAttribute)attributes.get(attr);
                            attribute.setKeyId(cfgObj.getKey().getId());
                        }
                    }
                    bRet = attributeDao.saveToDb(cfgObj, cfgObj.getAttributes());
                }
                else if (cfgObj.isDelete()) {
                    // delete all attributes first
                    attributeDao.deleteAttributes(cfgObj.getKey());
                    keyDao.delete(cfgObj.getKey());
                }
                else { // update mode
                    bRet = attributeDao.saveToDb(cfgObj, cfgObj.getAttributes());
                    if (cfgObj.getKey().hasChanged())
                        keyDao.update(cfgObj.getKey());
                }
            }
            conn.commit();//clear flags after commmit
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
            new DConfigSystemDao().setNewDataChangeTimeStamp();
            bRet = true;
    	} catch (Exception e) {
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

    /**
     * Commit changes to database.
     * @return true if changes committed successfully; false otherwise.  
     */
    public boolean importToDb(DConfig cfgObj) {
        boolean bRet = false;
        DConfigKeyDao keyDao = new DConfigKeyDao(getDataSource());
        DConfigAttributeDao attributeDao = new DConfigAttributeDao(getDataSource());
        keyDao.importToDb(cfgObj.getKey());
        //update keyid
        List attributes = cfgObj.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.size(); i++) {   
                DConfigAttribute attribute = (DConfigAttribute)attributes.get(i);
                attribute.setKeyId(cfgObj.getKey().getId());
            }
        }
        bRet = attributeDao.importToDb(cfgObj.getAttributes());
        return bRet;
    }
}
