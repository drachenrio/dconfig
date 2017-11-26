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

import java.util.Comparator;

/**
 *
 * A class wraps a <code>String</code> object and identifies itself as a 
 * <code>LongText</code>.
 *
 * @author Jonathan Luo
 */
public class LongText implements Comparator {
   
    String text;
    /** Creates a new instance of LongText */
    public LongText() {
    }
    public LongText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;        
    }
    public void setText(String text) {
        this.text = text;        
    }
    public String toString() {
        return text;
    }
    public int compare(Object o1, Object o2) {    	
    	LongText t1 = (LongText)o1;
    	LongText t2 = (LongText)o2;

        String text1 = t1.getText();
        String text2 = t2.getText();

        return text1.compareTo(text2);
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongText)) return false;

        final LongText obj = (LongText) o;

        if (!text.equals(obj.text)) return false;

        return true;
    }
    public int hashCode() {
        return text.hashCode();
    }    
}
