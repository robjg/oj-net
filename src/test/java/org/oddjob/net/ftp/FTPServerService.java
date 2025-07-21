package org.oddjob.net.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;


public class FTPServerService {

	private static final Logger logger = LoggerFactory.getLogger(FTPServerService.class);

	private FtpServer server;

	private File usersFile;
	
	private int port;
	
	public void start() throws FtpException {
		
		PropertiesUserManagerFactory umf =
			new PropertiesUserManagerFactory();
		umf.setFile(Objects.requireNonNull(usersFile,
				"No Server Properties File."));
		
		FtpServerFactory serverFactory = new FtpServerFactory();
		serverFactory.setUserManager(umf.createUserManager());
		
		if (port > 0) {
			ListenerFactory factory = new ListenerFactory();
	        
			// set the port of the listener
			factory.setPort(port);

			// replace the default listener
			serverFactory.addListener("default", factory.createListener());
			logger.info("Starting FTP Server on Port {} with properties file {}",
					port, usersFile);
		}
		else {
			logger.info("Starting FTP Server on Default Port with properties file {}",
					usersFile);
		}
		
		
		this.server = serverFactory.createServer();
		
		// start the server
		server.start();
		
	}
	
	public void stop() {

		logger.info("Stopping FTP Server.");
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
