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

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.DConfigReader;
import org.moonwave.dconfig.dao.springfw.DConfigAttributeDao;
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigDataType;

/**
 *
 * @author Jonathan Luo
 */
public class DataAccessUtil {
    
    private static final Log log = LogFactory.getLog(DataAccessUtil.class);

    public DataAccessUtil() {
    }

    public static String getDataType(HttpServletRequest request) {
        String submitType = request.getParameter("action");
        boolean postback = (submitType != null) ? true : false;
        String keyName = (request.getParameter("keyName") != null) ? request.getParameter("keyName") : "";
        String attributeName = (request.getParameter("attributeName") != null) ? request.getParameter("attributeName") : "";
        String dataTypeAlias = (request.getParameter("dataType") != null) ? request.getParameter("dataType") : "";
        String attributeValue = (request.getParameter("attributeValue") != null) ? request.getParameter("attributeValue") : "";
        String comments = (request.getParameter("comments") != null) ? request.getParameter("comments") : "";
        String inherited = "";
        
        if (postback && submitType.equalsIgnoreCase("readData")) {
            dataTypeAlias = (String)request.getAttribute("dataTypeAlias");
        }

        log.info("submitType: " + submitType);
        log.info("postback: " + postback);
        log.info("dataTypeAlias: " + dataTypeAlias);

        StringBuffer sbDataTypeOptions = new StringBuffer(300);
        //<select name="dataType" onchange="javascript:dataTypeChanged(this);"><%= sbDataTypeOptions.toString() %></select>
        java.util.List list = DConfigDataType.getDataTypeList();
        sbDataTypeOptions.append("<select name='dataType' id='dataType' onchange='javascript:dataTypeChanged(this);'>");
        for (int i = 0; i < list.size(); i++) {
            String alias = (String) list.get(i);
            sbDataTypeOptions.append("<option value='").append(alias).append("'");
            if (postback) {
                if (alias.equals(dataTypeAlias))
                    sbDataTypeOptions.append(" selected ");
                else if (alias.equals(DConfigDataType.aliasString) && (dataTypeAlias.length() == 0))
                    sbDataTypeOptions.append(" selected "); // deafult to String
            } else if (alias.equals(DConfigDataType.aliasString)) {
                // set default to String on page startup
                sbDataTypeOptions.append(" selected ");
            }
            sbDataTypeOptions.append(">");
            sbDataTypeOptions.append(DConfigDataTypeDao.getDataTypeNameByAlias(alias));
            sbDataTypeOptions.append("</option>");
        }
        sbDataTypeOptions.append("</select>");
        return sbDataTypeOptions.toString();
    }
    
    
    public static String readData(HttpServletRequest request)
    {            
        String keyName = request.getParameter("keyName");
        String attributeName = request.getParameter("attributeName");
        log.info("request.getParameter(keyName): " + keyName);
        log.info("request.getParameter(attributeName): " + attributeName);

        StringBuffer sb = new StringBuffer();
        DConfigAttributeDao dao = new DConfigAttributeDao();

        DConfigAttribute attribute = DConfigReader.getAttribute(keyName, attributeName);

        String dataType = attribute.getDataTypeName();
        request.setAttribute("dataTypeAlias", attribute.getDataTypeAlias());
        
        sb.append("{");
        sb.append("\"dataTypeAlias\":\"").append(attribute.getDataTypeAlias()).append("\",");
        sb.append("\"attributeValue\":\"").append(attribute.getAttributeValue()).append("\",");
        sb.append("\"dataType\":\"").append(DataAccessUtil.getDataType(request)).append("\",");
        sb.append("\"comments\":\"").append(attribute.getComments()).append("\",");        
        sb.append("\"inherited\":\"").append(attribute.isInherited() ? "Yes" : "No").append("\",");
        String arrayElements = "&nbsp;";
        if (dataType.indexOf("Array") >= 0) {
            arrayElements = "<b>Array elements:</b><br/>";
            String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
            for (int i = 0; i < items.length; i++) {
                if (i != 0)
                    arrayElements += "<br/>";
                arrayElements += items[i];
            }
        }
        sb.append("\"arrayElements\":\"").append(arrayElements).append("\"");
        sb.append("}");
        
        log.info("return value: " + sb.toString());

        return sb.toString();
    }
}
