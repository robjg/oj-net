package org.oddjob.net.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

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
