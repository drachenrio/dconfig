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
 *
 * @author Jonathan Luo
 */
%>
<%@ page import="org.apache.commons.logging.LogFactory,
                 org.apache.commons.logging.Log"%>

<%@ taglib prefix="tiles" uri="/WEB-INF/struts-tiles.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% Log log = LogFactory.getLog("jsp");    %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" xmlns:iam="http://www.duml.org/iaml">
<head>
    <meta http-equiv="Content-Language" content="en-us">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="no-store">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="must-revalidate">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="max-age=0">
    <META HTTP-EQUIV="EXPIRES" CONTENT="0">

    <title><tiles:getAsString name="title"/></title>
<style type="text/css">
/*margin and padding on body element
  can introduce errors in determining
  element position and are not recommended;
  we turn them off as a foundation for YUI
  CSS treatments. */
body {
	margin:0;
	padding:0;
}
</style>       

<style type="text/css">
/* custom styles for multiple stacked instances with custom formatting */
#example0 { z-index:9001; } /* z-index needed on top instances for ie & sf absolute inside relative issue */
#example1 { z-index:9000; } /* z-index needed on top instances for ie & sf absolute inside relative issue */
.autocomplete { padding-bottom:2em;width:40%; }/* set width of widget here*/
.autocomplete .yui-ac-highlight .sample-quantity,
.autocomplete .yui-ac-highlight .sample-result,
.autocomplete .yui-ac-highlight .sample-query { color:#FFF; }
.autocomplete .sample-quantity { float:right; } /* push right */
.autocomplete .sample-result { color:#A4A4A4; }
.autocomplete .sample-query { color:#000; }
</style>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/yui/fonts/fonts-min.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/yui/tabview/assets/skins/sam/tabview.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/yui/autocomplete/assets/skins/sam/autocomplete.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/yui/utilities/utilities.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/element/element-beta.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/tabview/tabview.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/json/json.js"></script>

</head>

<% try { %>
<body bgcolor="#FFFFFF" class="yui-skin-sam">
<table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0">
    <tr valign="top">
        <td height="10px">
            <!-- Header Starts Here -->
            <tiles:insert attribute="header" />
            <!-- Header Ends Here -->
        </td>
    </tr>
    <tr valign="top" align="center">
        <td valign="top" align="center" style="padding: 2px;" height="10px">
            <!-- Menu Starts Here -->
            <tiles:insert attribute="menu" />
            <!-- Menu Ends Here -->
        </td>
    </tr>
    <tr valign="top" align="center">
        <td valign="top" align="center" style="padding: 2px;" height="10px">
            <!-- Message Starts Here -->
            <tiles:insert attribute="message" />
            <!-- Message Ends Here -->
        </td>
    </tr>
    <tr valign="top">
        <td valign="top" align="center">
            <!-- Body Starts Here -->
            <tiles:insert attribute='body' />
            <!-- Body Ends Here -->
        </td>
    </tr>
    <tr valign="bottom">
        <td height="5%">
            <!-- Footer Starts Here -->
            <tiles:insert attribute="footer" />
            <!-- Footer Ends Here -->
        </td>
    </tr>
</table>
</body>
<% } catch (Throwable e) {
	log.error("Error in Jsp: ", e);
   } finally {
   }
%>
</html>
