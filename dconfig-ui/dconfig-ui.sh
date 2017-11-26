#!/bin/bash

echo dconfig-ui.bat demo derby
echo dconfig-ui.bat demo derby verbose
echo dconfig-ui.bat demo hsqldb
echo dconfig-ui.bat demo hsqldb verbose
echo dconfig-ui.bat demo h2
echo dconfig-ui.bat demo h2 verbose

cp ../dconfig-db/lib/derby*.jar lib
cp ../dconfig-db/lib/hsqldb.jar lib
cp ../dconfig-db/lib/h2*.jar lib
cp ../dconfig-db/lib/LICENSE*.txt lib

java -jar dconfig-ui.jar $@


