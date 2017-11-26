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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import org.moonwave.dconfig.dao.springfw.DConfigAttributeDao;
import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.DataConverter;

/**
 * 
 * @author Jonathan Luo
 */
public class DConfigReader {
    
    private static final Log log = LogFactory.getLog(DConfigReader.class);
    
    /*
     * Key: Integer key id; value: DConfigKey
     */
    private static Map keyId_key_map = new HashMap();

    /*
     * key: String the full keyname; value: DConfigKey
     */
    private static Map keyName_key_map = new HashMap();

    /*
     * key: String keyId + "-" + attributeName; value: DConfigAttribute
     */
    private static Map keyIdAttributeName_Attribute_map = new HashMap();
        
    /**
     * Returns keyid-to-key map.
     */
    public static Map getKeyId_KeyMap() {
        return keyId_key_map;
    }
    
    /**
     * Returns keyname-to-key map.
     */
    public static Map getKeyName_KeyMap() {
        return keyName_key_map;
    }

    /**
     * Returns key id + attribute name-to-attribute value map.
     */
    public static Map getKeyIdAttributeName_AttributeMap() {
        return keyIdAttributeName_Attribute_map;
    }

    /**
     * Populates local key and attribute caches from database.
     */
    public static synchronized void load() {
        Map temp_keyid_key_map = new HashMap();
        Map temp_keyname_key_map = new HashMap();
        Map temp_keyidAttribute_value = new HashMap();
        List list = new DConfigKeyDao().getAllKeys();
        for (int i = 0; i < list.size(); i++) {
            DConfigKey key = (DConfigKey)list.get(i);
            temp_keyname_key_map.put(key.getKeyName(), key);
            temp_keyid_key_map.put(key.getId(), key);
        }
        list.clear();
        list = new DConfigAttributeDao().getAllAtributes();
        for (int i = 0; i < list.size(); i++) {
            DConfigAttribute attribute = (DConfigAttribute)list.get(i);
            temp_keyidAttribute_value.put(attribute.getKeyId() + "-" + 
                    attribute.getAttributeName(), attribute);
        }
        // remove old caches
        keyId_key_map.clear();
        keyName_key_map.clear();
        keyIdAttributeName_Attribute_map.clear();
        keyId_key_map = null;
        keyName_key_map = null;
        keyIdAttributeName_Attribute_map = null;
        // set new caches
        keyId_key_map = temp_keyid_key_map;
        keyName_key_map = temp_keyname_key_map;
        keyIdAttributeName_Attribute_map = temp_keyidAttribute_value;
        log.info("done AttributeReader.loadCaches()");
    }
    
    /**
     * Releases resource from memory.
     */
    public static synchronized void unload() {
        keyId_key_map.clear();
        keyName_key_map.clear();
        keyIdAttributeName_Attribute_map.clear();
    }
    
    /**
     * Find key by key id.
     *
     * @param keyId  key id to search.
     * @return <code>DConfigKey</code> if found; null otherwise.
     */
    public static DConfigKey findKeyById(Integer keyId) {
        return (DConfigKey) keyId_key_map.get(keyId);
    }

    /**
     * Find key by keyname.
     *
     * @param name key name to search.
     * @return <code>DConfigKey</code> if found; null otherwise.
     */
    public static DConfigKey findKeyByName(String name) {
        return (DConfigKey) keyName_key_map.get(name);
    }

    /**
     * Checks whether a key exists.
     * 
     * @param keyName key name to check.
     * @return true if argument keyname exists; false otherwise.
     */
    public static boolean isKeyExist(String keyName) {
        return (findKeyByName(keyName) != null) ? true : false;
    }
    
    /**
     * Returns primary attribute value for specified keyName.
     *
     * @param keyName keyName name.
     */
    public static DConfigAttribute getAttribute(String keyName) {
        return getAttribute(keyName, Constants.PRIMARY_ATTRIBUTE, null);
    }

    /**
     * Retrieves attribute value for specified keyName and attribute name.
     *
     * @param keyName keyName name.
     * @param attributeName attribute name.
     */
    public static DConfigAttribute getAttribute(String keyName, String attributeName) {
        return getAttribute(keyName, attributeName, null);
    }

