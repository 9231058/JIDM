package jidm;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import jdlib.Download;
import jidm.download.DownloadObserver;
import jidm.download.DownloadStatus;
import jidm.download.QueuedStaticDownload;
import jidm.download.StaticDownload;
import jidm.gui.DownloadFrame;
import jidm.gui.DownloadInternalFrame;
import jidm.gui.DownloadTable;
import jidm.system.SystemDownloadManager;

public class DownloadController {

	private HashMap<DownloadObserver, StaticDownload> runningDownloads = new HashMap<>();
	private ArrayList<DownloadObserver> runningDownloadObservers = new ArrayList<>();
	private ArrayList<DownloadObserver> pausedDownloadObservers = new ArrayList<>();
	private ArrayList<StaticDownload> stopedDownloads = new ArrayList<>();
	private ArrayList<StaticDownload> finishedDownloads = new ArrayList<>();
	private ArrayList<QueuedStaticDownload> queuedDownloads = new ArrayList<>();
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private int AIRunningDownlaod = 0;
	private boolean isAIRunning = false;
	private static DownloadController downloadController;

	public static DownloadController getInstance() {
		if (downloadController == null) {
			downloadController = new DownloadController();
		}
		return downloadController;
	}

	@SuppressWarnings("unchecked")
	private DownloadController() {
		SystemDownloadManager sysManager = SystemDownloadManager.getInstance();
		if (sysManager.getList("finishedDownloads") != null) {
			try {
				finishedDownloads = (ArrayList<StaticDownload>) sysManager
						.getList("finishedDownloads");
				for (int i = 0; i < finishedDownloads.size(); i++) {
					DownloadTable.getInstance().getTableModel()
							.insertDownload(finishedDownloads.get(i));
				}
			} catch (ClassCastException exception) {
				finishedDownloads = new ArrayList<>();
			}
		}
		if (sysManager.getList("stopedDownloads") != null) {
			try {
				stopedDownloads = (ArrayList<StaticDownload>) sysManager
						.getList("stopedDownloads");
				for (int i = 0; i < stopedDownloads.size(); i++) {
					DownloadTable.getInstance().getTableModel()
							.insertDownload(stopedDownloads.get(i));
				}
			} catch (ClassCastException exception) {
				stopedDownloads = new ArrayList<>();
			}
		}
		if (sysManager.getList("queuedDownloads") != null) {
			try {
				queuedDownloads = (ArrayList<QueuedStaticDownload>) sysManager
						.getList("queuedDownloads");
				for (int i = 0; i < queuedDownloads.size(); i++) {
					DownloadTable.getInstance().getTableModel()
							.insertDownload(queuedDownloads.get(i));
				}
			} catch (ClassCastException exception) {
				queuedDownloads = new ArrayList<>();
			}
		}

	}

	synchronized public boolean isAIRunning() {
		return isAIRunning;
	}

	synchronized public JInternalFrame add(URL url) {
		Download download = new Download(url, new File(SystemDownloadManager
				.getInstance().getDefaultPath()).getPath());
		if (downloadExists(download)) {
			return null;
		}
		DownloadInternalFrame internalFrame = new DownloadInternalFrame(
				download.getFileName());
		internalFrame.getTextField().setText(url.toString());
		DownloadObserver downloadObserver = new DownloadObserver(download,
				internalFrame);
		StaticDownload staticDownload = new StaticDownload();
		download.addObserver(staticDownload);
		runningDownloads.put(downloadObserver, staticDownload);
		runningDownloadObservers.add(downloadObserver);
		internalFrame.getLabel().setText(
				String.valueOf(runningDownloadObservers.indexOf(download) + 1));
		executorService.submit(downloadObserver);
		staticDownload.setStartDate(downloadObserver.getStartDate());
		DownloadTable.getInstance().getTableModel()
				.insertDownload(staticDownload);
		return internalFrame;
	}

	synchronized public JInternalFrame add(StaticDownload staticDownload) {
		if (staticDownload instanceof QueuedStaticDownload) {
			queuedDownloads.remove(staticDownload);
		} else {
			finishedDownloads.remove(staticDownload);
			stopedDownloads.remove(staticDownload);
		}
		URL url = staticDownload.getUrl();
		Download download = new Download(url, new File(SystemDownloadManager
				.getInstance().getDefaultPath()).getPath());
		DownloadInternalFrame internalFrame = new DownloadInternalFrame(
				download.getFileName());
		internalFrame.getTextField().setText(url.toString());
		DownloadObserver downloadObserver = new DownloadObserver(download,
				internalFrame);
		download.addObserver(staticDownload);
		runningDownloads.put(downloadObserver, staticDownload);
		runningDownloadObservers.add(downloadObserver);
		internalFrame.getLabel().setText(
				String.valueOf(runningDownloadObservers.indexOf(download) + 1));
		executorService.submit(downloadObserver);
		staticDownload.setStartDate(downloadObserver.getStartDate());
		return internalFrame;
	}

	synchronized public DownloadObserver getRuningDownload(
			StaticDownload staticDownload) {
		if (runningDownloads.containsValue(staticDownload)) {
		}
		return null;
	}

	synchronized public void addQueuedDownload(URL url) {
		QueuedStaticDownload queuedStaticDownload = new QueuedStaticDownload(
				url);
		queuedDownloads.add(queuedStaticDownload);
		DownloadTable.getInstance().getTableModel()
				.insertDownload(queuedStaticDownload);
	}

