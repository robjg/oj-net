package org.oddjob.net.ftp;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.State;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.TestCase;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.oddjob.FailedToStopException;
import org.oddjob.Helper;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.OurDirs;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.io.DeleteJob;
import org.oddjob.state.JobState;

public class FTPClientJobTest extends TestCase {

	private static final Logger logger = Logger.getLogger(FTPClientJobTest.class);
	
	private final FTPServerService server = 
		new FTPServerService();
	
	OurDirs dirs;
	
	File toSend;
	
	File gottenFile;
	
	int port;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		logger.info("-----------------  " + getName() + "  ------------------");
		
		// Note this isn't relative because ftp server can't be
		// given a base dir.
		File home = new File("work/home");
		if (home.exists()) {
			DeleteJob delete = new DeleteJob();
			delete.setFiles(new File[] { home });
			delete.setForce(true);
			delete.run();
		}
		home.mkdirs();
		
		dirs = new OurDirs();
		
		toSend = dirs.relative("test/files/test.txt");
		
		gottenFile = dirs.relative("work/result.txt");
				
		if (gottenFile.exists()) {
			DeleteJob delete = new DeleteJob();
			delete.setFiles(new File[] { gottenFile });
			delete.setForce(true);
			delete.run();
		}
				
		server.setUsersFile(
				dirs.relative(
						"test/tools/apache-ftpserver-1.0.2" +
						"/res/conf/users.properties"));
		
		String portText = System.getProperty("oddjob.net.test.ftp.port");
		if (portText != null) {
			logger.info("Attempting to set port to " + portText);
			try {
				this.port = Integer.parseInt(portText);
			}
			catch (NumberFormatException e) {
				logger.info("Port not a number. Using default port.");
			}
			server.setPort(port);
		}
		else {
			logger.info("Using default port.");			
		}
		
		server.start();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		server.stop();
	}
	
	public void testLotsOfCommands() {

		
		FTPClientJob test = new FTPClientJob();
		
		test.setHost("localhost");
		test.setPort(port);
		test.setUsername("admin");
		test.setPassword("admin");
		
		FTPPwd pwd = new FTPPwd();
		
		FTPMkDir mkdir = new FTPMkDir();
		mkdir.setPath("stuff");
		
		FTPCd cd = new FTPCd();
		cd.setPath("stuff");
		
		FTPList list1 = new FTPList();
		
		FTPAscii ascii = new FTPAscii();
		
		FTPPut put = new FTPPut();
		put.setFile(toSend);
		put.setRemote("stuff.txt");
		
		FTPList list2 = new FTPList();
		
		FTPRename rename = new FTPRename();
		rename.setFrom("stuff.txt");
		rename.setTo("things.txt");
		
		FTPGet get = new FTPGet();
		get.setFile(gottenFile);
		get.setRemote("things.txt");

		FTPDelete delete = new FTPDelete();
		delete.setPath("things.txt");
		
		FTPBinary binary = new FTPBinary();
		
		FTPCd cd2 = new FTPCd();
		
		FTPRmDir rmDir = new FTPRmDir();
		rmDir.setPath("stuff");
		
		test.setCommands(new FTPCommand[] {
				pwd, 
				mkdir, 
				cd, 
				list1, ascii, put, list2, rename, 
				get, 
				delete,
				binary, cd2, 
				rmDir
		});
		
		test.run();
	
		assertEquals(0, test.getResult());
		
		assertEquals("/", pwd.getPwd());
		
		assertEquals(0, list1.getFiles().length);
		
		assertEquals(1, list2.getFiles().length);
		assertEquals("stuff.txt", list2.getFiles()[0].getName());
		
		assertTrue(gottenFile.exists());
		assertEquals(toSend.length(), gottenFile.length());
	}
	
	public void testInOddjob() throws ArooaPropertyException, Exception {

		Properties properties = new Properties();
		properties.setProperty("ftp.port", new Integer(port).toString());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setArgs(new String[] { toSend.getPath(), gottenFile.getPath() });
		oddjob.setProperties(properties);
		oddjob.setConfiguration(new XMLConfiguration(
				"org/oddjob/net/ftp/FTPClientTest.xml", 
				getClass().getClassLoader()));
		oddjob.run();
	
		assertEquals(JobState.COMPLETE, oddjob.lastJobStateEvent().getJobState());
		
		OddjobLookup lookup = new OddjobLookup(oddjob);
		
		assertEquals("/", lookup.lookup("pwd.pwd"));
		
		assertEquals(0, lookup.lookup("list1.files", FTPFile[].class).length);
		
		FTPFile[] list2 = lookup.lookup("list2.files", FTPFile[].class);
		assertEquals(1, list2.length);
		assertEquals("stuff.txt", list2[0].getName());
		
		assertTrue(gottenFile.exists());
		assertEquals(toSend.length(), gottenFile.length());
	}
	
	public void testGettingAFileThatDoesntExist() {

		
		FTPClientJob test = new FTPClientJob();
		
		test.setHost("localhost");
		test.setPort(port);
		test.setUsername("admin");
		test.setPassword("admin");
		
		FTPGet get = new FTPGet();
		get.setFile(gottenFile);
		get.setRemote("doesnt-exist.txt");

		test.setCommands(new FTPCommand[] {
				get, 
		});
		
		test.run();
		
		assertEquals(1, test.getResult());
		
		// A zero byte file does exist.
		assertTrue(gottenFile.exists());
		assertEquals(0, gottenFile.length());
	}
	
	public void testStop() {
		
		final FTPClientJob test = new FTPClientJob();
		
		test.setHost("localhost");
		test.setPort(port);
		test.setUsername("admin");
		test.setPassword("admin");
		
		test.setCommands(new FTPCommand[] {
				new FTPCommand() {
					
					@Override
					public boolean executeWith(FTPClient client) throws IOException {
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
									test.stop();
								} catch (FailedToStopException e) {
									fail("Failed to stop.");
								}
							}
						}).start();
						
						while(true) {
							client.noop();
						}
					}
				}
		});
		
		try {
			test.run();
			fail("Exception expected.");
		}
		catch (Exception e) {
			// expected
		}
	}
	
	public void testStopLongRunningTransfer() throws FailedToStopException, InterruptedException, TimeoutException {
		
		final FTPClientJob test = new FTPClientJob();
		
		test.setHost("localhost");
		test.setPort(port);
		test.setUsername("admin");
		test.setPassword("admin");
		
		VerySlowInputStream input = new VerySlowInputStream();
		
		FTPPut put = new FTPPut();
		put.setInput(input);
		put.setRemote("WillNeverMakeIt.txt");
		
		test.setCommands(new FTPCommand[] {
				put
		});
		
		final AtomicReference<Throwable> exceptionRef = new AtomicReference<Throwable>();
		
		Thread t = new Thread(test);
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				exceptionRef.set(e);
			}
		});
		logger.info("Starting FTPJob thread.");
		t.start();
		
		
		logger.info("Ensure FTP put is reading from out InputStream.");
		input.start();
		
		logger.info("Stop the FTP put job.");
		test.stop();
		
		logger.info("Stop the FTP put job.");		
		t.join(Helper.TEST_TIMEOUT);
		if (t.getState() != State.TERMINATED) {
			throw new RuntimeException("FTP Thread didn't finish.");
		}
		
		assertNotNull(exceptionRef.get());
		Throwable e = exceptionRef.get().getCause();
		logger.info("FTPException is " + e.toString());		
		assertTrue(e instanceof IOException);
	}
}
