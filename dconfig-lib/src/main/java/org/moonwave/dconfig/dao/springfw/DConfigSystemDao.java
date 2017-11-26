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

import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.moonwave.dconfig.model.DConfigSystem;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.DataConverter;
import org.moonwave.dconfig.util.DateUtil;
import org.moonwave.dconfig.util.TableConst;
import org.springframework.jdbc.core.SqlParameter;

/**
 * 
 * @author Jonathan Luo
 */
public class DConfigSystemDao {
    private static final Log log = LogFactory.getLog(DConfigSystemDao.class);
    
    private static String QUERY_ALL = "select system_name, data_type_alias, system_value, comments from " + TableConst.DConfigSystem.tableName + " order by system_name";
    private static String QUERY_DT_TS = "select system_name, data_type_alias, system_value, comments from " + TableConst.DConfigSystem.tableName + " where system_name = 'db.datachange.timestamp'";
    private static String UPDATE_DT_TS = "update " + TableConst.DConfigSystem.tableName + " set system_value = ? where system_name = 'db.datachange.timestamp'";

    private static List dataTypeList;
    
    // key - (String) attribute name, value - <code>DConfigSystem</code> object.
    private static Map attributeName_systemData_Map = new HashMap();

    private DataSource dataSource;

    // --------------------------------------------------- Public Static Methods
    public static void load() {
    	if (dataTypeList != null)
            dataTypeList.clear();
    	attributeName_systemData_Map.clear();

        DConfigSystemDao dao = new DConfigSystemDao();
    	dataTypeList = dao.getSystemData();
        for (int i = 0; i < dataTypeList.size(); i++) {
            DConfigSystem dataType = (DConfigSystem) dataTypeList.get(i);
            attributeName_systemData_Map.put(dataType.getSystemName().toLowerCase(), dataType);
        }
    }

    public static DConfigSystem getByAttributeName(String attributeName) {
    	return (attributeName != null) ? (DConfigSystem) attributeName_systemData_Map.get(attributeName.toLowerCase()) : null;
    }

    public static List getDataTypeList() {
    	return dataTypeList;
    }

    // ---------------------------------------------------------- Public Methods    
    public DConfigSystemDao() {
    }
    
    public DConfigSystemDao(DataSource ds) {
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
     * Gets last data change time stamp.
     *
     */
    public Date getLastDataChangeTimeStamp() {
    	Date retObj = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_DT_TS);
            query.compile();
            query.setRowMapper(new DConfigSystemMapper());
            List list = query.execute();
            if (list.size() > 0) {
                DConfigSystem system = (DConfigSystem) list.get(0);
                retObj = DataConverter.toDate(system.getSystemValue());
            }
    	} catch (Exception e) {
            log.error(e, e);
    	}
    	return retObj;
    }

    /**
     * Sets new data change time stamp. Called this method whenever insert, update,
     * or delete actions are performed against dconfig_key, dconfig_attribute tables.
     */
    public int setNewDataChangeTimeStamp() {
        int iRet = 0;
        try {
            UpdateTemplate ut = new UpdateTemplate(getDataSource());
            ut.setSql(UPDATE_DT_TS);
            ut.declareParameter(new SqlParameter("systemValue", Types.VARCHAR));
            ut.compile();

            iRet = ut.update(new Object[] { DateUtil.toStoredFormat(Calendar.getInstance().getTime()) });
    	} catch (Exception e) {
            log.error(e, e);
    	}
    	return iRet;
    }

    /**
     * Gets all system data from database.
     */
    public List getSystemData() {
    	List retList = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_DT_TS);
            query.compile();
            query.setRowMapper(new DConfigSystemMapper());
            retList = query.execute();
    	} catch (Exception e) {
            log.error(e, e);
            retList = Constants.EMPTY_ARRAYLIST;
    	}
    	return retList;
    }

    public static void main(String[] argv) {
        DConfigSystemDao dao = new DConfigSystemDao();
        Date date = dao.getLastDataChangeTimeStamp();
        System.out.println(date);

        new DConfigSystemDao().setNewDataChangeTimeStamp();
        System.out.println(new DConfigSystemDao().getLastDataChangeTimeStamp());        
    }
}
