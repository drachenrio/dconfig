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

package org.moonwave.dconfig.dao;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.dao.springfw.DConfigSystemDao;

/**
 * This class manages dconfig caches.
 *
 * @author Jonathan Luo
 */
public class CacheManager {
    
    private static final Log log = LogFactory.getLog(CacheManager.class);

    private static java.util.Date lastCacheLoadTimeStamp = null;

    public CacheManager() {
    }
    
    /*
     * Loads cacahes for various components.
     *
     */
    public static boolean load() {
        boolean bRet = false;
        boolean loadData = true; // TODO - put as true for now until the feature is complete for write data to db
        if (lastCacheLoadTimeStamp != null) {
            Date dataChangeTimeStamp = new DConfigSystemDao().getLastDataChangeTimeStamp();
            if (dataChangeTimeStamp != null) {
                if (lastCacheLoadTimeStamp.compareTo(dataChangeTimeStamp) <= 0)
                    loadData = true;
            } else
                    loadData = true;
        } else
            loadData = true;
        if (loadData) {
            bRet = loadData();
            lastCacheLoadTimeStamp = Calendar.getInstance().getTime();
        }
        return bRet;
    }
    
    /*
     * Loads cacahes for various components.
     *
     */
    protected synchronized static boolean loadData() {
        boolean bRet = false;
        try {
            DConfigDataTypeDao.load();
            DConfigReader.load();
            bRet = true;
            log.info("CacheManager.load() complete");
        } catch (Exception e) {
            log.error(e);
        }
        return bRet;
    }

    /*
     * Releases resources used by caches.
     *
     */
    public static void unload() {
        DConfigReader.unload();
    }
}
