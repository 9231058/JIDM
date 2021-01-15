package jidm.download;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import jdlib.Download;
import jidm.gui.DownloadTable;

public class StaticDownload implements Observer, Serializable {

	private static final long serialVersionUID = -4247081807085769267L;
	private String fileName;
	private String size;
	private DownloadStatus status;
	private String filePath;
	private String speed;
	private float process;
	private Date startDate;
	private Date endDate;
	private URL url;
	private long UUID;

	public StaticDownload() {
		UUID = hashCode();
	}

	protected StaticDownload(URL url) {
		UUID = hashCode();
		this.url = url;
		this.status = DownloadStatus.getEnum("queued");
	}

	@Override
	public void update(Observable observable, Object object) {
		if (observable instanceof Download) {
			Download download = (Download) observable;
			status = DownloadStatus.getEnum(download.getMessage());
			size = String.valueOf(download.getSize() / 1000) + " KB";
			speed = download.getSpeed();
			filePath = download.getFilePath();
			fileName = download.getFileName();
			process = download.getProgress();
			try {
				url = new URL(download.getURL());
			} catch (MalformedURLException e) {
				url = null;
			}
			DownloadTable.getInstance().getTableModel().update(this);
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFileName() {
		return fileName;
	}

	public String getSize() {
		return size;
	}

	public DownloadStatus getStatus() {
		return status;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getSpeed() {
		return speed;
	}

	public float getProcess() {
		return process;
	}

	public URL getUrl() {
		return url;
	}

	public long getUUID() {
		return UUID;
	}

}
