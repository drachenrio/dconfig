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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

/**
 * Read SQL statements from a file. 
 *
 * @author Jonathan Luo
 */
public class SqlReader extends BufferedReader {

    public SqlReader(String filename) throws java.io.IOException {
        super(new InputStreamReader(FileUtil.toURL(filename).openStream()));
    }

    public SqlReader(InputStreamReader in) {
        super(in);
    }

    public SqlReader(FileReader in) throws java.io.FileNotFoundException, java.net.URISyntaxException {
        super(in);
    }

    
    /**
     * Gets next valid sql line.
     */
    public final String readLine() throws IOException {
        String line;
        do {
            line = super.readLine();
            if (line != null)
                line = line.trim();
        } while ((line != null) && (line.startsWith("--") || (line.length() == 0)));
        return line;
    }

    /**
     * Gets the next sql statement.
     */
    public final String nextSql() throws IOException {
        String sql = "";
        String line;
        while (true) {
            line = readLine();
            if (line == null)
                break;
            sql += " " + line;
            if (line.endsWith(";")) {
                sql = StringUtils.chomp(sql, ";");
                break;
            }
        }
        return sql;
    }

    public static void main(String args[]) {
        try {
            SqlReader in = new SqlReader("conf/derby_10.1.3.1.sql");
            String sql;
            while (true) {
                sql = in.nextSql();
                if ((sql == null) || (sql.length() == 0))
                    break;        
                System.out.println(sql);
            }
            in.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}