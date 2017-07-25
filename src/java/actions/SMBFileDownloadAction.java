/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import java.io.*;
import javax.servlet.ServletOutputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
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
public class SMBFileDownloadAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private String SUCCESS = "success";
    String path=null;
    SmbFile inputFile=null;
    File outFile=null;
    SmbFileInputStream smbFileStream=null;
    FileOutputStream foutStream=null;
    ServletOutputStream sos=null;

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

            path=request.getParameter("path");
            /*
            DownloadFile dwn=new DownloadFile();
            boolean status=dwn.smbDownload(path,);
            if(!status)
                SUCCESS="fail";
             */
        System.out.println("Got Path :: "+path);
        try
        {
            if(path!=null||path.compareTo("")!=0)
            {
                inputFile=new SmbFile(path);

                //InputStream reader=file.getInputStream();
                //outFile=new File(inputFile.getName());

                byte b[]=new byte[4096];
                System.out.println(">>>>>>>>1>>>>>>>");
                smbFileStream=new SmbFileInputStream(inputFile);
                String s=inputFile.getContentType();
                int length=inputFile.getContentLength();
                System.out.println("File Name :: "+inputFile.getName()+"\tLength :: "+length);
                
                //OutputStream output=response.getOutputStream();
                //foutStream=(FileOutputStream)output;
                //fout=new FileOutputStream(outFile);
                //response.setContentType("APPLICATION/OCTET-STREAM");
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition","attachment; filename="+inputFile.getName());

                sos=response.getOutputStream();
                
                int size=0;
                int len=4096;
                int begin=0;
                while((size=smbFileStream.read(b))!=-1)
                {
                    sos.write(b);
                }
                System.out.println("Loop finished!!");
                sos.flush();
                /*
                while((size=smbFileStream.read(b,0,len))!=-1)
                {
                    System.out.println("size : "+size+"\tbyte[0] :: "+b[0]+"\tbyte[1] :: "+b[1]);
                    sos.write(b, begin, size);
                    System.out.println("Written!!");
                    begin+=size;
                    //foutStream.write(b);
                }
                 */
                sos.close();
                smbFileStream.close();
            }
            else
                SUCCESS="fail";
        }
        catch(Exception e)
        {
            SUCCESS="fail";
            try
            {
                foutStream.close();
                try
                {
                    smbFileStream.close();
                }
                catch(Exception exp){}
            }
            catch(Exception excp)
            {}
            System.err.println("Exception1 :: "+e.getMessage());
        }
        finally
        {
            return mapping.findForward(SUCCESS);
        }
    }
}
