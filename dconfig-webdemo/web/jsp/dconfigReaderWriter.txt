<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="org.moonwave.dconfig.dao.CacheManager" %>
<%@page import="org.moonwave.dconfig.dao.DConfigReader" %>
<%@page import="org.moonwave.dconfig.dao.DConfigWriter" %>
<%@page import="org.moonwave.dconfig.dao.LibInitializer" %>
<%@page import="org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao" %>
<%@page import="org.moonwave.dconfig.model.DConfigAttribute" %>
<%@page import="org.moonwave.dconfig.model.DConfigDataType" %>
<%@page import="org.moonwave.dconfig.util.AppState" %>
<%@page import="org.moonwave.dconfig.util.Constants" %>
<%@page import="org.moonwave.dconfig.util.DemoDataPopulator" %>
<%@page import="org.moonwave.dconfig.util.DemoDbManager" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
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
 */    
%>
<%
/*
 * This page demonstrates how to read an attribute value from DConfig cache, 
 * update an attribute value to database and cache, and how to write a new key 
 * and attribute value to database and cache .
 *
 * Note: This page is intentionally written entirely in a single jsp page, including
 * post back processing scriptlet code, without using any outside frameworks, such as
 * struts, JSF, etc., or even a servlet. It is the hope that to list all logic 
 * in just one place by using jsp only approach may help to analyze DConfigReader,
 * DConfigWriter logic easier without the extra burden to understand 3rd party libraries.
 *
 * Please note that it is not good practice to embed Java code (scriptlets) 
 * inside a jsp page, usually those code should go to action classes or backing beans. 
 *
 * For a sample JSF web demo, please look at 
 * http://dconfig.sourceforge.net/dconfig/webdemo.php#run-jsf-webdemo
 *
 * @author Jonathan Luo
 */
