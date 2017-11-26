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
-- The scripts in a generic.sql work for all supported databases
--------------------------------------------------------------------------------
-- Login as a user with admin role
-- For example, app (default password: app)

--------------------------------------------------------------------------------
-- Update db.schema.version value
update dconfig_system set system_value = '1.3' where system_name = 'db.schema.version';

--------------------------------------------------------------------------------
-- Add new action types to dconfig_datatype
insert into dconfig_datatype (alias, data_type_name) values ('op', 'Operation');
insert into dconfig_datatype (alias, data_type_name) values ('opgrp', 'Operation Group');


