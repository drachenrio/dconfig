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

package org.moonwave.dconfig.ui.util;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.datatype.DatatypeDocumentFactory;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.moonwave.dconfig.dao.DConfigReader;
import org.moonwave.dconfig.dao.springfw.DConfigDao;
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.dao.springfw.DataSourceManager;
import org.moonwave.dconfig.dao.springfw.SmartDataSourceEx;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigKey;
import org.springframework.jdbc.datasource.DataSourceUtils;


/**
 * Imports configuration data to database.
 *
 * @author Jonathan Luo
 */
public class Import {

    private static final Log log = LogFactory.getLog(Import.class);
    
    String importFile;
    
    /**
     * Creates an <code>Import</code> instance.
     *
     * @param importFile import file name.
     */
    public Import(String importFile) {
        this.importFile = importFile;
    }
        
    /**
     * Parses import document and saves data to database.
     */
    public synchronized boolean doImport() {
        boolean bRet = false;
        Connection conn = null;
        try {
            // update caches before import
            DConfigReader.load();
            
            DataSource dataSource = DataSourceManager.getDataSource();
            conn = DataSourceUtils.getConnection(dataSource);
            conn.setAutoCommit(false);
            ((SmartDataSourceEx) dataSource).setConnectionClose(false);

            SAXReader reader = new SAXReader();
            reader.setDocumentFactory( DatatypeDocumentFactory.getInstance() );
            Document schema =  reader.read(importFile);
            XPath xpathSelector = DocumentHelper.createXPath("//" + Export.ELEM_ENTRYNAME);
            List xsdElements = xpathSelector.selectNodes(schema);

            List<DConfigAttribute> attributes = null;
            for (int i=0; i < xsdElements.size(); i++) {
                DefaultElement obj = (DefaultElement)xsdElements.get(i); // dconfig element
                Attribute attributeKeyName = obj.attribute(Export.ATTR_KEYNAME);
                String keyName = attributeKeyName.getValue();

                Attribute attributeInherited = obj.attribute(Export.ATTR_INHERITED);
                String inherited = (attributeInherited != null) ? attributeInherited.getValue() : DConfigKey.inheritedN;

                DConfig dconfig = new DConfig();
                DConfigKey key = new DConfigKey();
                key.setKeyName(keyName);
                key.setInherited(inherited);
                dconfig.setKey(key);
                
                attributes = new ArrayList<DConfigAttribute>();
                int nodeCount = obj.nodeCount(); 
                for (int k = 0; k < nodeCount; k++) {
                    Object node = obj.node(k);
                    if (node instanceof Element) {
                        DConfigAttribute dcfgAttribute = getAttribute((Element)node);
                        //System.out.println (" DConfigAttribute: " + dcfgAttribute.toString());
                        attributes.add(dcfgAttribute);
                    }
                }
                dconfig.setAttributes(attributes);
                new DConfigDao(dataSource).importToDb(dconfig);
                attributes.clear();
            }
            conn.commit();
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
            DConfigReader.unload();            
            bRet = true;
    	} catch (Exception e) {
            log.error(e, e);
            try {
                log.info("conn.rollback()");
                conn.rollback();
            } catch (Exception e1)
            {
            }
    	} finally {
            DbUtils.closeQuietly(conn);
        }
        return bRet;
    }
    
    private static DConfigAttribute getAttribute(Element node) {
        DConfigAttribute dcfgAttribute = new DConfigAttribute();
        Element attr = (Element) node;
        dcfgAttribute.setAttributeName(attr.attribute(Export.ATTR_NAME).getValue());
        DConfigDataTypeDao dao = new DConfigDataTypeDao();
        String alias = dao.getAliasByDataTypeName(attr.attribute(Export.ATTR_DATATYPE).getValue());
        dcfgAttribute.setDataTypeAlias(alias);

        Element attributeValue = attr.element(Export.ELEM_ATTRIBUTE_VALUE);
        String data = "";
        if (attributeValue != null) {
            data = attributeValue.getStringValue();
            data = attributeValue.getText();
            dcfgAttribute.setAttributeValue(data);
            //System.out.println (", attributeValue: " + data);
        }
        Element comments = attr.element(Export.ELEM_COMMENTS);
        if (comments != null) {
            data = comments.getStringValue();
            data = comments.getText();
            dcfgAttribute.setComments(data);
        }
        return dcfgAttribute;
    }
}