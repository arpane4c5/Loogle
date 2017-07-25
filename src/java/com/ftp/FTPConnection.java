package com.ftp;

import com.net.*;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * <p>Implements an RFC 959 ftp class. It should be noted that many FTP servers
 * do not provide a complete implementation of this RFC. There for this class
 * does not attempt to support the full RFC either.
 * </p>
 *
 * <p>The most notable feature of this class is that when a '5xx' response is
 * returned by the server the method that recieved this response will return a
 * 0 or a null, instead of throwing an exception.</p>
 *
 * <p>Exceptions will only be thrown when we run into an unexpected error such
 * as socket time out or server disconnection.</p>
 *
 * <p> NOTE: This class is based on the FTPConnection class used in the
 *
 * @author Arpan Gupta
 * 
 */


public class FTPConnection extends com.net.FTPConnection {

	private boolean showHiddenFiles;
        String hst;
	/**
	 * Open the connection.
	 *
	 * @param host Name or IP of the FTP server
	 * @param port the port that the FTP server has bound to.
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public void openConnection(String host,int port) throws IOException, UnknownHostException
	{
            
                hst=host;
		sock_control = new Socket();//
               
                
               
		port = (port == 0) ? 21 : port;
               
		InetSocketAddress addr = new InetSocketAddress(host,port);
               
		sock_control.connect(addr,timeout);
               


		sock_control.setSoTimeout(timeout);
		in = sock_control.getInputStream();
		out = sock_control.getOutputStream();
		writer = new OutputStreamWriter(out);
	}

	/**
	 * Retrieves the path to the current working directory by executing
	 * the PWD command.
	 * @return current directory.
	 * @throws IOException
	 */
	public String pwd() throws IOException
	{
		writeln("PWD");
		if(check_reply("257"))
		{
			
			int strt = lastMessage.indexOf('"');
			if(strt > 0)
			{
				/* we have a path name that is quoted. */
				int end = lastMessage.lastIndexOf('"');
				return lastMessage.substring(strt-1,end+1).trim();
			}
			else
			{
				/* path name is not quoted */
				return lastMessage.substring(4,lastMessage.indexOf("is current directory"));
			}
		}
		else
		{
	        return null;
                }
        }

        /**
         * List the contents of the current working directory.
         * @return file list.
         * @throws IOException
         */
        public java.util.List list() throws IOException
        {
                return list("");
        }

        /**
         * Opens a data connection and retrieves the directory listing. The list
         * is a collection of string objects.
         *
         * @param path The pathname to list.
         * @return file list
         * @throws IOException
         */
        public java.util.List<IRemoteFile> list(String path) throws IOException
        {
                List<IRemoteFile> list = new ArrayList();
                    DataConnection data_sock = makeDataConnection();
                    if(data_sock != null)
                    {
                            String cmd = (showHiddenFiles) ? "LIST -al" : "LIST";
                            if(path == null || path.equals(""))
                            {
                                writeln(cmd);
                            }
                            else
                            {
                                    writeln(cmd +" " + path);
                            }
                            if(check_reply("150") || lastMessage.startsWith("125"))
                            {
                                    /*
                                     * windows ftp server returns 125 at times
                                     */

                                    BufferedReader bin =new BufferedReader(new InputStreamReader(data_sock.getInputStream()));
                                    // here comes the directory listing
                                    while(true)
                                    {
                                            String s = bin.readLine();
                                            if(s==null)
                                            {
                                                    break;
                                            }
                                            IRemoteFile f = RemoteFile.parse(s);
                                            if(f != null){
                                                if(hst.charAt(hst.length()-1)!='/')hst=hst+"/";
                                                f.getAttrs().setPath(hst+path);
                                                list.add(f);
                                            }
                                    }
                                    bin.close();
                                    //sock_data.close();
                                    return (check_reply("226")) ? list : null;
                        }
                    }
                return null;
        }

	/**
	 * Creates an OutputStream to the file on the remote server. Does not
	 * bother with overwriting existing files. You must call the
	 * method after you complete writing to clean up the control connection
	 *
	 * @param path - path name on the server.
	 * @return <code>OutputStream</code> to which the contents of the file
	 *  should be written.
	 * @throws IOException
	 */
	public OutputStream put(String path) throws IOException
	{
		/* switch to passive mode */
		DataConnection data_sock = makeDataConnection();
		if(data_sock != null)
		{
			if(path == null || path.equals(""))
			{
			    return null;
			}
			else
			{
				writeln("STOR " + path);
			}
			if(check_reply("150") || lastMessage.startsWith("125"))
			{
				return data_sock.getOutputStream();
			}
		}

		return null;
	}
	public InputStream get(String path) throws IOException
	{
		/* switch to passive mode */
		DataConnection data_sock = makeDataConnection();
		if(data_sock != null)
		{
			if(path == null || path.equals(""))
			{
			    return null;
			}
			else
			{
				writeln("RETR " + path);
			}
			if(check_reply("150") || lastMessage.startsWith("125"))
			{
				return data_sock.getInputStream();
			}
		}
		return null;
	}


	/**
	 * Deletes specified path. Does not glob.
	 *
	 * @param path the file to delete
	 * @return success or failure
	 * @throws IOException
	 */
	public boolean rm(String path) throws IOException
	{
	    writeln("DELE " +path);
		return check_reply("250");
	}

	/**
	 * Deletes specified directory. Does not glob. failes if empty
	 * @param path the directory to delete
	 * @return success or failure
	 * @throws IOException
	 */
	public boolean rmd(String path) throws IOException
	{
		writeln("RMD " +path);
		return check_reply("250");
	}

	/**
	 * First step in the rename operation.
	 * @param path for the victim
	 * @return did it work?
	 * @throws IOException
	 */
	public boolean rnfr(String path) throws IOException
	{
		writeln("RNFR " + path);
		return check_reply("350");
	}

	/**
	 * the second part of the rename operation.
	 * @param path the new name
	 * @return how did it go?
	 * @throws IOException
	 */
	public boolean rnto(String path) throws IOException
	{
		writeln("RNTO " + path);
                return check_reply("250");
        }

        /**
         *
         * we collect information about the operating system in this method. We
         * need to know if we are dealing with the redmond stuff.
         *
         * @throws IOException
         */

        public void initStream() throws IOException
        {
                /** @todo collect the welcome message */
                while(true)
                {
                        if(check_reply("220"))
                        {
                                if(lastMessage.indexOf("Microsoft") != -1)
                                {
                                        RemoteFile.setServerType("MS");
                                }
                                break;
                        }
                }
        }

        /**
         * Switch between ascii and binary modes.
         * @param mode A for ASCII or I for Binary.
         * @return success ir failure
         * @throws IOException
         */
        public boolean type(String mode) throws IOException
        {
                writeln("TYPE "+mode);
		try {
			return check_reply("200");
		}
		catch (IOException ex) {
			return false;
		}
	}

	public void setShowHiddenFiles(boolean showHiddenFiles) {
		this.showHiddenFiles = showHiddenFiles;
	}

	public boolean isShowHiddenFiles() {
		return showHiddenFiles;
	}
        
}
