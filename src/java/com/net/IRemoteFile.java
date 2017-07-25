package com.net;

public interface IRemoteFile {
	public abstract String getFilename();
	public abstract FileAttrs getAttrs();
	public abstract void setAttrs(FileAttrs attrs);
	public abstract void setFileName(String fileName);
}
