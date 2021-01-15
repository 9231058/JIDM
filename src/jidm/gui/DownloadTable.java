package jidm.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;

import jidm.DownloadController;
import jidm.download.DownloadTableModel;
import jidm.download.QueuedStaticDownload;

public class DownloadTable extends JPanel {
	private static final long serialVersionUID = -3236271995938499306L;
	private static DownloadTable downloadTable;
	private JScrollPane scrollPane;
	private DownloadTableModel downloadTableModel;
	private JTable table;
	private JPopupMenu popupMenu;
	private JMenuItem reDownloadMenuItem;
	private JMenuItem deleteDownloadMenuItem;
	private JMenuItem priorityDownloadMenuItem;
	private JMenuItem stopDownloadMenuItem;

	public static DownloadTable getInstance() {
		if (downloadTable == null) {
			downloadTable = new DownloadTable();
		}
		return downloadTable;
	}

	private DownloadTable() {
		downloadTableModel = new DownloadTableModel();
		setLayout(new BorderLayout());

		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable(downloadTableModel);
		table.setSelectionMode(0);
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		scrollPane.setViewportView(table);
		table.setFillsViewportHeight(true);
		table.setEnabled(true);
		table.addMouseListener(new TableMouseHandler());
		table.addKeyListener(new TableKeyHandler());

		reDownloadMenuItem = new JMenuItem("Redownload");
		reDownloadMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int rowIndex = table.getSelectedRow();
					DownloadFrame.getInstance().addInternalFrame(
							DownloadController.getInstance().add(
									downloadTableModel.getDownload(rowIndex)));
				} catch (ArrayIndexOutOfBoundsException exception) {
					return;
				}

			}
		});

		deleteDownloadMenuItem = new JMenuItem("Delete Download");
		deleteDownloadMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int rowIndex = table.getSelectedRow();
					DownloadController.getInstance().removeUnknownDownload(
							downloadTableModel.getDownload(rowIndex));
				} catch (ArrayIndexOutOfBoundsException exception) {

				}
			}
		});

		priorityDownloadMenuItem = new JMenuItem("Set Download Priority");
		priorityDownloadMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int priority = Integer.parseInt(JOptionPane
							.showInputDialog(DownloadTable.this,
									"Please enter priority", "Set Priority",
									JOptionPane.INFORMATION_MESSAGE));
					int rowIndex = table.getSelectedRow();
					((QueuedStaticDownload) downloadTableModel
							.getDownload(rowIndex)).setPriority(priority);
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {
					return;
				} catch (ClassCastException exception) {
					JOptionPane
							.showMessageDialog(
									DownloadTable.this,
									"Download you selected is not a queued download...",
									"Set Priority Error",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		stopDownloadMenuItem = new JMenuItem("Stop Download");
		stopDownloadMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int rowIndex = table.getSelectedRow();
				downloadTableModel.getDownload(rowIndex);
			}
		});

		popupMenu = new JPopupMenu();
		popupMenu.add(reDownloadMenuItem);
		popupMenu.add(deleteDownloadMenuItem);
		popupMenu.add(priorityDownloadMenuItem);
		popupMenu.add(stopDownloadMenuItem);

	}

	public DownloadTableModel getTableModel() {
		return downloadTableModel;
	}

	private class TableKeyHandler implements KeyListener {

		@Override
		public void keyPressed(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.VK_CONTEXT_MENU) {
				popupMenu.show(event.getComponent(), 0, 0);
			}
		}

		@Override
		public void keyReleased(KeyEvent event) {
		}

		@Override
		public void keyTyped(KeyEvent event) {
		}

	}

	private class TableMouseHandler implements MouseListener, ActionListener {

		private final int clickInterval = (Integer) Toolkit.getDefaultToolkit()
				.getDesktopProperty("awt.multiClickInterval");

		private MouseEvent lastEvent;
		private Timer timer;

		public TableMouseHandler() {
			timer = new Timer(clickInterval, this);
		}

		public void singleClick(MouseEvent event) {
		}

		public void doubleClick(MouseEvent event) {
			int row = table.rowAtPoint(event.getPoint());
			try {
				Desktop.getDesktop().open(
						new File(downloadTableModel.getDownload(row)
								.getFilePath()));
			} catch (IOException | NullPointerException exception) {
				JOptionPane.showMessageDialog(DownloadTable.this,
						"IO Error ... we are very sorry", "Open Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException exeption) {

			}
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			singleClick(lastEvent);
			timer.stop();
		}

		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.isPopupTrigger()) {
				popupMenu
						.show(event.getComponent(), event.getX(), event.getY());
			} else {
				if (event.getClickCount() > 2)
					return;
				lastEvent = event;
				if (timer.isRunning()) {
					timer.stop();
					doubleClick(lastEvent);
				} else {
					timer.restart();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent event) {
		}

		@Override
		public void mouseExited(MouseEvent event) {
		}

		@Override
		public void mousePressed(MouseEvent event) {
			if (event.isPopupTrigger()) {
				popupMenu
						.show(event.getComponent(), event.getX(), event.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			if (event.isPopupTrigger()) {
				popupMenu
						.show(event.getComponent(), event.getX(), event.getY());
			}
		}

	}
}
