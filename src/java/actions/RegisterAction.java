/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import java.sql.*;
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
public class RegisterAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    //private static final String SUCCESS = "success";
    private String SUCCESS = null;
    Connection conn=null;
    ResultSet rs=null;
    ResultSet rs1=null;
    PreparedStatement pst=null;
    PreparedStatement pst1=null;
    HttpSession session=null;
    boolean mailVerify=false;

    
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

        SUCCESS="fail";
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
        
        formbeans.RegisterActionForm raf=(formbeans.RegisterActionForm)form;

        String username=raf.getUsername();
        String password=raf.getPassword();
        String email=raf.getEmail()+"@loogle.com";

        try
        {
            qp.init();
            qp.connect();
            //Finding registration date and time and parsing in yyyy-mm-dd hh:mm:ss format.
            java.util.Date loginDate=new java.util.Date();
            System.out.println("Date :: "+loginDate.toString());
            java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date=sdf.format(loginDate);

            System.out.println("Date1 :: "+date);

            //Insertion of query.
            String query="insert into users(Username, Password, Email, LoginTime) values (\""+username+"\",\""+password+"\",\""+email+"\",\""+date+"\");";

            int a=qp.insertData(query);

            session=request.getSession(true);
            session.setAttribute("userID", username);
            SUCCESS="success";
            //mailing code
            
            try
            {
                String subj="Welcome to the Loogle family!";
                String content="You can log in and subscribe to the feeds from the servers and receive regular updates." +
                        "\nYour login information is as follows :: \n" +
                        "Username :: "+username+
                        "\nPassword :: "+password;
                SendMail sm=new SendMail();
                mailVerify=sm.send(email,subj,content);
            }
            catch(Exception e)
            {
                System.out.println("Mail not sent. Make sure that the mail id is correct connected to a local mail server.");
                mailVerify=false;
            }

            System.out.println(query);
            
        }
        catch(Exception e)
        {
            System.out.println("Insertion failed. Exception "+e.getMessage());
        }
        finally
        {
            if(!mailVerify)
                SUCCESS="mailFailed";
            qp.close();
            return mapping.findForward(SUCCESS);
        }
    }
}
