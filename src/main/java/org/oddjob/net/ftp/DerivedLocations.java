package org.oddjob.net.ftp;

import java.io.File;

public class DerivedLocations {

	private final String remote;
	
	private final File file;
	
	public DerivedLocations(String remote, File file) {
		
		if (remote == null) {
			if (file == null) {
				this.remote = null;
				this.file = null;
			}
			else {
				this.remote = file.getName();
				this.file = file;
			}
		}
		else {
			String remoteName = new File(remote).getName();
			if (file == null) {
				this.remote = remote;
				this.file = new File(remoteName);
			}
			else {
				this.remote = remote;
				if (file.isDirectory()) {
					this.file = new File(file, remoteName);
				}
				else {
					this.file = file;
				}
			}
		}
	}
	
	public File getFile() {
		return file;
	}
	
	public String getRemote() {
		return remote;
	}
	
}
