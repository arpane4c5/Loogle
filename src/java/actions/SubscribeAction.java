/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import javax.servlet.http.HttpSession;
import intranetFileSearch.QueryProcessor;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author arpan
 */
public class SubscribeAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = "fail";
    QueryProcessor qp=new QueryProcessor();
    ActionMessage msg=null;
    String query=null;
    ResultSet rsServers=null;
    ResultSet rsEmail=null;
    String param=null;
    String username=null;
    String query1=null;
    String email=null;

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
        try
        {

            HttpSession session=request.getSession(false);
            username=(String)session.getAttribute("userID");
            if(username!=null)
            {
                //Connecting
                qp.init();
                qp.connect();
                //getting the servers ip and type for creating the parameter list.
                query="select Ip, ServerType from servers;";
                rsServers=qp.getRecords(query);
                //getting the email of the logged in user.
                query="select email from users where username=\""+username+"\";";
                rsEmail=qp.getRecords(query);
                if(rsEmail.next())
                {
                    while(rsServers.next())
                    {
                        param=rsServers.getString(1)+":"+rsServers.getInt(2)+":1";
                        String paramValue=(String)request.getParameter(param);
                        System.out.println(">>>"+param);
                        
                        if(!(paramValue==null))
                        {
                            query="insert into subscribe(Ip, serverType, Email) values (\""+rsServers.getString(1)+"\"," +
                                "\""+rsServers.getInt(2)+"\",\""+rsEmail.getString(1)+"\");";
                            int result=qp.insertData(query);
                            
                        }

                    }
                    //System.out.println(">>>>>2>>>Reached !!");
                    SUCCESS="success";
                }
                else
                {
                    msg=new ActionMessage("LoginError","You are not logged in.");
                }
                qp.close();
            }
        }
        catch(Exception e)
        {
            msg=new ActionMessage("ConnectionError","Unable to retrieve the values from the database.");
            System.out.println(e.getMessage());
        }

        return mapping.findForward(SUCCESS);
    }


}
