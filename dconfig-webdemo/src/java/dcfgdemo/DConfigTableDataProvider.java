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

package dcfgdemo;

import com.sun.data.provider.FieldKey;
import com.sun.data.provider.impl.ObjectArrayDataProvider;
import com.sun.rave.web.ui.util.MessageUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Default date for the <code>Table</code> component. The following behavior is
 * implemented:
 * <ul>
 * <li>Upon component creation, pre-populate the table with some dummy data</li>
 * </ul>
 * 
 * Note: based on com.sun.rave.web.ui.model.DConfigTableDataProvider
 *
 * @author Jonathan Luo
 */
public class DConfigTableDataProvider extends ObjectArrayDataProvider{

    /** Default constructor. */
    public DConfigTableDataProvider() {
        setArray(getDefaultTableData());
    }

    /**
     * Create data that will be displayed when the table is first dropped
     * in the designer
     */
    public Data[] getDefaultTableData(){
        int noRows = 5;
        int noCols = 6;
        Data[] dataSet = new Data[noRows];
        for (int i = 0; i < noRows; i++) {
            String[] dataStrs = new String[noCols];
            for(int j=0; j < noCols; j++){
                dataStrs[j] = "TblCell" + String.valueOf(i + 1) + String.valueOf(j + 1);
            }
            dataSet[i] = new Data(dataStrs);
        }
        return dataSet;
    }
    
    /** Return the Field Keys skiiping the 0th index
     *   which is the "class" property
     */
    public FieldKey[] getFieldKeys() {
        FieldKey[] superFieldKeys = super.getFieldKeys();
        FieldKey[] fieldKeys = new FieldKey[superFieldKeys.length - 1];
        for(int i=1; i < superFieldKeys.length; i++){
             fieldKeys[i-1] = superFieldKeys[i];
        }
        return fieldKeys;
    }
        
    /**
     * Data structure that holds data for three columns of a table
     */
    public static class Data {
        private String[] columns = null;
        
        public Data(String[] cols) {
            columns = cols;
        }
        
        /** Get first column. */
        public String getColumn1() {
            return columns[0];
        }
        
        /** Set first column. */
        public void setColumn1(String col) {
            columns[0] = col;
        }
        
        /** Get second column. */
        public String getColumn2() {
            return columns[1];
        }
        
        /** Set second column. */
        public void setColumn2(String col) {
            columns[1] = col;
        }
        
        /** Get third column. */
        public String getColumn3() {
            return columns[2];
        }
        
        /** Set third column. */
        public void setColumn3(String col) {
            columns[2] = col;
        }
        
        /** Get fourth column. */
        public String getColumn4() {
            return columns[3];
        }
        
        /** Set fourth column. */
        public void setColumn4(String col) {
            columns[3] = col;
        }
        
        /** Get fifth column. */
        public String getColumn5() {
            return columns[4];
        }
        
        /** Set fifth column. */
        public void setColumn5(String col) {
            columns[4] = col;
        }
        
        /** Get sixth column. */
        public String getColumn6() {
            return columns[5];
        }
        
        /** Set sixth column. */
        public void setColumn6(String col) {
            columns[5] = col;
        }        
    }    
}
