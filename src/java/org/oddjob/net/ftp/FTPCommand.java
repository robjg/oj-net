package org.oddjob.net.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 * Abstract for an FTP command.
 * 
 * @author rob
 */
public interface FTPCommand {

	/**
	 * Execute the command.
	 * 
	 * @param client The client session.
	 * @return true if OK, false otherwise.
	 * 
	 * @throws IOException
	 */
	public boolean executeWith(FTPClient client)
	throws IOException ;
	
}
