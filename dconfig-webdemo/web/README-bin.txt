a
Dynamic Configuration Toolkit
=============================

    Dynamic Configuration Toolkit is a general purpose dynamic configuration 
toolkit. It provides a unified way to manage configuration data through sever 
side libraries, the DConfig Library, and a Swing based front-end, the DConfig 
Editor. 

1. Set up the database
-----------------------------

    Dynamic Configuration Toolkit uses RDBMS to save the configuration data.

    If you're a new user for Dynamic Configuration Toolkit, you may go to step 2 
Run the samples to see how it works.

    Sample db scripts (*.sql) located under sql. It begins with database name 
and followed by db version tested. For example, derby_10.1.3.1.sql means the
scripts has been executed against Derby v.10.1.3.1. If your db version is 
different from the version supported or if you couldn't find the scripts for
your database, you may need to change the scripts in order to work.

    Addtional scripts for other databases will be included in the futher releases.

    Currently there are three tables in the DConfig toolkit.

    Table name          Comments
    ----------------------------------------------------------------------------
    dconfig_datatype    saves the data types supported by DConfig toolkit
    dconfig_key         saves keys
    dconfig_attribute   saves the attributes for keys

    A configuration entry consists of a dot-separated key and a set of attributes.

    Keys like app, db.datasource, config.view are valid. Key does not allow '/', 
other printable characters and numbers are allowed.

    An attribute has attribute name, data type, attribute value and comments.

    Currently supported data types include:

    Boolean        
    Boolean Array  
    Integer        
    Integer Array  
    Long           
    Long Array     
    String         
    String Array   
    Float          
    Float Array    
    Double         
    Double Array   
    Datetime       
    Datetime array 


    There is no need to create DConfig tables on a separate database, it is 
recommended that to create these tables in the same application database that an 
application intend to use the Dynamic Configuration feature.

2. Run the samples
-----------------------------

    The Dynamic Configuration Toolkit editor needs Java Runtime Environment (JRE) 5 
or later. The library module needs JRE 1.3 or later. Java SE can be downloaded at

http://java.sun.com/javase/downloads/index.jsp

2.1 Run the DConfig Editor

    There are few ways to run the GUI Editor.

2.1.1 Using Command Line

    At the project root, type 'ant ui' to compile and package the GUI modules.
Switch to dist/ui sub-folder under project root. Issue the following commands:

command                             tasks
--------------------------------------------------------------------------------
java -jar dconfig.jar               Run DConfig GUI Editor (need database setup)
java -jar dconfig.jar demo derby    Run DConfig GUI Editor as demo mode with embedded 
                                    Derby database. No database setup is needed.
java -jar dconfig.jar demo hsqldb   Run DConfig GUI Editor as demo mode with embedded
                                    Hsqldb database. No database setup is needed.

    An optional command line argument verbose can be issued when running as demo 
mode. For example,

java -jar dconfig.jar demo derby verbose
java -jar dconfig.jar demo hsqldb verbose

    Additional information will be shown on the command console while the Editor 
is running.

    There is a file test/demo-import.xml, which can be used for import test. The 
data will be imported under node admin.applications.


2.1.2 Launch DConfig Editior from the Web using JNLP

    This requires Java web servers in order to use this feature. This is the
recommended way to distribute the desktop DConfig Editor through network.

    The following example shows how to set up Apache Tomcat. Apache Tomcat can be 
obtained from http://tomcat.apache.org/.

    copy dconfig.war to {tomcat.home}/webapps

    where {tomcat.home} stands for tomcat installed home.

    start tomcat web server

    {tomcat.home}/bin/startup.sh (Unix, Linux) 
    or
    {tomcat.home}/bin/startup.bat (Windows)


    Points a web browser to http://localhost:8080/dconfig/index.jsp or 
