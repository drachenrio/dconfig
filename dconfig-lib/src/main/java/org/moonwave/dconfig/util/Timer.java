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

package org.moonwave.dconfig.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper class to calculate the time elapse.
 *
 * @author Jonathan Luo
 */
public class Timer {

    private static final Log log = LogFactory.getLog(Timer.class);

    long lStartTime;
    long lEndTime;
    
	public Timer() {
        start(); // start the timer immediately        
    }
    
    public Timer(boolean startNow) {
        if (startNow)
        	start();
    }
    
    /**
     * Starts or restarts the timer.
     */
    public void start() {
        lStartTime = System.currentTimeMillis();
    }
    
    public long getMilliSeconds() {
        lEndTime = System.currentTimeMillis();
        return (lEndTime - lStartTime);
    }
    
    /**
     * Converts to minutes and seconds.
     */
    public String toMMSS() {
        StringBuffer sb = new StringBuffer(50);
        long seconds = getMilliSeconds() / 1000; 
        long minutes = seconds / 60;
        seconds -= minutes * 60;
        sb.append(" in ").append(minutes).append(":").append(seconds).append(" (mm:ss)");
        return sb.toString();        
    }
    
    /**
     * Converts to seconds.
     */
    public String toSeconds() {
        StringBuffer sb = new StringBuffer(50);
        sb.append(" in ").append(getMilliSeconds() / 1000).append(" seconds");
        return sb.toString();
    }
    
    /**
     * Converts to miliiseconds.
     */
    public String toMilliseconds() {
        StringBuffer sb = new StringBuffer(300);
        sb.append(" in ").append(getMilliSeconds()).append(" milliseconds");
        return sb.toString();
    }
    
    public String toString() {
        return toMilliseconds();
    }
}
