/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * DConfig - Free Dynamic Configuration Toolkit
 * Copyright (C) 2006, 2007 Jonathan Luo
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

package org.moonwave.dconfig.dao;

import junit.framework.*;

/**
 * AllTests.java
 * JUnit based test
 *
 * Created on January 29, 2007, 11:21 PM
 *
 * @author Jonathan Luo
 */
public class AllTests {
    
    public static void main (String[] args) {
        junit.textui.TestRunner.run (suite());
    }
    public static Test suite ( ) {
        TestSuite suite= new TestSuite("All DConfig JUnit Tests");
        suite.addTest(InheritedAttributeReadTest.suite());
        //suite.addTest(AttributeReadWriteTest.suite());
        //suite.addTest(new TestSuite(AttributeReadTest.class));
        return suite;
    }    
}