http://localhost:8080/dconfig.


    Click the launch link or launch button, at the Opening dconfig.jnlp popup box (if 
jnlp file type has not been linked to javaws), select Open with Browse.... From
the file chooser, select javaws from JRE_HOME/javaws directory, click Open, OK
buttons to continue.

    Since the certificate is not issued from Certification Authorities, such as 
VeriSign, Thawte, Entrust, etc., a Wainging - Security popup box will be shown
up, click the run button to proceed. The DConfig Editor should be launched 
shortly once all the downloads are completed.

    From DConfig Editor, select Tools Driver Manager... to configure the JDBC
drivers. Then select Tools DB Connection... to configure a connection. Once a
connection is setup, click Connection button to connect the database. 
See 5. JDBC Drivers on how to get JDBC drivers.


    Tomcat web server can be shut downed using the following command.

    {tomcat.home}/bin/shutdown.sh (Unix, Linux) 
    or
    {tomcat.home}/bin/shutdown.bat (Windows)


2.2 Use Dynamic Configuration Toolkit library

    DConfigReader is the core class of the Dynamic Configuration Toolkit library.
DConfigReader locates in org.moonwave.dconfig.dao package. Please see the
javadoc under docs/javadoc for more details.

    The library has built-in caching mechanism and has auto-refreshing feature
based on timer period setup in src/conf/lib/dconfig_lib.properties.

    The library caches can be refreshed on demand by calling 
org.moonwave.dconfig.dao.CacheManager.load().

    A sample page to reload library caches on demand has been included in
'ant web' build.

    Use the same step described in 4.1.3 Running from the Web using JNLP
to deploy the sample war file to tomcat. To reiterate, run

ant deploy

to perform the deployment.

The sample page's URL is http://uoponljxluo2:8080/dconfig/jsp/admin.jsp.


3. JDBC Drivers

In order to make DConfig Editor and DConfig Library connect to databases, JDBC
drivers are needed for different databases. Below shows databases currently 
supported by DConfig Toolkit and the URLs for downloading the JDBC drivers.


Database        JDBC driver
or Driver Type  Download URL
----------------------------------------------------------------------------
MySQL           http://dev.mysql.com/downloads/connector/j/5.0.html
Postgres        http://jdbc.postgresql.org/download.html
Derby server    http://db.apache.org/derby/derby_downloads.html
Hsqldb server   http://db.apache.org/derby/derby_downloads.html
Oracle Thin/OCI http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/index.html
SQL Server 2000 
(jTDS)          http://jtds.sourceforge.net/
SQL Server 2000, 
2005(Microsoft) http://msdn.microsoft.com/data/ref/jdbc/

src/con/lib/dconfig_lib.properties has more details on JDBC driver
class name and db url.


4. Configure Web Server to use Dynamic Configuration Toolkit

4.1 Place the following lines into web.xml file

    <servlet>
        <servlet-name>JnlpDownloadServlet</servlet-name>
        <servlet-class>com.sun.javaws.servlet.JnlpDownloadServlet</servlet-class>   
    </servlet>

    <servlet>
        <servlet-name>InitServlet</servlet-name>
        <servlet-class>org.moonwave.dconfig.servlet.InitServlet</servlet-class>
        <load-on-startup>5</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>JnlpDownloadServlet</servlet-name>
        <url-pattern>*.jnlp</url-pattern>
    </servlet-mapping>
    
    <servlet-mapping>
        <servlet-name>JnlpDownloadServlet</servlet-name>
        <url-pattern>*.jar</url-pattern>
    </servlet-mapping>

4.2 Configure connection and cache loader

    Set up active database connection information in lib/conf/dconfig_lib.properties
file. 

active.connection.prefix=<active connection lookup key>

For example, 
active.connection.prefix=hsqldbdemo
active.connection.prefix=mysql


# ------------------------------------------------------------------------------
# next cache loader execeution period in minutes. default is 5  minutes.
# A value of <= 0 turns off the automatical cache loading feature.
dconfig.cacheloader.taskperiod=5

