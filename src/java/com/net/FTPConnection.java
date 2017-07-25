

package com.net;

import javax.swing.JFrame;
import java.awt.*;
import java.net.*;
import java.io.*;


/**
 * @author Arpan
 * 
 */




/**
 * @todo better provide support for switching between ascii and binary.
 */
public class FTPConnection {
	public static int ACTIVE_MODE=1;
	public static int PASV_MODE=0;
	/**
	 * flag - defines whether to connect in active or passive modes.
	 */
	//protected int connectMode=ACTIVE_MODE;
	protected int connectMode=PASV_MODE;

	/**
	 * socket timeout
	 */
	protected int timeout = 30000;
	protected final static byte[] CRLF = {0x0d,0x0A};
    	protected int contentLength;

	/**
	 * The FTP server that we are connecting to, the username and password that
	 * is being used for authentication and the starting location on the folder
	 * tree are all defined by this URL.
	 */
	protected URL location;
	
	protected OutputStream out;
	protected Writer writer;
	
	protected InputStream in;
	protected Socket sock_control;
	protected Socket sock_data;

	protected String welcome="";
	protected String lastMessage;
    	private java.io.PrintStream logWriter = System.out;

	public FTPConnection(){}


	public FTPConnection(String location) throws MalformedURLException{
		this.location = new URL(location);
	}

	public boolean login() throws IOException
	{
		String userInfo = location.getUserInfo();
		if(userInfo == null)
		{
			log("anonymous login");
			return login("anonymous","pass");
		}
		else
		{
			String parts[] = userInfo.split(":");
			return login(parts[0],parts[1]);
		}
	}

	/**
	 * Set current working directory to the path defined in the
	 * {@link #location url}.
	 *
	 * @return success or failure
	 * @throws IOException
	 */
	public boolean cdhome() throws IOException
	{
		String path = location.getPath();
		return cwd(path);
	}

	/**
	 * Chmod is implemened via the site command.
	 *
	 * @param perms the permission to apply
	 * @param path the file name to apply the permissions to
	 * @return success or failure.
	 * @throws IOException
	 */
	public boolean chmod(int perms,String path)throws IOException
	{
		writeln("SITE CHMOD " + perms + " " + path);
		return  check_reply("200");
	}

	/**
	 * Change Working Directory
	 * @param dir to change to
	 * @return success or failure.
	 * @throws IOException
	 */
	public boolean cwd(String dir) throws IOException
	{
		writeln("CWD " + dir);
		return check_reply("250");
	}

	/**
	 * Logs into the system with the given username and password.
	 *
	 * @param username username
	 * @param password password
	 * @return succes or failure.
	 *
	 * @throws IOException
	 */

	public boolean login(String username,String password) throws IOException
	{
		return (user(username) && pass(password));
	}

	/**
	 * Open the connection to the previously defined URL given in
	 * {@link #location}.
	 *
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public void openConnection() throws IOException, UnknownHostException
	{
                
		sock_control = new Socket();//
		int port = (location.getPort() < 0) ? 21 : location.getPort();
                
                
		InetSocketAddress addr = new InetSocketAddress(location.getHost(),port);
                
                System.out.println("connect to " + location.getHost() + ":" + port);
		sock_control.connect(addr,timeout);
		
		sock_control.setSoTimeout(timeout);
		in = sock_control.getInputStream();
		out = sock_control.getOutputStream();
		writer = new OutputStreamWriter(out);
	}

	/**
	 * The server usually sends a 220 reply when your first
	 * connect to it.
	 * @throws IOException
	 */

	public void initStream() throws IOException
	{
		while(true)
		{
			if(check_reply("220-"))
			{
				// ignore the banner
				continue;
			}

			if(lastMessage != null && lastMessage.startsWith("220"))
			{
				if(lastMessage.indexOf("Microsoft") != -1)
				{
					RemoteFile.setServerType("MS");
				}
				else
				{
					RemoteFile.setServerType("*nix");
				}

			}
			break;
		}
	}

	/**
	 * Reads in a line from the control connection.
	 *
	 * @return the line that we just read.
	 * @throws IOException
	 */
	private String getLine() throws IOException
	{
		int iBufLen=4096;
		int i=0;
		byte[] buf = new byte[iBufLen];
		try {
			for(i=0 ; i < iBufLen; i++)
		    {
				buf[i] = (byte) in.read();
				if(buf[i] == CRLF[1])
				{
					break;
				}
			}
		}catch (IOException ex) {
		    ex.printStackTrace();
			throw (ex);
		}

		return new String(buf,0,i);
	}

	/**
	 * Send a command to the server over the control connection.
	 *
	 * @param command the command to excute
	 * @param params the parameters for the command.
	 * @throws IOException
	 */
	private void send_command(String command, String params) throws IOException
	{
		writeln(command + " " + params);
	}

