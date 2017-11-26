
Dynamic Configuration Toolkit
=============================

    Dynamic Configuration Toolkit (http://dconfig.sourceforge.net) is a general 
purpose configuration toolkit with dynamic features. It provides a simple, unified
mechanism to manage configuration data through sever side libraries, the DConfig 
Library, and a Swing based front-end, the DConfig Editor. 


1. Prerequisites
-----------------------------

1.1 ANT Build Tool

    In order to compile the project or to run the sample applications provided 
with this distribution, you need to have the ANT build tool 1.6.5 or later 
installed on your system. 

    You can get a copy of this tool and details about how to use it at this 
address: http://ant.apache.org/

    There are several ANT specific tasks inside the build.xml file. Each task has 
a description explaining what it does and the list of all available tasks inside 
the build.xml file can be obtained by launching "ant -p" from the command line.


1.2 JDK

    Dynamic Configuration Toolkit needs Java SE 5.0, which can be downloaded from
http://java.sun.com/javase/downloads/index.jsp.


2. Compile the source files
-----------------------------

    In the project's root directory there is a build.xml file that exposes 
different targets and helps compiling the Java source files, the documentation 
or build the JAR files.

command         tasks
--------------------------------------------------------------------------------
ant lib             Build DConfig library (Java Source level 1.3)

ant ui              Build DConfig GUI Editor (Java Source level 1.5)

ant ui-jnlp         Create DConfig GUI Editor JNLP web-launch war file

ant webdemo         Create web demo with JSF, Derby, and JNLP web launch

ant javadoc-lib     Create DConfig lib javadoc

ant javadoc-ui      Create DConfig ui javadoc

ant javadoc         Call javadoc-lib, javadoc-ui

ant                 default build, which includes all the above

ant deploy          Deploy dconfig-webdemo.war, dconfig-ui-jnlp.war to local Java
                    web server specified by deploy.home in the local_build.properties file.


3. Set up the database
-----------------------------

    Dynamic Configuration Toolkit uses RDBMS to save the configuration data.

    If you're a new to Dynamic Configuration Toolkit, you may go to step 4 
Run the demo to see how it works.

    Sample db scripts (*.sql) located under dconfig-db/src/sql/1.0. It begins 
with database name and followed by db version tested.   For example, 
derby_10.1.3.1.sql means the scripts has been executed against Derby v.10.1.3.1.
If your db version is different from the version supported or if you couldn't 
find the scripts for your database, you may need to change the scripts in order 
to work.

    Addtional scripts for other databases will be included in the future releases.

    Currently ther eare three tables in the DConfig toolkit.

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

    Please look at http://dconfig.sourceforge.net/dconfig/database-setup.php for
additional information on database setup.

4. Run the Demo
-----------------------------

4.1 Run the DConfig Editor

    There are few ways to run the GUI Editor.

4.1.1 Using ANT

    At the project root, use ant commands to run the GUI Editor:

command         tasks
--------------------------------------------------------------------------------
ant run         Run DConfig GUI Editor (need database setup, see step 3, 
                Set up the database)

ant run-derby   Run DConfig GUI Editor as demo mode with embedded Derby database.
                No database setup is needed. Database 'dcfgDemo' is created after
                the run. Any changes will be saved in the db. You can safely 
                remove the whole directory after the demo run.

ant run-hsqldb  Run the GUI Editor as demo mode with embedded Hsqldb database
                No database setup is needed.

ant run-h2      Run the GUI Editor as demo mode with embedded H2 database
                No database setup is needed.

Please look at http://dconfig.sourceforge.net/dconfig/dconfig-editor.php
on how to use DConfig GUI Editor.

4.1.2 Using Command Line

    Open a terminal or command window, at the project root, type 'ant ui' to 
compile and package the GUI modules.

    Switch to dist/dconfig-ui sub-folder under project root, execute the following
commands:

For Linux OS,
chmod u+x *.sh


Command                        Tasks
--------------------------------------------------------------------------------
./dconfig-ui.sh demo derby     Run DConfig GUI Editor as demo mode with embedded
                               Derby database. No database setup is needed.

./dconfig-ui.sh demo hsqldb    Run DConfig GUI Editor as demo mode with embedded 
                               Hsqldb database. No database setup is needed.

./dconfig-ui.sh demo h2        Run DConfig GUI Editor as demo mode with embedded 
                               H2 database. No database setup is needed.

./dconfig-ui.sh                Run DConfig GUI Editor (need database setup,
                               see step 3, Set up the database)

For Windows,


Command                        Tasks
--------------------------------------------------------------------------------
dconfig-ui.bat demo derby      Run DConfig GUI Editor as demo mode with embedded
                               Derby database. No database setup is needed.

dconfig-ui.bat demo hsqldb     Run DConfig GUI Editor as demo mode with embedded 
                               Hsqldb database. No database setup is needed.

dconfig-ui.bat demo h2         Run DConfig GUI Editor as demo mode with embedded 
                               H2 database. No database setup is needed.

dconfig-ui.bat                 Run DConfig GUI Editor (need database setup,
                               see step 3, Set up the database)


    An optional command line argument verbose can be issued when running as demo 
mode. For example,

For Linux,

./dconfig-ui.sh demo derby verbose
./dconfig-ui.sh demo hsqldb verbose
./dconfig-ui.sh demo h2 verbose

For Windows,

dconfig-ui.bat demo derby verbose
dconfig-ui.bat demo hsqldb verbose
dconfig-ui.bat demo h2 verbose

    Additional information will be shown on the command console while the Editor 
is running.

    There is a file dconfig-webdemo/test/demo-import.xml, which can be used for 
import test. The data will be imported under node admin.applications.


4.1.3 Launch DConfig Editior from the Web using JNLP

    This requires Java web servers in order to use this feature. This is the
recommended way to distribute the desktop DConfig Editor through network.

    The following example shows how to set up Apache Tomcat. Apache Tomcat can be 
obtained from http://tomcat.apache.org/.

    Please look at http://dconfig.sourceforge.net/dconfig/webdemo.php for a quick
webdemo setup.

    Change the values of following entries in the build.properties file.

# tomcat web server home directory
tomcat.home=/home/jonathan/local/ide/netbeans-5.5/enterprise3/apache-tomcat-5.5.17

# deploy home; replace deploy home here. You can put full path directly here
# without creating tomcat.home, jboss.home, geronimo.home, etc.
deploy.home=${tomcat.home}/webapps

    Issue the following command from a command console,

    ant deploy

    Now start tomcat web server

    {tomcat.home}/bin/startup.sh (Unix, Linux) 
    or
    {tomcat.home}/bin/startup.bat (Windows)

    Points a web browser to http://localhost:8080/dconfig-webdemo

    Please read dist/dconfig-webdemo/readme.txt on how to run the web demo.

    Tomcat web server can be shut downed using the following command.

    {tomcat.home}/bin/shutdown.sh (Unix, Linux) 
    or
    {tomcat.home}/bin/shutdown.bat (Windows)


4.2 Use Dynamic Configuration Toolkit library

    DConfigReader is the core class of the Dynamic Configuration Toolkit library.
DConfigReader locates in org.moonwave.dconfig.dao package. Please see the
javadoc under dist/docs/javadoc/dconfig-lib/index.html for more details.

    The library has built-in caching mechanism and has auto-refreshing feature
based on timer period setup in dconfig-lib/src/java/conf/dconfig_lib.properties.

    The library caches can be refreshed on demand by calling 
org.moonwave.dconfig.dao.CacheManager.load().


5. JDBC Drivers
-----------------------------

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

src/con/lib/dconfig_lib.properties has more details on JDBC driver
class name and db url.


6. Configure Web Server to use Dynamic Configuration Toolkit
------------------------------------------------------------

6.1 Place the following lines into web.xml file

    <servlet>
        <servlet-name>InitServlet</servlet-name>
        <servlet-class>org.moonwave.dconfig.servlet.InitServlet</servlet-class>
        <load-on-startup>5</load-on-startup>
    </servlet>

6.2 Configure connection and cache loader

    Set up active database connection information in 
dconfig-lib/src/java/conf/dconfig_lib.properties file.

active.connection.prefix=<active connection lookup key>

For example, 
active.connection.prefix=hsqldbdemo
active.connection.prefix=mysql


# ------------------------------------------------------------------------------
# next cache loader execeution period in minutes. default is 5  minutes.
# A value of <= 0 turns off the automatical cache loading feature.
dconfig.cacheloader.taskperiod=5

6.3 dconfig-ui-jnlp.war directory structure

Run 'ant ui-jnlp' to create a dconfig-ui-jnlp.war file. After extract the war file, you will 
see the following directory structure.

    Directory           Files                       Comments
    ----------------------------------------------------------------------------

                        dconfig.jnlp
                        dconfig-ui.jar              DConfig Editor jar file
                        index.jsp                   Sample jsp to launch EConfig Editor
                        webstart_small.gif

    lib                                             Files under lib are used by DConfig UI Editor,
                                                    will be downloaded automatically through jnlp (web-start)
                        commons-dbutils-1.0.jar
                        commons-lang.jar
                        commons-logging-api.jar
                        commons-logging.jar
                        dconfig-lib.jar
                        dom4j-1.6.1.jar
                        jaxen-1.1-beta-6.jar
                        jaxme-api-0.3.jar
                        jsr173_1.0_api.jar
                        LICENSE.apache.txt
                        LICENSE.cddl.txt
                        LICENSE.commons.txt
                        LICENSE.dom4j.txt
                        LICENSE.gpl.txt
                        LICENSE.hsqldb.txt
                        LICENSE.jfreechart.txt
                        LICENSE.xalan.txt
                        LICENSE.xerces.txt
                        log4j-1.2.8.jar
                        msv-20030807.jar
                        pull-parser-2.1.10.jar
                        relaxngDatatype-20030807.jar
                        spring-beans.jar
                        spring-core.jar
                        spring-dao.jar
                        spring-jdbc.jar
                        swing-layout-1.0.jar
                        xpp3-1.1.3.3.jar
                        xsdlib-20030807.jar

    WEB-INF
                        web.xml                     DConfig jnlp download set up

    WEB-INF/lib
                        jnlp-servlet.jar            jnlp servlet

You can also get the directory structure after deploy dconfig-ui-jnlp.war to tomcat web server, 
simple go to /webapps/dconfig-ui-jnlp folder. 