	synchronized public void startQueuedDownlaod() {
		isAIRunning = true;
		AIRunningDownlaod = SystemDownloadManager.getInstance()
				.getRuntimeDownload();
		Collections.sort(queuedDownloads);
		for (int i = 0; i < AIRunningDownlaod; i++) {
			try {
				add(queuedDownloads.get(0));
				queuedDownloads.remove(0);
			} catch (IndexOutOfBoundsException exception) {
				isAIRunning = false;
				break;
			}
		}
	}

	synchronized public void endQueuedDownload() {
		isAIRunning = false;
		Iterator<Map.Entry<DownloadObserver, StaticDownload>> iterator = runningDownloads
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<DownloadObserver, StaticDownload> mapEntry = iterator
					.next();
			if (mapEntry.getValue() instanceof QueuedStaticDownload) {
				mapEntry.getKey().pause();
			}
		}
	}

	synchronized public void stop(DownloadObserver downloadObserver) {
		if (runningDownloadObservers.remove(downloadObserver) == true) {
			stopedDownloads.add(runningDownloads.get(downloadObserver));
			runningDownloads.remove(downloadObserver);
		}
		if (pausedDownloadObservers.remove(downloadObserver) == true) {
			stopedDownloads.add(runningDownloads.get(downloadObserver));
			runningDownloads.remove(downloadObserver);
		}
	}

	synchronized public void complete(DownloadObserver downloadObserver) {
		runningDownloadObservers.remove(downloadObserver);
		runningDownloads.get(downloadObserver).setEndDate(
				downloadObserver.getEndDate());
		if (runningDownloads.get(downloadObserver) instanceof QueuedStaticDownload) {
			runNewQueuedDownload();
		}
		finishedDownloads.add(runningDownloads.get(downloadObserver));
		runningDownloads.remove(downloadObserver);
	}

	synchronized public void pause(DownloadObserver downloadObserver) {
		int index = runningDownloadObservers.indexOf(downloadObserver);
		pausedDownloadObservers.add(runningDownloadObservers.remove(index));
	}

	synchronized public void resume(DownloadObserver downloadObserver) {
		int index = pausedDownloadObservers.indexOf(downloadObserver);
		runningDownloadObservers.add(pausedDownloadObservers.remove(index));
	}

	synchronized public void saveEveryThing() {
		for (int i = 0; i < runningDownloadObservers.size(); i++) {
			runningDownloadObservers.get(i).stop();
		}
		for (int i = 0; i < pausedDownloadObservers.size(); i++) {
			pausedDownloadObservers.get(i).stop();
		}
		SystemDownloadManager.getInstance().saveList("finishedDownloads",
				finishedDownloads);
		SystemDownloadManager.getInstance().saveList("stopedDownloads",
				stopedDownloads);
		SystemDownloadManager.getInstance().saveList("queuedDownloads",
				queuedDownloads);
	}

	synchronized public void cleanup() {
		for (int i = 0; i < stopedDownloads.size(); i++) {
			File file = new File(stopedDownloads.get(i).getFilePath());
			file.delete();
		}
	}

	synchronized public void resetDownloadsList() {
		for (int i = 0; i < finishedDownloads.size(); i++) {
			removeFinishedDownload(finishedDownloads.get(i));
		}
		for (int i = 0; i < stopedDownloads.size(); i++) {
			removeStopedDownload(stopedDownloads.get(i));
		}
		for (int i = 0; i < queuedDownloads.size(); i++) {
			removeQueuedDownload(queuedDownloads.get(i));
		}
	}

	synchronized public void removeUnknownDownload(StaticDownload staticDownload) {
		if (staticDownload.getStatus().equals(DownloadStatus.COMPLETE)) {
			removeFinishedDownload(staticDownload);
		}
		if (staticDownload.getStatus().equals(DownloadStatus.CANCEL)
				|| staticDownload.getStatus().equals(DownloadStatus.ERROR)) {
			removeStopedDownload(staticDownload);
		}
		if (staticDownload.getStatus().equals(DownloadStatus.QUEUE)) {
			removeQueuedDownload(staticDownload);
		}
	}

	synchronized public void removeFinishedDownload(
			StaticDownload staticDownload) {
		finishedDownloads.remove(staticDownload);
		DownloadTable.getInstance().getTableModel()
				.removeDownload(staticDownload);
	}

	synchronized public void removeStopedDownload(StaticDownload staticDownload) {
		stopedDownloads.remove(staticDownload);
		DownloadTable.getInstance().getTableModel()
				.removeDownload(staticDownload);
	}

	synchronized public void removeQueuedDownload(StaticDownload staticDownload) {
		queuedDownloads.remove(staticDownload);
		DownloadTable.getInstance().getTableModel()
				.removeDownload(staticDownload);
	}

	synchronized private void runNewQueuedDownload() {
		try {
			add(queuedDownloads.get(0));
		} catch (IndexOutOfBoundsException exception) {
			isAIRunning = false;
		}
	}

	synchronized private boolean downloadExists(Download download) {
		File file = new File(download.getFilePath());
		if (file.exists()) {
			int result = JOptionPane.showConfirmDialog(
					DownloadFrame.getInstance(),
					"Your download exists right now ...", "Downlod Existance",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				return true;
			} else {
				file.delete();
				return false;
			}
		}
		return false;
	}
}
