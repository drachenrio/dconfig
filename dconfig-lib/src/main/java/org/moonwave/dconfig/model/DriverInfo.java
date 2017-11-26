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

import java.io.Serializable;

/**
 *
 * @author Jonathan Luo
 */
public class DriverInfo implements Serializable {
    String driverName;
    String driverClass;
    String supportedDriverClass;
    String urlFormat; // dburl
    String driverFilePath;
    Boolean configOK;
    transient String driverNameOld;
    transient String driverClassOld;
    transient String urlFormatOld; // dburl
    transient String driverFilePathOld;
    transient Boolean configOKOld;
    transient boolean saveRequested = false; // true indicates user has press save button on Driver Manager dialog
    transient boolean newItem = false; // true if just created from Driver Manager Dialog
    
    public DriverInfo() {
        driverName = "";
        driverClass = "";
        supportedDriverClass = "";
        urlFormat = "";
        driverFilePath = "";
        configOK = Boolean.FALSE;
        
        driverNameOld = "";
        driverClassOld = "";
        urlFormatOld = "";
        driverFilePathOld = "";
        configOKOld = Boolean.FALSE;
        saveRequested = false;
        newItem = false;
    }
    
    /**
     * Make a copy from primary fields to old fields.
     */
    public void copyToOld() {        
        driverNameOld = new String(driverName);
        driverClassOld = new String(driverClass);
        urlFormatOld = new String(urlFormat);
        driverFilePathOld = new String(driverFilePath);
        configOKOld = configOK;
    }
    
    /**
     * Make a copy from primary fields to old fields.
     */
    public void restoreFromOld() {        
        driverName = driverNameOld;
        driverClass = driverClassOld;
        urlFormat = urlFormatOld;
        driverFilePath = driverFilePathOld;
        configOK = configOKOld;
    }

    public String getDriverName() {
        return driverName;
    }
    
    public void setDriverName(String driverName) {
        if (driverName != null)
            this.driverName = driverName;
    }
    
    public String getDriverNameOld() {
        return driverNameOld;
    }
    
    public void setDriverNameOld(String driverName) {
        this.driverNameOld = driverName;
    }

    public String getDriverClass() {
        return driverClass;
    }
    
    public void setDriverClass(String driverClass) {
        if (driverClass != null)
            this.driverClass = driverClass;
    }
    
    public String getDriverClassOld() {
        return driverClassOld;
    }
    
    public void setDriverClassOld(String driverClass) {
        this.driverClassOld = driverClass;
    }

    public String getSupportedDriverClass() {
        return supportedDriverClass;
    }
    
    public void setSupportedDriverClass(String driverClass) {
        this.supportedDriverClass = driverClass;
    }

    public String getUrlFormat() {
        return urlFormat;
    }
    
    public void setUrlFormat(String urlFormat) {
        if (urlFormat != null)
            this.urlFormat = urlFormat;
    }
    
    public String getUrlFormatOld() {
        return urlFormatOld;
    }
    
    public void setUrlFormatOld(String urlFormat) {
        this.urlFormatOld = urlFormat;
    }

    public String getDriverFilePath() {
        return driverFilePath;
    }
    
    public void setDriverFilePath(String driverFilePath) {
        if (driverFilePath != null)
            this.driverFilePath = driverFilePath;
    }
    
    public String getDriverFilePathOld() {
        return driverFilePathOld;
    }
    
    public void setDriverFilePathOld(String driverFilePath) {
        this.driverFilePathOld = driverFilePath;
    }

    public boolean isConfigOK() {
        return configOK.booleanValue();
    }

    public void setConfigOK(boolean b) {
        configOK = new Boolean(b);
    }
    
    public void setConfigOKOld(boolean b) {
        configOKOld = new Boolean(b);
    }

    public boolean isSaveRequested() {
        return saveRequested;
    }
    
    public void setSaveRequested(boolean b) {
        this.saveRequested = b;
    }

    public boolean isNewItem() {
        return newItem;
    }
    
    public void setNewItem(boolean b) {
        newItem = b;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DriverInfo)) return false;

        final DriverInfo other = (DriverInfo) o;

        if ((driverName != null) && !driverName.equals(other.driverName)) return false;
        if ((driverClass != null) && !driverClass.equals(other.driverClass)) return false;
        if ((supportedDriverClass != null) && !supportedDriverClass.equals(other.supportedDriverClass)) return false;
        if ((urlFormat != null) && !urlFormat.equals(other.urlFormat)) return false;
        if ((driverFilePath != null) && !driverFilePath.equals(other.driverFilePath)) return false;
        if ((configOK != null) && !configOK.equals(other.configOK)) return false;

        return true;
    }

    public int hashCode() {
        return driverName.hashCode();
    }

    public boolean hasChanged() {
        boolean changed = false;        
        if (!driverName.equals(driverNameOld))
            changed = true;
        else if (!driverClass.equals(driverClassOld))
            changed = true;
        else if (!urlFormat.equals(urlFormatOld))
            changed = true;
        else if (!driverFilePath.equals(driverFilePathOld))
            changed = true;
        return changed;
    }

    public String toString() {
        /*
        StringBuffer sb = new StringBuffer();
        sb.append("driverName: ").append(this.driverName);
        sb.append(", driverClass: ").append(this.driverClass);
        sb.append(", urlFormat: ").append(this.urlFormat);
        sb.append(", driverFilePath: ").append(this.driverFilePath);
        sb.append(", configOK: ").append(this.configOK);
        return sb.toString();
         */
        return this.driverName; // for DriverManger JLabel
    }
}
