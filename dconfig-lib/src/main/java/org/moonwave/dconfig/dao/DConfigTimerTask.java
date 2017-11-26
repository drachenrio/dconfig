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

/**
 * Setup a timer task to reload dconfig settings, keys, and attributes from db
 * to memory. Property dconfig.cacheloader.taskperiod defined in 
 * deconfig_lib.properties indicates how often (the interval) this timer task 
 * will checks and reloads data into memory caches. 
 *
 * @author Jonathan Luo
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import org.moonwave.dconfig.dao.LibProperties;

public class DConfigTimerTask extends TimerTask {

    private static final Log log = LogFactory.getLog(DConfigTimerTask.class);
    private static boolean taskIsRunning = false;

    public static void start() {        
        String sTaskPeriod = LibProperties.getInstance().getProperty("dconfig.cacheloader.taskperiod" , "5");
        long taskPeriod = 0;
        try {
            taskPeriod = (60 * 1000) * Long.parseLong(sTaskPeriod);            
        } catch (NumberFormatException e) {
            taskPeriod = 60 * 1000 * 5;
        }
        if (taskPeriod > 0) {
            new Timer(true).schedule(new DConfigTimerTask(), 0, taskPeriod);
        }
    }

    public void run() {
        Timer timer = new Timer();
        if (!taskIsRunning) {
            taskIsRunning = true;
            try {
                if (CacheManager.load())
                    log.info("Done DConfigTimerTask.run() successfully" + timer.toString());
                else
                    log.info("Done DConfigTimerTask.run() with error " + timer.toString());
            } catch (Exception e) {
                
            } finally {
                taskIsRunning = false;                
            }
        }
    }
}