-- ============================================================================
-- GNU Lesser General Public License
-- ============================================================================
--
-- DConfig - Free Dynamic Configuration Toolkit
-- Copyright (C) 2006, 2007 Jonathan Luo
-- 
-- This library is free software; you can redistribute it and/or
-- modify it under the terms of the GNU Lesser General Public
-- License as published by the Free Software Foundation; either
-- version 2.1 of the License, or (at your option) any later version.
-- 
-- This library is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
-- Lesser General Public License for more details.
-- 
-- You should have received a copy of the GNU Lesser General Public
-- License along with this library; if not, write to the Free Software
-- Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
-- 
--

--------------------------------------------------------------------------------
-- The scripts below has been executed on Oracle Database 10g release 2 (10.2.0.2)
-- Enterprise Edition
--------------------------------------------------------------------------------
-- Login as a user with admin role

-- a. Create a user account 'dcfg'
-- USER SQL
CREATE USER dcfg IDENTIFIED BY dconfig 
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP;

-- USER SQL
ALTER USER DCFG 
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP
ACCOUNT UNLOCK ;

-- ROLES
GRANT "RESOURCE" TO DCFG ;
ALTER USER DCFG DEFAULT ROLE "RESOURCE";

-- SYSTEM PRIVILEGES
GRANT CREATE DATABASE LINK TO DCFG ;
GRANT UNLIMITED TABLESPACE TO DCFG ;
GRANT CREATE TRIGGER TO DCFG ;
GRANT CREATE SYNONYM TO DCFG ;
GRANT CREATE SESSION TO DCFG ;
GRANT ALTER SESSION TO DCFG ;
GRANT CREATE SEQUENCE TO DCFG ;
GRANT CREATE VIEW TO DCFG ;
GRANT CREATE PROCEDURE TO DCFG ;
GRANT ALTER ANY SEQUENCE TO DCFG ;

--b. log in as dcfg (password:dconfig), and continue the next step

--------------------------------------------------------------------------------
-- 1. Create table dconfig_datatype (Required)

