package org.oddjob.net.ftp;

import org.apache.commons.net.ftp.FTPFile;
import org.hamcrest.MatcherAssert;
import org.junit.*;
import org.junit.rules.TestName;
import org.oddjob.FailedToStopException;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.OurDirs;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.io.DeleteJob;
import org.oddjob.state.ParentState;
import org.oddjob.tools.OddjobTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.State;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.is;

public class FTPClientJobTest extends Assert {

    private static final Logger logger = LoggerFactory.getLogger(FTPClientJobTest.class);

    @Rule
    public TestName name = new TestName();

    private final FTPServerService server =
            new FTPServerService();

    File toSend;

    File gottenFile;

    int port;

    @Before
    public void setUp() throws Exception {


        logger.info("-----------------  {}  ------------------", name.getMethodName());

        // Note this isn't relative because ftp server can't be
        // given a base dir.
        File home = new File("work/home");
        if (home.exists()) {
            DeleteJob delete = new DeleteJob();
            delete.setFiles(new File[]{home});
            delete.setForce(true);
            delete.call();
        }

        MatcherAssert.assertThat(home.mkdirs(), is(true));

        toSend = OurDirs.basePath()
                .resolve("src/test/files/test.txt")
                .toFile();

        gottenFile = OurDirs.workPathDir(getClass().getSimpleName(), true)
                .resolve("result.txt").toFile();

        server.setUsersFile(OurDirs.basePath()
                .resolve("src/test/tools/apache-ftpserver-1.2.1/res/conf/users.properties")
                .toFile());

        String portText = System.getProperty("oddjob.net.test.ftp.port");
        if (portText != null) {
            logger.info("Attempting to set port to {}", portText);
            this.port = Integer.parseInt(portText);
        } else {
            try (ServerSocket serverSocket = new ServerSocket(0)) {
                this.port = serverSocket.getLocalPort();
            }
        }

        server.setPort(this.port);
        server.start();
    }

    @After
    public void tearDown() {

        server.stop();
    }

    @Test
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

        test.setCommands(new FTPCommand[]{
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

    @Test
    public void testInOddjob() throws Exception {

        Properties properties = new Properties();
        properties.setProperty("ftp.port", Integer.toString(port));

        Oddjob oddjob = new Oddjob();
        oddjob.setArgs(new String[]{toSend.getPath(), gottenFile.getPath()});
        oddjob.setProperties(properties);
        oddjob.setConfiguration(new XMLConfiguration(
                "org/oddjob/net/ftp/FTPClientTest.xml",
                getClass().getClassLoader()));
        oddjob.run();

        assertEquals(ParentState.COMPLETE, oddjob.lastStateEvent().getState());

        OddjobLookup lookup = new OddjobLookup(oddjob);

        assertEquals("/", lookup.lookup("pwd.pwd"));

        assertEquals(0, lookup.lookup("list1.files", FTPFile[].class).length);

        FTPFile[] list2 = lookup.lookup("list2.files", FTPFile[].class);
        assertEquals(1, list2.length);
        assertEquals("stuff.txt", list2[0].getName());

        assertTrue(gottenFile.exists());
        assertEquals(toSend.length(), gottenFile.length());
    }

    @Test
    public void testGettingAFileThatDoesntExist() {


        FTPClientJob test = new FTPClientJob();

        test.setHost("localhost");
        test.setPort(port);
        test.setUsername("admin");
        test.setPassword("admin");

        FTPGet get = new FTPGet();
        get.setFile(gottenFile);
        get.setRemote("doesnt-exist.txt");

        test.setCommands(new FTPCommand[]{
                get,
        });

        test.run();

        assertEquals(1, test.getResult());

        // A zero byte file does exist.
        assertTrue(gottenFile.exists());
        assertEquals(0, gottenFile.length());
    }

    @Test
    public void testStop() {

        final FTPClientJob test = new FTPClientJob();

        test.setHost("localhost");
        test.setPort(port);
        test.setUsername("admin");
        test.setPassword("admin");

        AtomicReference<Boolean> stopped = new AtomicReference<>();

        test.setCommands(new FTPCommand[] {
                client -> {

                    new Thread(() -> {
                        try {
                            test.stop();
                        } catch (FailedToStopException e) {
                            fail("Failed to stop.");
                        }
                    }).start();

                    while (true) {
                        try {
                            client.noop();
                        } catch (IOException e) {
                            logger.info("Expected exception: {}", e.getMessage());
                            stopped.set(true);
                            break;
                        }
                    }
                    return true;
                }
        });

        test.run();

        MatcherAssert.assertThat(stopped.get(), is(true));
    }

    @Test
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

        test.setCommands(new FTPCommand[]{
                put
        });

        final AtomicReference<Throwable> exceptionRef = new AtomicReference<>();

        Thread t = new Thread(test);
        t.setUncaughtExceptionHandler((t1, e) -> exceptionRef.set(e));
        logger.info("Starting FTPJob thread.");
        t.start();

        logger.info("Ensure FTP put is reading from out InputStream.");
        input.start();

        logger.info("Stop the FTP put job.");
        test.stop();

        logger.info("Wait for FTP thread.");
        t.join(OddjobTestHelper.TEST_TIMEOUT);
        if (t.getState() != State.TERMINATED) {
            throw new RuntimeException("FTP Thread didn't finish.");
        }

        assertNotNull(exceptionRef.get());
        Throwable e = exceptionRef.get().getCause();
        logger.info("FTPException is {}", e.toString());
        assertTrue(e instanceof IOException);
    }
}