	/**
	 * Centralize all the write operations for ease of maintenance.
	 *
	 * @param s the data to be sent over the control connection.
	 * @throws IOException
	 */
	protected void writeln(String s) throws IOException
	{
		writer.write(s);
		writer.write("\r\n");
		writer.flush();
		log("> " + s);
	}

	/**
	 * Open a data connection in active mode. Active mode requires that the
	 * client listens for incoming connections - effectively a reversal of the
	 * tradicational client/server relationship.
	 *
	 * @return a ServerSocket to listen on.
	 * @throws IOException
	 */
	public ServerSocket port() throws IOException
	{
		ServerSocket socket = new ServerSocket(0);
		InetAddress localhost =  sock_control.getLocalAddress();

		int s_port =  socket.getLocalPort();

		byte[] ip = localhost.getAddress();
		byte[] port = new byte[2];

		port[0] =(byte) (s_port >> 8); // most significant babes
		port[1] =(byte) (s_port & 0x00FF);

		String cmd = "PORT " + makeUnsignedShort(ip[0]) + "," +
					  makeUnsignedShort(ip[1]) + "," + makeUnsignedShort(ip[2]) +
					  "," +  makeUnsignedShort(ip[3]) + "," +
					  makeUnsignedShort(port[0]) + "," + makeUnsignedShort(port[1]);

		writeln(cmd);
		if(check_reply("200"))
		{
			return socket;
		}
		else
		{
		    return null;
		}
	}

	/**
	 * Open a passive mode data connection.
	 *
	 * @return a client Socket
	 *
	 * @throws IOException
	 */
	protected Socket pasv() throws IOException
	{
		writeln("pasv");

		if(check_reply("227"))
		{
			int start = lastMessage.indexOf('(');
            int end = lastMessage.indexOf(')');
			String sockaddr = lastMessage.substring(start+1,end);
			String[] parts = sockaddr.split(",");
			/* why loop when it's only a single statement? */
			String s_hostIP = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
			/* get the port */
			int port = (Integer.parseInt(parts[4]) << 8) + Integer.parseInt(parts[5]);

			/* create a socket and return it */
			//System.out.println(s_hostIP +":" + port);
            return new Socket(s_hostIP, port);
		}
		else
		{
		   return null;
		}
	}

	/**
	 *
	 * @return the <code>InputStream</code> for the control connection.
	 */
	public InputStream getIn() {
		return in;
	}

	/**
	 *
	 * @return the <code>OutputStream</code> for the control connection.
	 */
	public OutputStream getOut() {
		return out;
	}

	/**
	 * Error messages are typically 500 status codes. This method returns
	 * true if such a status code is not encountered.
	 *
	 * @return did the server accept the last action.
	 *
	 * @throws IOException
	 */
	public boolean isOk() throws IOException
	{
		/** @todo implement support for other error code */
	    return !check_reply("500");
	}

