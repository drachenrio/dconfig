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
import org.moonwave.dconfig.model.ConnectionInfo;
import org.moonwave.dconfig.util.*;

/**
 *
 * @author jonathan
 */
public class ConnectionInfoUtil {
    private static final Log log = LogFactory.getLog(ConnectionInfoUtil.class);

    static String filename = Constants.DCFGHOME + "/connection_info.ser";
    static List connectionInfoList = new ArrayList();
    
    public ConnectionInfoUtil() {
    }

    
    /**
     * Returns the default <code>ConnectionInfo</code>.
     */
    public static ConnectionInfo getDefaultConnectionInfo() {
        //load();
        if (connectionInfoList.size() == 0)
            load();
        ConnectionInfo retObj = null;
        for (int i = 0; i < connectionInfoList.size(); i++) {
            ConnectionInfo entry = (ConnectionInfo) connectionInfoList.get(i);
            if (entry.isDefaultConnection()) {
                retObj = entry;
                break;
            }
        }        
        //unload();
        return retObj;
    }
    
    /**
     * Returns connection info list.
     */
    public static List getConnectionInfoList() {
        return connectionInfoList;
    }

    /**
     * Loads connection info list from local hard drive by deserialization.
     *
     */
    public static synchronized void load() {
        connectionInfoList.clear();
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
                ConnectionInfo info = (ConnectionInfo) in.readObject();
                info.setSaveRequested(true);
                connectionInfoList.add(info);
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
        connectionInfoList.clear();
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
            for (int i = 0; i < connectionInfoList.size(); i++) {
                ConnectionInfo info = (ConnectionInfo) connectionInfoList.get(i);
                if (info.isSaveRequested())
                    saveCount++;
            }
            out.writeInt(saveCount);
            for (int i = 0; i < saveCount; i++) {
                ConnectionInfo info = (ConnectionInfo) connectionInfoList.get(i);
                if (info.isSaveRequested()) {
                    if (!info.isSavePassword())
                        info.setPassword("");
                    out.writeObject(info);
                }
            }
            out.close();
            fos.close();
        } catch(Exception e) {
            log.error(e);
        } finally {
        }
    }
}
