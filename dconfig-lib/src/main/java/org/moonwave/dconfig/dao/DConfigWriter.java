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

package org.moonwave.dconfig.dao;

import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.springfw.DConfigAttributeDao;
import org.moonwave.dconfig.dao.springfw.DConfigDao;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.DateUtil;

/**
 *
 * @author Jonathan Luo
 */
public class DConfigWriter {
    
    private static final Log log = LogFactory.getLog(DConfigWriter.class);

    private String keyName;
    private String attributeName;
    private String dataTypeAlias;
    private String attributeValue;
    private String comments;
    
    public DConfigWriter() {
    }

    public DConfigWriter(String keyName, String attributeName, String dataTypeAlias, String attributeValue, String comments) {
        this.keyName = keyName;
        this.attributeName = attributeName;
        this.dataTypeAlias = dataTypeAlias;
        this.attributeValue = attributeValue;
        this.comments = comments;
    }
    
    /**
     * Get key name.
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * Set key name.
     * @param keyName the key name to set.
     */
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    /**
     * Get attribute name.
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Set attribute name.
     * @param attributeName the attribute name to set.
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    /**
     * Get data type alias.
     */
    public String getDataTypeAlias() {
        return dataTypeAlias;
    }

    /**
     * Set data type alias.
     * @param dataTypeAlias the data type alias to set.
     */
    public void setDataTypeAlias(String dataTypeAlias) {
        this.dataTypeAlias = dataTypeAlias;
    }

    /**
     * Get attribute value.
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * Set attribute value.
     * @param attributeValue the attribute value to set.
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     * Get comments.
     */
    public String getComments() {
        return comments;
    }

    /**
     * Set comments.
     * @param comments the comments to set.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    /**
     * Save <code>DConfig</code> data into database.
     * @return true on success; false otherwise.
     */
    public boolean save() {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            attribute.setAttributeValue(attributeValue);
            attribute.setDataTypeAlias(dataTypeAlias);
            attribute.setComments(comments);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }
    
