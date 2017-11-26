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
-- The scripts below has been executed on Apache Derby v.10.1.3.1
--------------------------------------------------------------------------------
-- Login as a user with admin role
-- For example, app (default password: app)

--------------------------------------------------------------------------------
-- 1. Create table dconfig_datatype (Required)

CREATE TABLE dconfig_datatype (
    alias VARCHAR(10) NOT NULL,
    data_type_name VARCHAR(20) NOT NULL,
    PRIMARY KEY  (alias)
);

--------------------------------------------------------------------------------
-- 2. Create table dconfig_key (Required)
CREATE TABLE     dconfig_key      (
    id      INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    key_name      VARCHAR(500) NOT NULL,
    inherited     CHAR(1) DEFAULT 'N',
    date_created  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY  (id),
    unique  ( key_name )
);

--------------------------------------------------------------------------------
-- 3. Create table dconfig_attribute (Required)

CREATE TABLE dconfig_attribute (
    id      INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    key_id INT NOT NULL REFERENCES dconfig_key(id),
    attribute_name VARCHAR(100) NOT NULL,
    data_type_alias VARCHAR(10) default NULL REFERENCES dconfig_datatype(alias),
    reference CHAR(1) DEFAULT 'N',
    attribute_value VARCHAR(5000) default NULL,
    comments VARCHAR(500) default NULL,
    date_created  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY  (id),
    unique (key_id,attribute_name)
);

--------------------------------------------------------------------------------
-- 4. Create table dconfig_system (Required)
CREATE TABLE     dconfig_system      (
    attribute_name VARCHAR(100) NOT NULL, 
    attribute_value VARCHAR(500)  default NULL, 
    date_created  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY  (attribute_name)
);


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
--commit;

--------------------------------------------------------------------------------
-- 6. add sample keys (Optional)

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
insert into dconfig_key (key_name) values ('config.view.demo');
insert into dconfig_key (key_name) values ('config.view.demo.page1');

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
--commit;

--------------------------------------------------------------------------------
-- 7. add sample attributes (Optional)

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

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.view.demo.page1'), '#Rows Per Page', 'int', '10', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.view.demo.page1'), 'visible columns', 'intar', '1&^#;2&^#;3&^#;4&^#;5&^#;6', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.view.demo.page1'), 'tab names', 'strar', 
    'Database&^#;DConfig UI - JNLP&^#;Refresh&^#;Search&^#;Results', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.view.demo.page1'), 'max # of keynames', 'int', '10', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments)
    values ((select id from dconfig_key where key_name = 'config.view.demo.page1'), 'max value length', 'int', '30', '');


--commit;

--------------------------------------------------------------------------------
-- 8. Create a user account 'dcfg' (Optional)
-- You can use existing user account, replace dcfg with the actual user account.
-- and GRANT proper permissions to this account
-- 
--Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_key to dcfg
--Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_attribute to dcfg
--Grant SELECT, INSERT, UPDATE, DELETE ON TABLE dconfig_datatype to dcfg


