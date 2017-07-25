/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import intranetFileSearch.QueryProcessor;
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
public class ChangePasswordAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = "fail";
    String currPassword=null;
    String newPassword=null;
    String username=null;
    String query=null;
    QueryProcessor qp=null;
    
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

        formbeans.ChangePasswordActionForm cpaf=(formbeans.ChangePasswordActionForm)form;
        currPassword=cpaf.getCurrPassword();
        newPassword=cpaf.getNewPassword();
        HttpSession session=request.getSession(false);
        username=(String)session.getAttribute("userID");
        qp=new QueryProcessor();
        if(username!=null&&username.compareTo("")!=0)
        {
            try
            {
                qp.init();
                try
                {
                    qp.connect();
                    query = "update users set password=\"" + newPassword + "\" where username=\"" + username + "\" and password=\"" + currPassword + "\";";
                    int i=qp.updateData(query);
                    System.out.println("Rows updated::"+i);
                    if(i>0)
                        SUCCESS="success";
                }
                catch (Exception exp)
                {
                    System.err.println(exp.getMessage());
                }
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }

        return mapping.findForward(SUCCESS);
    }
}
