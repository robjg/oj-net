package org.oddjob.net.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @oddjob.description FTP command to delete a remote file.
 * 
 * @author rob
 *
 */
public class FTPDelete implements FTPCommand {
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The path of the file to delete.
	 * @oddjob.required Yes.
	 */	
	private String path;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		
		if (path == null) {
			throw new IllegalStateException("No path");
		}
		
		return client.deleteFile(path);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "delete " + path;
	}

}
