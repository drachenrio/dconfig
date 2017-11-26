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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.TableConst;

/**
 * 
 * @author Jonathan Luo
 */
public class DConfigDataTypeDao {
    private static final Log log = LogFactory.getLog(DConfigKeyDao.class);

    private static String QUERY_ALL = "select alias, data_type_name from " + TableConst.DConfigDataType.tableName + " where alias != 'bin'";

    private static List dataTypeList;
    
    // key - (String) data type alias; value - DConfigDataType
    private static Map alias_dataTypeMap = new HashMap();

    // key - (String) data type name;  value - DConfigDataType
    private static Map name_dataTypeMap = new HashMap();

    private DataSource dataSource;

    // --------------------------------------------------- Public Static Methods
    public static void load() {
    	if (dataTypeList != null)
            dataTypeList.clear();
    	alias_dataTypeMap.clear();
    	name_dataTypeMap.clear();

        DConfigDataTypeDao dao = new DConfigDataTypeDao();
    	dataTypeList = dao.getAllDataTypes();
        for (int i = 0; i < dataTypeList.size(); i++) {
            DConfigDataType dataType = (DConfigDataType) dataTypeList.get(i);
            alias_dataTypeMap.put(dataType.getAlias(), dataType);
            name_dataTypeMap.put(dataType.getDataTypeName().toLowerCase(), dataType);
        }
        if (dataTypeList.size() == 0) {
            log.error("No data type values are found.");
        }
    }

    public static DConfigDataType getByAlias(String alias) {
    	return (alias != null) ? (DConfigDataType) alias_dataTypeMap.get(alias.toLowerCase()) : null;
    }

    public static String getDataTypeNameByAlias(String alias) {
        String sRet = null;
        if (alias != null) {
            DConfigDataType dataType = (DConfigDataType) alias_dataTypeMap.get(alias.toLowerCase());
            if (dataType != null)
                sRet = dataType.getDataTypeName();            
        }
        return sRet;
    }

    public static String getAliasByDataTypeName(String dataTypeName) {
        String sRet = null;
        if (dataTypeName != null) {
            DConfigDataType dataType = (DConfigDataType) name_dataTypeMap.get(dataTypeName.toLowerCase());
            if (dataType != null)
                sRet = dataType.getAlias();            
        }
        return sRet;
    }

    public static List getDataTypeList() {
    	return dataTypeList;
    }

    // ---------------------------------------------------------- Public Methods    
    public DConfigDataTypeDao() {
    }
    public DConfigDataTypeDao(DataSource ds) {
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
    
    public List getAllDataTypes() {
    	List retList = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_ALL);
            query.compile();
            query.setRowMapper(new DConfigDataTypeMapper());
            retList = query.execute();
    	} catch (Exception e) {
            log.error(e, e);
            retList = Constants.EMPTY_ARRAYLIST;
    	}
    	return retList;
    }

    public static void main(String[] argv) {
        DConfigDataTypeDao dao = new DConfigDataTypeDao();
        dataTypeList = dao.getAllDataTypes();
        System.out.println(dataTypeList);
    }
}
