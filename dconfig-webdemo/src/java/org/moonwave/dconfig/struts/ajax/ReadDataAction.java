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
import org.moonwave.dconfig.util.DataAccessUtil;

/**
 * A struts action acts as a 'web service' to return formatted data string
 * to an ajax web client.
 *
 * @author Jonathan Luo
 */
public class ReadDataAction extends Action {

    private static final Log log = LogFactory.getLog(ReadDataAction.class);
    
    public ReadDataAction() {
    }

    /**
     * Searches <code>DConfigAttribute</code> data for given key name and attribute 
     * name, and returns the results as a JSON string. Here is a sample JSON
     * string:
     * var jsonString = '{"productId":1234,"price":24.5,"inStock":true,
     *                    "bananas":null,"address":"435 Main ST, Stockton"}'; 
     */
    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm form,
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
                        throws IOException, ServletException
    {            
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(DataAccessUtil.readData(request));
        } catch (Exception e) {
        }
        return null;
    }
}
