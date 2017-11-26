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
import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;
import org.moonwave.dconfig.util.Constants;

/**
 *
 * @author jonathan luo
 */
public class DConfigAttribute {
    public static final Integer newAttributeId = new Integer(-2);

    Integer id;
    Integer keyId;
    Integer keyIdOld;
    String  attributeName;
    String  attributeNameOld;
    String  dataTypeAlias;
    String  dataTypeAliasOld;
    String  reference;
    String  referenceOld;
    String  attributeValue;
    String  attributeValueOld;
    String  comments;
    String  commentsOld;
    // fields for inheritance
    String  inherited;
    DConfigKey dcfgKey;
    
    boolean bDelete = false; 
    
    public DConfigAttribute() {        
        id = newAttributeId;
    }
    
    public DConfigAttribute(DConfigAttribute attribute) {        
        id = (attribute.id != null) ? new Integer(attribute.id.intValue()) : null;
        keyId = (attribute.keyId != null) ? new Integer(attribute.keyId.intValue()) : null;
        keyIdOld = (attribute.keyIdOld != null) ? new Integer(attribute.keyIdOld.intValue()) : null;
        attributeName = (attribute.attributeName != null) ? new String(attribute.attributeName) : null;
        attributeNameOld = (attribute.attributeNameOld != null) ? new String(attribute.attributeNameOld) : null;
        dataTypeAlias = (attribute.dataTypeAlias != null) ? new String(attribute.dataTypeAlias) : null;
        dataTypeAliasOld = (attribute.dataTypeAliasOld != null) ? new String(attribute.dataTypeAliasOld) : null;
        reference = (attribute.reference != null) ? new String(attribute.reference) : null;
        referenceOld = (attribute.referenceOld != null) ? new String(attribute.referenceOld) : null;
        attributeValue = (attribute.attributeValue != null) ? new String(attribute.attributeValue) : null;
        attributeValueOld = (attribute.attributeValueOld != null) ? new String(attribute.attributeValueOld) : null;
        comments = (attribute.comments != null) ? new String(attribute.comments) : null;
        commentsOld = (attribute.commentsOld != null) ? new String(attribute.commentsOld) : null;

        inherited = (attribute.inherited != null) ? new String(attribute.inherited) : null;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getKeyId() {
        return keyId;
    }
    
    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }
    
    public String getAttributeName() {
        return attributeName;
    }
    
    public void setAttributeName(String keyName) {
        this.attributeName = keyName;
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
    
    public String getAttributeValue() {
        return attributeValue;
    }
    
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Makes a copy of all fields. These copied values are used to check if any
     * data changes later.
     */
    public void makeAValueCopy() {
        keyIdOld = (keyId != null) ? new Integer(keyId.intValue()) : null;
        attributeNameOld = (attributeName != null) ? new String(attributeName) : null;
        dataTypeAliasOld = (dataTypeAlias != null) ? new String(dataTypeAlias) : null;
        attributeValueOld = (attributeValue != null) ? new String(attributeValue) : null;
        commentsOld = (comments != null) ? new String(comments) : null;
    }
    
    /**
     * Discards and changes for this attribute by restoring old values.
     */
    public void discardChanges() {
        keyId = (keyIdOld != null) ? new Integer(keyIdOld.intValue()) : null;
        attributeName = (attributeNameOld != null) ? new String(attributeNameOld) : null;
        dataTypeAlias = (dataTypeAliasOld != null) ? new String(dataTypeAliasOld) : null;
        attributeValue = (attributeValueOld != null) ? new String(attributeValueOld) : null;
        comments = (commentsOld != null) ? new String(commentsOld) : null;
        this.bDelete = false;
    }
    
    public void clearChangeFlag() {
        makeAValueCopy();
        this.bDelete = false;
    }
    
    /**
     * Returns true if any changes has been made to this attribute.
     */
    public boolean hasChanged() {
    	boolean bRet = false;
        if ((keyId != null) && (!keyId.equals(keyIdOld)) ||
                ((attributeName != null) && !attributeName.equals(attributeNameOld)) ||
                ((dataTypeAlias != null) && !dataTypeAlias.equals(dataTypeAliasOld)) ||
                ((attributeValue != null) && !attributeValue.equals(attributeValueOld)) ||
                ((comments != null) && !comments.equals(commentsOld)))
            bRet = true;
    	return bRet;
    }
    
    public void setDelete(boolean status) {
    	this.bDelete = status;
    }
    
    /**
     * Returns true if this attribue is marked as delete.
     */
    public boolean isDelete() {
        return this.bDelete;
    }
    
    /**
     * Returns true if this attribue is a new attribute.
     */
    public boolean isNew() {
        return this.id.equals(newAttributeId);
    }
    
    /**
     * Sets this attribute as a new.
     */
    public void setNew() {
        id = newAttributeId;
    }

    public String getInherited() {
        return isInherited() ? " *" : Constants.EMPTY_STRING;
    }

    /**
     * Sets attribute inheritance flag.
     *
     * @param inherited a value of either "Y" or "N"
     */
    public void setInherited(String inherited) {
        this.inherited = inherited;
    }

    /**
     * Sets attribute inheritance flag.
     *
     * @param inherited inheritance flag.
     */
    public void setInherited(boolean inherited) {
        this.inherited = inherited ? DConfigKey.inheritedY : DConfigKey.inheritedN;
    }

    public boolean isInherited() {
        return ((inherited != null) && inherited.equalsIgnoreCase(DConfigKey.inheritedY)) ? true : false;
    }

    public String getInheritedFrom() {
        return isInherited() ? dcfgKey.getKeyName() : Constants.EMPTY_STRING;
    }

    /**
     * Returns true if and only if all the fields are the same.
     *
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DConfigAttribute)) return false;

        final DConfigAttribute other = (DConfigAttribute) o;

        if (!id.equals(other.id)) return false;

        if (!this.toString().equals(other.toString()))
            return false;
        
        return true;
    }

    public int hashCode() {
        return id.hashCode();
    }    

    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("(id = ").append(id);
    	sb.append(", keyId = ").append(keyId);
    	sb.append(", attributeName = ").append(attributeName);
    	sb.append(", dataTypeAlias = ").append(dataTypeAlias);
    	sb.append(", attributeValue = '").append(attributeValue);
    	sb.append("', comments = '").append(comments);
    	sb.append("', delete = '").append(bDelete);
    	sb.append("', new = '").append(isNew());
    	sb.append("', inherited = '").append(inherited).append("')");
        return sb.toString();
    }

    public static Object newRow(ResultSet rs) throws SQLException {
    	DConfigAttribute obj = new DConfigAttribute();
    	obj.setId(Integer.valueOf(rs.getInt("id")));
    	obj.setKeyId(Integer.valueOf(rs.getInt("key_id")));
    	obj.setAttributeName(rs.getString("attribute_name"));
    	obj.setDataTypeAlias(rs.getString("data_type_alias"));
    	obj.setAttributeValue(rs.getString("attribute_value"));
    	obj.setComments(rs.getString("comments"));
        if (obj.dcfgKey == null)
            obj.dcfgKey = new DConfigKeyDao().getById(obj.keyId);
    	return obj;
    }
}
