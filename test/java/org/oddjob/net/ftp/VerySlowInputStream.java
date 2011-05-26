package org.oddjob.net.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.oddjob.Helper;

public class VerySlowInputStream extends InputStream {
	private static final Logger logger = Logger.getLogger(VerySlowInputStream.class);
	
	private final Exchanger<Void> exchange = new Exchanger<Void>();
	
	private boolean closed;
	
	public void start() throws InterruptedException, TimeoutException {
		logger.info("Waiting for input stream to start reading.");
		exchange.exchange(null, Helper.TEST_TIMEOUT, TimeUnit.MILLISECONDS);
		logger.info("Read started.");
	}
	
	@Override
	public int read() throws IOException {
		logger.info("InputStream read starting.");
		try {
			try {
				exchange.exchange(null, Helper.TEST_TIMEOUT, TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				throw new RuntimeException(e);
			}

			logger.info("InputStream starting to block.");
			
			synchronized (this) {
				while (closed) {
					wait();
				}
			}

			logger.info("Woken.");
			
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		throw new IOException("InputStream closed.");
	}

	@Override
	public void close() throws IOException {
		logger.info("Closing stream.");
		
		synchronized (this) {
			closed = true;
			notifyAll();
		}
	}
	
}