REM START DCFG DCONFIG_DATATYPE

  CREATE TABLE "DCFG"."DCONFIG_DATATYPE" 
   (	"ALIAS" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"DATA_TYPE_NAME" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	 CONSTRAINT "DCONFIG_DATATYPE_PK" PRIMARY KEY ("ALIAS")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "USERS" ;
 
REM END DCFG DCONFIG_DATATYPE

--------------------------------------------------------------------------------
-- 2. Create table dconfig_key (Required)

REM START DCFG DCONFIG_KEY_SEQ

    CREATE SEQUENCE  "DCFG"."DCONFIG_KEY_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
 
REM END DCFG DCONFIG_KEY_SEQ

REM START DCFG DCONFIG_KEY

  CREATE TABLE "DCFG"."DCONFIG_KEY" 
   (	"ID" NUMBER(8,0) NOT NULL ENABLE, 
	"KEY_NAME" VARCHAR2(500 BYTE) NOT NULL ENABLE, 
    "INHERTED"     CHAR(1) DEFAULT 'N',
	"DATE_CREATED" TIMESTAMP (6) DEFAULT current_timestamp NOT NULL ENABLE, 
	"DATE_MODIFIED" TIMESTAMP (6) DEFAULT current_timestamp NOT NULL ENABLE, 
	 CONSTRAINT "DCONFIG_KEY_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "USERS" ;
 
REM END DCFG DCONFIG_KEY

REM START DCFG DCONFIG_KEY_TRG

  CREATE OR REPLACE TRIGGER "DCFG"."DCONFIG_KEY_TRG" 
 BEFORE INSERT ON DCONFIG_KEY 
FOR EACH ROW 
BEGIN
  SELECT DCONFIG_KEY_SEQ.NEXTVAL INTO :NEW.ID FROM DUAL;
END;


/
ALTER TRIGGER "DCFG"."DCONFIG_KEY_TRG" ENABLE;
 
REM END DCFG DCONFIG_KEY_TRG



--------------------------------------------------------------------------------
-- 3. Create table dconfig_attribute (Required)

REM START DCFG DCONFIG_ATTRIBUTE_SEQ

   CREATE SEQUENCE  "DCFG"."DCONFIG_ATTRIBUTE_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
 
REM END DCFG DCONFIG_ATTRIBUTE_SEQ

REM START DCFG DCONFIG_ATTRIBUTE

  CREATE TABLE "DCFG"."DCONFIG_ATTRIBUTE" 
   (	"ID" NUMBER(8,0) NOT NULL ENABLE, 
	"KEY_ID" NUMBER(8,0) NOT NULL ENABLE, 
	"ATTRIBUTE_NAME" VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	"DATA_TYPE_ALIAS" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"REFERENCE" VARCHAR2(1 BYTE) DEFAULT 'N', 
	"ATTRIBUTE_VALUE" VARCHAR2(4000 BYTE), 
	"COMMENTS" VARCHAR2(500 BYTE), 
	"DATE_CREATED" TIMESTAMP (6) DEFAULT current_timestamp NOT NULL ENABLE, 
	"DATE_MODIFIED" TIMESTAMP (6) DEFAULT current_timestamp NOT NULL ENABLE, 
	 CONSTRAINT "DCONFIG_ATTRIBUTE_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "USERS"  ENABLE, 
	 CONSTRAINT "DCONFIG_ATTRIBUTE_DCONFIG_FK1" FOREIGN KEY ("KEY_ID")
	  REFERENCES "DCFG"."DCONFIG_KEY" ("ID") ENABLE, 
	 CONSTRAINT "DCONFIG_ATTRIBUTE_DCONFIG_FK2" FOREIGN KEY ("DATA_TYPE_ALIAS")
	  REFERENCES "DCFG"."DCONFIG_DATATYPE" ("ALIAS") ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "USERS" ;
 
REM END DCFG DCONFIG_ATTRIBUTE

REM START DCFG DCONFIG_ATTRIBUTE_TRG

  CREATE OR REPLACE TRIGGER "DCFG"."DCONFIG_ATTRIBUTE_TRG" before insert on "DCONFIG_ATTRIBUTE"    for each row begin     if inserting then       if :NEW."ID" is null then          select DCONFIG_ATTRIBUTE_SEQ.nextval into :NEW."ID" from dual;       end if;    end if; end;

/
ALTER TRIGGER "DCFG"."DCONFIG_ATTRIBUTE_TRG" ENABLE;
 
REM END DCFG DCONFIG_ATTRIBUTE_TRG


--------------------------------------------------------------------------------
-- 4. Create table dconfig_system (Required)
REM START DCFG DCONFIG_SYSTEM

  CREATE TABLE "DCFG"."DCONFIG_SYSTEM" 
   (	"SYSTEM_NAME" VARCHAR2(100) NOT NULL ENABLE, 
	"DATA_TYPE_ALIAS" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"SYSTEM_VALUE" VARCHAR2(500), 
    "COMMENTS" VARCHAR(500) default NULL,
	"DATE_CREATED" TIMESTAMP (6) DEFAULT current_timestamp NOT NULL ENABLE, 
	"DATE_MODIFIED" TIMESTAMP (6) DEFAULT current_timestamp NOT NULL ENABLE,
	 CONSTRAINT "DCONFIG_ATTRIBUTE_DCONFIG_FK2" FOREIGN KEY ("DATA_TYPE_ALIAS")
   ) ;
  ALTER TABLE "DCFG"."DCONFIG_SYSTEM" ADD CONSTRAINT "DCONFIG_SYSTEM_PK" PRIMARY KEY ("SYSTEM_NAME") ENABLE;
 
REM END DCFG DCONFIG_SYSTEM
REM START DCFG DCONFIG_SYSTEM_PK

  CREATE UNIQUE INDEX "DCFG"."DCONFIG_SYSTEM_PK" ON "DCFG"."DCONFIG_SYSTEM" ("SYSTEM_NAME") 
  ;
 
REM END DCFG DCONFIG_SYSTEM_PK

--------------------------------------------------------------------------------
-- 5. Create data type data (Required)

insert into dconfig_datatype (alias, data_type_name) values ('bin', 'binary');
insert into dconfig_datatype (alias, data_type_name) values ('bool', 'Boolean');
insert into dconfig_datatype (alias, data_type_name) values ('boolar', 'Boolean Array');
insert into dconfig_datatype (alias, data_type_name) values ('int', 'Integer');
insert into dconfig_datatype (alias, data_type_name) values ('intar', 'Integer Array');
insert into dconfig_datatype (alias, data_type_name) values ('long', 'Long');
insert into dconfig_datatype (alias, data_type_name) values ('longar', 'Long Array');
insert into dconfig_datatype (alias, data_type_name) values ('str', 'String');
insert into dconfig_datatype (alias, data_type_name) values ('strar', 'String Array');
insert into dconfig_datatype (alias, data_type_name) values ('float', 'Float');
insert into dconfig_datatype (alias, data_type_name) values ('floatar', 'Float Array');
insert into dconfig_datatype (alias, data_type_name) values ('double', 'Double');
insert into dconfig_datatype (alias, data_type_name) values ('doublear', 'Double Array');
insert into dconfig_datatype (alias, data_type_name) values ('dt', 'Datetime');
insert into dconfig_datatype (alias, data_type_name) values ('dtar', 'Datetime array');
commit;

-- 6. Create system data (Required)
insert into dconfig_system (system_name, data_type_alias, system_value, comments) values ('db.schema.version', 'str', '1.2', 'current db schema version');
insert into dconfig_system (system_name, data_type_alias, system_value, comments) values ('db.datachange.timestamp', 'dt', '2007-01-01 00:00:00', 'a new time stamp should be updated if any data changes (insert, update, delete) occurred in dconfig_key, dconfig_attribute tables');

--------------------------------------------------------------------------------
-- 7. add sample keys (Optional)

--uncomment the following line if existing data can be really deleted
--delete from dconfig_key;
insert into dconfig_key (key_name) values ('config');
insert into dconfig_key (key_name, inherited) values ('config.datasource', 'Y');
insert into dconfig_key (key_name) values ('config.datasource.oracle');
insert into dconfig_key (key_name, inherited) values ('config.datasource.mysql', 'Y');
insert into dconfig_key (key_name) values ('config.datasource.postgres');
insert into dconfig_key (key_name) values ('config.datasource.sqlserver');
insert into dconfig_key (key_name) values ('config.datasource.derby');
insert into dconfig_key (key_name) values ('config.application');
insert into dconfig_key (key_name) values ('config.webservices');
insert into dconfig_key (key_name) values ('config.system');
insert into dconfig_key (key_name) values ('config.cache');
insert into dconfig_key (key_name, inherited) values ('config.view', 'Y');

insert into dconfig_key (key_name) values ('admin');
insert into dconfig_key (key_name) values ('admin.system');
insert into dconfig_key (key_name, inherited) values ('admin.applications', 'Y');
insert into dconfig_key (key_name) values ('admin.webservices');
insert into dconfig_key (key_name) values ('admin.users');
insert into dconfig_key (key_name) values ('admin.database');

insert into dconfig_key (key_name) values ('user');
insert into dconfig_key (key_name) values ('user.preferences');

insert into dconfig_key (key_name) values ('opensource');
insert into dconfig_key (key_name) values ('opensource.antivirus');
insert into dconfig_key (key_name) values ('opensource.software');
insert into dconfig_key (key_name) values ('opensource.appserver');
insert into dconfig_key (key_name) values ('opensource.database');
insert into dconfig_key (key_name) values ('opensource.games');
insert into dconfig_key (key_name) values ('opensource.ide');
insert into dconfig_key (key_name) values ('opensource.ide.eclipse');
insert into dconfig_key (key_name) values ('opensource.ide.netbeans');
insert into dconfig_key (key_name) values ('opensource.os');
insert into dconfig_key (key_name) values ('opensource.os.linux');
insert into dconfig_key (key_name) values ('opensource.os.linux.vendor');
insert into dconfig_key (key_name) values ('opensource.os.linux.vendor.redhat');
insert into dconfig_key (key_name) values ('opensource.os.linux.vendor.novell');
insert into dconfig_key (key_name) values ('opensource.os.linux.vendor.redflag');
insert into dconfig_key (key_name) values ('opensource.os.opensolaris');
commit;

--------------------------------------------------------------------------------
-- 8. add sample attributes (Optional)

--uncomment the following line if existing data can be really deleted
--delete from dconfig_attribute;
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'admin'), 'attribute @ admin', 'str', 'attr defined @ admin', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'admin.applications'), 'attribute @ applications', 'str', 'attr defined @ applications', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config'), 'attribute @ config', 'str', 'attr defined @ config', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.datasource'), 'attribute @ datasource', 'str', 'attr defined @ datasource', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'opensource'), 'attribute @ opensource', 'str', 'attr defined @ opensource', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'user'), 'attribute @ user', 'str', 'attr defined @ user', '');

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.oracle'), '(primary)', 'str', 'Oracle Corporation, http://www.oracle.com', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.oracle'), 'vendor', 'str', 'Oracle Corp.', 'v1.4 http://www.oracle.com');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.oracle'), 'JdbcDriver', 'str', 'oracle.jdbc.OracleDriver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.oracle'), 'dbURL', 'str', 'jdbc:oracle:thin:@mycompany:1521:orcl', '');

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.mysql'), '(primary)', 'str', 'MySQL, http://www.mysql.com', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.mysql'), 'JdbcDriver', 'str', 'com.mysql.jdbc.Driver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.mysql'), 'dbURL', 'str', 'jdbc:mysql://myhost:port3306/testdb', '');

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.postgres'), '(primary)', 'str', 'PostgreSQL, http://www.postgresql.org/', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.postgres'), 'JdbcDriver', 'str', 'org.postgresql.Driver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.postgres'), 'dbURL', 'str', 'jdbc:postgresql://ourorg:5432/testdb', '');

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.sqlserver'), '(primary)', 'str', 'jTDS, http://jtds.sourceforge.net/', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.sqlserver'), 'vendor', 'str', 'jTDS', 'v1.2 http://jtds.sourceforge.net/');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.sqlserver'), 'JdbcDriver', 'str', 'net.sourceforge.jtds.jdbc.Driver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.sqlserver'), 'dbURL', 'str', 'jdbc:jtds:sqlserver://mainhost:1433/testdb', '');

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.derby'), '(primary)', 'str', 'Apache Derby, http://db.apache.org/derby/', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.derby'), 'JdbcDriver', 'str', 'org.apache.derby.jdbc.ClientDriver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values ((select id from dconfig_key where key_name = 'config.datasource.derby'), 'dbURL', 'str', 'jdbc:derby:net://localhost:1527/testdb', '');
commit;

--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- Create a user account 'dcfg' (Optional)
-- You can use existing user account, replace dcfg with the actual user account.
-- and GRANT proper permissions to this account
-- 
Grant SELECT, INSERT, UPDATE, DELETE ON dconfig_key to dcfg;
Grant SELECT, INSERT, UPDATE, DELETE ON dconfig_attribute to dcfg;
Grant SELECT, INSERT, UPDATE, DELETE ON dconfig_datatype to dcfg;

-- QUOTAS
