package org.oddjob.net.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @oddjob.description FTP command to create a remote directory.
 * 
 * @author rob
 *
 */
public class FTPMkDir implements FTPCommand {
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The remote directory path.
	 * @oddjob.required Yes.
	 */
	private String path;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		
		if (path == null) {
			throw new IllegalStateException("No path");
		}
		
		return client.makeDirectory(path);
	}

	@Override
	public String toString() {
		return "mkdir " + path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
