#!/bin/bash
dblibdir=../../lib
dblib=$dblibdir/h2.jar
param='org.h2.tools.Server'

#Using another Port
#If the port is in use by another application, you may want to start the H2 Console 
#on a different port. This can be done by changing the port in the file .h2.server.properties.
#This file is stored in the user directory (for Windows,
#this is usually in "Documents and Settings/<username>"). The relevant entry is webPort

# Connecting to a Database using JDBC
#    Client jar file: h2.jar
#    Class.forName("org.h2.Driver");
#    Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", ""); 
# where jdbc:h2:~/test indicates that 'test' database would be created under
# use home directory


echo java -cp $dblib $param
exec java -cp $dblib $param
