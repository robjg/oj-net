package org.oddjob.net.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * @oddjob.description FTP command to change remote directory.
 * 
 * @author rob
 *
 */
public class FTPCd implements FTPCommand {
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The name of the remote directory.
	 * @oddjob.required Yes.
	 */	
	private String path;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		
		return client.changeWorkingDirectory(path);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "cd" + (path == null ? "" : path);
	}

}
