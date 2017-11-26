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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.util.*;

/**
 *
 * @author Jonathan Luo
 */
public class LibInitializer {

    private static final Log log = LogFactory.getLog(LibInitializer.class);	
    
    /** Creates a new instance of LibInitializer */
    public LibInitializer() {
    }

    public static void initialize() {
        LibProperties.getInstance().load();
        String datasource = LibProperties.getInstance().getProperty("active.connection.prefix");
        log.info("LibProperties.getInstance().getProperty('active.connection.prefix') --> " + datasource);
        
        AppState.setLibMode(true);
        if (datasource != null) {
            if (datasource.indexOf("demo") >= 0)
                AppState.setDemo(true);
            if (datasource.indexOf("derby") >= 0)
                AppState.setDerby(true);
            if (datasource.indexOf("hsqldb") >= 0)
                AppState.setHsqldb(true);
            if (datasource.indexOf("h2") >= 0)
                AppState.setH2(true);
        }
        
        if (AppState.isDemo()) {
            DemoDbManager.startup();
            if (new DemoDataPopulator().populate()) {
                log.info("run DemoDataPopulator().populate() ok");
            } else {
                log.error("run DemoDataPopulator().populate() failed");                
            }
        }
    }
}
