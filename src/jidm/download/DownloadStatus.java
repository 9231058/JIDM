package jidm.download;

public enum DownloadStatus {
	COMPLETE("Complete"), DOWNLOADING("Downloading"), PAUSE("Paused"), ERROR(
			"Error"), CANCEL("Canceled"), QUEUE("Queued");
	private String status;

	private DownloadStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static DownloadStatus getEnum(String status) {
		for (DownloadStatus downloadStatus : values()) {
			if (downloadStatus.getStatus().equalsIgnoreCase(status)) {
				return downloadStatus;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return status;
	}

}