%>
<%
    String status = "&nbsp;";
    String arrayElements = "&nbsp;";
    long lStartTime = System.currentTimeMillis();
    String submitType = request.getParameter("submitType");
    boolean postback = (submitType != null) ? true : false;
    if (!postback) { // initial request
        CacheManager.load();
    }

    String keyName = (request.getParameter("keyName") != null) ? request.getParameter("keyName") : "";
    String attributeName = (request.getParameter("attributeName") != null) ? request.getParameter("attributeName") : "";
    String dataTypeAlias = (request.getParameter("dataType") != null) ? request.getParameter("dataType") : "";
    String attributeValue = (request.getParameter("attributeValue") != null) ? request.getParameter("attributeValue") : "";
    String comments = (request.getParameter("comments") != null) ? request.getParameter("comments") : "";
    String inherited = "";
    if (postback) {
        if (submitType.equalsIgnoreCase("clearFields")){
            keyName = "";
            attributeName = "";
            dataTypeAlias = "str";
            attributeValue = "";
            comments = "";
            inherited = "";
        }
        else if (submitType.equalsIgnoreCase("createDerbyDemoDb")){ // create dconfig tables, populate demo data
            LibInitializer.initialize();
            AppState.setDemo(true);
            AppState.setDerby(true);
            DemoDbManager.startup();
            if (new DemoDataPopulator().populate()) {
                status = "<font color=\"blue\">Derby database started successfully.</font>";
                // copied from InitServlet
                CacheManager.load();
                //DConfigTimerTask.start();
            } else {
                status = "<font color=\"red\">Failed to create Derby database. Check tomcat/bin/dconfig.log for details.</font>";
            }
        }
        else if (submitType.equalsIgnoreCase("loadCache")){
	        if (CacheManager.load()) {
	            long lEndTime = System.currentTimeMillis();
	            status = "<font color=\"blue\">Done loading DConfig cache in " + (lEndTime - lStartTime) + " milliseconds</font>";
	        } else {
	            status = "<font color=\"red\">Load DConfig cache failed.</font>";
	        }
        } else if (submitType.equalsIgnoreCase("readData")){
            try {
                DConfigAttribute attribute = DConfigReader.getAttribute(keyName, attributeName);
                String dataType = attribute.getDataTypeName();
                dataTypeAlias = attribute.getDataTypeAlias();
                attributeValue = attribute.getAttributeValue();
                comments = attribute.getComments();
                inherited = attribute.isInherited() ? "Yes" : "No";
                if (dataType.indexOf("Array") >= 0) {
                    arrayElements = "<b>Array elements:</b><br/>";
                    String[] items = StringUtils.split(attribute.getAttributeValue(), Constants.DELIMITER);
                    for (int i = 0; i < items.length; i++) {
                        if (i != 0)
                            arrayElements += "<br/>";
                        arrayElements += items[i];
                    }
                }
            } catch (Exception e) {
                dataTypeAlias = "";
                attributeValue = "";
                comments = "";
                inherited = "";
                status = "<font color=\"red\">Failed to find specified DConfig data, check key name and attribute name and try again</font>";
            }
        } else if (submitType.equalsIgnoreCase("writeToDB")){
            DConfigWriter dcfgWriter = new DConfigWriter();
            dcfgWriter.setKeyName(keyName);
            dcfgWriter.setAttributeName(attributeName);
            dcfgWriter.setDataTypeAlias(dataTypeAlias);
            dcfgWriter.setAttributeValue(attributeValue);
            dcfgWriter.setComments(comments);
            boolean bStatus = dcfgWriter.save();
            if (bStatus) {
                inherited = "No";
                long lEndTime = System.currentTimeMillis();
                status = "<font color=\"blue\">Done writing DConfig data to database in " + (lEndTime - lStartTime) + " milliseconds</font>";
            } else {
                status = "<font color=\"red\">Failed to write DConfig data to database, try again later</font>";
            }
            String dataType = DConfigDataTypeDao.getDataTypeNameByAlias(dataTypeAlias);
            if (bStatus && dataType.indexOf("Array") >= 0) {
                arrayElements = "<b>Array elements:</b><br/>";
                String[] items = StringUtils.split(attributeValue, Constants.DELIMITER);
                for (int i = 0; i < items.length; i++) {
                    if (i != 0)
                        arrayElements += "<br/>";
                    arrayElements += items[i];
                }
            }
        }
    }

    // set up data type dropdown list and selected entry
    StringBuffer sbDataTypeOptions = new StringBuffer(300);
    java.util.List list = DConfigDataType.getDataTypeList();
    for (int i = 0; i < list.size(); i++) {
        String alias = (String) list.get(i);
        sbDataTypeOptions.append("<option value=\"").append(alias).append("\"");
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
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DConfigReader / DConfigWriter Demo</title>    
<script language="Javascript">
function createDerbyDemoDb() {
    document.forms[0].submitType.value = "createDerbyDemoDb";
    document.forms[0].submit();
    return false;
}
function loadcache() {
    document.forms[0].submitType.value = "loadCache";
    document.forms[0].submit();
    return false;
}
function readData() {
    document.forms[0].submitType.value = "readData";
    document.forms[0].submit();
    return false;
}
function writeToDB() {
    document.forms[0].submitType.value = "writeToDB";
    document.forms[0].submit();
    return false;
}
function clearFields() {
    document.forms[0].submitType.value = "clearFields";
    document.forms[0].submit();
    return false;
}
function dataTypeChanged(ctrl) {
    var selectedValue = ctrl.value;
    var idx = selectedValue.lastIndexOf("ar");
    var status = getField("status");
    if ((idx > 0) && (idx == selectedValue.length - 2))
        status.innerHTML = "<font color=\"blue\">Use &^#; as the delimiter for array element</font>";
    else
        status.innerHTML = "&nbsp;";
}
function getField(fieldId) {
    return document.getElementById(fieldId);
}
</script>
       
</head>
<body>
<form name="admin" action="<%=request.getContextPath()%>/jsp/dconfigReaderWriter.jsp" method="post">
  <input type="hidden" name="loadCache" />
  <input type="hidden" name="submitType" />
  <p/>
  <p/>
  <p/>
  <table align="center">
        <tr>
            <th >DConfigReader / DConfigWriter JSP Demo</th>
        </tr>
        <tr><td >&nbsp;</td></tr>
        <tr>        
            <td align="center" valign="bottom">
                <input type="button" value="Create Derby Demo Database" onclick="javascript:createDerbyDemoDb()">&nbsp;&nbsp;&nbsp;
                <input type="button" value="Reload DConfig Cache" onclick="javascript:loadcache()">
            </td>
        </tr>
        <tr>
            <td align="center" valign="top">
                <a href="/dconfig-ui-jnlp/dconfig.jnlp">Launch DConfig GUI Editor</a>&nbsp;&nbsp;&nbsp;
                <a href="/dconfig-webdemo" >JSF Web Demo</a>&nbsp;&nbsp;&nbsp;
                <a href="/dconfig-webdemo/dconfigDemo.do" >Struts / yui Demo</a>&nbsp;&nbsp;&nbsp;
                <a href="/dconfig-webdemo/jsp/dconfigReaderWriter.txt" >JSP Source</a>
            </td>
        </tr>
        <tr><td align="center" valign="middle" colspan="2"><span id="status"><%= status %></span></td></tr>
  </table>
  <table align="center" border="1" cellpadding="3" cellspacing="0">
        <tr>
            <td>Key Name:</td><td><input type="text" name="keyName" value="<%= keyName %>" size="30"></td>
            <td>Attribute Name: </td><td><input type="text" name="attributeName" value="<%= attributeName %>" size="30"></td>
        </tr>
        <tr>
            <td>Data Type: </td>
            <td><select name="dataType" onchange="javascript:dataTypeChanged(this);"><%= sbDataTypeOptions.toString() %></select></td>
            <td>Inherited: </td>
            <td><%= inherited %></td>
        </tr>
        <tr>
            <td>Attribute Value: </td>
            <td  colspan="2"><textarea name="attributeValue" rows="3" cols="60"><%= attributeValue %></textarea></td>
            <td align="left" valign="top"><span id="arrayElements"><%= arrayElements %></span></td>
        </tr>
        <tr>
            <td>Comments: </td><td  colspan="2"><textarea name="comments" rows="3" cols="60"><%= comments %></textarea></td><td>&nbsp;</td>
	</tr>
        <tr>
            <td align="center" valign="middle" colspan="4">
                <input type="button" value="Search from Cache" onclick="javascript:readData()">&nbsp;&nbsp;&nbsp;
                <input type="button" value="Write to DB / Cache" onclick="javascript:writeToDB()">&nbsp;&nbsp;&nbsp;
                <input type="button" value="Clear Fields" onclick="javascript:clearFields()">
            </td>
        </tr>
  </table>
</form>
</body>
</html>
