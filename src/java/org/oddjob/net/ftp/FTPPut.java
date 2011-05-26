package org.oddjob.net.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.oddjob.FailedToStopException;
import org.oddjob.Stoppable;

/**
 * @oddjob.description FTP command to transfer a file to the server.
 * 
 * @author rob
 *
 */
public class FTPPut implements FTPCommand, Stoppable {
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The remote name of the file.
	 * @oddjob.required Not if a file is used for input.
	 */
	private String remote;

	/**
	 * @oddjob.property
	 * @oddjob.description An input to copy remotely.
	 * @oddjob.required Either this or a file is required.
	 */
	private InputStream input;
	
	/**
	 * @oddjob.property
	 * @oddjob.description A file to copy remotely.
	 * @oddjob.required Either this or an input is required.
	 */
	private File file;
	
	private volatile InputStream is;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {
				
		DerivedLocations derived = new DerivedLocations(remote, file);
		if (derived.getRemote() == null) {
			throw new IllegalStateException(
					"No remote file name.");
		}
		
		is = this.input;
		if (is == null) {
			is = new FileInputStream(derived.getFile());
		}
		
		try {
			return client.storeFile(derived.getRemote(), is);
		}
		finally {
			is.close();
			is = null;
		}
	}

	@Override
	public void stop() throws FailedToStopException {
		InputStream is = this.is;
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				throw new FailedToStopException(this, toString(), e);
			}
		}
	}
	
	@Override
	public String toString() {
		
		DerivedLocations derived = new DerivedLocations(remote, file);
		
		return "put " + 
			(input == null ? derived.getFile() : "Input") +
			" as " + derived.getRemote();
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String as) {
		this.remote = as;
	}

	public InputStream getInput() {
		return input;
	}

	public void setInput(InputStream input) {
		this.input = input;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
