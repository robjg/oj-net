package org.oddjob.net.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.oddjob.FailedToStopException;
import org.oddjob.Stoppable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @oddjob.description FTP command to retrieve a remote file.
 * 
 * @author rob
 *
 */
public class FTPGet implements FTPCommand, Stoppable {
	
	/**
	 * @oddjob.property 
	 * @oddjob.description The name of the remote file.
	 * @oddjob.required Yes.
	 */
	private String remote;

	/**
	 * @oddjob.property
	 * @oddjob.description A local output to copy to.
	 * @oddjob.required Either this or a file is required.
	 */	
	private OutputStream output;
	
	/**
	 * @oddjob.property
	 * @oddjob.description A local file to copy to.
	 * @oddjob.required Either this or an output is required.
	 */	
	private File file;
	
	private volatile OutputStream os;
	
	@Override
	public boolean executeWith(FTPClient client) 
	throws IOException {

		DerivedLocations derived = new DerivedLocations(remote, file);
		if (derived.getRemote() == null) {
			throw new IllegalStateException(
					"Can not derive a remote file name.");
		}
		
		os = this.output;
		if (os == null) {
			os = new FileOutputStream(derived.getFile());
		}
		
		try {
			return client.retrieveFile(remote, os);
		}
		finally {
			os.close();
			os = null;
		}
	}
	
	@Override
	public void stop() throws FailedToStopException {
		OutputStream os = this.os;
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				throw new FailedToStopException(this, toString(), e);
			}
		}
	}

	@Override
	public String toString() {
		
		DerivedLocations derived = new DerivedLocations(remote, file);		

		return "get " + derived.getRemote() + " as " + 
			(output == null ? derived.getFile() : "Input" );
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String as) {
		this.remote = as;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public OutputStream getOutput() {
		return output;
	}

	public void setOutput(OutputStream output) {
		this.output = output;
	}	
}
