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

import java.util.Date;
import junit.framework.*;
import org.moonwave.dconfig.dao.springfw.DataSourceManager;
import org.moonwave.dconfig.util.DateUtil;
import org.moonwave.dconfig.util.DemoDataPopulator;
import org.moonwave.dconfig.util.DemoDbManager;

/**
 * InheritedAttributeReadTest.java
 * JUnit based test
 *
 * Created on October 15, 2007, 11:29 PM
 *
 * @author Jonathan Luo
 */
public class InheritedAttributeReadTest extends TestCase {
    public static Test suite() {
        return new TestSuite(AttributeReadWriteTest.class);
    }
    
    public InheritedAttributeReadTest(String testName) {
        super(testName);
    }
//  attribute @ config
    protected void setUp() throws Exception {
        LibInitializer.initialize();
        DConfigReader.load();
    }

    protected void tearDown() throws Exception {
        DConfigReader.unload();        
    }
    
    public void testStringRead() {
        try {
            // read test
            String objectExpected = "attr defined @ config";
            assertEquals("single value is here", DConfigReader.getString("config.datasource.mysql", "attribute @ config"), objectExpected);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
