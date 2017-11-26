<% 
/**
 * Autocomplete 'web service' jsp page
 *
 * This page serves as a template for returning data to client
 */
 
StringBuffer sb = new StringBuffer();
sb.append("123").append("\t").append("a").append("\t").append("a123").append("\n");
sb.append("456").append("\t").append("b").append("\t").append("b456").append("\n");
sb.append("789").append("\t").append("c").append("\t").append("c789").append("\n");
sb.append("0ab").append("\t").append("d").append("\t").append("d0ab").append("\n");
sb.append("From_ws_test.jsp").append("\t").append("From_ws_test.jsp").append("\t").append("From_ws_test.jsp").append("\n");

sb.append("From_ws_test.jsp").append("\t").append("keyNameParam=").append("\t").append( request.getParameter("keyNameParam")).append("\n");
sb.append("From_ws_test.jsp-2").append("\t").append("ysearchinput0=").append("\t").append( request.getParameter("ysearchinput0")).append("\n");
sb.append("From_ws_test.jsp-3").append("\t").append("query=").append("\t").append( request.getParameter("query")).append("\n");
sb.append("From_ws_test.jsp-4").append("\t").append("keyName=").append("\t").append( request.getParameter("keyName")).append("\n");

        
String responseData = sb.toString();

%>
<%=responseData%>
