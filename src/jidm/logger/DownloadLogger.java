package jidm.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DownloadLogger {
	static private FileHandler fileLog;
	static private SimpleFormatter formatterLog;
	static private DownloadLogger downloadLogger;

	private DownloadLogger() {
	}

	public static DownloadLogger getInstance() {
		if (downloadLogger == null) {
			Logger logger = Logger.getGlobal();
			logger.setLevel(Level.ALL);
			try {
				fileLog = new FileHandler("DownloadLogging.log");
				formatterLog = new SimpleFormatter();
				fileLog.setFormatter(formatterLog);
				logger.addHandler(fileLog);
				downloadLogger = new DownloadLogger();
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
		}
		return downloadLogger;
	}

	public Logger getLogger(Class<? extends Object> myClass) {
		return Logger.getLogger(myClass.getName());
	}
}
