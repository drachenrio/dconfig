set derbylibdir=../../lib
set derbylib=%derbylibdir%/derby.jar;%derbylibdir%/derbynet.jar
set param=org.apache.derby.drda.NetworkServerControl start -h localhost -p 1527

rem Connecting to a Database using JDBC
rem    Client jar file: derbyclient.jar
rem    Class.forName("org.apache.derby.jdbc.ClientDriver");
rem    Connection conn = DriverManager.getConnection("jdbc:derby://<host>:<port1527>/<databaseName>", "app", "app"); 

java -cp %derbylib% %param%