    /**
     * Writes boolean attribute value to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeBoolean(String keyName, boolean val) {
        return writeBoolean(keyName, Constants.PRIMARY_ATTRIBUTE, val);
    }

    /**
     * Writes boolean attribute value to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeBoolean(String keyName, Boolean val) {
        return writeBoolean(keyName, Constants.PRIMARY_ATTRIBUTE, val);
    }

    /**
     * Writes boolean attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeBoolean(String keyName, String attributeName, boolean val) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            attribute.setAttributeValue(val ? "true" : "false");
            attribute.setDataTypeAlias(DConfigDataType.aliasBoolean);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes boolean attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeBoolean(String keyName, String attributeName, Boolean val) {
        return writeBoolean(keyName, attributeName, val.booleanValue());
    }

    /**
     * Writes boolean attribute value array to "(primary)" attribute
     * for a specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeBooleanArray(String keyName, boolean[] valArray) {
        return writeBooleanArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }
    
    /**
     * Writes boolean attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeBooleanArray(String keyName, String attributeName, boolean[] valArray) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < valArray.length; i++) {
                if (i > 0)
                    sb.append(Constants.DELIMITER);
                sb.append(valArray[i] ? "true" : "false");
            }
            attribute.setAttributeValue(sb.toString());
            attribute.setDataTypeAlias(DConfigDataType.aliasBooleanArray);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes <code>Boolean</code> attribute value array to "(primary)" attribute
     * for a specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeBooleanArray(String keyName, Boolean[] valArray) {
        return writeBooleanArray(keyName, Constants.PRIMARY_ATTRIBUTE, to_booleanArray(valArray));
    }

    /**
     * Writes boolean attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeBooleanArray(String keyName, String attributeName, Boolean[] valArray) {
        return writeBooleanArray(keyName, attributeName, to_booleanArray(valArray));
    }

    /**
     * Writes integer attribute value to "(primary)" attribute for a 
     * specified key name.
     * 
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeInteger(String keyName, int val) {
        return writeInteger(keyName, Constants.PRIMARY_ATTRIBUTE, val);
    }

    /**
     * Writes integer attribute value to "(primary)" attribute for a 
     * specified key name.
     * 
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeInteger(String keyName, Integer val) {
        return writeInteger(keyName, Constants.PRIMARY_ATTRIBUTE, val.intValue());
    }

    /**
     * Writes integer attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeInteger(String keyName, String attributeName, int val) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            attribute.setAttributeValue(String.valueOf(val));
            attribute.setDataTypeAlias(DConfigDataType.aliasInteger);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes integer attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeInteger(String keyName, String attributeName, Integer val) {
        return writeInteger(keyName, attributeName, val.intValue());
    }

    /**
     * Writes integer attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeIntegerArray(String keyName, int[] valArray) {
         return writeIntegerArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }

    /**
     * Writes integer attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeIntegerArray(String keyName, String attributeName, int[] valArray) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < valArray.length; i++) {
                if (i > 0)
                    sb.append(Constants.DELIMITER);
                sb.append(String.valueOf(valArray[i]));
            }
            attribute.setAttributeValue(sb.toString());
            attribute.setDataTypeAlias(DConfigDataType.aliasIntegerArray);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes integer attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeIntegerArray(String keyName, Integer[] valArray) {
         return writeIntegerArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }

    /**
     * Writes integer attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeIntegerArray(String keyName, String attributeName, Integer[] valArray) {
        return writeIntegerArray(keyName, attributeName, to_intArray(valArray));
    }

    /**
     * Writes long attribute value to "(primary)" attribute for a 
     * specified key name.
     * 
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.w
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeLong(String keyName, long val) {
        return writeLong(keyName, Constants.PRIMARY_ATTRIBUTE, val);
    }
    
    /**
     * Writes long attribute value to "(primary)" attribute for a 
     * specified key name.
     * 
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.w
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeLong(String keyName, Long val) {
        return writeLong(keyName, Constants.PRIMARY_ATTRIBUTE, val.longValue());
    }

    /**
     * Writes long attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeLong(String keyName, String attributeName, long val) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            attribute.setAttributeValue(String.valueOf(val));
            attribute.setDataTypeAlias(DConfigDataType.aliasLong);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes long attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeLong(String keyName, String attributeName, Long val) {
        return writeLong(keyName, attributeName, val.longValue());
    }

    /**
     * Writes long attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeLongArray(String keyName, long[] valArray) {
         return writeLongArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }

    /**
     * Writes long attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeLongArray(String keyName, String attributeName, long[] valArray) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < valArray.length; i++) {
                if (i > 0)
                    sb.append(Constants.DELIMITER);
                sb.append(String.valueOf(valArray[i]));
            }
            attribute.setAttributeValue(sb.toString());
            attribute.setDataTypeAlias(DConfigDataType.aliasLongArray);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }
    
    /**
     * Writes <code>Long</code> attribute value array to "(primary)" attribute 
     * for a specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeLongArray(String keyName, Long[] valArray) {
         return writeLongArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }

    /**
     * Writes <code>Long</code> attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeLongArray(String keyName, String attributeName, Long[] valArray) {
        return writeLongArray(keyName, attributeName, to_longArray(valArray));
    }
    
    /**
     * Writes <code>String</code> attribute value to "(primary)" attribute for a 
     * specified key name.
     * 
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeValue value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeString(String keyName, String attributeValue) {
        return writeString(keyName, Constants.PRIMARY_ATTRIBUTE, attributeValue);
    }

    /**
     * Writes String attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param attributeValue value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeString(String keyName, String attributeName, String attributeValue) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            attribute.setAttributeValue(attributeValue);
            attribute.setDataTypeAlias(DConfigDataType.aliasString);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes <code>String</code> attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeStringArray(String keyName, String[] valArray) {
         return writeStringArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }
    
    /**
     * Writes <code>String</code> attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeStringArray(String keyName, String attributeName, String[] valArray) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < valArray.length; i++) {
                if (valArray[i] == null)
                    continue;
                if (i > 0)
                    sb.append(Constants.DELIMITER);
                sb.append(valArray[i]);
            }
            attribute.setAttributeValue(sb.toString());
            attribute.setDataTypeAlias(DConfigDataType.aliasStringArray);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }
    
    /**
     * Writes float attribute value to "(primary)" attribute for a 
     * specified key name.
     * 
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeFloat(String keyName, float val) {
        return writeFloat(keyName, Constants.PRIMARY_ATTRIBUTE, val);
    }

    /**
     * Writes float attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeFloat(String keyName, String attributeName, float val) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            attribute.setAttributeValue(String.valueOf(val));
            attribute.setDataTypeAlias(DConfigDataType.aliasFloat);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes float attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeFloatArray(String keyName, float[] valArray) {
         return writeFloatArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }

    /**
     * Writes float attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeFloatArray(String keyName, String attributeName, float[] valArray) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < valArray.length; i++) {
                if (i > 0)
                    sb.append(Constants.DELIMITER);
                sb.append(String.valueOf(valArray[i]));
            }
            attribute.setAttributeValue(sb.toString());
            attribute.setDataTypeAlias(DConfigDataType.aliasFloatArray);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }
    

    /**
     * Writes <code>Float</code> attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeFloatArray(String keyName, Float[] valArray) {
         return writeFloatArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }

    /**
     * Writes float attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeFloatArray(String keyName, String attributeName, Float[] valArray) {
        return writeFloatArray(keyName, attributeName, to_floatArray(valArray));
    }
    
    /**
     * Writes double attribute value to "(primary)" attribute for a 
     * specified key name.
     * 
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDouble(String keyName, double val) {
        return writeDouble(keyName, Constants.PRIMARY_ATTRIBUTE, val);
    }
    
    /**
     * Writes double attribute value to "(primary)" attribute for a 
     * specified key name.
     * 
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDouble(String keyName, Double val) {
        return writeDouble(keyName, Constants.PRIMARY_ATTRIBUTE, val.doubleValue());
    }

    /**
     * Writes double attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDouble(String keyName, String attributeName, double val) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            attribute.setAttributeValue(String.valueOf(val));
            attribute.setDataTypeAlias(DConfigDataType.aliasDouble);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes <code>Double</code> attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDoubleArray(String keyName, double[] valArray) {
         return writeDoubleArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }    
    
    /**
     * Writes double attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDoubleArray(String keyName, String attributeName, double[] valArray) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < valArray.length; i++) {
                if (i > 0)
                    sb.append(Constants.DELIMITER);
                sb.append(String.valueOf(valArray[i]));
            }
            attribute.setAttributeValue(sb.toString());
            attribute.setDataTypeAlias(DConfigDataType.aliasDoubleArray);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }
    
    /**
     * Writes <code>Double</code> attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDoubleArray(String keyName, Double[] valArray) {
         return writeDoubleArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }

    /**
     * Writes double attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDoubleArray(String keyName, String attributeName, Double[] valArray) {
        return writeDoubleArray(keyName, attributeName, to_doubleArray(valArray));
    }

    /**
     * Writes <code>Date</code> attribute value to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDate(String keyName, java.util.Date val) {
         return writeDate(keyName, Constants.PRIMARY_ATTRIBUTE, val);
    }
    
    /**
     * Writes date attribute value for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param val value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDate(String keyName, String attributeName, java.util.Date val) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            attribute.setAttributeValue(DateUtil.toStoredFormat(val));
            attribute.setDataTypeAlias(DConfigDataType.aliasDatetime);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }

    /**
     * Writes <code>Date</code> attribute value array to "(primary)" attribute for a 
     * specified key name.
     *
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDateArray(String keyName, java.util.Date[] valArray) {
         return writeDateArray(keyName, Constants.PRIMARY_ATTRIBUTE, valArray);
    }

    /**
     * Writes date attribute value array for a specified key name, attribute name.
     * If attribute does not exist, create a new one; if attribute exists, replace
     * the existing attribute value with specified value.
     *
     * @param keyName key name.
     * @param attributeName attribute name.
     * @param valArray array value to save to db.
     *
     * @return true on success; false otherwise.
     */
    public static boolean writeDateArray(String keyName, String attributeName, java.util.Date[] valArray) {
        boolean bRet = false;
        try {
            DConfig cfgObj = new DConfig();
            DConfigKey key = DConfigReader.findKeyByName(keyName);
            cfgObj.setKey((key != null) ? key : new DConfigKey(keyName));
            DConfigAttribute attribute = (new DConfigAttributeDao()).getAttribute(keyName, attributeName);
            if (attribute == null)
                attribute = new DConfigAttribute();
            attribute.setAttributeName(attributeName);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < valArray.length; i++) {
                if (valArray[i] == null)
                    continue;
                if (i > 0)
                    sb.append(Constants.DELIMITER);
                sb.append(DateUtil.toStoredFormat(valArray[i]));
            }
            attribute.setAttributeValue(sb.toString());
            attribute.setDataTypeAlias(DConfigDataType.aliasDatetimeArray);
            cfgObj.addAttribute(attribute);
            bRet = (new DConfigDao()).save(cfgObj);
            // write to local cache
            if (bRet)
                writeToCache(cfgObj);
        } catch (Exception e) {
            log.error(e, e);
        }
        return bRet;
    }
    
    /**
     * Writes <code>DConfig</code> object into local cache.
     *
     * @param cfgObj <code>DConfig</code> object.
     */
    protected static void writeToCache(DConfig cfgObj) {
        // write to local cache
        Map keyId_KeyMap = DConfigReader.getKeyId_KeyMap();
        Map keyName_KeyMap = DConfigReader.getKeyName_KeyMap();
        Map keyIdAttributeName_AttributeMap = DConfigReader.getKeyIdAttributeName_AttributeMap();
        
        DConfigKey dcfgKey = cfgObj.getKey();
        Integer keyId = dcfgKey.getId();
        keyId_KeyMap.put(keyId, dcfgKey);
        keyName_KeyMap.put(dcfgKey.getKeyName(), dcfgKey);

        List attributes = cfgObj.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.size(); i++) {
                DConfigAttribute attribute = (DConfigAttribute)attributes.get(i);
                String lookupKey = keyId + "-" + attribute.getAttributeName();
                keyIdAttributeName_AttributeMap.put(lookupKey, attribute);
            }
        }
    }

    private static boolean[] to_booleanArray(Boolean[] arrayToConvert) {
        boolean[] arrayToReturn = new boolean[getCount(arrayToConvert)];
        int j = 0;
        for (int i = 0; i < arrayToConvert.length; i++) {
            if (arrayToConvert[i] != null)
                arrayToReturn[j++] = arrayToConvert[i].booleanValue();
        }
        return arrayToReturn;
    }

    private static int[] to_intArray(Integer[] arrayToConvert) {
        int[] arrayToReturn = new int[getCount(arrayToConvert)];
        int j = 0;
        for (int i = 0; i < arrayToConvert.length; i++) {
            if (arrayToConvert[i] != null)
                arrayToReturn[j++] = arrayToConvert[i].intValue();
        }
        return arrayToReturn;
    }

    private static long[] to_longArray(Long[] arrayToConvert) {
        long[] arrayToReturn = new long[getCount(arrayToConvert)];
        int j = 0;
        for (int i = 0; i < arrayToConvert.length; i++) {
            if (arrayToConvert[i] != null)
                arrayToReturn[j++] = arrayToConvert[i].longValue();
        }
        
        return arrayToReturn;
    }

    private static float[] to_floatArray(Float[] arrayToConvert) {
        float[] arrayToReturn = new float[getCount(arrayToConvert)];
        int j = 0;
        for (int i = 0; i < arrayToConvert.length; i++) {
            if (arrayToConvert[i] != null)
                arrayToReturn[j++] = arrayToConvert[i].floatValue();
        }
        return arrayToReturn;
    }

    private static double[] to_doubleArray(Double[] arrayToConvert) {
        double[] arrayToReturn = new double[arrayToConvert.length];
        int j = 0;
        for (int i = 0; i < arrayToReturn.length; i++) {
            if (arrayToConvert[i] != null)
                arrayToReturn[i] = arrayToConvert[i].doubleValue();
        }
        return arrayToReturn;
    }

    private static int getCount(Object[] arrayToConvert) {
        int count = 0;
        for (int i = 0; i < arrayToConvert.length; i++) {
            if (arrayToConvert[i] != null)
                count++;
        }
        return count;
    }

    public static void main (String[] args) {
        LibInitializer.initialize();
        DConfigReader.load();

        System.out.println(DConfigReader.getString("test.config.datasource.mysql", "attribute @ config", "n/a in db"));
        //System.out.println(DConfigReader.getString("test.config.datasource.mysql"));//get primary value
        System.out.println(DConfigWriter.writeString("test.config.datasource.mysql", "attribute @ config", "my new value"));
        System.out.println(DConfigReader.getString("test.config.datasource.mysql", "attribute @ config", "n/a in db"));

        Long[] longArray = new Long[3];
        longArray[0] = new Long(101);
        longArray[1] = new Long(202);
        longArray[2] = new Long(303);
        DConfigWriter.writeLong("test.admin.applications.long", 7755); // (primary) attribute
        DConfigWriter.writeLong("test.admin.applications", "long", 7755);
        DConfigWriter.writeLongArray("test.admin.applications.LongArray", longArray);// (primary) attribute
        
        DConfigWriter dcfgWriter = new DConfigWriter();
        dcfgWriter.setKeyName("");
        dcfgWriter.setAttributeName("");
        dcfgWriter.setDataTypeAlias("str");
        dcfgWriter.setAttributeValue("");
        dcfgWriter.setComments("hello");
        dcfgWriter.save();        
    }
}
