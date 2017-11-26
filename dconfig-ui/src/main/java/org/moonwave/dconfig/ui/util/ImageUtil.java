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

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.util.*;

/**
 * Helper class for loading images and icons.
 *
 * @author Jonathan Luo
 */
public class ImageUtil {

    private static final Log log = LogFactory.getLog(ImageUtil.class);	
    private static Map<String, ImageIcon> imageMap = new HashMap<String, ImageIcon>(); 

    public ImageUtil() {
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * 
     * @param imagePath
     * @param description
     * @return <code>ImageIcon</code> created on success; null otherwise.
     */
    public static ImageIcon createImageIcon(String imagePath, String description) {
    	ImageIcon icon = imageMap.get(imagePath);
    	if (icon == null) {
            ClassLoader cl = ImageUtil.class.getClassLoader();
            java.net.URL imgURL = ImageUtil.class.getClassLoader().getResource(imagePath);
            if (imgURL == null)
                imgURL = ClassLoader.class.getResource("/" + imagePath);
            
            if (imgURL != null) {
                if (AppState.isVerbose())
                    log.info(imgURL.toString());
                icon = new ImageIcon(imgURL, description);
                imageMap.put(imagePath, icon);
            } else {
                log.error("Couldn't find file: " + imagePath);
            }
    	}
    	return icon;
    }
}
		