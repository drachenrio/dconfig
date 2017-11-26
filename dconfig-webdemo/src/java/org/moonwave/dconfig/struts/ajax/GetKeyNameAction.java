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

import java.util.List;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.moonwave.dconfig.dao.springfw.DConfigKeyDao;
import org.moonwave.dconfig.model.DConfigKey;

/**
 *
 * @author Jonathan Luo
 */
public class GetKeyNameAction extends Action {

    private static final Log log = LogFactory.getLog(GetKeyNameAction.class);
    
    public GetKeyNameAction() {
    }

    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm form,
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
                        throws IOException, ServletException
    {        
        String keyName = request.getParameter("keyName");
        log.info("request.getParameter(keyName): " + keyName);
        
        StringBuffer sb = new StringBuffer();
        DConfigKeyDao dao = new DConfigKeyDao();
        
        List keyList = null;
        if (keyName.equals("*"))
            keyList = dao.getAllKeys();
        else
            keyList = dao.getKeyStartWith(keyName);
        log.info("keyList: " + ((keyList != null) ? keyList.size() : 0));

        // ["Record Delimiter", "Field Delimiter"]
        // ["\n", "\t"]
        for (int i = 0; (keyList != null) && (i < keyList.size()); i++) {
            DConfigKey key = (DConfigKey) keyList.get(i);
            //sb.append(key.getId()).append("\t").append(key.getKeyName()).append("\n");
            sb.append(key.getKeyName()).append("\n");
        }
        // returns data to client through tiles / jsp page
        String responseData = sb.toString();
        request.setAttribute("responseData", responseData);
        log.info("GetKeyNameAction > responseData: " + responseData);

        ActionForward forward = mapping.findForward("jsp.ac_ws");
        return forward;
    }
}
