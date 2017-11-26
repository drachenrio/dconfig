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

import java.util.ArrayList;
import java.util.List;

import org.moonwave.dconfig.dao.springfw.DConfigAttributeDao;
import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;
import org.moonwave.dconfig.util.Constants;

/**
 * A wrapper class which represents a key and its attributes. 
 *
 * @author Jonathan Luo
 */
public class DConfig {

    DConfigKey key;
    List    attributes;
    // monitor variables
    boolean bDelete = false; 

    public DConfig() {
    }
    
    public DConfig(DConfigKey key) {
    	this.key = key;
    }
    
    /**
     * Copy constructor. Makes a deep copy.
     */
    public DConfig(DConfig other) {
        key = new DConfigKey(other.key);
        List attributeList = other.getOwnAttributes();
        if (attributeList != null) {
            attributes = new ArrayList(attributeList.size());
            for (int i = 0; i < attributeList.size(); i++) {
                DConfigAttribute attribute = (DConfigAttribute) attributeList.get(i);
                DConfigAttribute attributeItem = new DConfigAttribute(attribute);
                attributes.add(attributeItem);
            }
        }
        bDelete = other.bDelete;
    }
    
    public DConfigKey getKey() {
    	return this.key;
    }
    
    public void setKey(DConfigKey key) {
    	this.key = key;
    }
    
    /**
     * dot-separated key representation.
     * 
     * @return key name.
     */
    public String getKeyName() {
        return key.getKeyName();
    }
    
    public String getLastKeyName() {
    	return key.getLastKeyName();
    }
    
    public List getOwnAttributes() {
        return new DConfigAttributeDao().getByKeyId(this.key.id);
    }

    public List getAttributes() {
    	if (attributes == null) { // get attributes on the fly if inheritance changed
            attributes = getAttributesFromDb();
    	}
        return attributes;
    }

    public List getAttributesFromDb() {
        attributes = new DConfigAttributeDao().getByKeyId(this.key.id);
        // get inherited attributes
        DConfigKey currentKey = this.key;
        while (currentKey.isInherited()) {
            String parentKeyName = currentKey.getParentKeyName();
            if (parentKeyName.equalsIgnoreCase(Constants.EMPTY_STRING))
                break;
            currentKey = new DConfigKeyDao().getByKey(parentKeyName);
            List inheritedAttributes = new DConfigAttributeDao().getAttributes(currentKey);
            for (int i = 0; i < inheritedAttributes.size(); i++) {
                DConfigAttribute attribute = (DConfigAttribute) inheritedAttributes.get(i);
                attribute.setInherited("Y");
                if (!contains(attributes, attribute))
                    attributes.add(attribute);
            }
        }
        return attributes;
    }

    protected boolean contains(List attributes, DConfigAttribute attribute) {
        boolean bRet = false;
        for (int i = 0; i < attributes.size(); i++) {
            DConfigAttribute item = (DConfigAttribute) attributes.get(i);
            if (item.getAttributeName().equals(attribute.getAttributeName())) {
                bRet = true;
                break;
            }
        }
        return bRet;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(DConfigAttribute attribute) {
        if (attributes == null)
            attributes = new ArrayList();
        this.attributes.add(attribute);
    }
    
    /**
     * Clears change flags.
     */
    public void clearChangeFlag() {
        key.clearChangeFlag();
        if (attributes != null) {
            for (int i = 0; i < attributes.size(); i++) {
                DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
                attribute.clearChangeFlag();
            }
        }
        bDelete = false;
    }

    /**
     * Checks whether key name has been changed.
     * Not includes the attribute changes.
     * @return true if key name changed; false otherwise.
     */
    public boolean hasChanged() {
    	if (key.hasChanged())
            return true;
    	boolean bRet = false;
    	if (attributes != null) {
            for (int i =0; i < attributes.size(); i++) {
                DConfigAttribute attr = (DConfigAttribute) attributes.get(i);
                if (attr.hasChanged()) {
                    bRet = true;
                    break;
                }
            }
    	}
        return bRet;
    }
    
    public void setDelete(boolean status) {
    	this.bDelete = status;
    }

    public boolean isDelete() {
        return this.bDelete;
    }

    public boolean isNew() {
        return this.getKey().isNew();
    }

    public void setNew() {
        key.setNew();
        if (attributes != null) {
            for (int i = 0; i < attributes.size(); i++) {
                DConfigAttribute attribute = (DConfigAttribute) attributes.get(i);
                attribute.setNew();
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DConfig)) return false;

        final DConfig other = (DConfig) o;

        if (!key.equals(other.key)) return false;

        return true;
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    public String toString() {
        return this.key.getLastKeyName();
    }
}
