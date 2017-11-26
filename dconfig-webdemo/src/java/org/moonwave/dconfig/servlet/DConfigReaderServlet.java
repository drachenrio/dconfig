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

import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.moonwave.dconfig.dao.CacheManager;
import org.moonwave.dconfig.dao.LibInitializer;
import org.moonwave.dconfig.util.AppState;
import org.moonwave.dconfig.util.DemoDataPopulator;
import org.moonwave.dconfig.util.DemoDbManager;


/**
 *
 * @author Jonathan Luo
 */
public class DConfigReaderServlet extends HttpServlet {
    
    /** Creates a new instance of GetKeyName */
    public DConfigReaderServlet() {
    }
    
    protected  void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doService(req, resp);        
    }

    protected  void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doService(req, resp);
    }

    /**
     * Precosses ajax client request. Returns the results as a JSON string. Here 
     * is a sample JSON string:
     * var jsonString = '{"productId":1234,"price":24.5,"inStock":true,
     *                    "bananas":null,"address":"435 Main ST, Stockton"}'; 
     */
    protected void doService(HttpServletRequest request, HttpServletResponse response)
    {
        long lStartTime = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        String action = request.getParameter("action");
        if (action == null)
            action = "";
        if (action.equals("createDerbyDemoDb")) {
            LibInitializer.initialize();
            AppState.setDemo(true);
            AppState.setDerby(true);
            DemoDbManager.startup();
            sb.append("{");
            if (new DemoDataPopulator().populate()) {
                sb.append("\"msg\":\"").append("<font color='blue'>Derby database started successfully.</font>").append("\"");
                // copied from InitServlet
                CacheManager.load();
                //DConfigTimerTask.start();
            } else {
                sb.append("\"msg\":\"").append("<font color='red'>Failed to create Derby database. Check tomcat/bin/dconfig.log for details.</font>").append("\"");
            }            
            sb.append("}");
        } else if (action.equals("loadCache")) {
            sb.append("{");
            if (CacheManager.load()) {
                long lEndTime = System.currentTimeMillis();
                sb.append("\"msg\":\"").append("<font color='blue'>Done loading DConfig cache in " + (lEndTime - lStartTime) + " milliseconds</font>").append("\"");
            } else {
                sb.append("\"msg\":\"").append("<font color='red'>Load DConfig cache failed.</font>").append("\"");
            }
            sb.append("}");
        } else { // returns default test data, not a JSON format, \t and \n as the field and record delimiters
            sb.append("from_servlet").append("\t").append("from_servlet").append("\t").append("from_servlet").append("\n");
            sb.append("t-123").append("\t").append("t-xa").append("\t").append("t-xa123").append("\n");
            sb.append("t-456").append("\t").append("t-xb").append("\t").append("t-xb456").append("\n");
            sb.append("t-789").append("\t").append("t-xc").append("\t").append("t-xc789").append("\n");
            sb.append("t-abc").append("\t").append("t-xd").append("\t").append("t-xabc").append("\n");
            sb.append("t-def").append("\t").append("t-xe").append("\t").append("t-xdef").append("\n");
            sb.append("t-ghi").append("\t").append("t-xf").append("\t").append("t-xghi").append("\n");
            sb.append("t-tuv").append("\t").append("t-tuv").append("\t").append("t-tuv").append("\n");
            sb.append("t-xyz").append("\t").append("t-xyz").append("\t").append("t-xyz").append("\n");            
        }
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(sb.toString());
        } catch (Exception e) {

        }
    }
}
