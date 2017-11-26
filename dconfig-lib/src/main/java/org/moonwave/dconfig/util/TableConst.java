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

import org.moonwave.dconfig.dao.LibProperties;

/**
 * Defines table name, column name, and column size, etc.
 *
 * @author Jonathan Luo
 */
public class TableConst {

    static {
        DbMetaDataUtil util = new DbMetaDataUtil();
        util.init();
        try {
            util.getColumnSize(TableConst.DConfigKey.tableName);
            util.getColumnSize(TableConst.DConfigAttribute.tableName);
            util.getColumnSize(TableConst.DConfigSystem.tableName);
        } catch (Exception e) {
        }
        util.cleanUp();
    }

    /**
     * Defines table name, column name, and column size for table dconfig_datatype.
     */
    public static class DConfigDataType {
        public static String tableName = LibProperties.getInstance().getProperty("tablename.dconfig_datatype", "dconfig_datatype");
    }

    /**
     * Defines table name, column name, and column size for table dconfig_key.
     */
    public static class DConfigKey {
        public static String tableName = LibProperties.getInstance().getProperty("tablename.dconfig_key", "dconfig_key");
        public static String keyName = "key_name";
        public static int keyNameSize;
    }

    /**
     * Defines table name, column name, and column size for table dconfig_attribute.
     */
    public static class DConfigAttribute {
        public static String tableName = LibProperties.getInstance().getProperty("tablename.dconfig_attribute", "dconfig_attribute");
        public static String attributeName = "attribute_name";
        public static String attributeValue = "attribute_value";
        public static String comments = "comments";
        public static int attributeNameSize;
        public static int attributeValueSize;
        public static int commentsSize;
    }
    
    /**
     * Defines table name, column name, and column size for table dconfig_system.
     */
    public static class DConfigSystem {
        public static String tableName = LibProperties.getInstance().getProperty("tablename.dconfig_system", "dconfig_system");
        public static String systemName = "system_name";
        public static String systemValue = "system_value";
        public static int systemNameSize;
        public static int systemValueSize;
    }
}
