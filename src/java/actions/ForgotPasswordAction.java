/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import formbeans.ForgotPasswordActionForm;
import intranetFileSearch.QueryProcessor;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author arpan
 */
public class ForgotPasswordAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = "success";
    String content=null;
    String subject=null;
    String query=null;
    String email=null;
    boolean status=true;
    QueryProcessor qp=new QueryProcessor();
    SendMail mail=new SendMail();
    ResultSet rs=null;

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
            throws Exception
    {
        formbeans.ForgotPasswordActionForm fpaf=(ForgotPasswordActionForm)form;
        email=fpaf.getEmail();
        if(email!=null||email.compareTo("")!=0)
            email=email+"@loogle.com";

        try
        {
            qp.init();
            try
            {
                qp.connect();
                query="select username, password from users where Email=\""+email+"\";";
                rs=qp.getLoginRecords(query);
                if(rs.next())
                {
                    subject="Loogle Password Retrieval.";
                    content="You've requested for your login information which is as follows ::\n" +
                            "Username :: "+rs.getString(1)+"\n"+
                            "Password :: "+rs.getString(2);

                    status=mail.send(email, subject, content);
                    if(!status)
                        SUCCESS="fail";
                }
                else
                    SUCCESS="fail";
            }
            catch(Exception excp)
            {
                //System.out.println(">>>>1>>>>");
                SUCCESS="fail";
                System.err.println("Exception in connection :: "+excp.getMessage());
            }
            qp.close();
        }
        catch(Exception e)
        {
            System.err.println("Exception :: "+e.getMessage());
            //System.out.println(">>>>2>>>>>");
            SUCCESS="fail";
            try
            {
                qp.close();
                rs.close();
            }
            catch(Exception exp){}
        }
        finally
        {
            return mapping.findForward(SUCCESS);
        }
    }
}
