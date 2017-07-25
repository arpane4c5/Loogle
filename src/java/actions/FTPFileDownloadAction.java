/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import java.io.*;
import intranetFileSearch.*;
//import com.ftp.*;
import com.net.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import javax.servlet.ServletOutputStream;
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
public class FTPFileDownloadAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = "success";
    String path=null;
    String user=null;
    String pwd=null;
    String query=null;
    String destFile=null;
    //SmbFile inputFile=null;
    File outFile=null;
    FTPConnection con=null;
    FileOutputStream foutStream=null;
    ServletOutputStream sos=null;
    boolean status=true;
    QueryProcessor qp=new QueryProcessor();
    
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
            path=request.getParameter("path");
            qp.init();
            try
            {
                qp.connect();
                query="select username, password, ip from servers where IP=(select ip from files where path=\""+path+"\") and servertype=0;";
                ResultSet rs=qp.getRecords(query);
                query="select name from files where path=\""+path+"\" and dir=0;";
                ResultSet rs1=qp.getRecords(query);
                if(rs1.next())
                {
                    destFile=rs1.getString(1);
                
                    if(rs.next())
                    {
                        user=rs.getString(1);
                        pwd=rs.getString(2);
                        System.out.println(">>>>1 : User : "+user+"\tPwd :: "+pwd);
                        status=download(path,user,pwd,response);
                        if(!status)
                            SUCCESS="fail";
                    }
                    else
                        SUCCESS="fail";
                }
                else
                {
                    SUCCESS="fail";
                    System.out.println("Status = "+SUCCESS);
                }
            }
            catch(Exception excp)
            {
                System.err.println("Exception :: "+excp.getMessage());
                SUCCESS="fail";
            }
        }
        catch(Exception e)
        {
            SUCCESS="fail";
            System.err.println("Exception :: "+e.getMessage());
        }
        //download("10.10.28.241","anonymous","anon","pub/Server/mysql-server-5.0.77-3.el5.i386.rpm",new File("c:/mysql-server-5.0.77-3.el5.i386.rpm"));
        finally
        {
            if(qp!=null)
            {
                try
                {
                    qp.close();
                }
                catch(Exception ignored){}
            }
            System.out.println("Reached >>");
            return mapping.findForward(SUCCESS);
        }
    }

    public boolean download(String path, String user, String password,HttpServletResponse response)
    {
        if(user.equals("anonymous")&&(password==null||password.compareTo("")==0||password.equals(" ")))
        {
            password="anon";
        }
        try
        {
            StringBuffer sb = new StringBuffer( "ftp://" );
            // check for authentication else assume its anonymous access.
            if (user != null && password != null)
            {
                sb.append(user);
                sb.append(':');
                sb.append(password);
                sb.append('@');
            }
            sb.append(path);
            /*
            * type ==> a=ASCII mode, i=image (binary) mode, d= file directory
            * listing
            */
            sb.append( ";type=i" );
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            URL url = new URL( sb.toString() );
            URLConnection urlc = url.openConnection();
            bis = new BufferedInputStream(urlc.getInputStream());

            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition","attachment; filename="+destFile);
            sos=response.getOutputStream();

            int size=0;
            byte b[]=new byte[4096];
            while((size=bis.read(b))!=-1)
            {
                sos.write(b);
            }
            System.out.println("Loop finished!!");
            sos.flush();
            //bos = new BufferedOutputStream(new FileOutputStream(destFile));
            sos.close();
            return true;
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return false;
        }
    }
}
   /**
    * Download a file from a FTP server. A FTP URL is generated with the
    * following syntax:
    * ftp://user:password@host:port/filePath;type=i.
    *
    * @param ftpServer , FTP server address (optional port ':portNumber').
    * @param user , Optional user name to login.
    * @param password , Optional password for user.
    * @param fileName , Name of file to download (with optional preceeding
    *            relative path, e.g. one/two/three.txt).
    * @param destination , Destination file to save.
    * @throws MalformedURLException, IOException on error.
    */
    /*
   public void download( String ftpServer, String user, String password,
         String fileName, File destination ) throws MalformedURLException,
         IOException
   {
      if (ftpServer != null && fileName != null && destination != null)
      {
         StringBuffer sb = new StringBuffer( "ftp://" );
         // check for authentication else assume its anonymous access.
         if (user != null && password != null)
         {
            sb.append( user );
            sb.append( ':' );
            sb.append( password );
            sb.append( '@' );
         }
         sb.append( ftpServer );
         sb.append( '/' );
         sb.append( fileName );
         /*
          * type ==> a=ASCII mode, i=image (binary) mode, d= file directory
          * listing
          */
    /*
         sb.append( ";type=i" );
         BufferedInputStream bis = null;
         BufferedOutputStream bos = null;
         try
         {
            URL url = new URL( sb.toString() );
            URLConnection urlc = url.openConnection();

            bis = new BufferedInputStream( urlc.getInputStream() );
            bos = new BufferedOutputStream( new FileOutputStream(destination));
            System.out.println("----->"+destination.getName());
            int i;
            byte b[]=new byte[4026];
            while ((i = bis.read(b)) != -1)
            {
                System.out.println("b[0]:"+b[0]+"\tb[1]:"+b[1]);
               bos.write(b);
            }
            bos.flush();
            System.out.println("----->Finished");
         }
         finally
         {
            if (bis != null)
               try
               {
                  bis.close();
               }
               catch (IOException ioe)
               {
                  ioe.printStackTrace();
               }
            if (bos != null)
               try
               {
                  bos.close();
               }
               catch (IOException ioe)
               {
                  ioe.printStackTrace();
               }
         }
      }
      else
      {
         System.out.println( "Input not available" );
      }
   }
     */