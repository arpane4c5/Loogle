/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package intranetFileSearch;

import actions.SendMailMultipleRecipients;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import jcifs.smb.SmbFile;
import java.net.MalformedURLException;
import java.io.IOException;

public class SmbCrawler {

    int maxDepth;
    String ip=null;
    String path=null;
    int serverType=1;
    String fileName=null;
    String ext=null;
    QueryProcessor qp=new QueryProcessor();
    long size=(long)Integer.MAX_VALUE;
    String temp[];
    String mailContent="New Files added to the share :\n";
    String tempMailContent="";
    boolean status=false;
    SendMailMultipleRecipients sendMailToMany=new SendMailMultipleRecipients();

    SmbCrawler( int maxDepth ) {
        this.maxDepth = maxDepth;
    }

    void traverse( SmbFile f, int depth ) throws MalformedURLException, IOException {

        if( depth == 0 ) {
            return;
        }
        System.out.println(">>>>>>0>>>>>>");

        if(depth==maxDepth)
        {
        java.util.Date loginDate=new java.util.Date();
        //System.out.println("Date :: "+loginDate.toString());
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sdf.format(loginDate);
        //if connection is established then add the ip to the list of active servers.
        try
        {
            try
            {
                qp.init();
                qp.connect();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }

            f.connect();
            String servQuery="insert into servers (ServerType, IP, Status, LastActive, ShareSize) values ("+
                    serverType+",\""+f.getServer()+"\",1,\""+date+"\","+f.getContentLength()+")" +
                    "on duplicate key update status=values(status),LastActive=values(LastActive),ShareSize=values(ShareSize);";
            qp.insertData(servQuery);
        }
        catch(Exception e)
        {
            String servQuery="update servers set Status=0 where ServerType="+serverType+" and IP=\""+
                    f.getServer()+"\";";
            try
            {
                qp.updateData(servQuery);
            }
            catch(Exception exp)
            {
                System.out.println(e.getMessage());
            }
            finally
            {
                return;
            }
        }
        finally
        {
            try
            {
                qp.close();
            }
            catch(Exception exp1)
            {
                System.out.println(exp1.getMessage());
            }
        }
        }
        if(depth<maxDepth)
            f.connect();
        
        System.out.println(">>>>>>1>>>>>>");
        SmbFile[] l = f.listFiles();
        System.out.println(">>>>>>2>>>>>>");
        for(int i = 0; l != null && i < l.length; i++ )
        {
            try
            {
                for( int j = maxDepth - depth; j > 0; j-- )
                {
                    System.out.print( "    " );
                }
                //getting the IP
                ip=f.getServer();
                //getting path
                path=l[i].getPath();
                //path=path.replaceFirst(":", "");

                //getting name
                fileName=l[i].getName();
                //calculating size
                long sizeTemp=(long)l[i].getContentLength();
                if(sizeTemp<0)
                {
                    sizeTemp=((size*2)+2l)+sizeTemp;
                }

                //System.out.println( l[i] + " " + l[i].exists() );

                if( l[i].isDirectory() )
                {
                    fileName=fileName.replaceAll("/","");
                    System.out.println(l[i] + " "+path+"  "+fileName+"  "+sizeTemp+"  "+ip);
                    
                    try
                    {
                        
                        qp.init();
                        qp.connect();

                        /*
                        String query="insert into files(IP, Path, Name, Extension, size, Dir, ServerType) values (\""+
                                ip+"\",\""+path+"\",\""+fileName+"\",\""+""+"\","+sizeTemp+",1,"+serverType+") " +
                                "on duplicate key update size=values(size);";
                         */
                        String query="insert into files(IP, Path, Name, Extension, size, Dir, ServerType) values (\""+
                                ip+"\",\""+path+"\",\""+fileName+"\",\""+""+"\","+sizeTemp+",1,"+serverType+");";
                        try
                        {
                            qp.insertData(query);
                            //mailContent+=(fileName+"\n");
                            //Mail 
                        }
                        catch(Exception ex1){}
                        qp.close();
                    }
                    catch(Exception e)
                    {
                        System.out.println("Exception in SMBCrawler :: "+e.getMessage());
                    }
                    traverse( l[i], depth - 1 );
                }
                else
                {
                    //size=l[i].getContentLength()>0?l[i].getContentLength():(size+l[i].getContentLength());
                    //finding the extension
                    int index=fileName.lastIndexOf(".", fileName.length()-1);
                    
                    System.out.println(fileName);
                    ext=fileName.substring(index+1);

                    try
                    {
                        qp.init();
                        qp.connect();
                        String query="insert into files(IP, Path, Name, Extension, size, Dir, ServerType) values (\""+
                                ip+"\",\""+path+"\",\""+fileName+"\",\""+ext+"\","+sizeTemp+",0,"+serverType+");";
                        //String query="insert into files(IP, Path, Name, Extension, size, Dir, ServerType) values (\""+
                        //        ip+"\",\""+path+"\",\""+fileName+"\",\""+ext+"\","+sizeTemp+",0,"+serverType+")" +
                        //        " on duplicate key update size=values(size);";
                        try
                        {
                            qp.insertData(query);
                            tempMailContent+=(fileName+"\n");
                            if(!status)
                                status=true;
                        }
                        catch(Exception ex1){}
                        qp.close();

                    }
                    catch(Exception e)
                    {
                        System.out.println("Exception in SMBCrawler :: "+e.getMessage());
                    }
                    //
                    //size=l[i].
                    System.out.println(l[i] + " "+path+"  "+fileName+"  "+ext+"  "+sizeTemp+"  "+ip);
                }
            }
            catch( IOException ioe ) {
                System.out.println( l[i] + ":" );
                ioe.printStackTrace( System.out );
                
            }
        }
    }

