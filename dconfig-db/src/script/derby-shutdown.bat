set derbylibdir=../../lib
set derbylib=%derbylibdir%/derby.jar;%derbylibdir%/derbynet.jar
set param=org.apache.derby.drda.NetworkServerControl shutdown -h localhost -p 1527

java -cp %derbylib% %param%
