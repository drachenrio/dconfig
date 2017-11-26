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
 * AttributeReadWriteTest.java
 * JUnit based test
 *
 * Created on January 29, 2007, 11:20 PM
 *
 * @author Jonathan Luo
 */
public class AttributeReadWriteTest extends TestCase {
    public static Test suite() {
        return new TestSuite(AttributeReadWriteTest.class);
    }
    
    public AttributeReadWriteTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        LibInitializer.initialize();
        DConfigReader.load();
    }

    protected void tearDown() throws Exception {
        DConfigReader.unload();        
    }
    
    public void testBooleanDataRead() {
        try {
            boolean[] bArray = new boolean[2];
            bArray[0] = false;
            bArray[1] = true;
            
            Boolean[] booleanArray = new Boolean[5];
            booleanArray[0] = Boolean.FALSE;
            booleanArray[1] = Boolean.TRUE;
            booleanArray[2] = null;
            booleanArray[3] = Boolean.TRUE;
            booleanArray[4] = Boolean.TRUE;       
            
            DConfigWriter.writeBoolean("test.admin.applications.boolean", false); // (primary) attribute
            DConfigWriter.writeBoolean("test.admin.applications.Boolean", Boolean.TRUE); // (primary) attribute
            DConfigWriter.writeBoolean("test.admin.applications", "boolean", false);
            DConfigWriter.writeBoolean("test.admin.applications", "Boolean", Boolean.FALSE);
            DConfigWriter.writeBooleanArray("test.admin.applications.booleanArray", bArray); // (primary) attribute
            DConfigWriter.writeBooleanArray("test.admin.applications.BooleanArray", booleanArray); // (primary) attribute
            DConfigWriter.writeBooleanArray("test.admin.applications", "booleanArray", bArray);
            DConfigWriter.writeBooleanArray("test.admin.applications", "BooleanArray", booleanArray);
            
            DConfigReader.load();

            // read test
            boolean[] booleanArExpected = new boolean[4];
            booleanArExpected[0] = false;
            booleanArExpected[1] = true;
            booleanArExpected[2] = true;
            booleanArExpected[3] = true;
            
            assertEquals("bool", false, DConfigReader.getBoolean("test.admin.applications.boolean"));
            assertEquals("bool", false, DConfigReader.getBoolean("test.admin.applications.boolean", false));
            assertEquals("bool", true, DConfigReader.getBoolean("test.admin.applications.Boolean"));
            assertEquals("bool", true, DConfigReader.getBoolean("test.admin.applications.Boolean", true));
            assertEquals("bool", false, DConfigReader.getBoolean("test.admin.applications", "boolean"));
            assertEquals("bool", false, DConfigReader.getBoolean("test.admin.applications", "boolean", false));
            assertEquals("bool", false, DConfigReader.getBoolean("test.admin.applications", "Boolean"));
            assertEquals("bool", false, DConfigReader.getBoolean("test.admin.applications", "Boolean", false));

            boolean[] bRet = DConfigReader.getBooleanArray("test.admin.applications.booleanArray");
            for (int i = 0; i < bRet.length; i++)
                assertEquals(bArray[i], bRet[i]);

            bRet = DConfigReader.getBooleanArray("test.admin.applications.booleanArray", bArray);
            for (int i = 0; i < bRet.length; i++)
                assertEquals(bArray[i], bRet[i]);
            
            bRet = DConfigReader.getBooleanArray("test.admin.applications.BooleanArray");
            for (int i = 0; i < bRet.length; i++)
                assertEquals(booleanArExpected[i], bRet[i]);

            bRet = DConfigReader.getBooleanArray("test.admin.applications.BooleanArray", bArray);
            for (int i = 0; i < bRet.length; i++)
                assertEquals(booleanArExpected[i], bRet[i]);

            bRet = DConfigReader.getBooleanArray("test.admin.applications", "booleanArray");
            for (int i = 0; i < bRet.length; i++)
                assertEquals(bArray[i], bRet[i]);
            
            bRet = DConfigReader.getBooleanArray("test.admin.applications", "booleanArray", bArray);
            for (int i = 0; i < bRet.length; i++)
                assertEquals(bArray[i], bRet[i]);

            bRet = DConfigReader.getBooleanArray("test.admin.applications", "BooleanArray");
            for (int i = 0; i < bRet.length; i++)
                assertEquals(booleanArExpected[i], bRet[i]);

            bRet = DConfigReader.getBooleanArray("test.admin.applications", "BooleanArray", bArray);
            for (int i = 0; i < bRet.length; i++)
                assertEquals(booleanArExpected[i], bRet[i]);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public void testIntegerRead() {
        try {
            int[] intArray = new int[3];
            intArray[0] = 10;
            intArray[1] = 20;
            intArray[2] = 30;
            Integer[] integerArray = new Integer[3];
            integerArray[0] = new Integer(10);
            integerArray[1] = new Integer(20);
            integerArray[2] = new Integer(30);
            DConfigWriter.writeInteger("test.admin.applications.int", 55);// (primary) attribute
            DConfigWriter.writeInteger("test.admin.applications.Integer", new Integer(101));// (primary) attribute
            DConfigWriter.writeInteger("test.admin.applications", "int", 55);
            DConfigWriter.writeInteger("test.admin.applications", "Integer", new Integer(202));
            DConfigWriter.writeIntegerArray("test.admin.applications.intArray", intArray);// (primary) attribute
            DConfigWriter.writeIntegerArray("test.admin.applications.IntegerArray", integerArray);// (primary) attribute
            DConfigWriter.writeIntegerArray("test.admin.applications", "intArray", intArray);
            DConfigWriter.writeIntegerArray("test.admin.applications", "IntegerArray", integerArray);
            DConfigReader.load();

            // read test
            boolean[] booleanArExpected = new boolean[4];
            booleanArExpected[0] = false;
            booleanArExpected[1] = true;
            booleanArExpected[2] = true;
            booleanArExpected[3] = true;
            
            assertEquals("integer",  55, DConfigReader.getInteger("test.admin.applications.int"));
            assertEquals("integer",  55, DConfigReader.getInteger("test.admin.applications.int", 55));
            assertEquals("int",      55, DConfigReader.getInteger("test.admin.applications", "int"));
            assertEquals("int",      55, DConfigReader.getInteger("test.admin.applications", "int", 55));
            assertEquals("Integer", 101, DConfigReader.getInteger("test.admin.applications.Integer"));
            assertEquals("Integer", 101, DConfigReader.getInteger("test.admin.applications.Integer", 101));
            assertEquals("Integer", 202, DConfigReader.getInteger("test.admin.applications", "Integer"));
            assertEquals("Integer", 202, DConfigReader.getInteger("test.admin.applications", "Integer", 202));

            int[] iRet = DConfigReader.getIntegerArray("test.admin.applications.intArray");
            for (int i = 0; i < iRet.length; i++)
                assertEquals(intArray[i], iRet[i]);
            
            iRet = DConfigReader.getIntegerArray("test.admin.applications.intArray", intArray);
            for (int i = 0; i < iRet.length; i++)
                assertEquals(intArray[i], iRet[i]);
            
            iRet = DConfigReader.getIntegerArray("test.admin.applications.IntegerArray");
            for (int i = 0; i < iRet.length; i++)
                assertEquals(integerArray[i].intValue(), iRet[i]);
            
            iRet = DConfigReader.getIntegerArray("test.admin.applications.IntegerArray", intArray);
            for (int i = 0; i < iRet.length; i++)
                assertEquals(integerArray[i].intValue(), iRet[i]);
            
            iRet = DConfigReader.getIntegerArray("test.admin.applications", "intArray");
            for (int i = 0; i < iRet.length; i++)
                assertEquals(intArray[i], iRet[i]);

            iRet = DConfigReader.getIntegerArray("test.admin.applications", "intArray", intArray);
            for (int i = 0; i < iRet.length; i++)
                assertEquals(intArray[i], iRet[i]);
            
            iRet = DConfigReader.getIntegerArray("test.admin.applications", "IntegerArray");
            for (int i = 0; i < iRet.length; i++)
                assertEquals(integerArray[i].intValue(), iRet[i]);

            iRet = DConfigReader.getIntegerArray("test.admin.applications", "IntegerArray", intArray);
            for (int i = 0; i < iRet.length; i++)
                assertEquals(integerArray[i].intValue(), iRet[i]);
        } catch (Exception e) {            
            e.printStackTrace();
        }
    }
    public void testLongRead() {
        try {
            long[] lArray = new long[3];
            lArray[0] = 10;
            lArray[1] = 20;
            lArray[2] = 30;
            Long[] longArray = new Long[3];
            longArray[0] = new Long(101);
            longArray[1] = new Long(202);
            longArray[2] = new Long(303);
            DConfigWriter.writeLong("test.admin.applications.long", 7755); // (primary) attribute
            DConfigWriter.writeLong("test.admin.applications.Long", new Long(1010)); // (primary) attribute
            DConfigWriter.writeLong("test.admin.applications", "long", 7755);
            DConfigWriter.writeLong("test.admin.applications", "Long", new Long(10101));
            DConfigWriter.writeLongArray("test.admin.applications.longArray", lArray); // (primary) attribute
            DConfigWriter.writeLongArray("test.admin.applications.LongArray", longArray);// (primary) attribute
            DConfigWriter.writeLongArray("test.admin.applications", "longArray", lArray);
            DConfigWriter.writeLongArray("test.admin.applications", "LongArray", longArray);

            DConfigReader.load();

            // read test
            assertEquals(7755L, DConfigReader.getLong("test.admin.applications.long"));
            assertEquals(7755L, DConfigReader.getLong("test.admin.applications.long", 7755));
            assertEquals(7755L, DConfigReader.getLong("test.admin.applications", "long"));
            assertEquals(7755L, DConfigReader.getLong("test.admin.applications", "long", 7755L));
            assertEquals(1010L, DConfigReader.getLong("test.admin.applications.Long"));
            assertEquals(1010L, DConfigReader.getLong("test.admin.applications.Long", 1010));
            assertEquals(10101L, DConfigReader.getLong("test.admin.applications", "Long"));
            assertEquals(10101L, DConfigReader.getLong("test.admin.applications", "Long", 10101L));
            
            long[] lRet = DConfigReader.getLongArray("test.admin.applications.longArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(lArray[i], lRet[i]);
            
            lRet = DConfigReader.getLongArray("test.admin.applications.longArray", lArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(lArray[i], lRet[i]);

            lRet = DConfigReader.getLongArray("test.admin.applications.LongArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(longArray[i].longValue(), lRet[i]);
            
            lRet = DConfigReader.getLongArray("test.admin.applications.LongArray", lArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(longArray[i].longValue(), lRet[i]);
        
            lRet = DConfigReader.getLongArray("test.admin.applications", "longArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(lArray[i], lRet[i]);
            
            lRet = DConfigReader.getLongArray("test.admin.applications", "longArray", lArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(lArray[i], lRet[i]);

            lRet = DConfigReader.getLongArray("test.admin.applications", "LongArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(longArray[i].longValue(), lRet[i]);
            
            lRet = DConfigReader.getLongArray("test.admin.applications", "LongArray", lArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(longArray[i].longValue(), lRet[i]);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public void testStringRead() {
        try {
            String[] sArray = new String[4];
            sArray[0] = "1 - Australia Open";
            sArray[1] = "2 - French Open";
            sArray[2] = "3 - Wimbledon";
            sArray[3] = "4 - US Open";
            DConfigWriter.writeString("test.admin.applications.string", "single value is here");// (primary) attribute
            DConfigWriter.writeString("test.admin.applications", "string", "single value is here");
            DConfigWriter.writeStringArray("test.admin.applications.stringArray", sArray);// (primary) attribute
            DConfigWriter.writeStringArray("test.admin.applications", "stringArray", sArray);

            DConfigReader.load();

            // read test
            assertEquals("single value is here", DConfigReader.getString("test.admin.applications.string"));
            assertEquals("single value is here", DConfigReader.getString("test.admin.applications", "string"));
            assertEquals("single value is here", DConfigReader.getString("test.admin.applications", "string", "default value here"));
            
            String[] lRet = DConfigReader.getStringArray("test.admin.applications.stringArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(sArray[i], lRet[i]);
            
            lRet = DConfigReader.getStringArray("test.admin.applications.stringArray", sArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(sArray[i], lRet[i]);

            lRet = DConfigReader.getStringArray("test.admin.applications", "stringArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(sArray[i], lRet[i]);
            
            lRet = DConfigReader.getStringArray("test.admin.applications", "stringArray", sArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(sArray[i], lRet[i]);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public void testDateRead() {
        try {
            Date[] dateArray = new Date[3];
            dateArray[0] = DateUtil.toDate("07/01/2007 10:01:01");
            dateArray[1] = DateUtil.toDate("07/02/2007");
            dateArray[2] = DateUtil.toDate("07/03/2007");
            DConfigWriter.writeDate("test.admin.applications.date", DateUtil.toDate("07/23/2007")); // (primary) attribute
            DConfigWriter.writeDate("test.admin.applications", "date", DateUtil.toDate("07/06/2007"));
            DConfigWriter.writeDateArray("test.admin.applications.dateArray", dateArray); // (primary) attribute
            DConfigWriter.writeDateArray("test.admin.applications", "dateArray", dateArray);
            
            DConfigReader.load();

            // read test
            assertEquals(DateUtil.toDate("07/23/2007"), DConfigReader.getDate("test.admin.applications.date"));
            assertEquals(DateUtil.toDate("07/23/2007"), DConfigReader.getDate("test.admin.applications.date", DateUtil.toDate("07/23/2007")));
            assertEquals(DateUtil.toDate("07/06/2007"), DConfigReader.getDate("test.admin.applications", "date"));
            assertEquals(DateUtil.toDate("07/06/2007"), DConfigReader.getDate("test.admin.applications", "date", DateUtil.toDate("07/06/2007")));
            
            Date[] lRet = DConfigReader.getDateArray("test.admin.applications.dateArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(dateArray[i], lRet[i]);
            
            lRet = DConfigReader.getDateArray("test.admin.applications.dateArray", dateArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(dateArray[i], lRet[i]);
            
            lRet = DConfigReader.getDateArray("test.admin.applications", "dateArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(dateArray[i], lRet[i]);

            lRet = DConfigReader.getDateArray("test.admin.applications", "dateArray", dateArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(dateArray[i], lRet[i]);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public void testFloatRead() {
        try {
            float[] fArray = new float[3];
            fArray[0] = 10;
            fArray[1] = 20;
            fArray[2] = 30;
            Float[] floatArray = new Float[2];
            floatArray[0] = new Float(10);
            floatArray[1] = new Float(780);
            DConfigWriter.writeFloat("test.admin.applications.float", 39.8745f); // (primary) attribute
            DConfigWriter.writeFloat("test.admin.applications", "float", 39.8745f);
            DConfigWriter.writeFloat("test.admin.applications.Float", 139.45f); // (primary) attribute
            DConfigWriter.writeFloat("test.admin.applications", "Float", 139.45f);

            DConfigWriter.writeFloatArray("test.admin.applications.floatArray", fArray); // (primary) attribute
            DConfigWriter.writeFloatArray("test.admin.applications.FloatArray", floatArray); // (primary) attribute
            DConfigWriter.writeFloatArray("test.admin.applications", "floatArray", fArray);
            DConfigWriter.writeFloatArray("test.admin.applications", "FloatArray", floatArray);

            DConfigReader.load();

            // read test
            float delta = 0.001f;
            assertEquals(39.8745f, DConfigReader.getFloat("test.admin.applications.float"), delta);
            assertEquals(39.8745f, DConfigReader.getFloat("test.admin.applications.float", 739.8745f), delta);
            assertEquals(39.8745f, DConfigReader.getFloat("test.admin.applications", "float"), delta);
            assertEquals(39.8745f, DConfigReader.getFloat("test.admin.applications", "float", 739.9876f), delta);
            assertEquals(139.45f, DConfigReader.getFloat("test.admin.applications.Float"), delta);
            assertEquals(139.45f, DConfigReader.getFloat("test.admin.applications.Float", 1139.45f), delta);
            assertEquals(139.45f, DConfigReader.getFloat("test.admin.applications", "Float"), delta);
            assertEquals(139.45f, DConfigReader.getFloat("test.admin.applications", "Float", 1139.45f), delta);

            
            float[] lRet = DConfigReader.getFloatArray("test.admin.applications.floatArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(fArray[i], lRet[i], delta);
            
            lRet = DConfigReader.getFloatArray("test.admin.applications.floatArray", fArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(fArray[i], lRet[i], delta);

            lRet = DConfigReader.getFloatArray("test.admin.applications.FloatArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(floatArray[i].floatValue(), lRet[i], delta);
            
            lRet = DConfigReader.getFloatArray("test.admin.applications.FloatArray", fArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(floatArray[i].floatValue(), lRet[i], delta);
        
            lRet = DConfigReader.getFloatArray("test.admin.applications", "floatArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(fArray[i], lRet[i], delta);
            
            lRet = DConfigReader.getFloatArray("test.admin.applications", "floatArray", fArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(fArray[i], lRet[i], delta);

            lRet = DConfigReader.getFloatArray("test.admin.applications", "FloatArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(floatArray[i].floatValue(), lRet[i], delta);
            
            lRet = DConfigReader.getFloatArray("test.admin.applications", "FloatArray", fArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(floatArray[i].floatValue(), lRet[i], delta);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public void testDoubleRead() {
        try {
            double[] dArray = new double[3];
            dArray[0] = 10;
            dArray[1] = 20;
            dArray[2] = 30;
            Double[] doubleArray = new Double[3];
            doubleArray[0] = new Double(10);
            doubleArray[1] = new Double(20);
            doubleArray[2] = new Double(30);
            
            DConfigWriter.writeDouble("test.admin.applications.double", 179.89);// (primary) attribute
            DConfigWriter.writeDouble("test.admin.applications.Double", new Double(2379.34));// (primary) attribute
            DConfigWriter.writeDouble("test.admin.applications", "double", 179.89);
            DConfigWriter.writeDouble("test.admin.applications", "Double", 2379.34);
            
            DConfigWriter.writeDoubleArray("test.admin.applications.doubleArray", dArray);// (primary) attribute
            DConfigWriter.writeDoubleArray("test.admin.applications.DoubleArray", doubleArray);// (primary) attribute
            DConfigWriter.writeDoubleArray("test.admin.applications", "doubleArray", dArray);
            DConfigWriter.writeDoubleArray("test.admin.applications", "DoubleArray", doubleArray);

            DConfigReader.load();

            // read test
            double delta = 0.001d;
            assertEquals(179.89, DConfigReader.getDouble("test.admin.applications.double"), delta);
            assertEquals(179.89, DConfigReader.getDouble("test.admin.applications.double", 179.89), delta);
            assertEquals(179.89, DConfigReader.getDouble("test.admin.applications", "double"), delta);
            assertEquals(179.89, DConfigReader.getDouble("test.admin.applications", "double", 179.89), delta);
            assertEquals(2379.34, DConfigReader.getDouble("test.admin.applications.Double"), delta);
            assertEquals(2379.34, DConfigReader.getDouble("test.admin.applications.Double", 2379.34), delta);
            assertEquals(2379.34, DConfigReader.getDouble("test.admin.applications", "Double"), delta);
            assertEquals(2379.34, DConfigReader.getDouble("test.admin.applications", "Double", 2379.34), delta);

            
            double[] lRet = DConfigReader.getDoubleArray("test.admin.applications.doubleArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(dArray[i], lRet[i], delta);
            
            lRet = DConfigReader.getDoubleArray("test.admin.applications.doubleArray", dArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(dArray[i], lRet[i], delta);

            lRet = DConfigReader.getDoubleArray("test.admin.applications.DoubleArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(doubleArray[i].doubleValue(), lRet[i], 0.001d);
            
            lRet = DConfigReader.getDoubleArray("test.admin.applications.DoubleArray", dArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(doubleArray[i].doubleValue(), lRet[i], 0.001d);
        
            lRet = DConfigReader.getDoubleArray("test.admin.applications", "doubleArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(dArray[i], lRet[i], 0.001d);
            
            lRet = DConfigReader.getDoubleArray("test.admin.applications", "doubleArray", dArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(dArray[i], lRet[i], 0.001d);

            lRet = DConfigReader.getDoubleArray("test.admin.applications", "DoubleArray");
            for (int i = 0; i < lRet.length; i++)
                assertEquals(doubleArray[i].doubleValue(), lRet[i], 0.001d);
            
            lRet = DConfigReader.getDoubleArray("test.admin.applications", "DoubleArray", dArray);
            for (int i = 0; i < lRet.length; i++)
                assertEquals(doubleArray[i].doubleValue(), lRet[i], 0.001d);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
