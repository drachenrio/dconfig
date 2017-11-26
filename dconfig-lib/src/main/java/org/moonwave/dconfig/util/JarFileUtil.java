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

package org.moonwave.dconfig.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jonathan Luo
 */
public class JarFileUtil {
    
    private static final Log log = LogFactory.getLog(JarFileUtil.class);

    private static final String DRIVER_TOKEN_1 = "Driver";
    private static final String DRIVER_TOKEN_2 = "DataSource";
    
    /** Creates a new instance of JarFileReaderT */
    public JarFileUtil() {
    }
 
    /**
     * Checks whether a jar file contains specified jdbc driver.
     *
     * @param jarFile jar file to check against with.
     * @param jdbcDriverName jdbc driver name.
     * @return true if driver is found; null otherwise.
     */
    public static boolean checkJdbcDriver(String jarFile, String jdbcDriverName) {
        if ((jarFile == null) || (jarFile.length() == 0))
            return false;
        boolean found = false;
        JarFile jar = null;
        try {
          jar = new JarFile(jarFile);
          Enumeration e = jar.entries();
          while (e.hasMoreElements()) {
              JarEntry obj = (JarEntry)e.nextElement();
              String name = obj.toString();
              name = name.replaceAll("/", ".");
              if (name.indexOf(jdbcDriverName) >= 0) {
                  found = true;
                  break;
              }
          }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            try {
                if (jar != null)
                    jar.close();
            } catch (Exception e) {
                
            }
        }
        return found;
    }

    /**
     * Returns a list of possible JDBC drivers.
     *
     * @param jarFile jar file to search.
     * @param jdbcDriverName jdbc driver name.
     * @return list of drivers if found; empty list otherwise.
     */
    public static List getDriverList(String jarFile, String jdbcDriverName) {
        List list = new ArrayList();
        if ((jarFile == null) || (jarFile.length() == 0))
            return list;
        if (jdbcDriverName == null)
            jdbcDriverName = DRIVER_TOKEN_1;

        JarFile jar = null;
        try {
          jar = new JarFile(jarFile);
          Enumeration e = jar.entries();
          while (e.hasMoreElements()) {
              JarEntry obj = (JarEntry)e.nextElement();
              String name = obj.toString();
              //System.out.println(name);
              if ((name.indexOf(jdbcDriverName) >= 0) || (name.indexOf(DRIVER_TOKEN_2) >= 0)) {
                  if (name.endsWith(".class")) {
                      name = name.replaceAll("/", ".");
                      name = name.substring(0, name.lastIndexOf(".class"));// remove the last ".class" token
                      list.add(name);
                  }
              }
          }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            try {
                if (jar != null)
                    jar.close();
            } catch (Exception e) {
                
            }
        }
        return list;
    }

    public static void main(String argv[]) {      
        try {
           String driver = "com.mysql.jdbc.Driver";
           //String driver = "Driver";
          JarFile jarFile = new JarFile("/home/jonathan/local/sys/jdbc-driver/mysql/mysql-connector-java-3.1.13-bin.jar");        
          Enumeration e = jarFile.entries();
          while (e.hasMoreElements()) {
              JarEntry obj = (JarEntry)e.nextElement();
              String name = obj.toString();
              name = name.replaceAll("/", ".");
              if (name.indexOf(driver) >= 0)
                System.out.println(name);
          }
        } catch (Exception e) {
                System.out.println(e);            
        }

    }
    
}
