<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
    <%--
    This example uses JSTL, uncomment the taglib directive above.
    To test, display the page like this: index.jsp?sayHello=true&name=Murphy
    --%>
    <%--
    <c:if test="${param.sayHello}">
        <!-- Let's welcome the user ${param.name} -->
        Hello ${param.name}!
    </c:if>
    --%>

<div class="exampleIntro">
    <font style="font-size: 24; font-style: bold">DConfig Struts / yui Demo</font>
</div>

<!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->

<div id="demo" class="yui-navset">
    <ul class="yui-nav">
        <li class="selected"><a href="#tab1"><em>Database</em></a></li>
        <li><a href="#tab2"><em>DConfig ui-jnlp</em></a></li>
        <li><a href="#tab2"><em>DConfigReader/Writer</em></a></li>
    </ul>            
    <div class="yui-content">
        <div id="tab1">
            <jsp:include page="inc-dconfigDatabase.jsp" />
        </div>
        <div id="tab2">
            <br/>
            <br/>
            <br/>
            <a href="/dconfig-ui-jnlp/dconfig.jnlp">Launch DConfig GUI Editor</a>
            <br/>
            <br/>
            <br/>
        </div>
        <div id="tab3">
            <jsp:include page="inc-dconfigReaderWriter.jsp" />
        </div>
    </div>
    <div id="divMessage"></div>
</div>
<script>
    var tabView = new YAHOO.widget.TabView('demo');
</script>
