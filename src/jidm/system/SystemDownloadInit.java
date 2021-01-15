package jidm.system;

import jidm.gui.DownloadFrame;

public class SystemDownloadInit implements Runnable {

	private static SystemDownloadInit systemDownloadInit;

	public static SystemDownloadInit getInstance() {
		if (systemDownloadInit == null) {
			systemDownloadInit = new SystemDownloadInit();
		}
		return systemDownloadInit;
	}

	private SystemDownloadInit() {
	}

	@Override
	public void run() {
		Runtime.getRuntime().addShutdownHook(new SystemDownloadExit());
		DownloadFrame downloadFrame = DownloadFrame.getInstance();
		downloadFrame.setVisible(true);
	}
}
