/*
 * LoginAction.java
 *
 * Created on September 6, 2009, 11:22 PM
 */

package actions;
import java.sql.*;
import java.util.Date;
import intranetFileSearch.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.catalina.Session;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
/**
 *
 * @author arpan
 * @version
 */

public class LoginAction extends Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = null;
    Connection conn=null;
    ResultSet rs=null;
    ResultSet rs1=null;
    PreparedStatement pst=null;
    PreparedStatement pst1=null;
    HttpSession session=null;
    ActionErrors errors=null;
    
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

            /*
            Class.forName("com.mysql.jdbc.Driver");
            
            //To be altered
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/animalhusbandry","root","arpan");
            System.out.println(">>>>1>>>>>");
             */

             qp.init();

                try
                {
                    qp.connect();
                    String query = "select LoginTime from users where Username=\""+ username + "\" and Password=\"" + password + "\";";
                        try
                        {
                            ResultSet rs = qp.getLoginRecords(query);
                            if (rs.next() == false)
                            {
                                SUCCESS="fail";
                                errors=new ActionErrors();
                                errors.add("invalidLogin",new ActionMessage("error.login.incorrect"));
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
                                    query = "update users set LoginTime=\"" + timestamp
                                            + "\" where Username=\"" + username + "\"and"
                                            + " Password=\"" + password + "\";";
                                    
                                    int rows = qp.updateData(query);
                                    System.err.println("LoginProcessor :Number of rows updated :"
                                            + rows + "\n....Forwarding to Home Page");
                                    session=request.getSession(true);
                                    session.setAttribute("userID", username);
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
/*
            pst=conn.prepareStatement("Select password from user_login_details where Email_id=?");
            pst1=conn.prepareStatement("Select First_Name from user_details where Email_Id=?");
            pst.setString(1,username);
            pst1.setString(1,username);
            rs=pst.executeQuery();
            if(!rs.equals(null))
            {
            if(rs.next())
            {
                
                
                System.out.println(rs.getString("password"));
                System.out.println(password);
                
                if(rs.getString("password").equals(password))
                {
                    
                    SUCCESS="success";

                    rs1=pst1.executeQuery();
                    if(rs1.next())
                    {
                        //request.setAttribute("userName",rs1.getString("First_Name"));
                        HttpSession ses=request.getSession(true);
                        System.out.println(ses.getId());
                        ses.setAttribute("userName",rs1.getString("First_Name"));
                        ses.setAttribute("emailId",username);
                    }
                }
                else
                    SUCCESS="fail";
            }
            }
        }
        catch(Exception e)
        {
            System.out.println("Connection Problem");
            SUCCESS="fail";
        }
        finally
        {
            try
            {
                if(conn!=null)
                    conn.close();
            }
            catch(SQLException ignored)
            {
                
            }
        }
 */
            catch(Exception e)
            {
                System.out.println("Error in processing");
            }

        return mapping.findForward(SUCCESS);
        
    }
}
