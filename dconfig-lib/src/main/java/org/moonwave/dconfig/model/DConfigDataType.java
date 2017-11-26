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

package org.moonwave.dconfig.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jonathan luo
 */
public class DConfigDataType {
    public static String aliasBinary = "bin";
    public static String aliasBoolean = "bool";
    public static String aliasBooleanArray = "boolar";
    public static String aliasInteger = "int";
    public static String aliasIntegerArray = "intar";
    public static String aliasLong = "long";
    public static String aliasLongArray = "longar";
    public static String aliasString = "str";
    public static String aliasStringArray = "strar";
    public static String aliasFloat = "float";
    public static String aliasFloatArray = "floatar";
    public static String aliasDouble = "double";
    public static String aliasDoubleArray = "doublear";
    public static String aliasDatetime = "dt";
    public static String aliasDatetimeArray = "dtar";
    public static String aliasOperation = "op";
    public static String aliasOperationGroup = "opgrp";
    public static List   dataTypeList = new ArrayList();

    static {
        //singleDataType.add(aliasBinary);
        dataTypeList.add(aliasBoolean);
        dataTypeList.add(aliasBooleanArray);
        dataTypeList.add(aliasInteger);
        dataTypeList.add(aliasIntegerArray);
        dataTypeList.add(aliasLong);
        dataTypeList.add(aliasLongArray);
        dataTypeList.add(aliasString);
        dataTypeList.add(aliasStringArray);
        dataTypeList.add(aliasFloat);
        dataTypeList.add(aliasFloatArray);
        dataTypeList.add(aliasDouble);
        dataTypeList.add(aliasDoubleArray);
        dataTypeList.add(aliasDatetime);
        dataTypeList.add(aliasDatetimeArray);
        dataTypeList.add(aliasOperation);
        dataTypeList.add(aliasOperationGroup);
   }
    
    String alias;
    String dataTypeName;
    
    public DConfigDataType() {
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getDataTypeName() {
        return dataTypeName;
    }
    public void setDataTypeName(String keyName) {
        this.dataTypeName = keyName;
    }
    public static List getDataTypeList() {
        return dataTypeList;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DConfigDataType)) return false;

        final DConfigDataType other = (DConfigDataType) o;

        if (!alias.equals(other.alias)) return false;

        return true;
    }
    public int hashCode() {
        return alias.hashCode();
    }
    
    public String toString() {
        return "(alias = '" + alias + "', name = '" + dataTypeName + "')";
    }

    public static Object newRow(ResultSet rs) throws SQLException {
    	DConfigDataType obj = new DConfigDataType();
    	obj.setAlias(rs.getString("alias"));
    	obj.setDataTypeName(rs.getString("data_type_name"));
    	return obj;
    }
}
