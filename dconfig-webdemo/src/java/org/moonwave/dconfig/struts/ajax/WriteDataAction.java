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

package org.moonwave.dconfig.struts.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.moonwave.dconfig.dao.DConfigWriter;
import org.moonwave.dconfig.util.DataAccessUtil;

/**
 *
 * @author Jonathan Luo
 */
public class WriteDataAction extends Action {

    private static final Log log = LogFactory.getLog(WriteDataAction.class);
    
    public WriteDataAction() {
    }

    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm form,
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
                        throws IOException, ServletException
    {            
        long lStartTime = System.currentTimeMillis();
        String keyName = (request.getParameter("keyName") != null) ? request.getParameter("keyName") : "";
        String attributeName = (request.getParameter("attributeName") != null) ? request.getParameter("attributeName") : "";
        String dataTypeAlias = (request.getParameter("dataType") != null) ? request.getParameter("dataType") : "";
        String attributeValue = (request.getParameter("attributeValue") != null) ? request.getParameter("attributeValue") : "";
        String comments = (request.getParameter("comments") != null) ? request.getParameter("comments") : "";
        String inherited = "";

        log.info("keyName: '" + keyName + "'");
        log.info("attributeName: '" + attributeName + "'");
        log.info("dataTypeAlias: '" + dataTypeAlias + "'");
        log.info("attributeValue: '" + attributeValue + "'");
        log.info("comments: '" + comments + "'");
        
        DConfigWriter dcfgWriter = new DConfigWriter();
        dcfgWriter.setKeyName(keyName);
        dcfgWriter.setAttributeName(attributeName);
        dcfgWriter.setDataTypeAlias(dataTypeAlias);
        dcfgWriter.setAttributeValue(attributeValue);
        dcfgWriter.setComments(comments);
        boolean bStatus = dcfgWriter.save();
        String status = "";
        if (bStatus) {
            inherited = "No";
            long lEndTime = System.currentTimeMillis();
            status = "<font color='blue'>Done writing DConfig data to database in " + (lEndTime - lStartTime) + " milliseconds</font>";
        } else {
            status = "<font color='red'>Failed to write DConfig data to database, try again later</font>";
        }
        // returns data to client without going through tiles / jsp page
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(DataAccessUtil.readData(request));
        } catch (Exception e) {
        }
        return null;
    }
}
