package jidm.download;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

import jdlib.Download;
import jidm.DownloadController;
import jidm.gui.DownloadInternalFrame;
import jidm.logger.DownloadLogger;

public class DownloadObserver implements Runnable, Observer {

	private String fileName;
	private String size;
	private DownloadStatus status;
	private String filePath;
	private String speed;
	private Date startDate;
	private Date endDate;
	private Download download;
	private JProgressBar progressBar;
	private JTextPane textPane;
	private DownloadInternalFrame internalFrame;
	private long UUID;

	public DownloadObserver(Download download,
			DownloadInternalFrame internalFrame) {
		UUID = hashCode();
		this.download = download;
		this.progressBar = internalFrame.getProgressBar();
		this.textPane = internalFrame.getTextPane();
		this.internalFrame = internalFrame;
		this.startDate = new Date();
		internalFrame.getControlButton().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (e.getSource() instanceof JButton) {
							JButton button = (JButton) e.getSource();
							if (button.getText().equalsIgnoreCase("open")) {
								try {
									Desktop.getDesktop().open(
											new File(filePath));
								} catch (IOException exception) {
									JOptionPane
											.showMessageDialog(
													DownloadObserver.this.internalFrame,
													"IO Error ... we are very sorry",
													"Open Error",
													JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				});
		internalFrame.getControlButton().addActionListener(
				new ControlHandeler());
		internalFrame.getStopButton().addActionListener(new StopHandeler());
		internalFrame.getStopButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JButton) {
					JButton button = (JButton) e.getSource();
					if (button.getText().equalsIgnoreCase("close")) {
						DownloadObserver.this.internalFrame.dispose();
					}
				}
			}
		});
	}

	@Override
	public void run() {
		download.start();
		fileName = download.getFileName();
		size = String.valueOf(download.getSize() / 1000) + " KB";
		status = DownloadStatus.getEnum(download.getStatus());
		filePath = download.getFilePath();
		initializeTextPane();
		internalFrame.setVisible(true);
		download.addObserver(this);

	}

	@Override
	public void update(Observable observable, Object object) {
		if (observable instanceof Download) {
			Download download = (Download) observable;
			float progress = Math.abs(download.getProgress());
			DownloadLogger.getInstance().getLogger(this.getClass())
					.info(download.getMessage());
			DownloadLogger.getInstance().getLogger(this.getClass())
					.info(String.valueOf(progress));
			progressBar.setValue((int) Math.floor(progress));
			status = DownloadStatus.getEnum(download.getMessage());
			speed = download.getSpeed();
			size = String.valueOf(download.getSize() / 1000) + " KB";
			updateTextPane();
			if (download.getMessage().equalsIgnoreCase("Complete")) {
				complete();
			}
			if (download.getMessage().equalsIgnoreCase("Error")) {
				download.pause();
				pause();
			}
		}
	}

	private void updateTextPane() {
		textPane.setText(textPane.getText() + "\n" + speed + "\n" + size + "\n"
				+ status);
	}

	private void initializeTextPane() {
		textPane.setText(fileName + " with size " + size
				+ " dowloading start at " + startDate + " and saving in "
				+ filePath);
	}

	private void complete() {
		endDate = new Date();
		DownloadController.getInstance().complete(this);
		internalFrame.getStopButton().setText("Close");
		internalFrame.getControlButton().setText("Open");
	}

	synchronized public void stop() {
		if (internalFrame.getStopButton().getText().equalsIgnoreCase("stop")) {
			internalFrame.getStopButton().doClick();
		}
	}

	synchronized public void pause() {
		if (internalFrame.getControlButton().getText()
				.equalsIgnoreCase("pause")) {
			internalFrame.getControlButton().doClick();
		}
	}

	synchronized public void resume() {
		if (internalFrame.getControlButton().getText()
				.equalsIgnoreCase("resume")) {
			internalFrame.getControlButton().doClick();
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setVisble() {
		internalFrame.setVisible(true);
	}

	public long getUUID() {
		return UUID;
	}

	private class ControlHandeler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				JButton button = (JButton) e.getSource();
				if (button.getText().equalsIgnoreCase("resume")) {
					resume();
					button.setText("Pause");
				} else if (button.getText().equalsIgnoreCase("pause")) {
					System.err.println(button.getText());
					pause();
					button.setText("Resume");
				}
			}
		}

		private void pause() {
			download.pause();
			DownloadController.getInstance().pause(DownloadObserver.this);
		}

		public void resume() {
			download.resume();
			DownloadController.getInstance().resume(DownloadObserver.this);
		}

	}

	private class StopHandeler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				JButton button = (JButton) e.getSource();
				if (button.getText().equalsIgnoreCase("stop")) {
					stop();
				}
			}
		}

		public void stop() {
			download.cancel();
			endDate = new Date();
			DownloadController.getInstance().stop(DownloadObserver.this);
			internalFrame.getStopButton().setText("Close");
			internalFrame.getControlButton().setEnabled(false);
		}

	}

}
