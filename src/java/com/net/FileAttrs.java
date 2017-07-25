

package com.net;

import java.util.Date;
import java.text.DateFormat;



/**
 * Manages Attributes of files on the server.
 *
 * @author anshuman
 
 */

public class FileAttrs {
	/**
	 * dir is true if the current file is a directory
	 */
	boolean dir;
	/**
	 * file size
	 */
	private long size;
	/**
	 * inode number
	 */
    	private int inode;

	/**
	 * Group Id
	 */
	private int GId;
	/**
	 * User Id
	 */
	private int UId;
	/**
	 * modification time.
	 */
        private long milli_seconds;

        int perms;

    	private boolean symLink;
        String path;

        /**
         * default constructor.
         */
  	  public FileAttrs() {
  	  }

        /**
         * Determines the file's permissions and flags.
         * The first column of the directory listing contains the permissions in
         * unix like operating systems. This method parses this column to obtain
         * the permission as a numeric value. Also finds out if we are looking at
         * a directory.
         *
         * @param s
         * @return
         */
        public boolean parseFlags(String s)
        {
		dir = (s.charAt(0) == 'd');
		symLink = (s.charAt(0) == 'l');

		perms = (s.charAt(1) == 'r') ? perms | 4 : perms;
		perms = (s.charAt(2) == 'w') ? perms | 2 : perms;
		perms = (s.charAt(3) == 'x') ? perms | 1 : perms;
		perms <<=3;

		perms = (s.charAt(4) == 'r') ? perms | 4 : perms;
		perms = (s.charAt(5) == 'w') ? perms | 2 : perms;
		perms = (s.charAt(6) == 'x') ? perms | 1 : perms;
		perms <<=3;

		perms = (s.charAt(7) == 'r') ? perms | 4 : perms;
		perms = (s.charAt(8) == 'w') ? perms | 2 : perms;
		perms = (s.charAt(9) == 'x') ? perms | 1 : perms;

		return true;
	}

	/**
	 *
	 * @return true if the current file is a directory.
	 */
	public boolean isDir()
	{
                return dir;
        }

        /**
         *
         * @return size of the file
         */
        public long getSize()
        {
                return size;
	}

	/**
	 *
	 * @return modification time of the file in milli seconds.
	 */
	public long getMTime()
	{
		return milli_seconds;
	}

	/**
	 *
	 * @return file owner's user id.
	 */
	public int getUId()
	{
		return UId;
	}

	/**
	 *
	 * @return group id.
	 */
	public int getGId()
	{
		return GId;
	}
        public long getMilliSeconds(){
            return milli_seconds;
        }
	/**
	 *
	 * @return the modification time as a date string.
	 */
	public String  getMtimeString(){
		Date date= new Date(milli_seconds);
		return (date.toString());
	}

	/**
	 * Not yet fully implemented.
	 * @return the flags
	 */
	public int getFlags()
	{
		return 0;
	}
        public String getPath(){
            return path;
        }

	/**
	 *
	 * @param inode
	 */
	public void setInode(int inode) {
		this.inode = inode;
	}

	/**
	 *
	 * @return inode number for the current file.
	 */
	public int getInode() {
		return inode;
	}
	
	public void setGId(int GId) {
		this.GId = GId;
	}

	public void setUId(int UId) {
		this.UId = UId;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public void setDir(boolean dir) {
		this.dir = dir;
	}

	public void setTime(long seconds)
	{
		this.milli_seconds = seconds;
	}

	/**
	 *
	 * @return permissions as an int.
	 */
	public int getPerms() {
		return perms;
	}
	public void setSymLink(boolean symLink) {
		this.symLink = symLink;
	}
	public boolean isSymLink() {
		return symLink;
	}
        public void setPath(String path){
            this.path=path;
        }
}