    private String[] getIpFromFile(String filePath)throws FileNotFoundException,IOException
    {
        //  Scan the configuration file for list of ranges.
        String[] ret=new String[500];

        try
        {
            //FileOutputStream fout=new FileOutputStream("ABC.txt");
            FileInputStream fstream=new FileInputStream(filePath);
            DataInputStream in=new DataInputStream(fstream);
            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i=0;
            while((strLine=br.readLine())!=null)
            {
                ret[i]=strLine;
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
    }

    void crawl()throws FileNotFoundException,IOException
    {
        String singleIP;
        String ip[]=getIpFromFile("c:/Loogle/SMBConfigIP.txt");
        int i=0;

        while(ip[i]!=null||(!ip[i].equals("")))
        {
            if(ip[i].endsWith("*"))
            {
                
                ip[i]=ip[i].substring(0,ip[i].length()-1);
                System.out.println(ip[i]);
                for(int j=1;j<=254;j++)
                {
                    singleIP=ip[i]+j;
                    try
                    {
                        status=false;
                        traverse(new SmbFile("smb://"+singleIP+"/"),maxDepth);
                        if(status)
                        {
                            sendMailToMany.getUpdates(ip[i],mailContent+tempMailContent, serverType);
                            tempMailContent="";
                            status=false;
                        }

                    }
                    catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                    //System.out.println(singleIP);
                }
            }
            else
            {
                try
                {
                    System.out.println(ip[i]);
                    status=false;
                    traverse(new SmbFile("smb://"+ip[i]+"/"),maxDepth);
                    if(status)
                    {
                        sendMailToMany.getUpdates(ip[i],mailContent+tempMailContent , serverType);
                        tempMailContent="";
                        status=false;
                    }

                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            i++;
        }

        //clean up Thumbs.db
        String q="delete from files where name=\"Thumbs.db\";";
        try
        {
            qp.init();
            qp.connect();
            int t=qp.updateData(q);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            try
            {
                qp.close();
            }
            catch(Exception ex)
            {}
        }

    }

    public static void main(String[] argv) throws Exception {
        int depth = Integer.parseInt( "10" );
        SmbCrawler sc = new SmbCrawler( depth );
        //sc.traverse( new SmbFile( "smb://10.10.28.188/" ), depth );
        sc.crawl();
    }
}
