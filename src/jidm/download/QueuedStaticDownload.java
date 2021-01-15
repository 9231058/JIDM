package jidm.download;

import java.net.URL;
import java.util.Date;

public class QueuedStaticDownload extends StaticDownload implements
		Comparable<QueuedStaticDownload> {

	private int priority;
	private Date queuedDate;
	private static final long serialVersionUID = -8287147091482047590L;

	public QueuedStaticDownload(URL url) {
		super(url);
		queuedDate = new Date();
	}

	@Override
	public int compareTo(QueuedStaticDownload queuedStaticDownload) {
		if (queuedStaticDownload.getPriority() == this.priority) {
			return this.queuedDate.compareTo(queuedStaticDownload
					.getQueuedDate());
		}
		return this.priority - queuedStaticDownload.getPriority();
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getQueuedDate() {
		return queuedDate;
	}

}
