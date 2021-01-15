package jidm.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import jdlib.Download;
import jidm.DownloadController;
import jidm.system.SystemDownloadManager;

public class DownloadPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -8647762007261185071L;
	private JButton getURLButton;
	private JTextField URLField;
	private JTextPane textPane;
	private Download download;
	private Future<? extends Object> future;
	private URL url;
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private DownloadController downloadController;
	private JButton downloadButton;
	private JButton downloadLaterButton;

	public DownloadPanel() {
		downloadController = DownloadController.getInstance();
		SpringLayout springLayout = new SpringLayout();
		this.setLayout(springLayout);

		getURLButton = new JButton("Get URL");
		springLayout.putConstraint(SpringLayout.NORTH, getURLButton, 10,
				SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, getURLButton, 9,
				SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, getURLButton, 543,
				SpringLayout.WEST, this);
		getURLButton.setPreferredSize(new Dimension(0, 25));
		add(getURLButton);

		URLField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, URLField, 10,
				SpringLayout.SOUTH, getURLButton);
		springLayout.putConstraint(SpringLayout.WEST, URLField, 0,
				SpringLayout.WEST, getURLButton);
		springLayout.putConstraint(SpringLayout.EAST, URLField, 0,
				SpringLayout.EAST, getURLButton);
		URLField.setEditable(false);
		URLField.setPreferredSize(new Dimension(0, 25));
		this.add(URLField);

		downloadButton = new JButton("Download");
		springLayout.putConstraint(SpringLayout.NORTH, downloadButton, 10,
				SpringLayout.SOUTH, URLField);
		springLayout.putConstraint(SpringLayout.WEST, downloadButton, 0,
				SpringLayout.WEST, URLField);
		springLayout.putConstraint(SpringLayout.EAST, downloadButton, 0,
				SpringLayout.EAST, URLField);
		downloadButton.setEnabled(false);
		downloadButton.setPreferredSize(new Dimension(0, 25));
		this.add(downloadButton);

		downloadLaterButton = new JButton("Download Later");
		springLayout.putConstraint(SpringLayout.NORTH, downloadLaterButton, 10,
				SpringLayout.SOUTH, downloadButton);
		springLayout.putConstraint(SpringLayout.WEST, downloadLaterButton, 0,
				SpringLayout.WEST, downloadButton);
		springLayout.putConstraint(SpringLayout.EAST, downloadLaterButton, 0,
				SpringLayout.EAST, downloadButton);
		downloadLaterButton.setPreferredSize(new Dimension(0, 25));
		downloadLaterButton.setEnabled(false);
		add(downloadLaterButton);

		textPane = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, textPane, 10,
				SpringLayout.SOUTH, downloadLaterButton);
		springLayout.putConstraint(SpringLayout.WEST, textPane, 0,
				SpringLayout.WEST, downloadLaterButton);
		springLayout.putConstraint(SpringLayout.EAST, textPane, 0,
				SpringLayout.EAST, downloadLaterButton);
		textPane.setEditable(false);
		textPane.setPreferredSize(new Dimension(0, 50));
		add(textPane);

		setDownloadSetting();

		getURLButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String URLString = JOptionPane.showInputDialog(
						DownloadPanel.this, "Please enter URL", "URL Getter",
						JOptionPane.INFORMATION_MESSAGE);
				if (URLString == null || URLString == "") {
					return;
				}
				try {
					url = new URL(URLString);
					URLField.setText(url.toString());
					downloadButton.setEnabled(true);
					downloadLaterButton.setEnabled(true);
					download = new Download(url, ".");
					download.addObserver(DownloadPanel.this);
					executor.submit(download);
				} catch (MalformedURLException e) {
					JOptionPane.showMessageDialog(DownloadPanel.this,
							"Your URL is incorrect ...", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		downloadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DownloadFrame.getInstance().addInternalFrame(
						(downloadController.add(url)));
				setDownloadSetting();
			}
		});

		downloadLaterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DownloadController.getInstance().addQueuedDownload(url);
				setDownloadSetting();
			}
		});

	}

	private void setDownloadSetting() {
		textPane.setText("");
		closeDownload();
		String URLString = SystemDownloadManager.getInstance()
				.getClipboardText();
		if (URLString != null) {
			try {
				url = new URL(URLString);
				URLField.setText(URLString);
				downloadButton.setEnabled(true);
				downloadLaterButton.setEnabled(true);
				download = new Download(url, ".");
				download.addObserver(this);
				executor.submit(download);
			} catch (MalformedURLException e) {
				URLField.setText("");
				downloadButton.setEnabled(false);
				downloadLaterButton.setEnabled(false);
			}
		} else {
			URLField.setText("");
			downloadButton.setEnabled(false);
			downloadLaterButton.setEnabled(false);
		}
	}

	private void closeDownload() {
		if (download != null) {
			File file = new File(download.getFilePath());
			file.delete();
		}
	}

	@Override
	public void update(Observable observable, Object object) {
		if (observable instanceof Download) {
			System.err.println("I am updating ..");
			Download download = (Download) observable;
			textPane.setText("File Name : " + download.getFileName()
					+ "\nSize : " + (download.getSize() / 1000) + " KB");
			closeDownload();
			future.cancel(true);
		}
	}
}
