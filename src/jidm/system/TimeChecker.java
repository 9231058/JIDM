package jidm.system;

import java.util.Date;

import jidm.DownloadController;

public class TimeChecker implements Runnable {

	private static TimeChecker timeChecker;

	public static TimeChecker getInstance() {
		if (timeChecker == null) {
			timeChecker = new TimeChecker();
		}
		return timeChecker;

	}

	private TimeChecker() {
	}

	@Override
	public void run() {
		while (true) {
			Date nowDate = new Date();
			Date startDownload = SystemDownloadManager.getInstance()
					.getStartDownload();
			Date endDownload = SystemDownloadManager.getInstance()
					.getEndDownload();
			if (startDownload != null && nowDate.compareTo(startDownload) >= 0
					&& !DownloadController.getInstance().isAIRunning()) {
				DownloadController.getInstance().startQueuedDownlaod();
			}
			if (endDownload != null && nowDate.compareTo(endDownload) >= 0) {
				DownloadController.getInstance().endQueuedDownload();
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
