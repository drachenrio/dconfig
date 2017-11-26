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
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;

/**
 *
 * @author Jonathan Luo
 */
public class DConfigSystem {

    String  systemName;
    String  dataTypeAlias;
    String  systemValue;
    String  comments;
    
    public DConfigSystem() {
    }

    public DConfigSystem(DConfigSystem system) {        
        systemName = (system.systemName != null) ? new String(system.systemName) : null;
        dataTypeAlias = (system.dataTypeAlias != null) ? new String(system.dataTypeAlias) : null;
        systemValue = (system.systemValue != null) ? new String(system.systemValue) : null;
        comments = (system.comments != null) ? new String(system.comments) : null;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
    
    public String getDataTypeAlias() {
        return dataTypeAlias;
    }
    
    public void setDataTypeAlias(String dataTypeAlias) {
        this.dataTypeAlias = dataTypeAlias;
    }
    
    public String getDataTypeName() {
        return DConfigDataTypeDao.getDataTypeNameByAlias(dataTypeAlias);
    }
    
    public void setDataTypeName(String dataTypeName) {
        this.dataTypeAlias = DConfigDataTypeDao.getAliasByDataTypeName(dataTypeName);
    }
    
    
    public String getSystemValue() {
        return systemValue;
    }

    public void setSystemValue(String systemValue) {
        this.systemValue = systemValue;
    }

    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Returns true if and only if all the fields are the same.
     *
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DConfigSystem)) return false;

        final DConfigSystem other = (DConfigSystem) o;

        if (!this.systemName.equals(other.systemName)) return false;

        if (!this.toString().equals(other.toString()))
            return false;
        
        return true;
    }

    public int hashCode() {
        return this.systemName.hashCode();
    }    

    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("systemName = ").append(systemName);
    	sb.append(", dataTypeAlias = ").append(dataTypeAlias);
    	sb.append(", systemValue = '").append(systemValue);
    	sb.append("', comments = '").append(comments);
        return sb.toString();
    }

    public static Object newRow(ResultSet rs) throws SQLException {
    	DConfigSystem obj = new DConfigSystem();
    	obj.setSystemName(rs.getString("system_name"));
    	obj.setDataTypeAlias(rs.getString("data_type_alias"));
    	obj.setSystemValue(rs.getString("system_value"));
    	obj.setComments(rs.getString("comments"));
    	return obj;
    }
}