    /**
     * Retrieves attribute value for specified keyName and attribute name.
     * Searches inherited attribute as well during the searching process.
     *
     * @param keyName keyName name.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return attribute if found; default value otherwise.
     */
    protected static DConfigAttribute getAttribute(String keyName, String attributeName, Object defaultValue) {
        checkArgument(keyName, attributeName);
        DConfigAttribute cfgAttribute = null;
        DConfigKey cfgKey = (DConfigKey) keyName_key_map.get(keyName);
        if (cfgKey == null) {
            if (defaultValue == null) {
                StringBuffer sb = new StringBuffer(50);
                sb.append("Key '").append(keyName).append("' does not exist");
                throw new KeyNotExistException(sb.toString());
            }
        } 
        if (cfgKey != null) {
            String lookupKey = cfgKey.getId() + "-" + attributeName;
            cfgAttribute = (DConfigAttribute) keyIdAttributeName_Attribute_map.get(lookupKey);
            if (cfgAttribute == null) {
                // search inherited attributes
                DConfigKey currentKey = cfgKey;
                while (currentKey.isInherited()) {
                    String parentKeyName = currentKey.getParentKeyName();
                    if (parentKeyName.equalsIgnoreCase(Constants.EMPTY_STRING))
                        break;
                    currentKey  = (DConfigKey) keyName_key_map.get(parentKeyName);                    
                    lookupKey = currentKey.getId() + "-" + attributeName;
                    DConfigAttribute attributeItem = (DConfigAttribute) keyIdAttributeName_Attribute_map.get(lookupKey);
                    if (attributeItem != null) {
                        cfgAttribute = new DConfigAttribute(attributeItem);
                        cfgAttribute.setInherited(true);
                        break;
                    }
                }
            }
        }
        if (cfgAttribute == null) {
            if (defaultValue == null) {
                StringBuffer sb = new StringBuffer(80);
                sb.append("Attribute '").append(attributeName).append("' of Key '").append(keyName).append("' does not exist");
                throw new AttributeNotExistException(attributeName);
            }
        }
        return cfgAttribute;
    }
        
    /**
     * Gets attribute list by key name, attribute name.
     * The returned results include inherited attributes.
     *
     * @param keyName key name.
     * @param attributeNameStartWith attribute name.
     *
     * @return <code>DConfigAttribute</code> list if found; empty list otherwise.
     */
    public static List getByKeyNameAttributeNameStartWith(String keyName, String attributeNameStartWith) {
        List resultList = new ArrayList();
        DConfigKey cfgKey = (DConfigKey) keyName_key_map.get(keyName);
        if (cfgKey == null) {
            return resultList;
        }
        DConfigAttributeDao dao = new DConfigAttributeDao();
        List list = dao.getByKeyNameAttributeNameStartWith(keyName, attributeNameStartWith);
        if (list != null)
            resultList.addAll(list);
        DConfigKey currentKey = cfgKey;
        while (currentKey.isInherited()) {
            String parentKeyName = currentKey.getParentKeyName();
            if (parentKeyName.equalsIgnoreCase(Constants.EMPTY_STRING))
                break;
            dao = new DConfigAttributeDao();
            list = dao.getByKeyNameAttributeNameStartWith(parentKeyName, attributeNameStartWith);
            if (list != null)
                resultList.addAll(list);
            currentKey = (DConfigKey) keyName_key_map.get(parentKeyName);
            if (currentKey == null)
                break;
        }
    	return resultList;
    }
    
    /**
     * Gets boolean attribute value of the primary attribute for a specified key name.
     *
     * @param keyName key name to search.
     */
    public static boolean getBoolean(String keyName) {
        return getBoolean(keyName, Constants.PRIMARY_ATTRIBUTE);
    }
    
    /**
     * Gets boolean attribute value of the primary attribute for a specified key name.
     *
     * @param keyName key name to search.
     * @param defaultValue default value.
     * @return boolean attribute value if found; default value otherwise.
     */
    public static boolean getBoolean(String keyName, boolean defaultValue) {
        return getBoolean(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets boolean attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     */
    public static boolean getBoolean(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        return Boolean.parseBoolean(attribute.getAttributeValue());
    }

    /**
     * Gets boolean attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return boolean attribute value if found; default value otherwise.
     */
    public static boolean getBoolean(String keyName, String attributeName, boolean defaultValue) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName, new Boolean(defaultValue));
        if (attribute == null)
            return defaultValue;
        else
            return Boolean.parseBoolean(attribute.getAttributeValue());
    }

