package jidm.system;

import jidm.DownloadController;

public class SystemDownloadExit extends Thread {

	@Override
	public void run() {
		DownloadController.getInstance().saveEveryThing();
		SystemDownloadManager.shutdown();
	}

}
