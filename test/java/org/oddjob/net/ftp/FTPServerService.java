package org.oddjob.net.ftp;
import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;


public class FTPServerService {

	private FtpServer server;

	private File usersFile;
	
	private int port;
	
	public void start() throws FtpException {
		
		PropertiesUserManagerFactory umf =
			new PropertiesUserManagerFactory();
		umf.setFile(usersFile);
		
		FtpServerFactory serverFactory = new FtpServerFactory();
		serverFactory.setUserManager(
				umf.createUserManager());
		
		if (port > 0) {
			ListenerFactory factory = new ListenerFactory();
	        
			// set the port of the listener
			factory.setPort(2221);

			// replace the default listener
			serverFactory.addListener("default", factory.createListener());
		}
		
		
		this.server = serverFactory.createServer();
		
		// start the server
		server.start();
		
	}
	
	public void stop() {
		
		server.stop();
		
	}

	public File getUsersFile() {
		return usersFile;
	}

	public void setUsersFile(File usersFile) {
		this.usersFile = usersFile;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
