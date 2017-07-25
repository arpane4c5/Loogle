package intranetFileSearch;

/**
 *
 * @author Arpan Gupta
 */
import java.io.*;
import com.net.IRemoteFile;
import com.ftp.FTPConnection;
import java.util.*;

public class FTPList {

    FTPConnection con = new FTPConnection();

    public FTPList() {
    }

    public static void main(String[] args)
    {
        //ScanDaily obj = new ScanDaily();
        String[] args2 = new String[4];
        args2[0] = "10.10.28.237";
        args2[1] = "mango";
        args2[2] = "nettech";
        if (args2.length < 3)
        {
            System.out.println("Please enter a hostname, username and password");
        }
        else
        {
            FTPList lister = new FTPList();
            lister.doStuff(args2[0], args2[1], args2[2]);
        }
    }

    private void doStuff(String host, String user, String pass)
    {
        try
        {
            con.openConnection(host, 21);
            con.initStream();
            con.login(user, pass);
            PrintListing("");

        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void PrintListing(String path)
    {
        System.out.println(">>>>>>1>>>>>>>");
        try
        {
            List list = con.list(path);
            Iterator iter = list.iterator();
            System.out.println(">>>>>>2>>>>>>>");
            while (iter.hasNext())
            {
                Object item = iter.next();
                com.net.UnixRemoteFile temp = (com.net.UnixRemoteFile) item;
                System.out.println(temp.getAttrs().getPath() + (temp.getFilename()));
            }
            Iterator iter2 = list.iterator();
            while (iter2.hasNext()) {
                Object item = iter2.next();
                com.net.UnixRemoteFile temp = (com.net.UnixRemoteFile) item;
                if (temp.getAttrs().isDir() == true) {
                    PrintListing(path + temp.getFilename() + "/");
                }
            }
            System.out.println(">>>>>>>>>>3 >>>>>>>>>");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
