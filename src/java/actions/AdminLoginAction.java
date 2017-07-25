/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import java.sql.*;
import java.util.Date;
import intranetFileSearch.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Arpan
 */
public class AdminLoginAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = null;
    Connection conn=null;
    ResultSet rs=null;
    ResultSet rs1=null;
    PreparedStatement pst=null;
    PreparedStatement pst1=null;
    HttpSession session=null;

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

    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {

        // Forces caches to obtain a new copy of the page from the origin server
        response.setHeader("Cache-Control", "no-cache");
        // Directs caches not to store the page under any circumstance
        response.setHeader("Cache-Control", "no-store");
        // HTTP 1.0 backward compatibility
        response.setHeader("Pragma", "no-cache");
        // Causes the proxy cache to see the page as "stale
        response.setDateHeader("Expires", 0);
        response.setContentType("text/html;charset=UTF-8");
        QueryProcessor qp = new QueryProcessor();
        SUCCESS="fail";
        formbeans.LoginActionForm laf=(formbeans.LoginActionForm)form;
        System.out.println("!!!!!!!!!!!!!!!!");
        String username=laf.getUsername();
        String password=laf.getPassword();
        String timestamp=laf.getTimestamp();
        System.out.println("Login DateTime :: "+timestamp);

        try
        {
            if (username == null || password == null || username.compareTo("") == 0
                        || password.compareTo("") == 0)
            {
                SUCCESS="fail";
                System.out.println(">>>>a>>>>>");
            }

            else
            {

                qp.init();

                try
                {

                    qp.connect();
                    System.out.println(">>>>>>>>>>XX>>>>>>>>>>>");
                    String query = "select LoginTime from administrator_acc where Username=\""+ username + "\" and Password=\"" + password + "\";";
                        try
                        {
                            ResultSet rs = qp.getLoginRecords(query);
                            System.out.println(">>>>>>>>>>YY>>>>>>>>>>>");
                            if (rs.next() == false)
                            {
                                SUCCESS="fail";
                                System.out.println(">>>>b>>>>>");
                            }
                            else
                            {
                                

                                Timestamp t = rs.getTimestamp(1);
                                String tout = t.toString();
                                System.err.println("LoginProcessor :Internal Date " + tout);
                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                java.util.Date din = sdf.parse(timestamp);
                                java.util.Date dout = sdf.parse(tout);
                                System.out.println(">>>>c>>>>>");
                                if (din.after(dout) == true)
                                {
                                    query = "update administrator_acc set LoginTime=\"" + timestamp
                                            + "\" where Username=\"" + username + "\"and"
                                            + " Password=\"" + password + "\";";

                                    int rows = qp.updateData(query);
                                    System.err.println("LoginProcessor :Number of rows updated :"
                                            + rows + "\n....Forwarding to Home Page");
                                    session=request.getSession(true);
                                    session.setAttribute("adminID", username);
                                    SUCCESS="success";

                                }
                                else
                                {
                                    SUCCESS="fail";
                                    //rd=getServletContext().getRequestDispatcher("fileSearch.do");
                                    System.err.println("LoginProcessor :Invalid attempt to login\n"+"....Forwarding to Home Page");
                                }
                            }
                        }
                        catch (SQLException sqle)
                        {
                            SUCCESS="fail";
                            //rd = getServletContext().getRequestDispatcher("/ErrorPage.jsp?id=1");
                            System.err.println("LoginProcessor : Could not execute query ("+ query + ") on the database\n"+ sqle + "\n....Forwarding to Error Page - Error Code 1");
                        }
                        finally
                        {
                            qp.close();
                        }
                    }
                    catch (Exception f)
                    {
                        System.err.println("LoginProcessor : Could not connect to database\n" + f
                                + "....Forwarding to Error Page - Error Code 1");
                    }
                }
        }
            catch(Exception e)
            {
                System.out.println("Error in processing");
            }

        return mapping.findForward(SUCCESS);

    }
}


