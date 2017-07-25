
package com.net;

/**
 * @author Arpan
 *
 */
public class RemoteFile implements IRemoteFile {

    static private boolean redMond = false;
    /**
     * Contains additonal information about the file such as modification times,
     * size etc.
     */
    FileAttrs attrs;
    /**
     * the name of the file
     */
    String fileName;

    protected RemoteFile() {
    }

    protected RemoteFile(String s) {
    }

    /**
     * A factory method that creates a remote file instanace for the current
     * server type.
     *
     * @param s a line read in from the LIST command.
     * @return an instance of a RemoteFile subclass.
     */
    public static IRemoteFile parse(String s) {
        IRemoteFile f = null;
        if (redMond) {
            f = new MSRemoteFile(s);
            if (f.getFilename() == null) {
                try {
                    f = new UnixRemoteFile(s);
                } catch (Exception ex) {
                    return null;
                }
            }
        } else {
            if (!s.startsWith("total")) {
                f = new UnixRemoteFile(s);
            }
        }
        return (f == null || f.getFilename() == null) ? null : f;
    }

    /**
     * Create an entry for the parent folder.
     *
     * The ftp server maynot return a '..' entry, in that case we have to
     * create one, however doing a ls ../.. is not always a good idea so
     * we hack together a filename.
     *
     * @return an object that corresponds to the parent folder of the current
     *  working directory.
     */
    public static IRemoteFile doubleDot() {
        IRemoteFile f = null;
        if (redMond) {
            f = new MSRemoteFile();
        } else {
            f = new UnixRemoteFile();
        }
        f.setFileName("..");
        FileAttrs at = new FileAttrs();
        at.setDir(true);
        f.setAttrs(at);
        return f;
    }

    /**
     *
     * @return this file's name
     */
    public String getFilename() {
        return fileName;
    }

    /**
     *
     * @return attributes for this file.
     */
    public FileAttrs getAttrs() {
        return attrs;
    }

    /**
     * Are we running on windows or linux?
     * @todo this needs to be improved to support other platforms and use
     * better methods of os detection.
     * @param st
     */
    public static void setServerType(String st) {
        redMond = (st.indexOf("MS") != -1 || st.indexOf("Windows") != -1);
    }

    /**
     *
     * @return true if the server is running on that lousy operating system.
     */
    public static boolean isRedmond() {
        return redMond;
    }

    /**
     *
     * @param attrs attributes for this file.
     */
    public void setAttrs(FileAttrs attrs) {
        this.attrs = attrs;
    }

    /**
     *
     * @param fileName name of this file.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
