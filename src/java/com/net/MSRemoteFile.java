


package com.net;


/**
 * Represents a file on a FTP server running on the windows platform.
 * @author Arpan Gupta
 *
 */

import java.text.*;
import java.util.*;

public class MSRemoteFile extends RemoteFile {

        /**
         * This defaults constructor with package access is intended for use by
         * the {@link RemoteFile} class.
	 */

	MSRemoteFile()
	{
	}

	/**
	 * Creates the new instance based by parsing the data provided in the
	 * input string.
	 *
         * @param s
         */
	public MSRemoteFile(String s) {
		attrs = new FileAttrs();
		/*
		 * the order of the parts are
		 * date time <dir> size filename
		 * mm-dd-yy hh:mm(AM/PM)
		 */

		/* is this is a directory */
		if(s.substring(24,29).equals("<DIR>"))
		{
		attrs.setDir(true);
		}
		else
		{
			/*
			 *the file size in bytes, in windows a file size is
			 * not assigned to directories
			 */
			String num = s.substring(30,39);
			if(num == null)
			{
				num ="0";
			}
			try {
				attrs.setSize(Long.parseLong(num.trim()));
			} catch (Exception ex1) {
				return ;
			}
       		 }

		Calendar cal = Calendar.getInstance();
		String dateTime ="";
	
		/* grab the date and time (first two columns) */
		dateTime = s.substring(0,9) + s.substring(10,17);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-DD-yyHH:mm");
		try {
		Date date =  dateFormat.parse(dateTime);
		attrs.setTime(date.getTime());
		}
		catch (ParseException ex) {
		//ex.printStackTrace();
		}
	
		/* grab the file name */
		fileName = s.substring(39,s.length());
	}
}