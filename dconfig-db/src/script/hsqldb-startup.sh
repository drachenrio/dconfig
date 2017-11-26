#!/bin/bash
dblibdir=../../lib
dblib=$dblibdir/hsqldb.jar
param='org.hsqldb.Server -port 9001 -database.0 hsqldbdemo -dbname.0 hsqldbdemo'

# Connecting to a Database using JDBC
#    Client jar file: hsqldb.jar
#    Class.forName("org.hsqldb.jdbcDriver");
#    Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://<host>:<port>", "", "");

echo java -cp $dblib $param
exec java -cp $dblib $param
