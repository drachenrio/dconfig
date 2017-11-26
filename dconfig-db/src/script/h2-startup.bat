set dblibdir=../../lib
set dblib=%dblibdir%/h2.jar
set param=org.h2.tools.Server

rem Using another Port
rem If the port is in use by another application, you may want to start the H2 Console 
rem on a different port. This can be done by changing the port in the file .h2.server.properties.
rem This file is stored in the user directory (for Windows,
rem this is usually in "Documents and Settings/<username>"). The relevant entry is webPort

rem  Connecting to a Database using JDBC
rem     Client jar file: h2.jar
rem     Class.forName("org.h2.Driver");
rem     Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", ""); 
rem  where jdbc:h2:~/test indicates that 'test' database would be created under
rem  use home directory


java -cp %dblib% %param%
