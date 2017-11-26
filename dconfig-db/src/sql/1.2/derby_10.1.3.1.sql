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

--------------------------------------------------------------------------------
-- 4. Create table dconfig_system (Required)
CREATE TABLE     dconfig_system      (
    system_name VARCHAR(100) NOT NULL, 
    data_type_alias VARCHAR(10) default NULL REFERENCES dconfig_datatype(alias),
    system_value VARCHAR(500)  default NULL, 
    comments VARCHAR(500) default NULL,
    date_created  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY  (system_name)
);


--------------------------------------------------------------------------------
-- 2. Create default data for dconfig_system (Required)

insert into dconfig_system (system_name, data_type_alias, system_value, comments) values ('db.schema.version', 'str', '1.2', 'current db schema version');
insert into dconfig_system (system_name, data_type_alias, system_value, comments) values ('db.datachange.timestamp', 'dt', '2007-01-01 00:00:00', 'a new time stamp should be updated if any data changes (insert, update, delete) occurred in dconfig_key, dconfig_attribute tables');


