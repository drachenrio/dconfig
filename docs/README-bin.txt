
Dynamic Configuration Toolkit
=============================

    Dynamic Configuration Toolkit (http://dconfig.sourceforge.net) is a general 
purpose configuration toolkit with dynamic features. It provides a simple, unified
mechanism to manage configuration data through sever side libraries, the DConfig 
Library, and a Swing based front-end, the DConfig Editor. 

1. Prerequisites
-----------------------------

    Dynamic Configuration Toolkit needs Java SE 5.0, which can be downloaded from
http://java.sun.com/javase/downloads/index.jsp.


2. Set up the database
-----------------------------

    Dynamic Configuration Toolkit uses RDBMS to save the configuration data.

    If you're a new to Dynamic Configuration Toolkit, you may go to step 3
Run the demo to see how it works.

    Sample db scripts (*.sql) located under dconfig-db/src/sql/1.0. It begins 
with database name and followed by db version tested.   For example, 
derby_10.1.3.1.sql means the scripts has been executed against Derby v.10.1.3.1.
If your db version is different from the version supported or if you couldn't 
find the scripts for your database, you may need to change the scripts in order 
to work.

    Addtional scripts for other databases will be included in the future releases.

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

    Please look at http://dconfig.sourceforge.net/dconfig/database-setup.php for
additional information on database setup.


3. Run the Demo
-----------------------------

3.1 Run the DConfig Editor

3.1.1 Using Command Line

    Open a terminal or command window, at the project root, switch to dconfig-ui sub-folder. 

For Linux,
cd /path/to/unzipped/folder
cd dconfig-ui
chmod u+x *.sh

Issue the following commands to start dconfig gui editor,

command                                 tasks
---------------------------------------------------------------------------------------------------
./dconfig-ui.sh demo derby  Run DConfig GUI Editor as demo mode with embedded Derby database.
                                No database setup is needed.

./dconfig-ui.sh demo hsqldb Run DConfig GUI Editor as demo mode with embedded Hsqldb database.
                                No database setup is needed.

./dconfig-ui.sh demo h2     Run DConfig GUI Editor as demo mode with embedded H2 database.
                                No database setup is needed.

./dconfig-ui.sh             Run DConfig GUI Editor (need database setup,
                                see Database Setup section)


For Windows,
cd \path\to\unzipped\folder
cd dconfig-ui

Issue the following commands to start dconfig gui editor,

command                                 tasks
---------------------------------------------------------------------------------------------------
dconfig-ui.bat demo derby  Run DConfig GUI Editor as demo mode with embedded Derby database.
                                No database setup is needed.

dconfig-ui.bat demo hsqldb Run DConfig GUI Editor as demo mode with embedded Hsqldb database.
                                No database setup is needed.

dconfig-ui.bat demo h2     Run DConfig GUI Editor as demo mode with embedded H2 database.
                                No database setup is needed.

dconfig-ui.bat             Run DConfig GUI Editor (need database setup,
                                see Database Setup section)


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


3.1.2 Launch DConfig Editior from the Web using JNLP

    This requires Java web servers in order to use this feature. This is the
recommended way to distribute the desktop DConfig Editor through network.

    Please refer to dconfig-webdemo/readme.txt on how to run the web demo or
look at http://dconfig.sourceforge.net/dconfig/webdemo.php for a quick webdemo 
setup.


3.2 Use Dynamic Configuration Toolkit library

    DConfigReader is the core class of the Dynamic Configuration Toolkit library.
DConfigReader locates in org.moonwave.dconfig.dao package. Please see the
javadoc under dist/docs/javadoc/dconfig-lib/index.html for more details.

    The library caches can be refreshed on demand by calling 
org.moonwave.dconfig.dao.CacheManager.load().


4. JDBC Drivers
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

dconfig_lib/conf/dconfig_lib.properties has more details on JDBC driver
class name and db url.


5. Configure Web Server to use Dynamic Configuration Toolkit
------------------------------------------------------------

5.1 Place the following lines into web.xml file

    <servlet>
        <servlet-name>InitServlet</servlet-name>
        <servlet-class>org.moonwave.dconfig.servlet.InitServlet</servlet-class>
        <load-on-startup>5</load-on-startup>
    </servlet>

5.2 Configure connection and cache loader

    Set up server side active database connection information in 
dconfig-lib/conf/dconfig_lib.properties file.

active.connection.prefix=<active connection lookup key>

For example, 
active.connection.prefix=hsqldbdemo
active.connection.prefix=mysql


# ------------------------------------------------------------------------------
# next cache loader execeution period in minutes. default is 5  minutes.
# A value of <= 0 turns off the automatical cache loading feature.
dconfig.cacheloader.taskperiod=5

