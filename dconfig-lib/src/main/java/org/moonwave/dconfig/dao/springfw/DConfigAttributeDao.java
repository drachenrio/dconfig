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
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.DConfigReader;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.DateUtil;
import org.moonwave.dconfig.util.TableConst;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * 
 * @author Jonathan Luo
 */
public class DConfigAttributeDao {
    private static final Log log = LogFactory.getLog(DConfigAttributeDao.class);
	
    private static String CREATE_ATTRIBUTE = "insert into " + TableConst.DConfigAttribute.tableName + " (key_id, " +
        "attribute_name, data_type_alias, attribute_value, comments, date_created, date_modified) values (?, ?, ?, ?, ?, ?, ?)";

    private static String UPDATE_ATTRIBUTE = "update " + TableConst.DConfigAttribute.tableName + " set key_id = ?, " + 
        "attribute_name = ?, data_type_alias = ?, attribute_value = ?, comments = ?, date_modified = ? where id = ?";

    private static String DELETE_ATTRIBUTE = "delete from " + TableConst.DConfigAttribute.tableName + " where id = ?";
    private static String DELETE_ATTRIBUTES_BY_KEYID = "delete from " + TableConst.DConfigAttribute.tableName + " where key_id = ?";

    private static String QUERY_ALL_ATTRIBUTES = "select  id, key_id, attribute_name, data_type_alias, " +
        "attribute_value, comments from " + TableConst.DConfigAttribute.tableName + " order by key_id, attribute_name";

    private static String QUERY_ATTRIBUTE = "select da.id, da.key_id, da.attribute_name, " +
        "da.data_type_alias, da.attribute_value, da.comments from " + TableConst.DConfigAttribute.tableName + " da " + 
        "join dconfig_key dk on dk.id = da.key_id where dk.key_name = ? and da.attribute_name = ?";

    private static String QUERY_ATTRIBUTE_SATRT_WITH = "select da.id, da.key_id, da.attribute_name, " +
        "da.data_type_alias, da.attribute_value, da.comments from " + TableConst.DConfigAttribute.tableName + " da " + 
        "join dconfig_key dk on dk.id = da.key_id where dk.key_name = ? and da.attribute_name like ?";
    
    private static String QUERY_FOR_ID = "select id from " + TableConst.DConfigAttribute.tableName + " where " +
        "key_id = ? and attribute_name = ?";

    private static String QUERY_BY_ID = "select id, key_id, attribute_name, " +
        "data_type_alias, attribute_value, comments from " + TableConst.DConfigAttribute.tableName + " where " +
        "id = ?";
    
    private static String QUERY_BY_KEYID = "select id, key_id, attribute_name, " +
        "data_type_alias, attribute_value, comments from " + TableConst.DConfigAttribute.tableName + " where " +
        "key_id = ?";

    private DataSource dataSource;
    
    private JdbcTemplate jt;

    // ---------------------------------------------------------- Public Methods
    public DConfigAttributeDao() {
    }
    
    public DConfigAttributeDao(DataSource ds) {
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

    public List getAllAtributes() {
    	List retList = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setSql(QUERY_ALL_ATTRIBUTES);
            query.compile();
            query.setRowMapper(new DConfigAttributeMapper());
            retList = query.execute();
    	} catch (Exception e) {
            log.error(e, e);
            retList = Constants.EMPTY_ARRAYLIST;
    	}
    	return retList;
    }

