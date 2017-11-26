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

/**
 * Application state manager.
 *
 * @author Jonathan Luo
 */
public class AppState {
    private static boolean libMode = true;
    private static boolean demo = false;
    private static boolean derby = false;
    private static boolean hsqldb = false;
    private static boolean h2 = false;
    private static boolean verbose = false;
    
    /** Creates a new instance of SystemState */
    public AppState() {
    }
        
    /**
     * Returns true if running as Library mode;
     */
    public static boolean isLibMode() {
        return libMode;
    }
    
    /**
     * Sets library mode flag.
     *
     * @param b the flag value.
     */
    public static void setLibMode(boolean b) {
        libMode = b;
    }
    
    /**
     * Returns true if it is demo mode.
     */
    public static boolean isDemo() {
        return demo;
    }
    
    /**
     * Sets demo flag.
     *
     * @param b the flag value.
     */
    public static void setDemo(boolean b) {
        demo = b;
    }
    
    /**
     * Returns true if it is demo mode with derby embedded database.
     */
    public static boolean isDemoDerby() {
        return derby && demo;
    }
    
    /**
     * Sets demo with derby embedded database flag.
     *
     * @param b the flag value.
     */
    public static void setDerby(boolean b) {
        derby = b;
    }
    
    /**
     * Returns true if it is demo mode with hsqldb embedded database.
     */
    public static boolean isDemoHsqldb() {
        return hsqldb && demo;
    }
    
    /**
     * Sets demo with hsqldb embedded database flag.
     *
     * @param b the flag value.
     */
    public static void setHsqldb(boolean b) {
        hsqldb = b;
    }
    
    /**
     * Returns true if it is demo mode with hsqldb embedded database.
     */
    public static boolean isDemoH2() {
        return h2 && demo;
    }
    
    /**
     * Sets demo with h2 embedded database flag.
     *
     * @param b the flag value.
     */
    public static void setH2(boolean b) {
        h2 = b;
    }

    /**
     * Returns verbose mode.
     */
    public static boolean isVerbose() {
        return verbose;
    }
    
    /**
     * Sets verbose flag.
     *
     * @param b the flag value.
     */
    public static void setVerbose(boolean b) {
        verbose = b;
    }
}
