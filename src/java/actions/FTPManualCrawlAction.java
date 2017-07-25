/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import webIndexActions.WebCrawlAction;

/**
 *
 * @author arpan
 */
public class FTPManualCrawlAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = "success";

    String username=null;

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session=request.getSession(false);
        username=(String)session.getAttribute("adminID");
        try
        {
            if(username!=null||username.compareTo("")!=0)
            {
                intranetFileSearch.FTPTriggerRunner trigger=new intranetFileSearch.FTPTriggerRunner();
                trigger.FTPManualCrawlTask();
            }
            else
                SUCCESS="fail";
        }
        catch(Exception e)
        {
            System.out.println("Exception in starting the crawling process :: "+e.getMessage());
            SUCCESS="fail";
        }

        return mapping.findForward(SUCCESS);
    }
}
