package org.oddjob.net.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * @oddjob.description FTP command to get the current remote present 
 * working directory.
 * 
 * @author rob
 *
 */
public class FTPPwd implements FTPCommand {

	/**
	 * @oddjob.property 
	 * @oddjob.description The present working directory.
	 * @oddjob.required R/O.
	 */
	private String pwd;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
		pwd = client.printWorkingDirectory();
		if (pwd == null) {
			return false;
		}
		else {
			return true;			
		}
	}

	public String getPwd() {
		return pwd;
	}
	
	@Override
	public String toString() {
		return "pwd";
	}

}
