<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

   <style type="text/css">
       
#scrollBox{
max-height: 300px;
max-width: 650px;
border: 1px solid;
padding: 5px;
overflow: auto;
}
       
   </style>
   
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>

    <body>
    <h1>Launch Dynamic Config Editor</h1>
    <a href="dconfig.jnlp"><img border=0 src="webstart_small.gif">Launch from Local Web Server</a><br/>
    <a href="/dconfig-ui-jnlp/dconfig.jnlp"><img border=0 src="webstart_small.gif">Launch from Remote Web Server</a><br/>

    <div id="scrollBox">
<pre>
The following back-bean code will be executed after the above button is clicked:

import org.moonwave.dconfig.*;

    public String btnCreateDemoDB_action() {
        LibInitializer.initialize();
        AppState.setDemo(true);
        AppState.setDerby(true);
        DemoDbManager.startup();
        if (new DemoDataPopulator().populate()) {
            log.info("run DemoDataPopulator().populate() ok");
            setStatus("Derby database started successfully.");
            // copied from InitServlet
            CacheManager.load();
            //DConfigTimerTask.start();
        } else {
            log.error("run DemoDataPopulator().populate() failed");                
            setStatus("Failed to create Derby database. Check log file for details.");
        }
        return null;
    }
        
</pre>        
        </div>
    
</body>
</html>
