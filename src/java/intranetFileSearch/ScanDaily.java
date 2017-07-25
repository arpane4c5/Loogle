package intranetFileSearch;

import com.ftp.FTPConnection;
import com.net.IRemoteFile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Performs daily scan of file sharing servers and updates the database.
 * @author Arpan Gupta
 */
public class ScanDaily {

    public static final long UPDATE_THRESHOLD = 5 * 24 * 60 * 60 * 1000L;
    private FTPConnection con = new FTPConnection();
    private QueryProcessor qp = new QueryProcessor();
    private IRemoteFile parent;
    private String user,  host,  pass, port;
    private long sizeOfIp = 0;
    private static final String SPECIAL_CHARACTER = ":;'=\"\\?*&()[]{}<>";
    // Update threshold is set to 5 days ( in milliseconds )
    private static final int FTP_PORT = 21;

    /**
     * Scans the ranges written in configuration file to find list of servers.
     * @return List of ftp servers.
     */
    private String[][] getIpFromFile()throws FileNotFoundException,IOException
    {
        //  Scan the configuration file for list of ranges.
        String[][] ret=new String[4][];

        try
        {
            
            //FileOutputStream fout=new FileOutputStream("ABC.txt");
            
            FileInputStream fstream=new FileInputStream("c:/Loogle/FTPConfigIP.txt");
            DataInputStream in=new DataInputStream(fstream);
            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i=0;
            
            while((strLine=br.readLine())!=null)
            {
                
                ret[i]=strLine.split(" ");
                //System.out.println("Pattern Syntax Excp after this."+ret[i][0]);
                i++;
            }
            
            in.close();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            return ret;
        }

        /*
        ret[0] = "10.10.28.189";
         */
        
    }

    /**
     * Returns the user-id and password of an ip address provided. If the user
     * and password doesnt exist in the database, returns anonymous instead.
     * @param ip ip address of the server
     * @return User and password corresponding to that ip.
     */
    private String[] getUserAndPassword(final String ip) {
        String[] ret = new String[2];
        ret[0] = "anonymous";
        ret[1] = "";
        return ret;
    }

