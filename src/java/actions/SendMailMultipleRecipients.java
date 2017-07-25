/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.smtp.*;
import intranetFileSearch.QueryProcessor;
import java.sql.ResultSet;
/**
 *
 * @author arpan
 */
public class SendMailMultipleRecipients {

    private String host="mail.loogle.com";        //the mail domain
    private String username="admin";            //mail id of the administrator i.e admin@mail.loogle.com
    private String password="adminpass";        //his password.
    String server=null;
    QueryProcessor qp=new QueryProcessor();
    ResultSet rs=null;
    String query=null;
    String subj=null;


    Properties props = null;

    public void getUpdates(String ip, String files, int serverType)
    {
        try
        {
            subj="Loogle Updates for "+ip;
            qp.init();
            try
            {
                qp.connect();
                query="select email from subscribe where ip=\""+ip+"\" and serverType="+serverType+";" ;
                rs=qp.getRecords(query);
                send(rs,subj,files);
            }
            catch(Exception ex){}
        }
        catch(Exception e)
        {
            System.err.println("Connection Error..");
        }
    }

    public void send(ResultSet mailIdResultSet, String subj ,String content)
    {
        props = System.getProperties(); // Define SMTP host
        props.put("mail.smtp.host", host);
        props.put("username",username);
        props.put("password",password);

        try
        {
            // Get a session
            Session session = Session.getDefaultInstance(props, null);
            // Create a new
            MimeMessage message = new MimeMessage(session); // Populate

            message.setSubject(subj);
            //Change this to the updates on the server.
            message.setText(content);

            message.setFrom(new InternetAddress(username+"@"+host));
            while(mailIdResultSet.next())
            {
                System.out.print("To :: "+mailIdResultSet.getString(1)+"\t");
                message.addRecipient(Message.RecipientType.TO,new InternetAddress(mailIdResultSet.getString(1)));
            }
            // Send the message
            System.out.println("Content :: "+content);
            Transport.send(message);
            System.out.println("Message Sent.");
            //return true;
        }
        catch (MessagingException me)
        {
            System.err.println(me.getMessage());
            System.out.println("Mail Not Sent!");
            //return false;
        }
        catch(Exception e)
        {
            System.err.println("Empty ResultSet");
        }
    }
}