    /**
     * Gets attribute by key name, attribute name.
     * Note: returned results do not include inherited attributes
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * 
     *
     * @return <code>DConfigAttribute</code> object if attribute is found;
     *         or null otherwise.
     */
    public DConfigAttribute getAttribute(String keyName, String attributeName) {
    	DConfigAttribute attribute = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setRowMapper(new DConfigAttributeMapper());
            query.setSql(QUERY_ATTRIBUTE);
            query.declareParameter(new SqlParameter("key_name", Types.VARCHAR));
            query.declareParameter(new SqlParameter("attribute_name", Types.VARCHAR));
            query.compile();
            List resultList = query.execute(new Object[] {keyName, attributeName});
            if (resultList.size() > 0)
                attribute = (DConfigAttribute) resultList.get(0);
    	} catch (Exception e) {
            log.error(e, e);
    	}
    	return attribute;
    }

    /**
     * Gets attribute by key name, attribute name.
     * The returned results do not include inherited attributes.
     *
     * @param keyName key name.
     * @param attributeNameStartWith attribute name.
     *
     * @return <code>DConfigAttribute</code> object if attribute is found;
     *         or null otherwise.
     */
    public List getByKeyNameAttributeNameStartWith(String keyName, String attributeNameStartWith) {
        List resultList = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setRowMapper(new DConfigAttributeMapper());
            query.setSql(QUERY_ATTRIBUTE_SATRT_WITH);
            query.declareParameter(new SqlParameter("key_name", Types.VARCHAR));
            query.declareParameter(new SqlParameter("attribute_name", Types.VARCHAR));
            query.compile();
            resultList = query.execute(new Object[] {keyName, attributeNameStartWith + "%"});
    	} catch (Exception e) {
            log.error(e, e);
    	}
    	return resultList;
    }

    /**
     * Gets a list of attributes by key id.
     *
     * @param keyId key id.
     * @return lits of <code>DConfigAttribute</code> objects if found; empty list
     *         otherwise.
     */
    public List getByKeyId(Integer keyId) {
    	List list = Constants.EMPTY_ARRAYLIST;
        if (keyId.intValue() != -1) {
            try {
                QueryTemplate ut = new QueryTemplate(getDataSource());
                ut.setRowMapper(new DConfigAttributeMapper());
                ut.setSql(QUERY_BY_KEYID);
                ut.declareParameter(new SqlParameter("keyId", Types.BIGINT));
                ut.compile();
                list = ut.execute(new Object[] { keyId });
            } catch (Exception e) {
                log.error(e, e);
            }
        }
    	return list;
    }
    
    /**
     * Gets a list of attributes by key name.
     *
     * @param keyName key name.
     * @return lits of <code>DConfigAttribute</code> objects if attributes exist;
     *         or an empty list if no attributes.
     */
    public List getByKeyName(String keyName) {
    	DConfigKeyDao dao = new DConfigKeyDao();
    	return getByKeyId(Integer.valueOf(dao.getIdForKeyname(keyName)));
    }

    /**
     * Gets a list of attributes by <code>DConfigKey</code> argument.
     *
     * @param key the <code>DConfigKey</code> to search attributes for.
     * @return lits of <code>DConfigAttribute</code> objects if found; empty list
     *         otherwise.
     */
    public List getAttributes(DConfigKey key) {
    	return (key != null) ? getByKeyId(key.getId()) : Constants.EMPTY_ARRAYLIST;
    }

    /**
     * Saves a list of attributes to database. Starts its own transaction.
     *
     * @param cfgObj the current <code>DConfig</code> object.
     * @param attributes list of attributes
     * @return true on success; false otherwise.
     */
    public boolean save(DConfig cfgObj, List attributes) {
    	if ((attributes == null) || attributes.size() == 0)
            return true;
    	boolean bRet = false;
        Connection conn = null;
    	try {
            conn = DataSourceUtils.getConnection(getDataSource());
            conn.setAutoCommit(false);
            ((SmartDataSourceEx) dataSource).setConnectionClose(false);            

            for (int i = 0; i < attributes.size(); i++) {
                DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
                if (attribute.isDelete() && attribute.isNew()) { // ignore, just created and deleted again
                    continue;
                }
                else if (attribute.isNew())
                    create(attribute);
                else if (attribute.isDelete()) {
                    if (!attribute.isInherited())
                        delete(attribute);
                }
                else if (attribute.hasChanged()) {
                    if (attribute.isInherited()) {
                        attribute.setNew();
                        attribute.setKeyId(cfgObj.getKey().getId());
                        attribute.setInherited(false);
                        create(attribute);
                    }
                    else
                        update(attribute);
                }
            }
            conn.commit();
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
            // removed attributes marked as delete
            for (int i = attributes.size() - 1; i >= 0; i--) {
                DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
                if (attribute.isDelete())
                    attributes.remove(i);
            }
            for (int i = 0; i < attributes.size(); i++) {
                DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
                attribute.clearChangeFlag();
            }
            new DConfigSystemDao().setNewDataChangeTimeStamp();
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
     * Saves a list of attributes to database. Transaction started by caller.
     *
     * @param cfgObj the current <code>DConfig</code> object.
     * @param attributes list of attributes
     * @return true on success; false otherwise.
     */
    public boolean saveToDb(DConfig cfgObj, List attributes) {
    	if ((attributes == null) || attributes.size() == 0)
            return true;
    	boolean bRet = false;
        for (int i = 0; i < attributes.size(); i++) {
            DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
            if (attribute.isNew())
                create(attribute);
            else if (attribute.isDelete()) {
                if (!attribute.isInherited())
                    delete(attribute);
            }
            else if (attribute.hasChanged()) {
                if (attribute.isInherited()) {
                    attribute.setNew();
                    attribute.setKeyId(cfgObj.getKey().getId());
                    attribute.setInherited(false);
                    create(attribute);
                }
                else
                    update(attribute);
            }
        }
        // clear change flags
        for (int i = 0; i < attributes.size(); i++) {
            DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
            attribute.clearChangeFlag();//TODO - move clear flags out to transaction code
        }
        new DConfigSystemDao().setNewDataChangeTimeStamp();
        bRet = true;
    	return bRet;
    }
    
    /**
     * Saves a list of attributes to database. Transaction started by caller.
     */
    public boolean importToDb(List attributes) {
    	if ((attributes == null) || attributes.size() == 0)
            return true;
    	boolean bRet = false;
        Map map = DConfigReader.getKeyIdAttributeName_AttributeMap();
        for (int i = 0; i < attributes.size(); i++) {
            DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
            String lookupKey = attribute.getKeyId() + "-" + attribute.getAttributeName();
            DConfigAttribute item = (DConfigAttribute) map.get(lookupKey);
            if (item == null) {
                create(attribute);
            } else {
                attribute.setId(item.getId());
                if (!attribute.equals(item))
                    update(attribute);
            }
            map.put(lookupKey, attribute);
        }
        // clear change flags
        for (int i = 0; i < attributes.size(); i++) {
            DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
            attribute.clearChangeFlag();
        }
        bRet = true; //?
    	return bRet;
    }
    
    /**
     * Gets an individual attribute by id.
     *
     * @param id <code>DConfigAttribute</code> id.
     * @return <code>DConfigAttribute</code> if found; null otherwise.
     */
    public DConfigAttribute getById(Integer id) {
    	DConfigAttribute attribute = null;
    	try {
            QueryTemplate query = new QueryTemplate(getDataSource());
            query.setRowMapper(new DConfigAttributeMapper());
            query.setSql(QUERY_BY_ID);
            query.declareParameter(new SqlParameter("id", Types.INTEGER));
            query.compile();
            List resultList = query.execute(new Object[] {id});
            if (resultList.size() > 0)
                attribute = (DConfigAttribute) resultList.get(0);
    	} catch (Exception e) {
            log.error(e, e);
    	}
    	return attribute;
    }
    
    /**
     * Creates a new attribute. Transaction started by caller.
     *
     * @param attribute <code>DConfigAttribute</code> to be created.
     * @return number of rows updated
     */
    public int create(DConfigAttribute attribute) {
    	int iRet = 0;
        UpdateTemplate ut = new UpdateTemplate(getDataSource());
        ut.setSql(CREATE_ATTRIBUTE);
        ut.declareParameter(new SqlParameter("keyId", Types.BIGINT));
        ut.declareParameter(new SqlParameter("attributeName", Types.VARCHAR));
        ut.declareParameter(new SqlParameter("dataTypeAlias", Types.VARCHAR));
        ut.declareParameter(new SqlParameter("attributeValue", Types.VARCHAR));
        ut.declareParameter(new SqlParameter("comment", Types.VARCHAR));
        ut.declareParameter(new SqlParameter("dataCreated", Types.TIMESTAMP));
        ut.declareParameter(new SqlParameter("dataModified", Types.TIMESTAMP));
        ut.compile();
        String attributeValue = attribute.getAttributeValue();
        if (attribute.getDataTypeAlias().equals(DConfigDataType.aliasDatetime)) {
            attributeValue = DateUtil.toStoredFormat(attributeValue);
        } else if (attribute.getDataTypeAlias().equals(DConfigDataType.aliasDatetimeArray)) {
            String[] dataAr = StringUtils.split(attributeValue, Constants.DELIMITER);
            if (dataAr != null) {
                StringBuffer sb = new StringBuffer(300);
                for (int i = 0; i < dataAr.length; i++) {
                    if (i > 0)
                        sb.append(Constants.DELIMITER);
                    sb.append(DateUtil.toStoredFormat(dataAr[i]));
                }
                attributeValue = sb.toString();
            }
        }
        iRet = ut.update(new Object[] {attribute.getKeyId(), 
                        attribute.getAttributeName(), attribute.getDataTypeAlias(),
                        attributeValue, attribute.getComments(),
                        Calendar.getInstance().getTime(),
                        Calendar.getInstance().getTime()});

        jt = new JdbcTemplate(getDataSource());
        int id = jt.queryForInt(QUERY_FOR_ID, new Object[] {
                        attribute.getKeyId(), attribute.getAttributeName()});
        attribute.setId(Integer.valueOf(id));
        new DConfigSystemDao().setNewDataChangeTimeStamp();
    	return iRet;
    }

    /**
     * Updates a specified attribute. Transaction started by caller.
     *
     */
    public int update(DConfigAttribute attribute) {    	
    	int iRet = 0;
        if ((attribute != null) && (attribute.getId() != null)) {
            UpdateTemplate ut = new UpdateTemplate(getDataSource());
            ut.setSql(UPDATE_ATTRIBUTE);
            ut.declareParameter(new SqlParameter("keyId", Types.BIGINT));
            ut.declareParameter(new SqlParameter("attributeName", Types.VARCHAR));
            ut.declareParameter(new SqlParameter("dataTypeAlias", Types.VARCHAR));
            ut.declareParameter(new SqlParameter("attributeValue", Types.VARCHAR));
            ut.declareParameter(new SqlParameter("comments", Types.VARCHAR));
            ut.declareParameter(new SqlParameter("dateModified", Types.TIMESTAMP));
            ut.declareParameter(new SqlParameter("id", Types.BIGINT));
            ut.compile();
            String attributeValue = attribute.getAttributeValue();
            if (attribute.getDataTypeAlias().equals(DConfigDataType.aliasDatetime)) {
                attributeValue = DateUtil.toStoredFormat(attributeValue);
            } else if (attribute.getDataTypeAlias().equals(DConfigDataType.aliasDatetimeArray)) {
                String[] dataAr = StringUtils.split(attributeValue, Constants.DELIMITER);
                if (dataAr != null) {
                    StringBuffer sb = new StringBuffer(300);
                    for (int i = 0; i < dataAr.length; i++) {
                        if (i > 0)
                            sb.append(Constants.DELIMITER);
                        sb.append(DateUtil.toStoredFormat(dataAr[i]));
                    }
                    attributeValue = sb.toString();
                }
            }
            iRet = ut.update(new Object[] {attribute.getKeyId(), 
                            attribute.getAttributeName(), attribute.getDataTypeAlias(),
                            attributeValue, attribute.getComments(),
                            Calendar.getInstance().getTime(),                
                            attribute.getId()});
        }
        new DConfigSystemDao().setNewDataChangeTimeStamp();
    	return iRet;
    }

    /**
     * Deletes a specified attribute. Transaction started by caller.
     *
     */
    public int delete(DConfigAttribute attribute) {
    	int iRet = 0;
        jt = new JdbcTemplate(getDataSource());
        iRet = jt.update(DELETE_ATTRIBUTE, new Object[] {attribute.getId()});
        new DConfigSystemDao().setNewDataChangeTimeStamp();
    	return iRet;
    }    
    
    /**
     * Deletes all attributes for a given dconfig key. Transaction started by caller.
     * 
     * @param key a <code>DConfigKey</code> object.
     * @return number fo rows affected.
     */
    public int deleteAttributes(DConfigKey key) {
    	int iRet = 0;
        jt = new JdbcTemplate(getDataSource());
        Object[] params = new Object[] { key.getId() };
        iRet = jt.update(DELETE_ATTRIBUTES_BY_KEYID, params);
        new DConfigSystemDao().setNewDataChangeTimeStamp();
    	return iRet;
    }
        
    public static void main(String[] argv) {
        DConfigAttributeDao dao = new DConfigAttributeDao();
        //createSmapleData(dao);
        DConfigAttribute attrib = new DConfigAttribute();
        //attrib.setKeyName("system.config.datasource.postgres");
        dao.create(attrib);
        System.out.println(attrib);
    }
}
