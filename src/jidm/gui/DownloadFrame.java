package jidm.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.Executors;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import jidm.DownloadController;
import jidm.gui.dialog.AboutDialog;
import jidm.gui.dialog.OptionDialog;
import jidm.system.TimeChecker;

public class DownloadFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JDesktopPane desktopPane;
	private JTabbedPane tabbedPane;
	private JMenuItem resetMenuItem;
	private JMenuItem settingMenuItem;
	private static DownloadFrame downloadFrame;

	public static DownloadFrame getInstance() {
		if (downloadFrame == null) {
			downloadFrame = new DownloadFrame();
		}
		return downloadFrame;
	}

	private DownloadFrame() {
		super("JIDM Java Download Manager");
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				int option = JOptionPane.showConfirmDialog(DownloadFrame.this,
						"Are you sure to quit ...", "Quit",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (option == JOptionPane.OK_OPTION) {
					DownloadFrame.this
							.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} else if (option == JOptionPane.CANCEL_OPTION) {
					DownloadFrame.this
							.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				} else {
					DownloadFrame.this
							.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 550, 400);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);

		settingMenuItem = new JMenuItem("Setting");
		settingMenuItem.setMnemonic('S');
		fileMenu.add(settingMenuItem);
		settingMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				OptionDialog optionDialog = new OptionDialog();
				optionDialog.setVisible(true);
			}
		});

		resetMenuItem = new JMenuItem("Reset");
		resetMenuItem.setMnemonic('R');
		fileMenu.add(resetMenuItem);
		resetMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				DownloadController.getInstance().resetDownloadsList();
			}
		});

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic('E');
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				InputEvent.CTRL_MASK));
		fileMenu.add(exitMenuItem);
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		menuBar.add(helpMenu);

		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.setMnemonic('A');
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				AboutDialog aboutDialog = new AboutDialog();
				aboutDialog.setVisible(true);
			}
		});

		contentPane = new DownloadPanel();
		desktopPane = new JDesktopPane();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		setContentPane(tabbedPane);
		tabbedPane.addTab("Control", contentPane);
		tabbedPane.addTab("Downloads", desktopPane);
		tabbedPane.addTab("Download Table", DownloadTable.getInstance());
		Executors.newCachedThreadPool().submit(TimeChecker.getInstance());
	}

	public void addInternalFrame(JInternalFrame internalFrame) {
		if (internalFrame == null) {
			return;
		}
		desktopPane.add(internalFrame);
	}
}
