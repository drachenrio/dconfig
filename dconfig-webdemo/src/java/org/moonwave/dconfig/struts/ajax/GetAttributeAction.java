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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.moonwave.dconfig.dao.DConfigReader;
import org.moonwave.dconfig.model.DConfigAttribute;

/**
 *
 * @author Jonathan Luo
 */
public class GetAttributeAction extends Action {

    private static final Log log = LogFactory.getLog(GetAttributeAction.class);
    
    public GetAttributeAction() {
    }

    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm form,
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
                        throws IOException, ServletException
    {            
        String keyName = request.getParameter("keyName");
        String attributeName = request.getParameter("attributeName");
        log.info("request.getParameter(keyName): " + keyName);
        log.info("request.getParameter(attributeName): " + attributeName);
        
        StringBuffer sb = new StringBuffer();
        
        List attributeList = null;
        if (attributeName.equals("*"))
            attributeList = DConfigReader.getByKeyNameAttributeNameStartWith(keyName, "");
        else
            attributeList = DConfigReader.getByKeyNameAttributeNameStartWith(keyName, attributeName);
        log.info("keyList: " + ((attributeList != null) ? attributeList.size() : 0));

        // ["Record Delimiter", "Field Delimiter"]
        // ["\n", "\t"]
        for (int i = 0; (attributeList != null) && (i < attributeList.size()); i++) {
            DConfigAttribute attribute = (DConfigAttribute) attributeList.get(i);
            sb.append(attribute.getAttributeName()).append("\n");
        }
        // returns data to client without going through tiles / jsp page
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(sb.toString());
        } catch (Exception e) {
        }
        return null;
    }
}
