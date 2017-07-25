/*Sending the mail to the user for the particular server for which he has subscribed.
 Any new files that are added to the server will be notified to the user.
 The Locally deployed mail server is used.
 Internet mailing can also be implemented without a proxy.
 */

package actions;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.smtp.*;
//import com.sun.mail.util.*;

public class SendMail
{

    private String host="mail.loogle.com";        //the mail domain
    private String username="admin";            //mail id of the administrator i.e admin@mail.loogle.com
    private String password="adminpass";        //his password.
    String server=null;
    
    Properties props = null;

    public boolean send(String sendToMailID, String subj ,String content)
    {
        if(username==null||username.equals("")||password==null||password.equals(""))
        {
            System.out.println("Mail Not sent.");
            return false;
        }

        props = System.getProperties(); // Define SMTP host
        props.put("mail.smtp.host", host);
        props.put("username",username);
        props.put("password",password);
        /*
        props.put("proxySet", "true");
        props.put("proxyHost", "192.200.12.8");
        props.put("proxyPort", "8080");
        */

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
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(sendToMailID));
            // Send the message
            Transport.send(message);
            System.out.println("Message Sent.");
            return true;
        }
        catch (MessagingException me)
        {
            System.err.println(me.getMessage());
            return false;
        }
    }


    /*
    String proxyEncoded=null;
         if (true)
         {
           proxyEncoded = crtProxyEncoding("192.200.12.8",8080,"be06it008","be06it008");
           System.out.println("using proxy:"+proxyEncoded);
        }
        if (true)
            System.setRequestProperty("Proxy-Authorization", "Basic " + proxyEncoded);
*/
        //props.put("mail.smtp.auth","true");

    /*
        props.put("mail.transport.protocol", "smtp");

        props.put("mail.host", host);
        props.put("mail.smtp.port", "25");

        props.put("mail.user",username);
        props.put("mail.password",password);
     */
        //props.put("proxySet", "true");
        //props.put("proxyHost", "192.200.12.8");
        //props.put("proxyPort", "8080");
        //props.put

/*
    static String crtProxyEncoding(String proxyhost, int proxyPort, String proxyUser, String proxyPassword )
   {
        // A base 64 encoder from the sun.misc
        BASE64Encoder b64E = new BASE64Encoder();
        // This applet/application goes via the proxy server
        System.getProperties().put( "proxySet", "true" );
        System.getProperties().put( "proxyHost", proxyhost );
        System.getProperties().put( "proxyPort", Integer.toString(proxyPort) );

        // Get proxy userid and possible password from the user and encode it
        String passw = ":" + proxyPassword;
        if (":".equals(passw))
            passw ="";
        String proxyEncoded = new String(b64E.encodeBuffer(new String(proxyUser+ passw).getBytes()));
        return proxyEncoded;
   }
*/
}
 
