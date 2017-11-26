-- ============================================================================-- GNU Lesser General Public License
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
-- The scripts below has been executed on Microsoft SQL Server 2000
--------------------------------------------------------------------------------
-- Login as a user with admin role

--------------------------------------------------------------------------------
-- 1. Create tables dconfig_key,  dconfig_attribute, dconfig_datatype (Required)

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_dconfig_attribute_dconfig_key]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[dconfig_attribute] DROP CONSTRAINT FK_dconfig_attribute_dconfig_key
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_dconfig_attribute_dconfig_datatype]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[dconfig_attribute] DROP CONSTRAINT FK_dconfig_attribute_dconfig_datatype
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[dconfig_key]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[dconfig_key]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[dconfig_datatype]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[dconfig_datatype]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[dconfig_attribute]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[dconfig_attribute]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[dconfig_system]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[dconfig_system]
GO

CREATE TABLE [dbo].[dconfig_system] (
	[system_name] [varchar] (50) COLLATE SQL_Latin1_General_CP1257_CS_AS NOT NULL ,
    [data_type_alias] [varchar] (10) COLLATE SQL_Latin1_General_CP1257_CS_AS NOT NULL ,
	[system_value] [varchar] (500) COLLATE SQL_Latin1_General_CP1257_CS_AS NOT NULL ,
	[comments] [varchar] (500) COLLATE SQL_Latin1_General_CP1257_CS_AS NULL ,
	[date_created] [datetime] NOT NULL ,
	[date_modified] [datetime] NOT NULL 
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[dconfig_system] WITH NOCHECK ADD 
	CONSTRAINT [PK_dconfig_system] PRIMARY KEY  CLUSTERED 
	(
		[system_name]
	)  ON [PRIMARY] ,
	CONSTRAINT [FK_dconfig_system_dconfig_datatype] FOREIGN KEY 
	(
		[data_type_alias]
	) REFERENCES [dbo].[dconfig_datatype] (
		[alias]
	)
GO

ALTER TABLE [dbo].[dconfig_system] ADD 
	CONSTRAINT [DF_dconfig_system_date_created] DEFAULT (getdate()) FOR [date_created],
	CONSTRAINT [DF_dconfig_system_date_modified] DEFAULT (getdate()) FOR [date_modified]
GO

    data_type_alias VARCHAR(10) default NULL REFERENCES dconfig_datatype(alias),


CREATE TABLE [dbo].[dconfig_datatype] (
	[alias] [varchar] (10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,
	[data_type_name] [varchar] (20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[dconfig_key] (
	[id] [bigint] IDENTITY (1, 1) NOT NULL ,
	[key_name] [varchar] (500) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,
        [inherited]    [char] (1) DEFAULT 'N',
	[date_created] [datetime] NOT NULL ,
	[date_modified] [datetime] NOT NULL 
) ON [PRIMARY]
GO


CREATE TABLE [dbo].[dconfig_attribute] (
	[id] [bigint] IDENTITY (1, 1) NOT NULL ,
	[key_id] [bigint] NOT NULL ,
	[attribute_name] [varchar] (100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,
	[data_type_alias] [varchar] (10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL ,
	[reference] [char] (1) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT 'N' ,
	[attribute_value] [varchar] (5000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,
	[comments] [varchar] (500) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,
	[date_created] [datetime] NOT NULL ,
	[date_modified] [datetime] NOT NULL 
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[dconfig_key] WITH NOCHECK ADD 
	CONSTRAINT [PK_dconfig_key] PRIMARY KEY  CLUSTERED 
	(
		[id]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[dconfig_datatype] WITH NOCHECK ADD 
	CONSTRAINT [PK_dconfig_datatype] PRIMARY KEY  CLUSTERED 
	(
		[alias]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[dconfig_attribute] WITH NOCHECK ADD 
	CONSTRAINT [PK_dconfig_attribute] PRIMARY KEY  CLUSTERED 
	(
		[id]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[dconfig_key] ADD 
	CONSTRAINT [DF_dconfig_key_date_created] DEFAULT (getdate()) FOR [date_created],
	CONSTRAINT [DF_dconfig_key_date_modified] DEFAULT (getdate()) FOR [date_modified],
	CONSTRAINT [IX_dconfig_key] UNIQUE  NONCLUSTERED 
	(
		[key_name]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[dconfig_attribute] ADD 
	CONSTRAINT [DF_registry_attribute_date_created] DEFAULT (getdate()) FOR [date_created],
	CONSTRAINT [DF_registry_attribute_date_modified] DEFAULT (getdate()) FOR [date_modified],
	CONSTRAINT [IX_dconfig_attribute] UNIQUE  NONCLUSTERED 
	(
		[key_id],
		[attribute_name]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[dconfig_attribute] ADD 
	CONSTRAINT [FK_dconfig_attribute_dconfig_datatype] FOREIGN KEY 
	(
		[data_type_alias]
	) REFERENCES [dbo].[dconfig_datatype] (
		[alias]
	),
	CONSTRAINT [FK_dconfig_attribute_dconfig_key] FOREIGN KEY 
	(
		[key_id]
	) REFERENCES [dbo].[dconfig_key] (
		[id]
	)
GO

--------------------------------------------------------------------------------
-- 2. Create data type data (Required)

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

-- 3. Create system data (Required)
insert into dconfig_system (system_name, data_type_alias, system_value, comments) values ('db.schema.version', 'str', '1.2', 'current db schema version');
insert into dconfig_system (system_name, data_type_alias, system_value, comments) values ('db.datachange.timestamp', 'dt', '2007-01-01 00:00:00', 'a new time stamp should be updated if any data changes (insert, update, delete) occurred in dconfig_key, dconfig_attribute tables');

--------------------------------------------------------------------------------
--4. add sample keys (Optional)

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
-- 5. add sample attributes (Optional)

--uncomment the following line if existing data can be really deleted
--delete from dconfig_attribute;
DECLARE @keyId int

SET @keyId = (select id from dconfig_key where key_name = 'admin')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'attribute @ admin', 'str', 'attr defined @ admin', '');

SET @keyId = (select id from dconfig_key where key_name = 'admin.applications')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'attribute @ applications', 'str', 'attr defined @ applications', '');

SET @keyId = (select id from dconfig_key where key_name = 'config')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'attribute @ config', 'str', 'attr defined @ config', '');

SET @keyId = (select id from dconfig_key where key_name = 'config.datasource')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'attribute @ datasource', 'str', 'attr defined @ datasource', '');

SET @keyId = (select id from dconfig_key where key_name = 'opensource')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'attribute @ opensource', 'str', 'attr defined @ opensource', '');

SET @keyId = (select id from dconfig_key where key_name = 'user')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'attribute @ user', 'str', 'attr defined @ user', '');


SET @keyId = (select id from dconfig_key where key_name = 'config.datasource.oracle')

insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, '(primary)', 'str', 'Oracle Corporation, http://www.oracle.com', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'vendor', 'str', 'Oracle Corp.', 'v1.4 http://www.oracle.com');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'JdbcDriver', 'str', 'oracle.jdbc.OracleDriver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'dbURL', 'str', 'jdbc:oracle:thin:@mycompany:1521:orcl', '');

SET @keyId = (select id from dconfig_key where key_name = 'config.datasource.mysql')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, '(primary)', 'str', 'MySQL, http://www.mysql.com', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'JdbcDriver', 'str', 'com.mysql.jdbc.Driver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'dbURL', 'str', 'jdbc:mysql://myhost:port3306/testdb', '');


SET @keyId = (select id from dconfig_key where key_name = 'config.datasource.postgres')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, '(primary)', 'str', 'PostgreSQL, http://www.postgresql.org/', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'JdbcDriver', 'str', 'org.postgresql.Driver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'dbURL', 'str', 'jdbc:postgresql://ourorg:5432/testdb', '');

SET @keyId = (select id from dconfig_key where key_name = 'config.datasource.sqlserver')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, '(primary)', 'str', 'jTDS, http://jtds.sourceforge.net/', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'vendor', 'str', 'jTDS', 'v1.2 http://jtds.sourceforge.net/');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'JdbcDriver', 'str', 'net.sourceforge.jtds.jdbc.Driver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'dbURL', 'str', 'jdbc:jtds:sqlserver://mainhost:1433/testdb', '');

SET @keyId = (select id from dconfig_key where key_name = 'config.datasource.derby')
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, '(primary)', 'str', 'Apache Derby, http://db.apache.org/derby/', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'JdbcDriver', 'str', 'org.apache.derby.jdbc.ClientDriver', '');
insert into dconfig_attribute (key_id, attribute_name, data_type_alias, attribute_value, comments) 
    values (@keyId, 'dbURL', 'str', 'jdbc:derby:net://localhost:1527/testdb', '');

commit;


--------------------------------------------------------------------------------
-- 6. Create a user account 'dcfg' (Optional)
-- You can use existing user account, replace dcfg with the actual user account.
if not exists (select * from master.dbo.syslogins where loginname = N'dcfg')
BEGIN
	declare @logindb nvarchar(132), @loginlang nvarchar(132) select @logindb = N'<dbname>', @loginlang = N'us_english'
	if @logindb is null or not exists (select * from master.dbo.sysdatabases where name = @logindb)
		select @logindb = N'master'
	if @loginlang is null or (not exists (select * from master.dbo.syslanguages where name = @loginlang) and @loginlang <> N'us_english')
		select @loginlang = @@language
	exec sp_addlogin N'dcfg', null, @logindb, @loginlang
END
GO

if not exists (select * from dbo.sysusers where name = N'dcfg' and uid < 16382)
	EXEC sp_grantdbaccess N'dcfg', N'dcfg'
GO
-- Grant object permissions
Grant SELECT, INSERT, UPDATE, DELETE ON dconfig_key to dcfg;
Grant SELECT, INSERT, UPDATE, DELETE ON dconfig_attribute to dcfg;
Grant SELECT, INSERT, UPDATE, DELETE ON dconfig_datatype to dcfg;
