/*
 * DConfigDemoAction.java
 *
 * Created on October 28, 2007, 9:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.moonwave.dconfig.struts;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.moonwave.dconfig.dao.CacheManager;

/**
 *
 * @author Jonathan Luo
 */
public class DConfigDemoAction extends Action {
    
    private static final Log log = LogFactory.getLog(DConfigDemoAction.class);
    
    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm form,
                                 HttpServletRequest request, 
                                 HttpServletResponse response)
                        throws IOException, ServletException
    {
        request.setAttribute("firstName", request.getParameter("firstName"));
        request.setAttribute("lastName", request.getParameter("lastName"));
        String submitType = request.getParameter("submitType");
        boolean postback = (submitType != null) ? true : false;
        if (!postback) { // initial request
            log.info("Calling CacheManager.load()");
            CacheManager.load();
        }
        ActionForward forward = mapping.findForward("jsp.dconfigDemo");
        return forward;
    }
}
