package jidm.system;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SystemDownloadManager implements Serializable {

	private static final long serialVersionUID = -6907461299936529939L;
	private static SystemDownloadManager systemDownloadManager;
	private HashMap<String, List<? extends Object>> lists;
	private String defaultPath;
	private Date startDownload;
	private Date endDownload;
	private boolean downloadDays[] = new boolean[7];
	private int runtimeDownload;

	public static SystemDownloadManager getInstance() {
		if (systemDownloadManager == null) {
			try {
				ObjectInputStream inputStream = new ObjectInputStream(
						new FileInputStream(".jidminfo"));
				systemDownloadManager = (SystemDownloadManager) inputStream
						.readObject();
				inputStream.close();
			} catch (IOException | ClassNotFoundException e) {
				systemDownloadManager = new SystemDownloadManager();
				System.err
						.println("The sysconfig file is not found ... your setting is not loaded ...");
			} finally {
			}
		}
		return systemDownloadManager;
	}

	private SystemDownloadManager() {
		lists = new HashMap<>();
		defaultPath = System.getenv("HOME");
		if (defaultPath == null) {
			defaultPath = System.getenv("HOMEPATH");
		}
		if (defaultPath == null) {
			defaultPath = ".";
		}
		defaultPath = defaultPath + File.separatorChar + "Downloads";
		System.err.println(defaultPath);
	}

	public void saveList(String name, List<? extends Object> list) {
		lists.put(name, list);
	}

	public List<? extends Object> getList(String name) {
		return lists.get(name);
	}

	public String getDefaultPath() {
		return defaultPath;
	}

	public void setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
	}

	@SuppressWarnings("deprecation")
	public Date getStartDownload() {
		if (startDownload == null) {
			return startDownload;
		} else {
			Date nowDate = new Date();
			if (downloadDays[nowDate.getDay()]) {
				startDownload.setDate(nowDate.getDate());
				return startDownload;
			} else {
				return null;
			}
		}
	}

	public void setStartDownload(Date startDownload) {
		this.startDownload = startDownload;
	}

	@SuppressWarnings("deprecation")
	public Date getEndDownload() {
		if (endDownload == null) {
			return endDownload;
		} else {
			Date nowDate = new Date();
			if (downloadDays[nowDate.getDay()]) {
				endDownload.setDate(nowDate.getDate());
				return endDownload;
			} else {
				return null;
			}
		}
	}

	public boolean isConnected() {
		return false;
	}

	public void setEndDownload(Date endDownload) {
		this.endDownload = endDownload;
	}

	public String getClipboardText() {
		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard()
					.getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
		}
		return "";

	}

	public static void shutdown() {
		File file = new File(".jidminfo");
		try {
			file.createNewFile();
			ObjectOutputStream outputStream = new ObjectOutputStream(
					new FileOutputStream(file));
			outputStream.flush();
			outputStream.writeObject(systemDownloadManager);
			outputStream.flush();
			outputStream.close();
			System.err.println("Success ..");
		} catch (IOException e) {
			e.printStackTrace();
			System.err
					.println("The sysconfig file can not save or create .... your setting in not saved ...");
		}
	}

	public int getRuntimeDownload() {
		return runtimeDownload;
	}

	public void setRuntimeDownload(int runtimeDownload) {
		this.runtimeDownload = runtimeDownload;
	}

	public boolean getDownloadDay(int i) {
		return downloadDays[i];
	}

	public void setDownloadDay(boolean[] downloadDays) {
		this.downloadDays = downloadDays;
	}
}