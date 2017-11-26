To run demo from ide, 

1. copy dconfig-db/src/sql to dconfig-ui/src/java
   or 
   make a link from dconfig-ui/src/java to dconfig-db/src/sql
   $ cd dconfig-ui/src/java
   $ ln -s ../../../dconfig-db/src/sql/ sql

2. Right click on dconfig-ui
   Select Properties
   Select Run at the Categories

   Set one of the following at the Arguments box for embedded database:
    demo h2
    demo hsqldb
    demo derby