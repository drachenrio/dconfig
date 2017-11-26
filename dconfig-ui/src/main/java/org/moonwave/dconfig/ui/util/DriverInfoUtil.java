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

package org.moonwave.dconfig.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.model.DriverInfo;
import org.moonwave.dconfig.util.*;

/**
 *
 * @author jonathan
 */
public class DriverInfoUtil {
    private static final Log log = LogFactory.getLog(DriverInfoUtil.class);

    static String filename = Constants.DCFGHOME + "/driver_info.ser";
    static List driverInfoList = new ArrayList();
    
    public DriverInfoUtil() {
    }
        
    /**
     * Returns connection info list.
     */
    public static List getDriverInfoList() {
        return driverInfoList;
    }

    public static List getConfiguredDriverList() {
        List list = new ArrayList();
        for (int i = 0; i < driverInfoList.size(); i++) {
            DriverInfo info = (DriverInfo) driverInfoList.get(i);
            if (info.isConfigOK())
                list.add(info);
        }
        return list;
    }
        
    /**
     * Returns true if specified driver name exists.
     *
     * @param driverName driver name to check.
     */
    public static boolean existDriverName(String driverName) {
        boolean bRet = false;
        for (int i = 0; i < driverInfoList.size(); i++) {
            DriverInfo driverInfo = (DriverInfo) driverInfoList.get(i);
            if (driverInfo.getDriverName().equalsIgnoreCase(driverName)) {
                bRet = true;
                break;
            }
        }
        return bRet;
    }
    
    /**
     * Loads connection info list from local hard drive by deserialization.
     *
     */
    public static synchronized void load() {
        driverInfoList.clear();
        if (!new File(filename).exists()) {
            return;
        }
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            int count = (int)in.readInt();
            for (int i = 0; i < count; i++) {
                DriverInfo info = (DriverInfo) in.readObject();
                info.setSaveRequested(true);
                driverInfoList.add(info);
            }
            in.close();
            fis.close();
        } catch(Exception e) {
            log.error(e);
        } finally {
        }
    }
    
    /**
     * Releases application properties resource.
     *
     */
    public static void unload() {
        driverInfoList.clear();
    }    

    /**
     * Saves list of connection info to local hard drive by serialization.
     *
     */
    public static synchronized void store() {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            int saveCount = 0;
            for (int i = 0; i < driverInfoList.size(); i++) {
                DriverInfo info = (DriverInfo) driverInfoList.get(i);
                if (info.isSaveRequested())
                    saveCount++;
            }
            out.writeInt(saveCount);
            for (int i = 0; i < saveCount; i++) {
                DriverInfo info = (DriverInfo) driverInfoList.get(i);
                out.writeObject(info);
            }
            out.close();
            fos.close();
        } catch(Exception e) {
            log.error(e);
        } finally {
        }
    }
}
