package org.oddjob.net.ftp;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.oddjob.FailedToStopException;
import org.oddjob.Stoppable;

/**
 * @oddjob.description Connect to an FTP server and run a set of FTP commands.
 * 
 * @oddjob.example
 * 
 * Doing lots of FTP things.
 * 
 * {@oddjob.xml.resource org/oddjob/net/ftp/FTPClientTest.xml}
 * 
 * @author rob
 *
 */
public class FTPClientJob implements Runnable, Stoppable, Serializable {
	private static final long serialVersionUID = 2009102100L;
	
	private static final Logger logger = LoggerFactory.getLogger(FTPClientJob.class);

	/**
	 * @oddjob.property 
	 * @oddjob.description The name of this Job.
	 * @oddjob.required No.
	 */
	private String name;
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The FTP server.
	 * @oddjob.required Yes.
	 */
	private String host;
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The Port.
	 * @oddjob.required No.
	 */
	private int port;
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The user name to connect to the FTP server with.
	 * @oddjob.required Yes.
	 */
	private String username;
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The password to connect to the FTP server with.
	 * @oddjob.required Yes, unless user is anonymous.
	 */
	private transient String password;
	
	/**
	 * @oddjob.property
	 * @oddjob.description A List of the FTP commands.
	 * @oddjob.required No.
	 */
	private transient FTPCommand[] commands;
	
	/**
	 * @oddjob.property
	 * @oddjob.description True if the connection is passive.
	 * @oddjob.required No, defaults to false.
	 */
	private transient boolean passive;
	
	private int result;
		
	private transient FTPClient client;
	
	@Override
	public void run() {
		
		if (host == null) {
			throw new IllegalStateException("No hostname.");
		}
		if (username == null) {
			throw new IllegalStateException("No username.");
		}
		
		result = 0;
		
		client = new FTPClient();
		
		try {
			if (port > 0) {
				logger.info("Connecting to " + host + ", port " + port + ".");
				client.connect(host, port);
			}
			else {
				logger.info("Connecting to " + host + ".");
				client.connect(host);
			}
   			logger.info(client.getReplyString());
		
            if (passive) {
                logger.info("entering passive mode");
                client.enterLocalPassiveMode();
                if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                    throw new RuntimeException("could not enter into passive "
                         + "mode: " + client.getReplyString());
                }
            }
            
			logger.info("Logging in as " + username + ".");
			
   			boolean login = client.login(username, password);
   		            
   			logger.info(client.getReplyString());
            
   			if (!login) {
   				result = 1;
   				return;
   			}
   			
			if (commands != null) {
				for(FTPCommand command : commands) {
					
					logger.info(command.toString());
					
					boolean ok = command.executeWith(client);
					
					logger.info(client.getReplyString());

					if (!ok) {
						result = 1;
						return;
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (client.isConnected()) {
				try {
					client.disconnect();
					logger.info("Disconnected from " + host + ".");
					client = null;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	public void stop() throws FailedToStopException {
		
		if (commands != null) {
			for(FTPCommand command : commands) {
				if (command instanceof Stoppable) {
					((Stoppable) command).stop();
				}
			}
		}
		FTPClient client = this.client;
		if (client != null) {
			try {
				client.disconnect();
			} catch (IOException e) {
				throw new FailedToStopException(this, e);
			}
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setCommands(FTPCommand[] commands) {
		this.commands = commands;
	}
	
	public int getResult() {
		return result;
	}
	
	@Override
	public String toString() {
		if (name == null) {
			return getClass().getSimpleName();
		}
		else {
			return name;
		}
	}

	public boolean isPassive() {
		return passive;
	}

	public void setPassive(boolean passive) {
		this.passive = passive;
	}
}
