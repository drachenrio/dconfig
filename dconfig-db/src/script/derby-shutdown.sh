#!/bin/bash
derbylibdir=../../lib
derbylib=$derbylibdir/derby.jar:$derbylibdir/derbynet.jar
param='org.apache.derby.drda.NetworkServerControl shutdown -h localhost -p 1527'
echo java -cp $derbylib $param
#exec java -cp $derbylib $param
java -cp $derbylib $param
