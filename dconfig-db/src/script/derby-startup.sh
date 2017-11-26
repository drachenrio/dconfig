#!/bin/bash
derbylibdir=../../lib
derbylib=$derbylibdir/derby.jar:$derbylibdir/derbynet.jar
param='org.apache.derby.drda.NetworkServerControl start -h localhost -p 1527'

# Connecting to a Database using JDBC
#    Client jar file: derbyclient.jar
#    Class.forName("org.apache.derby.jdbc.ClientDriver");
#    Connection conn = DriverManager.getConnection("jdbc:derby://<host>:<port1527>/<databaseName>", "app", "app"); 

echo java -cp $derbylib $param
exec java -cp $derbylib $param
