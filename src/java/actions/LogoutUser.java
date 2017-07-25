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

/**
 *
 * @author arpan
 */
public class LogoutUser extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = "success";
    
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

        HttpSession session = request.getSession(false);
        String fromPage = (String) request.getParameter("fromPage");
        System.err.println("Logout :fromPage = " + fromPage);

        if (fromPage == null) {
            fromPage = "index.jsp";
        }

        if (session != null)
        {
            if (session.getAttribute("userID") != null)
            {
                System.err.println("Logout : Logging out :"
                        + session.getAttribute("userID").toString());
                session.removeAttribute("userID");
            }
            else if(session.getAttribute("adminID")!=null)
            {
                System.err.println("Logout : Logging out : "+session.getAttribute("adminID").toString());
                session.removeAttribute("adminID");
            }
            else
            {
                System.err.println("Logout : Logging out without any userID");
                SUCCESS="fail";
            }
            session.invalidate();
        }
        
        return mapping.findForward(SUCCESS);
    }
}
