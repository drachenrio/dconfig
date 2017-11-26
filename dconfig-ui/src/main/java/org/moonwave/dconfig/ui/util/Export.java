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

import java.io.FileWriter;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeDao;
import org.moonwave.dconfig.model.DConfig;
import org.moonwave.dconfig.model.DConfigAttribute;

/**
 * Exports configuration data as a xml file for selected node.
 *
 * Sample export file format:
 * 
 * <?xml version="1.0" encoding="UTF-8"?>
 *   <configurations>
 *     <dconfig keyName="com.sun.java">
 *       <attribute name="(primary)" dataType="String">
 *       <attributeValue>http://www.sun.com</attributeValue>
 *       <comments></comments>
 *     </attribute>
 *   </dconfig>
 * </configurations>
 *
 * @author Jonathan Luo
 */
public class Export {

    private static final Log log = LogFactory.getLog(Export.class);

    public static String ROOTNAME = "configurations";
    public static String ELEM_ENTRYNAME = "dconfig";
    public static String ATTR_KEYNAME = "keyName";
    public static String ATTR_INHERITED = "inherited";
    public static String ELEM_ATTRIBUTE = "attribute";
    public static String ATTR_NAME = "name";
    public static String ATTR_DATATYPE = "dataType";
    public static String ELEM_ATTRIBUTE_VALUE = "attributeValue";
    public static String ELEM_COMMENTS = "comments";    
    
    Document document;
    Element root;
    String exportFile;
    DefaultMutableTreeNode node;
    
    
    /**
     * Creates an <code>Export</code> instance.
     *
     * @param outputFile output file name.
     * @param node currently selected tree node. This is where the export starts at.
     */
    public Export(String outputFile, DefaultMutableTreeNode node) {
        this.exportFile = outputFile;
        this.node = node;
    }
    
    /**
     * Creates export document and write to hard disk.
     */
    public boolean doExport() {
        boolean bRet = false;
        try {
            createDocument();
            write();
            bRet = true;
        }
        catch (Exception e) {

        }
        return bRet;
    }
            
    private Document createDocument() {
        document = DocumentHelper.createDocument();
        root = document.addElement(ROOTNAME);

        List nodeList = TreeUtil.getNodesByPreorder(this.node);
        for (int i = 0; i < nodeList.size(); i++) {            
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeList.get(i);
            DConfig cfg = (DConfig) node.getUserObject();
            if (cfg.getKey().isRoot())
                continue;
            Element entry = createEntry(cfg.getKeyName(), cfg.getKey().getInherited());
            List attributes = cfg.getOwnAttributes();
            for (int attrIdx = 0; attrIdx < attributes.size(); attrIdx++) {
                createAttribute(entry, (DConfigAttribute) attributes.get(attrIdx));
            }
            entry.addText("\n  ");
        }
        root.addText("\n");
        return document;
    }

    private Element createEntry(String keyName, String inherited) {
        Element cfgItem = root.addText("\n  ").addElement(ELEM_ENTRYNAME)
          .addAttribute(ATTR_KEYNAME, keyName)
          .addAttribute(ATTR_INHERITED, inherited);
        return cfgItem;
    }

    private void createAttribute(Element entry, DConfigAttribute attribute) {
        Element attributeItem = entry.addText("\n    ").addElement(ELEM_ATTRIBUTE)
          .addAttribute(ATTR_NAME, attribute.getAttributeName())
          .addAttribute(ATTR_DATATYPE, DConfigDataTypeDao.getDataTypeNameByAlias(attribute.getDataTypeAlias()));

        attributeItem.addText("\n      ").addElement(ELEM_ATTRIBUTE_VALUE)
          .addText((attribute.getAttributeValue() != null) ? attribute.getAttributeValue() : "");

        attributeItem.addText("\n      ").addElement(ELEM_COMMENTS)
          .addText((attribute.getComments() != null) ? attribute.getComments() : "");
        attributeItem.addText("\n    ");
    }

    private synchronized void write() throws java.io.IOException {
        FileWriter writer = new FileWriter(exportFile);
        document.write(writer);
        writer.flush();
        writer.close();
    }
}