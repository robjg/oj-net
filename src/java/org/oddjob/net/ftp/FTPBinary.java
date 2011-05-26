package org.oddjob.net.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @oddjob.description FTP command to change to binary transfer mode.
 * 
 * @author rob
 *
 */
public class FTPBinary implements FTPCommand {
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		
		return client.setFileType(FTP.BINARY_FILE_TYPE);
	}

	@Override
	public String toString() {
		return "bin";
	}

}
