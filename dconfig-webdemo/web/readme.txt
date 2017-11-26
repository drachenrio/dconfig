
Dynamic Configuration Toolkit
=============================

    Dynamic Configuration Toolkit is a general purpose dynamic configuration 
toolkit. It provides a unified way to manage configuration data through sever 
side libraries, the DConfig Library, and a Swing based front-end, the DConfig 
Editor. 


1. ANT Build Tool
-----------------------------

    In order to compile the project or to run the sample applications provided 
with this distribution, you need to have the ANT build tool installed on your system. 
    
    You can get a copy of this tool and details about how to use it at this 
address: http://ant.apache.org/

    There are several ANT specific tasks inside the build.xml file. Each task has 
a description explaining what it does and the list of all available tasks inside 
the build.xml file can be obtained by launching "ant -p" from the command line.


2. Compile the source files
-----------------------------

    In the project's root directory there is a build.xml file that exposes 
different targets and helps compiling the Java source files, the documentation 
or build the JAR files.

command         tasks
--------------------------------------------------------------------------------
ant ui          Build the client GUI editor
ant lib         Build the server side library (can be used on client side as well)
ant             Default task is web-demo
ant web         Package library and ui modules        
ant web-demo    Create web demo war file
ant deploy      Deploy web-demo war file to local Java web server specified by
                deploy.home in the local_build.properties file.

    To build the ui package, you need Java SE 5 JDK or later. Java SE can be 
downloaded at http://java.sun.com/javase/downloads/index.jsp.


3. Set up the database
-----------------------------

    Dynamic Configuration Toolkit uses RDBMS to save the configuration data.

    If you're a new user for Dynamic Configuration Toolkit, you may go to step 4 
Run the samples to see how it works.

    Sample db scripts (*.sql) located under src/conf. It begins with database name 
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

4. Run the samples
-----------------------------

    The Dynamic Configuration Toolkit editor needs Java Runtime Environment (JRE) 5 
or later. The library module needs JRE 1.3 or later. Java SE can be downloaded at

http://java.sun.com/javase/downloads/index.jsp

4.1 Run the DConfig Editor

    There are few ways to run the GUI Editor.

4.1.1 Using ANT

    At the project root, use ant commands to run the GUI Editor:

command         tasks
--------------------------------------------------------------------------------
ant run         Run DConfig GUI Editor (need database setup)
ant run-derby   Run DConfig GUI Editor as demo mode with embedded Derby database.
                No database setup is needed. Database 'dcfgDemo' is created after
                the run. Any changes will be saved in the db. You can safely 
                remove the whole directory after the demo run.
ant run-hsqldb  Run the GUI Editor as demo mode with embedded Hsqldb database
                No database setup is needed.

4.1.2 Using Command Line

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


4.1.3 Launch DConfig Editior from the Web using JNLP

    This requires Java web servers in order to use this feature. This is the
recommended way to distribute the desktop DConfig Editor through network.

    The following example shows how to set up Apache Tomcat. Apache Tomcat can be 
obtained from http://tomcat.apache.org/.


    Change the values of following entries in the local_build.properties file.

# tomcat web server home directory
tomcat.home=/home/jonathan/local/ide/netbeans-5.5/enterprise3/apache-tomcat-5.5.17

# deploy home; replace deploy home here. You can put full path directly here
# without creating tomcat.home, jboss.home, geronimo.home, etc.
deploy.home=${tomcat.home}/webapps

    Issue the following command from a command console,

    ant deploy

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


4.2 Use Dynamic Configuration Toolkit library

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


5. JDBC Drivers

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


6. Configure Web Server to use Dynamic Configuration Toolkit

6.1 Place the following lines into web.xml file

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

6.2 Configure connection and cache loader

    Set up active database connection information in src/conf/dconfig_lib.properties
file. 

active.connection.prefix=<active connection lookup key>

For example, 
active.connection.prefix=hsqldbdemo
active.connection.prefix=mysql


# ------------------------------------------------------------------------------
# next cache loader execeution period in minutes. default is 5  minutes.
# A value of <= 0 turns off the automatical cache loading feature.
dconfig.cacheloader.taskperiod=5

6.3 WAR file directory structure
    
    Run 'ant web' to create a sample war file directory structure with Dynamic 
Configuration Toolkit GUI modules and libraries and third part libraries are 
placed on the proper directories.

    Basically, ui components dconfig.jar, dconfig.jnlp, and its lib files are
placed under context root. DConfig library file dconfig-lib.jar and its supporting
libries are placed under WEB-INF/lib. DConfig library property files should be
placed under WEB-INF/classes.

