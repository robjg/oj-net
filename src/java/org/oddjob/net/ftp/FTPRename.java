package org.oddjob.net.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @oddjob.description FTP command to rename a file.
 * 
 * @author rob
 *
 */
public class FTPRename implements FTPCommand {
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The name of the file rename.
	 * @oddjob.required Yes.
	 */
	private String from;
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The new name of the file.
	 * @oddjob.required Yes.
	 */
	private String to;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		
		if (from == null) {
			throw new IllegalStateException("No from.");
		}
		
		if (to == null) {
			throw new IllegalStateException("No to.");
		}
		
		return client.rename(from, to);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String path) {
		this.from = path;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "rename " + from + " to " + to;
	}

}
