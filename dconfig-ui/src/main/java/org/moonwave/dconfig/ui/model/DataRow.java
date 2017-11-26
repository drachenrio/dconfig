/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * DConfig - Free Dynamic Configuration Toolkit
 * Copyright (C) 2006,2007 Jonathan Luo
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

package org.moonwave.dconfig.ui.model;

import org.apache.commons.lang.StringUtils;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.ui.*;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.DateUtil;

/**
 * Definition of a table row.
 *
 * @author Jonathan Luo
 */

public class DataRow {
    
    String  dataTypeName;
    LongText valueText;
    LongText commentsText;
    DConfigAttribute attribute;
    
    public DataRow(DConfigAttribute attribute) {
        this.attribute = attribute;
        this.dataTypeName = new String((attribute.getDataTypeName() != null) ? attribute.getDataTypeName() : "");
        String attributeValue = attribute.getAttributeValue();
        if (attribute.getDataTypeAlias().equals(DConfigDataType.aliasDatetime)) {
            attributeValue = DateUtil.toDispalyFormat(attributeValue);
        } else if (attribute.getDataTypeAlias().equals(DConfigDataType.aliasDatetimeArray)) {
            String[] dataAr = StringUtils.split(attributeValue, Constants.DELIMITER);
            if (dataAr != null) {
                StringBuffer sb = new StringBuffer(300);
                for (int i = 0; i < dataAr.length; i++) {
                    if (i > 0)
                        sb.append(Constants.DELIMITER);
                    sb.append(DateUtil.toDispalyFormat(dataAr[i]));
                }
                attributeValue = sb.toString();
            }
        }
        this.valueText = new LongText((attributeValue != null) ? attributeValue : "");
        this.commentsText = new LongText((attribute.getComments() != null) ? attribute.getComments() : "");
    }
    public String getInherited() {
    	return this.attribute.getInherited();
    }
    public boolean isInherited() {
    	return this.attribute.isInherited();
    }
    public void setInherited(String inherited) {
        this.attribute.setInherited(inherited);
    }
    public String getInheritedFrom() {
    	return this.attribute.getInheritedFrom();
    }
    public String getAttributeName() {
    	return this.attribute.getAttributeName();
    }
    public void setAttributeName(String attributeName) {
         this.attribute.setAttributeName(attributeName);
    }
    public String getDataTypeName() {
         return this.attribute.getDataTypeName();
    }
    public void setDataTypeName(String dataTypeName) {
         this.attribute.setDataTypeName(dataTypeName);
    }
    public LongText getValueText() {
        return this.valueText;
    }
    public void setValueText(String value) {
        attribute.setAttributeValue(value);
        this.valueText.setText(value);
    }
    public LongText getCommentsText() {
        return this.commentsText;
    }
    public void setComments(String comments) {
        attribute.setComments(comments);
        this.commentsText.setText(comments);
    }
    public boolean hasChanged() {
        return this.attribute.hasChanged();
    }
 
    public DConfigAttribute getAttribute() {
        return attribute;
    }    
    
    public void setDelete(boolean status) {
        this.attribute.setDelete(status);
    }
    
    public boolean isDelete() {
        return this.attribute.isDelete();
    }    
}