    /**
     * Gets primary attribute boolean array value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @return boolean array value if found; null otherwise.
     */
    public static boolean[] getBooleanArray(String keyName) {
        return getBooleanArray(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    /**
     * Gets primary attribute boolean array value for a specified key name.
     *
     * @param keyName key name to search.
     * @param defaultValue default value.
     * @return boolean array value if found; default value otherwise.
     */
    public static boolean[] getBooleanArray(String keyName, boolean[] defaultValue) {
        return getBooleanArray(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets boolean array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @return boolean array value if found; null otherwise.
     */
    public static boolean[] getBooleanArray(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        boolean[] retObj = null;
        try {
            String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
            retObj = new boolean[items.length];
            for (int i = 0; i < items.length; i++) {
                retObj[i] = Boolean.parseBoolean(items[i]);
            }
        } catch (Exception e) {
            throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
        }
        return retObj;
    }

    /**
     * Gets boolean array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return boolean array value if found; default value otherwise.
     */
    public static boolean[] getBooleanArray(String keyName, String attributeName, boolean[] defaultValue) {
        checkdefaultValue(defaultValue);
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        boolean[] retObj = null;
        if (attribute != null) {
            try {
                String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
                retObj = new boolean[items.length];
                for (int i = 0; i < items.length; i++) {
                    retObj[i] = Boolean.parseBoolean(items[i]);
                }
            } catch (Exception e) {
                throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
            }
        } else
            retObj = defaultValue;
        return retObj;
    }
    
    /**
     * Gets <code>String</code> attribute value of the primary attribute for a 
     * specified key name.
     *
     * @param      keyName   the key name to search
     * @return     string value if found; null otherwise;
     */
    public static String getString(String keyName) {
        DConfigAttribute attribute = getAttribute(keyName);
        return attribute.getAttributeValue();
    }    
    
    /**
     * Gets the attribute value for specified key name, attribute name.
     *
     * @param      keyName   the key name to search
     * @param      attributeName attribute name 
     * @return     string value if found; null otherwise;
     */
    public static String getString(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        return attribute.getAttributeValue();
    }

    /**
     * Gets the attribute value for specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @return string array value if found; default value otherwise.
     */
    public static String getString(String keyName, String attributeName, String defaultValue) {
        checkdefaultValue(defaultValue);
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        if (attribute == null)
            return defaultValue;
        else
            return attribute.getAttributeValue();
    }

    /**
     * Gets <code>String</code> array attribute value of the primary attribute for a specified key name, 
     *
     * @param keyName key name to search.
     * @return string array value if found; null otherwise.
     */
    public static String[] getStringArray(String keyName) {
        return getStringArray(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    /**
     * Gets <code>String</code> array attribute value of the primary attribute for a specified key name, 
     *
     * @param keyName key name to search.
     * @param defaultValue default value.
     * @return string array value if found; default value otherwise.
     */
    public static String[] getStringArray(String keyName, String[] defaultValue) {
        return getStringArray(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets <code>String</code> array attribute value for a specified key name, 
     * attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @return string array value if found; null otherwise.
     */
    public static String[] getStringArray(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        String[] retObj = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
        return retObj;
    }


    /**
     * Gets <code>String</code> array attribute value for a specified key name, 
     * attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return string array value if found; default value otherwise.
     */
    public static String[] getStringArray(String keyName, String attributeName, String[] defaultValue) {
        checkdefaultValue(defaultValue);
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        String[] retObj = null;
        if (attribute != null) {
            retObj = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
        } else
            retObj = defaultValue;
        return retObj;
    }
    
    /**
     * Gets int attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @return     int value if found; null otherwise;
     */
    public static int getInteger(String keyName) {
        return getInteger(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    /**
     * Gets int attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @param      defaultValue default value if not found.
     * @return     int value if found; default value otherwise;
     */
    public static int getInteger(String keyName, int defaultValue) {
        return getInteger(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets int attribute value for a  specified key name, attribute name.
     *
     * @param      keyName   the key name to search.
     * @param      attributeName   attribute name.
     * @return     int value if found; default value otherwise;
     */
    public static int getInteger(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        return Integer.parseInt(attribute.getAttributeValue());
    }
    
    /**
     * Gets int attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search.
     * @param      attributeName   attribute name.
     * @param      defaultValue default value if not found.
     * @return     int value if found; default value otherwise;
     */
    public static int getInteger(String keyName, String attributeName, int defaultValue) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName, new Integer(defaultValue));
        if (attribute == null)
            return defaultValue;
        else
            return Integer.parseInt(attribute.getAttributeValue());
    }
    
    /**
     * Gets int array attribute value of the primary attribute for a specified 
     * key name.
     *
     * @param keyName key name to search.
     * @return int array value if found; null otherwise.
     */
    public static int[] getIntegerArray(String keyName) {
        return getIntegerArray(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    /**
     * Gets int array attribute value of the primary attribute for a specified 
     * key name, and default value.
     *
     * @param keyName key name to search.
     * @param defaultValue default value.
     * @return int array value if found; default value otherwise.
     */
    public static int[] getIntegerArray(String keyName, int[] defaultValue) {
        return getIntegerArray(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets int array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @return int array value if found; null otherwise.
     */
    public static int[] getIntegerArray(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        int[] retObj = null;
        try {
            String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
            retObj = new int[items.length];
            for (int i = 0; i < items.length; i++) {
                retObj[i] = Integer.parseInt(items[i]);
            }
        } catch (Exception e) {
            throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
        }
        return retObj;
    }

    /**
     * Gets int array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return int array value if found; default value otherwise.
     */
    public static int[] getIntegerArray(String keyName, String attributeName, int[] defaultValue) {
        checkdefaultValue(defaultValue);
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        int[] retObj = null;
        if (attribute != null) {
            try {
                String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
                retObj = new int[items.length];
                for (int i = 0; i < items.length; i++) {
                    retObj[i] = Integer.parseInt(items[i]);
                }
            } catch (Exception e) {
                throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
            }
        } else
            retObj = defaultValue;
        return retObj;
    }

    /**
     * Gets long attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @return     long value if found; null otherwise;
     */
    public static long getLong(String keyName) {
        return getLong(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    public static long getLong(String keyName, long defaultValue) {
        return getLong(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    public static long getLong(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        return Long.parseLong(attribute.getAttributeValue());
    }
    
    public static long getLong(String keyName, String attributeName, long defaultValue) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName, Long.valueOf(defaultValue));
        if (attribute == null)
            return defaultValue;
        else
            return Long.parseLong(attribute.getAttributeValue());
    }
    
    /**
     * Gets long array attribute value of the primary attribute for a specified 
     * key name.
     *
     * @param keyName key name to search.
     * @return long array value if found; null otherwise.
     */
    public static long[] getLongArray(String keyName) {
        return getLongArray(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    /**
     * Gets long array attribute value of the primary attribute for a specified 
     * key name.
     *
     * @param keyName key name to search.
     * @param defaultValue default value.
     * @return long array value if found; defaultValue otherwise.
     */
    public static long[] getLongArray(String keyName, long[] defaultValue) {
        return getLongArray(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets long array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @return long array value if found; null otherwise.
     */
    public static long[] getLongArray(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        long[] retObj = null;
        try {
            String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
            retObj = new long[items.length];
            for (int i = 0; i < items.length; i++) {
                retObj[i] = Long.parseLong(items[i]);
            }
        } catch (Exception e) {
            throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
        }
        return retObj;
    }

    /**
     * Gets long array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return long array value if found; default value otherwise.
     */
    public static long[] getLongArray(String keyName, String attributeName, long[] defaultValue) {
        checkdefaultValue(defaultValue);
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        long[] retObj = null;
        if (attribute != null) {
            try {
                String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
                retObj = new long[items.length];
                for (int i = 0; i < items.length; i++) {
                    retObj[i] = Long.parseLong(items[i]);
                }
            } catch (Exception e) {
                throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
            }
        } else
            retObj = defaultValue;
        return retObj;
    }
    
    /**
     * Gets float attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @return     float value if found; null otherwise;
     */
    public static float getFloat(String keyName) {
        return getFloat(keyName, Constants.PRIMARY_ATTRIBUTE);
    }
    
    /**
     * Gets float attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @return     float value if found; null otherwise;
     */
    public static float getFloat(String keyName, float defaultValue) {
        return getFloat(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    public static float getFloat(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        return Float.parseFloat(attribute.getAttributeValue());
    }
    
    public static float getFloat(String keyName, String attributeName, float defaultValue) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName, Float.valueOf(defaultValue));
        if (attribute == null)
            return defaultValue;
        else
            return Float.parseFloat(attribute.getAttributeValue());
    }
    
    /**
     * Gets float array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @return float array value if found; null otherwise.
     */
    public static float[] getFloatArray(String keyName) {
        return getFloatArray(keyName, Constants.PRIMARY_ATTRIBUTE);
    }
    
    /**
     * Gets float array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param defaultValue default value.
     * @return float array value if found; null otherwise.
     */
    public static float[] getFloatArray(String keyName, float[] defaultValue) {
        return getFloatArray(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets float array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @return float array value if found; null otherwise.
     */
    public static float[] getFloatArray(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        float[] retObj = null;
        try {
            String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
            retObj = new float[items.length];
            for (int i = 0; i < items.length; i++) {
                retObj[i] = Float.parseFloat(items[i]);
            }
        } catch (Exception e) {
            throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
        }
        return retObj;
    }

    /**
     * Gets float array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return float array value if found; default value otherwise.
     */
    public static float[] getFloatArray(String keyName, String attributeName, float[] defaultValue) {
        checkdefaultValue(defaultValue);
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        float[] retObj = null;
        if (attribute != null) {
            try {
                String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
                retObj = new float[items.length];
                for (int i = 0; i < items.length; i++) {
                    retObj[i] = Float.parseFloat(items[i]);
                }
            } catch (Exception e) {
                throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
            }
        } else
            retObj = defaultValue;
        return retObj;
    }
    
    /**
     * Gets double attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @return     double value if found; null otherwise;
     */
    public static double getDouble(String keyName) {
        return getDouble(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    /**
     * Gets double attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @return     double value if found; null otherwise;
     */
    public static double getDouble(String keyName, double defaultValue) {
        return getDouble(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }


    public static double getDouble(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        return Double.parseDouble(attribute.getAttributeValue());
    }
    
    public static double getDouble(String keyName, String attributeName, double defaultValue) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName, Double.valueOf(defaultValue));
        if (attribute == null)
            return defaultValue;
        else
            return Double.parseDouble(attribute.getAttributeValue());
    }
    
    /**
     * Gets double array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @return double array value if found; null otherwise.
     */
    public static double[] getDoubleArray(String keyName) {
        return getDoubleArray(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    /**
     * Gets double array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param defaultValue default value.
     * @return double array value if found; null otherwise.
     */
    public static double[] getDoubleArray(String keyName, double[] defaultValue) {
        return getDoubleArray(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets double array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @return double array value if found; null otherwise.
     */
    public static double[] getDoubleArray(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        double[] retObj = null;
        try {
            String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
            retObj = new double[items.length];
            for (int i = 0; i < items.length; i++) {
                retObj[i] = Double.parseDouble(items[i]);
            }
        } catch (Exception e) {
            throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
        }
        return retObj;
    }
    
    /**
     * Gets double array attribute value for a specified key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return double array value if found; default value otherwise.
     */
    public static double[] getDoubleArray(String keyName, String attributeName, double[] defaultValue) {
        checkdefaultValue(defaultValue);
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        double[] retObj = null;
        if (attribute != null) {
            try {
                String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
                retObj = new double[items.length];
                for (int i = 0; i < items.length; i++) {
                    retObj[i] = Double.parseDouble(items[i]);
                }
            } catch (Exception e) {
                throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
            }
        } else
            retObj = defaultValue;
        return retObj;
    }
    

    /**
     * Gets <code>Date</code> attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @return     date value if found; null otherwise;
     */
    public static Date getDate(String keyName) {
        return getDate(keyName, Constants.PRIMARY_ATTRIBUTE);
    }

    /**
     * Gets <code>Date</code> attribute value of the primary attribute for a  specified key name.
     *
     * @param      keyName   the key name to search
     * @return     date value if found; null otherwise;
     */
    public static Date getDate(String keyName, Date defaultValue) {
        return getDate(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    public static Date getDate(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        return DataConverter.toDate(attribute.getAttributeValue());
    }
    
    public static Date getDate(String keyName, String attributeName, Date defaultValue) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        if (attribute == null)
            return defaultValue;
        else
            return DataConverter.toDate(attribute.getAttributeValue());
    }
    
    /**
     * Gets <code>java.util.Date</code> array attribute value for a specified 
     * key name, attribute name.
     *
     * @param keyName key name to search.
     * @return date array value if found; null otherwise.
     */
    public static Date[] getDateArray(String keyName) {
        return getDateArray(keyName, Constants.PRIMARY_ATTRIBUTE);
    }
    
    /**
     * Gets <code>java.util.Date</code> array attribute value for a specified 
     * key name, attribute name.
     *
     * @param keyName key name to search.
     * @param defaultValue default value.
     * @return date array value if found; null otherwise.
     */
    public static Date[] getDateArray(String keyName, Date[] defaultValue) {
        return getDateArray(keyName, Constants.PRIMARY_ATTRIBUTE, defaultValue);
    }

    /**
     * Gets <code>java.util.Date</code> array attribute value for a specified 
     * key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @return date array value if found; null otherwise.
     */
    public static Date[] getDateArray(String keyName, String attributeName) {
        DConfigAttribute attribute = getAttribute(keyName, attributeName);
        Date[] retObj = null;
        try {
            String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
            retObj = new Date[items.length];
            for (int i = 0; i < items.length; i++) {
                retObj[i] = DataConverter.toDate(items[i]);
            }
        } catch (Exception e) {
            throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
        }
        return retObj;
    }
    /**
     * Gets <code>java.util.Date</code> array attribute value for a specified 
     * key name, attribute name.
     *
     * @param keyName key name to search.
     * @param attributeName attribute name.
     * @param defaultValue default value.
     * @return date array value if found; default value otherwise.
     */
    public static Date[] getDateArray(String keyName, String attributeName, Date[] defaultValue) {
        checkdefaultValue(defaultValue);
        DConfigAttribute attribute = getAttribute(keyName, attributeName, defaultValue);
        Date[] retObj = null;
        if (attribute != null) {
            try {
                String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
                retObj = new Date[items.length];
                for (int i = 0; i < items.length; i++) {
                    retObj[i] = DataConverter.toDate(items[i]);
                }
            } catch (Exception e) {
                throw new IncompatibleTypeException("attributeName: " + attributeName + " has incompatible data type values");
            }
        } else
            retObj = defaultValue;
        return retObj;
    }
    
    /**
     * Helper method to convert a <code>Object</code> array to a List;
     */
    public static List toList(Object[] objArray) {
        List retList = new ArrayList();
        for (int i = 0; i < objArray.length; i++) {
            retList.add(objArray[i]);
        }
        return retList;
    }
            
    private static void checkKey(String keyName) {
        if (keyName == null) {
            throw new NullPointerException("keyName can't be null");
        }
        if (keyName.equals("")) {
            throw new IllegalArgumentException("keyName can't be empty");
        }
    }
            
    private static void checkArgument(String keyName, String attributeName) {
        checkKey(keyName);
        if (attributeName == null) {
            throw new NullPointerException("attribute name  can't be null");
        }
        if (attributeName.equals("")) {
            throw new IllegalArgumentException("attribute name can't be empty");
        }
    }
    
    private static void checkdefaultValue(Object defaultValue) {
        if (defaultValue == null) {
            throw new IllegalArgumentException("defaultValue can not be null.");
        }
    }

    public static void main (String[] args) {
        LibInitializer.initialize();
        DConfigReader.load();
        System.out.println(DConfigReader.getString("config.datasource.mysql", "attribute @ config"));
    }
}
