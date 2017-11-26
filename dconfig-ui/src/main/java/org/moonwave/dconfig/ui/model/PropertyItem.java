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

/**
 *
 * @author Jonathan Luo
 */
public class PropertyItem implements Comparable {

    String name;
    String value;

    public PropertyItem() {
    }
    
    public PropertyItem(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    } 
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    } 
    
    public int compareTo(Object o) {
        return name.compareTo(((PropertyItem) o).name);
    }
    
    public boolean equals(Object o) {
        return name.equals(((PropertyItem) o).name);
    }
    
    public int hashCode() {
        return name.hashCode();
    }
}
