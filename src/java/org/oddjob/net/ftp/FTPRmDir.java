package org.oddjob.net.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @oddjob.description FTP command to remove a remote directory.
 * 
 * @author rob
 *
 */
public class FTPRmDir implements FTPCommand {
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The path of the remote directory to remove.
	 * @oddjob.required Yes.
	 */
	private String path;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		
		if (path == null) {
			throw new IllegalStateException("No path");
		}
		
		return client.removeDirectory(path);
	}

	@Override
	public String toString() {
		return "rmdir " + path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
