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

import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.KeyUtil;

/**
 *
 * @author jonathan luo
 */
public class DConfigKey {
    public static final String inheritedY = "Y";
    public static final String inheritedN = "N";
    public static final Integer rootId = new Integer(-1);
    public static final Integer newKeyId = new Integer(-2);
    public static final DConfigKey root = new DConfigKey(rootId, "/", inheritedN);

    Integer id = newKeyId;
    String  keyName; // a complete dot-separated key name.
    String  inherited;
    String  keyNameOld; // save original keyName before any change
    String  inheritedOld;

    // monitor variables
    boolean bDelete = false; 

    public DConfigKey() {
    }

    public DConfigKey(DConfigKey other) {
        id = (other.id != null) ? new Integer(other.id.intValue()) : null;
        keyName = (other.keyName != null) ? new String(other.keyName) : null;
        inherited = (other.inherited != null) ? new String(other.inherited) : null;
        keyNameOld = (other.keyNameOld != null) ? new String(other.keyNameOld) : null;
        inheritedOld = (other.inheritedOld != null) ? new String(other.inheritedOld) : null;
    }

    /**
     * Creates a new <code>DConfigKey</code>.
     * 
     * @param keyName
     */
    public DConfigKey(String keyName) {
        //this.id = newKeyId;
    	this.keyName = keyName;
    	this.keyNameOld = this.keyName;
    	this.inherited = inheritedN;
    	this.inheritedOld = inheritedN;
    }

    /**
     * Creates a new <code>DConfigKey</code>.
     * 
     * @param keyName
     */
    public DConfigKey(String keyName, boolean changed) {
        //this.id = newKeyId;
    	this.keyName = keyName;
        if (changed)
            this.keyNameOld = "-" + this.keyName;
    	this.inherited = inheritedN;
    	this.inheritedOld = inheritedN;
    }

    /**
     * 
     * 
     * @param id
     * @param keyName a full dot-separated key name.  
     */
    public DConfigKey(Integer id, String keyName, String inherited) {
    	if (id != null)
            this.id = id;
    	else
            this.id = newKeyId;
    	this.keyName = keyName;
    	this.inherited = inherited;
    	this.keyNameOld = this.keyName;
    	this.inheritedOld = inherited;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * slash-separated key representation.
     * 
     * @return old key name.
     */
    public String getKeyNameOld() {
        return keyNameOld;
    }
    
    /**
     * @return old inherited flag.
     */
    public String getInheritedOld() {
        return inheritedOld;
    }
    
    /**
     * slash-separated key representation.
     * 
     * @return key name.
     */
    public String getKeyName() {
        return keyName;
    }
    
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
    
    public String getInherited() {
        return inherited;
    }
    
    public boolean isInherited() {
        return inherited.equalsIgnoreCase(inheritedY) ? true : false;
    }

    public void setInherited(String inherited) {
        this.inherited = inherited;
    }
    
    /**
     * Checks whether key name or inherited has been changed.
     * Not includes the attribute changes.
     * @return true if changed; false otherwise.
     */
    public boolean hasChanged() {
        return !keyName.equals(keyNameOld) || !inherited.equals(inheritedOld);
    }
    
    public boolean isRoot() {
    	return this.equals(root);
    }
    
    public boolean isNew() {
        return this.id.equals(newKeyId);
    }
    
    /**
     * Sets this attribute as a new.
     */
    public void setNew() {
        this.id = newKeyId;
    }
    
    public void setDelete(boolean status) {
    	this.bDelete = status;
    }

    public boolean isDelete() {
        return this.bDelete;
    }

    /**
     * Sets changed flag.
     */
    public void setChangeFlag() {
        this.keyNameOld = "-" + this.keyName;
    }

    public void clearChangeFlag() {
    	this.keyNameOld = (keyName != null) ? new String(keyName) : null;
    	this.inheritedOld = (inherited != null) ? new String(inherited) : null;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DConfigKey)) return false;

        final DConfigKey other = (DConfigKey) o;

        if (!id.equals(other.id)) return false;
        if (!keyName.equals(other.keyName)) return false;
        if (!inherited.equals(other.inherited)) return false;

        return true;
    }

    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Gets the lead portion of a dot-separated keyname, excludes the last keyname. 
     * e.g. for a key com.sun.studio, com.sun is returned.
     * for a key config, '' is returned.
     * 
     * @return lead key name; empty string if current node is the first level node.
     */
    public String getLeadKeyName() {
        return KeyUtil.getLeadKeyName(this.keyName);
    }

    /**
     * Gets the parent key name.
     *
     * @return parent key name; empty string if parent is root.
     */
    public String getParentKeyName() {
        return getLeadKeyName();
    }

    /**
     * Gets the last portion of a dot-separated keyname. e.g.
     * for a key com.sun.studio, 'studio' is returned.
     * for a key config, 'config' is returned.
     * This value is used to to show on a tree node.  
     * 
     * @return last key name.
     */
    public String getLastKeyName() {
    	if (this.keyName.equals("/")) // root
            return this.keyName;
    	int lastDotIdx = this.keyName.lastIndexOf(Constants.KEY_SEPARATOR);
    	if (lastDotIdx < 0)
            return this.keyName;
    	else
            return this.keyName.substring(lastDotIdx + 1, this.keyName.length());
    }
    
    /**
     * Convenient method to set full key name from lastKeyName argument.
     * 
     * @param lastKeyName last key name.
     */
    public void setLastKeyName(String lastKeyName) {
        this.setKeyName(getLeadKeyName() + "." + lastKeyName);
    }

    /**
     * Gets the last portion of a dot-separated keyname upto specified keyname.
     * e.g. for a key com.sun.studio, and keynameUpto 'sun', 'sun.studio' is returned.
     * 
     * @return key name.
     */
    public String getEndingKeyNameUpto(String keynameUpto) {
    	if (this.keyName.equals("/")) // root
            return this.keyName;
    	int lastDotIdx = this.keyName.lastIndexOf(Constants.KEY_SEPARATOR);
    	if (lastDotIdx < 0)
            return this.keyName;
    	else
            return this.keyName.substring(lastDotIdx + 1, this.keyName.length());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(50);
        sb.append("(id = ").append(id);
        sb.append(", keyName = ").append(keyName);
        sb.append(", inherited = ").append(inherited);
        sb.append(")");        
        return sb.toString();
    }

    public static Object newRow(ResultSet rs) throws SQLException {
    	DConfigKey key = new DConfigKey(Integer.valueOf(rs.getInt("id")), rs.getString("key_name"), rs.getString("inherited"));
    	return key;
    }
    
    public static void main(String[] argv) {
    	DConfigKey key = new DConfigKey(new Integer(1), "com", inheritedN);
    	System.out.println(key.getLastKeyName());
    	System.out.println(key.getLeadKeyName());
    	key = new DConfigKey(new Integer(1), "com.config", inheritedN);
    	System.out.println(key.getLastKeyName());    	
    	System.out.println(key.getLeadKeyName());
    	key = new DConfigKey(new Integer(2), "com.config.bea.apollo.ipod", inheritedN);
    	System.out.println(key.getLastKeyName());
    	System.out.println(key.getLeadKeyName());
    	key = new DConfigKey(new Integer(2), "/", inheritedN);
    	System.out.println(key.getLastKeyName());
    	System.out.println(key.getLeadKeyName());        
    }
}
