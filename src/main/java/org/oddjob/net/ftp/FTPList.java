package org.oddjob.net.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

/**
 * @oddjob.description FTP command to provide a listing of remote files.
 * 
 * @author rob
 *
 */
public class FTPList implements FTPCommand {

	/**
	 * @oddjob.property 
	 * @oddjob.description The remote directory path.
	 * @oddjob.required Yes.
	 */
	private String path;
	
	/**
	 * @oddjob.property 
	 * @oddjob.description An array of org.apache.commoons.net.FTPFile objects. 
	 * Use something like ${listing.files[0].name} to get a file name (where listing 
	 * is this id given to this command). For other properties pleas see
	 * the Apache documentation.
	 * @oddjob.required R/O.
	 */
	private FTPFile[] files;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		files = client.listFiles(path);
		return (files != null);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public FTPFile[] getFiles() {
		return files;
	}

	@Override
	public String toString() {
		return "list " + ( path == null ? "" : path);
	}

}
