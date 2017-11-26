set dblibdir=../../lib
set dblib=%dblibdir%/hsqldb.jar
set param=org.hsqldb.Server -port 9001 -database.0 hsqldbdemo -dbname.0 hsqldbdemo

rem Connecting to a Database using JDBC
rem    Client jar file: hsqldb.jar
rem    Class.forName("org.hsqldb.jdbcDriver");
rem    Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://<host>:<port>", "", "");

java -cp %dblib% %param%
