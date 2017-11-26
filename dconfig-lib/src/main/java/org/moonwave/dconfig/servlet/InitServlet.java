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

package org.moonwave.dconfig.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import org.moonwave.dconfig.dao.DConfigTimerTask;
import org.moonwave.dconfig.dao.CacheManager;
import org.moonwave.dconfig.dao.LibInitializer;
import org.moonwave.dconfig.dao.LibProperties;
import org.moonwave.dconfig.util.LoggerUtil;

/**
 * Initializes web application resources.
 * Note: This servlet should be marked as load-on-startup in the web.xml file.
 * 
 * @author Jonathan Luo
 */
public class InitServlet extends HttpServlet {
    
    public void init() {

        try {
            ServletContext sc = getServletContext();

            String filePath = sc.getRealPath("/WEB-INF/classes/log4j.properties");
            if (filePath != null)
                LoggerUtil.load(filePath);
            else {
                java.net.URL fileURL = sc.getResource("log4j.properties");
                if (fileURL != null)
                    LoggerUtil.load(fileURL);
            }
        } catch (Throwable e) {
            System.err.println("Log4j initialization error: " + e);
        }
        String loadOnStartup = LibProperties.getInstance().getProperty("dconfig.InitServlet.loadOnStartup");
        if (loadOnStartup.equalsIgnoreCase("true")) {
            try {
                LibInitializer.initialize();
                CacheManager.load();
                DConfigTimerTask.start();
            } catch (Throwable e) {
                System.err.println("InitServlet error: " + e);
            }
        }
    }

    public void destroy() {
        CacheManager.unload();
    }
}
