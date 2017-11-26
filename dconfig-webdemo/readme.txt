
                  Dynamic Configuration Toolkit Web Demo

                            Jonathan Luo

                        Last update: 10/24/2007

            http://dconfig.sourceforge.net/dconfig/webdemo.php

1. Prerequisites

    JDK - Dynamic Configuration Web Demo needs Java SE 5.0,
which can be downloaded from http://java.sun.com/javase/downloads/index.jsp.

    Web Server - Apache Tomcat 5.* or later or Servlet 2.4+/JSP 2.0+ compliant 
web servers. However, this document is written for Apache Tomcat web server. If 
you're using a different web server, please consult related document to see how 
to deploy a war file to your web server.

    Apache Tomcat can be obtained from http://tomcat.apache.org/.


2. Start Derby Database Server

    Change to directory /path/to/unzipped/directory/.
    Change to directory dconfig-db/src/script.
run 
    $ chmod u+x *.sh
    $ ./derby-startup.sh  (Linux)
or 
    derby-startup.bat   (Windows)

[dconfig-db/src/script]$ ./derby-startup.sh
java -cp ../../lib/derby.jar:../../derbynet.jar 
org.apache.derby.drda.NetworkServerControl start -h localhost -p 1527
Server is ready to accept connections on port 1527.


3. Deploy Web Demo War File to Apache Tomcat 5.0.* or later

    Copy /path/to/unzipped/directory/dconfig-webdemo/dconfig-webdemo.war to /path/to/tomcat-home/webapps
    Copy /path/to/unzipped/directory/dconfig-ui-jnlp/dconfig-ui-jnlp.war to /path/to/tomcat-home/webapps

4. Startup Tomcat

Run
    /path/to/tomcat-home/bin/startup.sh (Linux)
or
    \path\to\tomcat-home\bin\startup.bat (Windows)


5. Run the Web Demo

5.1 Points browser to 

    http://localhost:8080/dconfig-webdemo/jsp/dconfigReaderWriter.jsp to start the
    plain JSP DConfigReader / DConfigWriter demo.

    or
    http://localhost:8080/dconfig-webdemo/ to start the JSF demo.

    where 8080 is the default port number.

    The JSP DConfigReader / DConfigWriter demo is straightforward. You can always look at
    http://dconfig.sourceforge.net/dconfig/webdemo.php#run-jsp-webdemo for more details, especially
    there are some examples, screen shots over there.

    The following descriptions are for the JSF demo:

    When the page shows up, click on the Database tab (if not already is),
click on the Create Derby Demo Database button to create a demo database.

    You should see Derby database started successfully message shortly.
    
    Note:
	IE under Windows may have difficult to view the dconfig-webdemo example.
	Typical error message are:
    
    The XML page cannot be displayed
    Cannot view XML input using XSL style sheet. Please correct the error and then click the Refresh button, or try again later.
    Cannot have a DOCTYPE declaration outside of a prolog. Error processing resource 'http://localhost:8080/dconfig-webdemo/'. ...

    If you experience similar IE issue, you may try to use Firefox browser to view the webdemo example.    
    Firefox browser can be downloaded from http://www.mozilla.com/en-US/firefox/.

5.2 Select Search Tab

    Click Sample Query link, then click Execute Query button.
You should results table is displayed under Results Tab.
The results table is displayed as 10 rows, 6 columns per page.

5.3 Launch DConfig Editor

    Select DConfig UI - JNLP tab.

    Click the launch link or launch button, at the Opening dconfig.jnlp popup box 
(if jnlp file type has not been linked to javaws), select Open with Browse.... 
From the file chooser, select javaws from JRE_HOME/javaws directory or 
JAVA_HOME/jre/javaws/javaws, click Open, OK buttons to continue.

    Since the certificate is not issued from Certification Authorities, such as 
VeriSign, Thawte, Entrust, etc., a Wainging - Security popup box will be shown
up, click the Run button to proceed. The DConfig Editor should be launched 
shortly once all the downloads are completed.

    From DConfig Editor, select Tools | Driver Manager... to configure the JDBC
drivers.

    Click JavaDB/Derby server in the Driver Name list. Click the folder icon
in the end of Driver File Path, select jar file
/path/to/unzipped/directory/dconfig-db/lib/derbyclient.jar. Close dialog.


    Then select Tools | DB Connection... to configure a connection. 
    Click Add a new connection button, enter or select the following info:

    Alias:          derby-dcfgdemo
    JDBC Driver:    JavaDB/Derby server
    Database URL:   jdbc:derby://localhost:1527/dcfgdemo;create=true
    User Name:      app
    Password:       app

    Check both Save Password and Set as default connnection
    Click Save Changes button.
    Click top-right X to close the dialog.

    Click Connection to default database or open connection wizard toolbar
button (the third button to the right), the DConfig Editor should connect the
demo Derby database with tree populated.

5.4 Change Attribute Values

    Expands to config.view.demo.page1 tree node from the DConfig Editor, click 
the Update / View mode toggle button, the last one on the toolbar, to switch the 
Editor to the update mode.

    Click value column of the '#Rows per page' attribute, change the value to 8;
    Click value column of the 'tab names' attribute, change 'Search' to 'My Search';
    Click value column of the 'visible columns' attribute, change data from
    1, 2, 3, 4, 5, 6 to 1, 2, 3, 4, 5

    Click the toolbar Save button.


5.5 Reload Dynamic Configuration Data

    Select Refresh tab.
    Click Reload DConfig Cache link.


5.6 View the changes on the fly

    You may have notice that the 4th tab name has changed from Search to My 
Search.
    Select Results tab. The table now display as 8 rows, 5 columns per page.

    You may repeat steps 4.4 to 4.6 to adjust the mentioned attribute values to
see the dynamic feature offered by Dynamic Configuration Toolkit.


6. Shutdown Tomcat

    /path/to/tomcat-home/bin/shutdown.sh (Linux)
or
    \path\to\tomcat-home\bin\shutdown.bat (Windows)


7. Shutdown Derby Database Server

    Change to directory /path/to/unzipped/directory/.
    Change to directory dconfig-db/src/script.
run 
    ./derby-shutdown.sh  (Linux)
or 
    derby-shutdown.bat   (Windows)

[dconfig-db/src/script]$ ./derby-shutdown.sh
java -cp ../../lib/derby.jar:../../lib/derbynet.jar 
org.apache.derby.drda.NetworkServerControl shutdown -h localhost -p 1527
Shutdown successful.

    A directory called dcfgdemo is generated by Derby database server, you can 
safely remove the directory. If you don't remove it, start Derby server again, 
dcfgdemo database will be loaded automatically next time.


Appendix:

JDBC Drivers

In order to make DConfig Editor and DConfig Library connect to databases, JDBC
drivers are needed for different databases. Below shows databases currently 
supported by DConfig Toolkit and the URLs for downloading the JDBC drivers.


Database        JDBC driver
or Driver Type  Download URL
----------------------------------------------------------------------------
MySQL           http://dev.mysql.com/downloads/connector/j/5.0.html
Postgres        http://jdbc.postgresql.org/download.html
Derby server    http://db.apache.org/derby/derby_downloads.html
Hsqldb server   http://sourceforge.net/project/showfiles.php?group_id=23316
Oracle Thin/OCI http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/index.html
SQL Server 2000 
(jTDS)          http://jtds.sourceforge.net/
SQL Server 2000, 
2005(Microsoft) http://msdn.microsoft.com/data/ref/jdbc/
