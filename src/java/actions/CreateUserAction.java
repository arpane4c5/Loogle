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
 * @author root
 */
public class CreateUserAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = "success";

    String id=null;
    String username=null;
    String password=null;

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

        //values of the bean
        formbeans.CreateUserActionForm cuaf=(formbeans.CreateUserActionForm)form;
        username=cuaf.getUsername();
        password=cuaf.getPassword();

        System.out.println("User :: "+username);
        System.out.println("Password :: "+password);
        
        try
        {
            HttpSession session=request.getSession(false);
            id=(String)session.getAttribute("adminID");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
         
        System.out.println(" id!=null :: ");
        if(System.getProperty("os.name").equals("Linux")&&id!=null)
        {
            CreateUser cu=new CreateUser();
            boolean created=cu.create(username, password);
                //System.out.println("Exception in starting the crawling process :: "+e.getMessage());
            if(created)
                SUCCESS="success";
            else
                SUCCESS="fail";
        }
        else
            SUCCESS="fail";

        return mapping.findForward(SUCCESS);
    }
}
