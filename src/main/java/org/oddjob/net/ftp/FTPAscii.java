package org.oddjob.net.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * @oddjob.description FTP command to change to ascii transfer mode.
 * 
 * @author rob
 *
 */
public class FTPAscii implements FTPCommand {
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		
		return client.setFileType(FTP.ASCII_FILE_TYPE);
	}

	@Override
	public String toString() {
		return "ascii";
	}

}
