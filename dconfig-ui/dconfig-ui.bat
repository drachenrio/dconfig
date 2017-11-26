rem sample commands:

rem dconfig-ui.bat demo derby
rem dconfig-ui.bat demo derby verbose
rem dconfig-ui.bat demo hsqldb
rem dconfig-ui.bat demo hsqldb verbose
rem dconfig-ui.bat demo h2
rem dconfig-ui.bat demo h2 verbose

copy ..\dconfig-db\lib\derby*.jar lib
copy ..\dconfig-db\lib\hsqldb.jar lib
copy ..\dconfig-db\lib\h2*.jar lib
copy ..\dconfig-db\lib\LICENSE*.txt lib

java -jar dconfig-ui.jar %* 

