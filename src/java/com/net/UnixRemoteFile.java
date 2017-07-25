
package com.net;


/**
 * Represents a file on a FTP server that runs on a unix like operating system.
 *
 * @author Arpan
 * 
 */

import java.text.*;
import java.util.*;

public class UnixRemoteFile extends RemoteFile {
	/**
	 * This defaults constructor with package access is inteded for use by
	 * the RemoteFile class.
	 */
         
	UnixRemoteFile()
	{
	}

	/**
	 * Creates a new instance by parsing the data given.
	 * @param s a line that was read in after executing the LIST command.
	 */

	public UnixRemoteFile(String s) {
		/*
		 * the order of the parts are
		 * flags, inode, uid, gid, size,  month, day, time, name
		 */
		//System.out.println(" here  enters to create Unix" + s);
		String[] parts = s.split(" ");
		Calendar cal = Calendar.getInstance();
		String dateTime ="";

		if(parts.length < 8)
		{
			fileName=null;
			return;
		}
		for (int i = 0, j=0 ; i < parts.length; i++) {
			if(parts[i].trim().equals(""))
			{
				continue;
			}
			else
			{
				switch(j)
				{
					case 0: attrs = new FileAttrs();
						attrs.parseFlags(parts[i]);
						break;
					case 1:
						try {
							attrs.setInode(Integer.parseInt(parts[i]));
						}
						catch (NumberFormatException ex) {
							attrs.setInode(0);
						}
	
						break;
	
					case 2:
						try {
							attrs.setUId(Integer.parseInt(parts[i]));
						}
						catch (NumberFormatException ex) {
							attrs.setUId(0);
						}
						break;
	
					case 3:
						try {
							attrs.setGId(Integer.parseInt(parts[i]));
						}
						catch (NumberFormatException ex) {
							attrs.setGId(0);
						}
						break;
	
					case 4: attrs.setSize(Long.parseLong(parts[i]));
						break;
	
					case 5:
						if(parts[i+2].indexOf(":") != -1)
						{
							/* time given assume current year */
						}
					case 6: dateTime += parts[i] + " ";
						break;
					case 7: dateTime += parts[i];
						try {
							if(parts[i].indexOf(":") != -1)
							{
								//DateFormat dateFormat = DateFormat.getDateTimeInstance();
								SimpleDateFormat dateFormat =
										new SimpleDateFormat("yyyy MMM dd HH:mm");
								dateTime = cal.get(cal.YEAR) + " " + dateTime;
	
								Date date =  dateFormat.parse(dateTime);
								/*
								System.out.println("date " + date.getTime());
								System.out.println("date " + new Date(date.getTime()));
								*/
								attrs.setTime(date.getTime());
							}
							else
							{
								SimpleDateFormat dateFormat =
										new SimpleDateFormat("MMM dd yyyy");
								Date date =  dateFormat.parse(dateTime);
	
								attrs.setTime(date.getTime());
							}
	
						}
						catch (Exception ex) {
							System.err.println("unparsable data for " + s);
						}
						break;
	
					case 8:
						fileName = (parts[i] == null) ? "" : parts[i];
						break;
	
					case 9:
						       /*
							* We are here probably because the filename has spaces
							* in it (there are only nine fields in the list, since
							* we start counting from zero this is the tenth).
							*
							* If the file name does have spaces it could be a
							* symlink.
							*/
						if(i> 8)
						{
							while(i < parts.length)
							{
								if(attrs.isSymLink() && parts[i].trim().equals("->"))
								{
									/*
										* what follows is the 'other' file name which
										* we do not want.
										*/
									i++;
									break;
								}
								this.fileName += " " + parts[i++];
							}
						}
						break;
	
					default:
						break;
				}
				j++;
			}
		}
		fileName = fileName.trim();
    	}
}