    public ScanDaily(final String ip) {

        try {
            qp.init();
            qp.connect();
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        String[] ret = getUserAndPassword(ip);
        user = ret[0];
        pass = ret[1];
        host = ip;
        doRecursiveScan();

        try {
            qp.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public ScanDaily()throws FileNotFoundException,IOException,NumberFormatException
    {
        String[][] ips = getIpFromFile();
        //System.out.println(ips[0][0]+"\t"+ips[0][1]+"\t"+ips[0][2]);
        try 
        {
            qp.init();
            qp.connect();
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }

        try
        {
        for (int i = 0; i < ips.length; i++)
        {
            port=null;
            user="anonymous";
            pass="";
            for(int j=0;j < ips[i].length;j++)
            {
                System.out.println(">>>>>>>Pre Connect>>>>>>>");
                //String[] ret = getUserAndPassword(ips[i]);
                if(j==0 && ips[i][0]!=null && ips[i][0].compareTo("")!=0)
                    host=ips[i][0];
                if(j==1 && ips[i][1]!=null && ips[i][1].compareTo("")!=0)
                    user=ips[i][1];
                if(j==2 && ips[i][2]!=null && ips[i][2].compareTo("")!=0)
                    pass=ips[i][2];
                if(j==3 && ips[i][3]!=null && ips[i][3].compareTo("")!=0)
                    port=ips[i][3];
            }
            
                doRecursiveScan();
        }
        
        try {
            qp.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        }
        catch(Exception excp)
        {
            System.err.println("Exception :: "+excp.getMessage());
        }

    }

    private void doRecursiveScan()throws NumberFormatException
    {
        try {
            System.out.println(">>>>>>>Connect>>>>>>>");
            if(port!=null)
            {
                con.openConnection(host, Integer.parseInt(port));
            }
            else
            {
                con.openConnection(host, FTP_PORT);
            }
            System.out.println(">>>>>>>>>>32>>>>>>");
            con.initStream();
            boolean tmp = con.login(user, pass);
            System.out.println(">>>>>>>1>>>>>>>");
            if (tmp == false)
            {
                try
                {
                    String q = "select * from servers where ServerType=0 and Ip='" + host + "'";
                    ResultSet rs = qp.getRecords(q);
                    if (rs != null && rs.next() == true) {
                        q = "Update servers set Status=0 where ServerType=0 and Ip='" + host + "'";
                        qp.updateData(q);
                    }
                    rs.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            } 
            else
            {
                parent = null;
                recursiveScan2("", 0);
                sizeOfIp = 0;
                parent = null;
                recursiveScan("", 0);
            }
        } catch (Exception ex) {
            try {
                String q = "select * from servers where ServerType=0 and Ip='" + host + "'";
                ResultSet rs = qp.getRecords(q);
                if (rs != null && rs.next() == true) {
                    q = "Update servers set Status=0 where ServerType=0 and Ip='" + host + "'";
                    qp.updateData(q);
                }
                rs.close();
                return;
            } catch (Exception e) {
                System.out.println(e);
                return;
            }
        }
        finally
        {
            try
            {
                qp.close();
            }
            catch(Exception e1){}
        }
    }

    private void recursiveScan2(final String path, final int depth) {
        System.out.println("Entered");
        try {
            if (depth > 40) {
                return;
            }
            List<IRemoteFile> list = con.list(path);
            if (list.isEmpty() == true) {
                return;
            }
            
            Iterator iter = list.iterator();
            while (iter.hasNext())
            {
                Object item = iter.next();
                com.net.RemoteFile temp;

                if(com.net.RemoteFile.isRedmond())
                    temp = (com.net.MSRemoteFile) item;
                else
                    temp=(com.net.UnixRemoteFile)item;
                java.util.Date d = new java.util.Date();
                long d1 = temp.getAttrs().getMilliSeconds(), d2 = d.getTime();
                if (d2 - d1 < UPDATE_THRESHOLD && temp.getAttrs().isDir() == false)
                {
                    String q = "select * from files where ServerType=0 and Ip='" + host
                            + "' and Path='" + escapeSpecialCharacters(temp.getAttrs().getPath()
                            + (temp.getFilename())) + "'";
                    System.out.println(q);
                    ResultSet rs = qp.getRecords(q);
                    if (rs != null && rs.next() == false)
                    {
                        rs.close();
                        q = "select * from fileadded where Name='"
                                + escapeSpecialCharacters(temp.getFilename()) + "' and Size="
                                + Long.toString(temp.getAttrs().getSize());
                        ResultSet rs2 = qp.getRecords(q);
                        if (rs2 != null && rs2.next() == false)
                        {
                            q = "insert into fileadded (Name,Size,AdditionTime) values('"
                                    + escapeSpecialCharacters(temp.getFilename()) + "',"
                                    + Long.toString(temp.getAttrs().getSize()) + ",'"
                                    + getTimeStamp(temp.getAttrs().getMilliSeconds()) + "')";
                            qp.updateData(q);
                        }
                        rs2.close();
                    }
                }
            }
            Iterator<IRemoteFile> iter2 = list.iterator();
            while (iter2.hasNext())
            {
                IRemoteFile item = iter2.next();
                com.net.RemoteFile temp;
                if(com.net.RemoteFile.isRedmond())
                    temp = (com.net.MSRemoteFile) item;
                else
                    temp=(com.net.UnixRemoteFile)item;

                //com.net.UnixRemoteFile temp = (com.net.UnixRemoteFile) item;
                java.util.Date d = new java.util.Date();
                long d1 = temp.getAttrs().getMilliSeconds(), d2 = d.getTime();
                if (temp.getAttrs().isDir() == true && d2 - d1 < UPDATE_THRESHOLD)
                {
                    boolean flag = false;
                    if (flag == false)
                    {
                        IRemoteFile temporary = parent;
                        parent = item;
                        recursiveScan2(path + temp.getFilename() + "/", depth + 1);
                        parent = temporary;
                    }
                }

            }
            System.out.println(">>>>>>> MS REMOte File>>>>>>>>");
        } 
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void recursiveScan(final String path, final int depth) {
        if (depth > 40) {
            return;
        }
        try {
            List list = con.list(path);
            if (list.isEmpty() == true && depth == 0) {

                /* update Server set Server as Inactive */
                try {
                    String q = "select * from servers where ServerType=0 and Ip='" + host + "'";
                    ResultSet rs = qp.getRecords(q);
                    if (rs != null && rs.next() == true) {
                        q = "Update servers set Status=0 where ServerType=0 and Ip='" + host + "'";
                        qp.updateData(q);
                    }
                    rs.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                return;
            } else if (depth == 0) {
                try {
                    String q = "delete from files where ServerType=0 and Ip='" + host + "'";
                    qp.updateData(q);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (depth == 0) {
                try {
                    //update Server
                    String q = "select * from servers where ServerType=0 and Ip='" + host + "'";
                    ResultSet rs = qp.getRecords(q);
                    if (rs != null && rs.next() == true) {
                        q = "Update servers set Status=1,LastActive='" + getCurrentTimeStamp()
				+ "',ShareSize=" + Long.toString(sizeOfIp)
				+ " where ServerType=0 and Ip='" + host + "'";
                        qp.updateData(q);
                    } else {
                        q = "insert into servers (ServerType,Ip,Status,ShareSize,Username,Password) values(0,'"
				+ host + "',1," + Long.toString(sizeOfIp) + ",'" + escapeSpecialCharacters(user) + "','" + escapeSpecialCharacters(pass) + "')";
                        qp.updateData(q);
                    }
                    rs.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Object item = iter.next();
                com.net.UnixRemoteFile temp = (com.net.UnixRemoteFile) item;
                if (temp.getAttrs().isSymLink() == false) {
                    sizeOfIp += temp.getAttrs().getSize();

                }
                try {
                    String extension = getExtension(temp.getFilename());
                    int dir = 0;
                    if (temp.getAttrs().isDir() == true) {
                        dir = 1;
                    } else {
                        String q = "insert into files (Ip,Path,Name,Extension,Size,Dir,ServerType) values('"
                                + host + "','" + escapeSpecialCharacters(temp.getAttrs().getPath()
                                + temp.getFilename()) + "','" + escapeSpecialCharacters(temp.getFilename())
                                + "','" + escapeSpecialCharacters(extension) + "',"
                                + Long.toString(temp.getAttrs().getSize()) + "," + Integer.toString(dir) + ",0)";
                        qp.updateData(q);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                System.out.println(temp.getAttrs().getPath() + (temp.getFilename()));
            }

            Iterator<IRemoteFile> iter2 = list.iterator();
            while (iter2.hasNext()) {
                IRemoteFile item = iter2.next();
                com.net.UnixRemoteFile temp = (com.net.UnixRemoteFile) item;
                if (temp.getAttrs().isSymLink() == true) {
                    System.out.println("LINK FOUND LINK FOUND LINK FOUND LLINK ");
                    continue;
                }
                //boolean flag = false;
                if (temp.getAttrs().isDir() == true) {
                    IRemoteFile temporaryObject = parent;
                    parent = item;
                    long tempSize = sizeOfIp;
                    recursiveScan(path + temp.getFilename() + "/", depth + 1);
                    String q = "insert into files (Ip,Path,Name,Extension,Size,Dir,ServerType) values('" +
                            host + "','" + escapeSpecialCharacters(temp.getAttrs().getPath() +
                            temp.getFilename()) + "','" + escapeSpecialCharacters(temp.getFilename()) +
                            "','" + escapeSpecialCharacters(" ") + "'," +
                            Long.toString(temp.getAttrs().getSize() + sizeOfIp - tempSize) + "," + "1" + ",0)";
                    try {
                        qp.updateData(q);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    parent = temporaryObject;
                }
            }
            if (depth == 0) {
                try {
                    //update Server
                    String q = "select * from servers where ServerType=0 and Ip='" + host + "'";
                    ResultSet rs = qp.getRecords(q);
                    if (rs != null && rs.next() == true) {
                        q = "Update servers set Status=1,LastActive='" + getCurrentTimeStamp() + "',ShareSize=" + Long.toString(sizeOfIp) + " where ServerType=0 and Ip='" + host + "'";
                        qp.updateData(q);
                    } else {
                        q = "insert into servers (ServerType,Ip,Status,ShareSize,Username,Password) values(0,'" + host + "',1," + Long.toString(sizeOfIp) + ",'" + escapeSpecialCharacters(user) + "','" + escapeSpecialCharacters(pass) + "')";
                        qp.updateData(q);
                    }
                    rs.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Returns the current timestamp.
     */
    private String getCurrentTimeStamp() {
        java.util.Date d = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    /**
     * Returns the timestamp corresponding to the milliseconds provided.
     * @return timestamp in yyyy-MM-dd-HH:mm:ss format.
     */
    private String getTimeStamp(long ms) {
        java.util.Date d = new java.util.Date(ms);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    /**
     * Returns the extention of the given filename as a string.
     * Returns empty string if the filename doesn't have an extention.
     */
    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return fileName.substring(index + 1);
        }
    }

    /**
     * Puts a '\' behind the special characters in the string provided.
     * @return Modified string.
     */
    private String escapeSpecialCharacters(String q) {
        String ret = "";
        for (int i = 0; i < q.length(); i++) {
            if (SPECIAL_CHARACTER.indexOf(q.charAt(i)) != -1) {
                ret += ("\\" + q.charAt(i));
            } else {
                ret += q.charAt(i);
            }
        }
        return ret;
    }

    public static void main(String... args)
    {
        try
        {
            ScanDaily sd=new ScanDaily();
        }
        catch(Exception e)
        {
            System.out.println(">>>>>>>2>>>>>>>");
            System.err.println(e.getMessage());
        }
    }

}
