/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import java.io.File;
import java.io.FileOutputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 *
 * @author arpan
 */
public class DownloadFile {
    
    boolean ret=true;
    private String SUCCESS = "success";
    SmbFile inputFile=null;
    File outFile=null;
    SmbFileInputStream smbFile=null;
    FileOutputStream fout=null;

    public static void main(String ...args)
    {

    }

    public boolean smbDownload(String src, String dest)
    {
        //String path="smb://10.10.28.188/movies/Hindi/Anand -(1970)- Hindi Movie - Eng Sub-Xvid/Anand -(1970)- Hindi Movie - Eng Sub-Xvid.avi";
        //path=request.getParameter("path");
        System.out.println("Got Path :: "+src);
        try
        {
            if(src!=null||src.compareTo("")!=0)
            {
                inputFile=new SmbFile(src);
                //InputStream reader=file.getInputStream();

                outFile=new File("c:\\"+inputFile.getName());

                byte b[]=new byte[4096];
                smbFile=new SmbFileInputStream(inputFile);
                fout=new FileOutputStream(outFile);

                int i=0;
                while((i=smbFile.read(b))!=-1)
                {
                    fout.write(b);
                }
                fout.close();
                smbFile.close();

            }
        }
        catch(Exception e)
        {
            try
            {
                fout.close();
                try
                {
                    smbFile.close();
                }
                catch(Exception exp){}
            }
            catch(Exception excp)
            {}

            System.err.println("Exception :: "+e.getMessage());
            ret=false;
        }
        finally
        {
            return ret;
        }
    }

}
/*
    SmbFile file = new SmbFile(url);
    OutputStream out = new SmbFileOutputStream(file);
    BufferedInputStream bis = new BufferedInputStream(is);
    while ((i = bis.read(b)) != -1) {
        out.write(b, 0, i);
    }
*/
/*
    public static void main(String args[])throws Exception
    {
        SMBFileDownloadAction s=new SMBFileDownloadAction();
        s.execute1(null, null, null, null);
    }
 */

/*
 * Code for writing the smb file to another path
 * import jcifs.smb.NtlmPasswordAuthentication;
 *
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

public class Logon {
    public static void main( String argv[] ) throws Exception {
        String user = "user:password";
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
        String path = "smb://my_machine_name/D/MyDev/test.txt";
        SmbFile sFile = new SmbFile(path, auth);
        SmbFileOutputStream sfos = new SmbFileOutputStream(sFile);
        sfos.write("Muneeb Ahmad".getBytes());
        System.out.println("Done");
    }
}

 *
 */