	/**
	 * Returns true if the server returns the expected code. The exact message
	 * is not interesting.
	 *
	 * @param code the status code that we expect.
	 * @return did we recieve the expected code.
	 *
	 * @throws IOException
	 */
	public boolean check_reply(String code) throws IOException
	{
		if(code.length() == 4)
		{
			/*
			 * a specific check is made for a multiline reply (eg '230-')
			 */
			lastMessage = getLine();
			log("> "+lastMessage);
			if(lastMessage== null || !lastMessage.startsWith(code))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			/*
			 * the programmer doesn't want to bother with handling multi
			 * line replies himself so let's handle it for him.
			 */
			lastMessage="";
			String code2 = code +"-";
			String s;

			while(true)
			{
				s = getLine();
				log("> " + s);


				if(s != null)
				{
					lastMessage += s;
					if(s.startsWith(code2))
					{
						continue;
					}
				}
				break;
			}
			if(s== null || !s.startsWith(code))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
	}

	/**
	 * Create a new directory on the server, will not attempt to determine if
	 * the directory already exists.
	 *
	 * @param dir - name of the new folder.
	 * @return success or failure
	 * @throws IOException
	 */
	public boolean mkdir(String dir) throws IOException
	{
		writeln("MKD " + dir);
		return check_reply("257");
	}

	/**
	 * sends the USER command to the server, you can call this method
	 * directory if you want more control than given by the
	 * {@link #login login()} method.
	 * @param user username
	 * @return true if the username is acceptable.
	 * @throws IOException
	 */
	public boolean user(String user) throws IOException
	{
		String user_cmd ="user " + user;
		writeln(user_cmd);
		return check_reply("331");
	}

	/**
	 * sends the password as part of the user authentication procedure and reads
	 * the welcome message if one is available.
	 *
	 * @param pass - password for the user
	 * @return Is the password accepted?
	 *
	 * @throws IOException
	 */
	public boolean pass(String pass) throws IOException
	{
		send_command("PASS", (pass==null) ? "anonymous@localhost" : pass);
		if(check_reply("230-"))
		{
			/* we have a welcome message */
			while(true)
			{
				welcome += lastMessage;
				if(! check_reply("230-"))
				{
					return lastMessage.startsWith("230");
				}
			}
		}
		else
		{
			return lastMessage.startsWith("230");
		}
	}

	/**
	 * logs the communitcation.
	 * @param mes add this message to the log.
	 */
	protected void log(String mes)
	{
		if(logWriter != null)
		{
			logWriter.println(mes);
		}
	}

	/**
	 * Returns the last response from the server. You may need to call this
	 * method if {@link #check_reply check_reply()} returned false, which
	 * indicates that the expected response was not recieved. The last
	 * message should then be retrieved for closer ispection.
	 *
	 * @return last response.
	 */
		public String getLastMessage() {
		return lastMessage;
		}

	/**
	 * Swtich between Active and Passive modes.
	 *
	 * @return current mode
	 */
	public int switchMode()
	{
		connectMode = (connectMode == ACTIVE_MODE) ? PASV_MODE : ACTIVE_MODE;
		return connectMode;
	}

	/**
	 * open a new active or passive data connection.
	 *
	 * @return DataConnection
	 * @throws IOException
	 */

	public DataConnection makeDataConnection() throws IOException
	{

		DataConnection con = new DataConnection();
		if(connectMode == ACTIVE_MODE)
		{
			con.sock_active = port();
		}
		else
		{
			con.sock_pasv = pasv();
		}

		return con;
	}


	/**
	 * Utility method used in calculating port/ip numbers.
	 *
	 * @param b byte to be converted to an unsigned short.
	 * @return short
	 */
	private short makeUnsignedShort(byte b)
	{
		return ( b < 0 )         ? (short) (b + 256)  : (short) b;
	}

	/**
	 * Where to log. The default is System.out, use null to disbale logging.
	 * @param logWriter use this writer for loggin.
	 */
	public void setLogWriter(PrintStream logWriter) {
		this.logWriter = logWriter;
	}
	
	public PrintStream getLogWriter() {
		return logWriter;
	}

	/**
	 * <p>
	 * Inner class that acts as an abstraction layer for <code>Socket</code>
	 * and <code>ServerSocket</code>.
	 * </p>
	 * <p>
	 * Unfortunately Socket and SeverSocket do not share any ancestors.
	 * Therefore we need to create our adapter class that encloses both a
   	 * ServerSocket and a Socket.
	 * </p>
	 * <p>Though it's possible to obtain a Socket
	 * instance from ServerSocket by calling the accept method() it's not
	 * suitable for our work because the thread would immidiately become
	 * blocked.
	 * </p>
	 */
	public class DataConnection
	{
		ServerSocket sock_active;
		Socket sock_pasv;

		/**
		 * Follows the adapter pattern, returns the input stream of the
		 * server socket or client socket.
		 *
		 * @return <code>InputStream</code> for the data connection.
		 * @throws IOException
		 */
		public InputStream getInputStream() throws IOException
		{
			if(connectMode == ACTIVE_MODE)
			{
				return sock_active.accept().getInputStream();
			}
			else
			{
				return sock_pasv.getInputStream();
			}
		}

		/**
		 * returns the output stream of the client or server socket depending
		 * on whether the active or passive mode is in effect.
		 *
		 * @return <code>OutputStream</code> for the data connection.
		 * @throws IOException
		 */
		public OutputStream getOutputStream() throws IOException
		{
			if(connectMode == ACTIVE_MODE)
			{
				return sock_active.accept().getOutputStream();
			}
			else
			{
				return sock_pasv.getOutputStream();
			}
		}
	}

	/**
	 * This method is used to determine if the connection is still active
	 * or has been dropped due to time out or some related issue. We make
	 * use of the noop call.
	 * @return
	 */
	public boolean testConnection()
	{
		if(sock_control.isConnected() &&
				!sock_control.isInputShutdown() &&
				!sock_control.isOutputShutdown())
        {
            try {
                writeln("NOOP");
                return  check_reply("200");
            }
            catch (IOException ex) {
                return false;
            }
        }
        return false;
	}

	/**
	 * Returns the writer associated with the control socket.
	 * @return Writer
	 */
	public Writer getWriter() {
		return writer;
	}
	
	public void setLocation(URL location) {
		this.location = location;
	}
	
	public URL getLocation() {
		return location;
	}
}