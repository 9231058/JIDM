package jidm.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import jidm.system.SystemDownloadManager;

public class OptionDialog extends JDialog {

	private static final long serialVersionUID = 4193739885407064337L;
	private final JPanel contentPanel = new JPanel();
	private String defaultLocation;
	private Date startDownload = SystemDownloadManager.getInstance()
			.getStartDownload();
	private Date endDownload = SystemDownloadManager.getInstance()
			.getEndDownload();
	private int runtimeDownload = SystemDownloadManager.getInstance()
			.getRuntimeDownload();
	private boolean[] downloadDays = new boolean[7];
	private NumberFormat numberFormat = NumberFormat.getIntegerInstance();
	private JFormattedTextField hourFormatter;
	private JFormattedTextField minuteFormatter;
	private JFormattedTextField runtimeDownloadFormatter;
	private JButton startDownloadDate;
	private JButton locationButton;
	private JButton runtimeDownloadButton;
	private JButton endDownloadDate;
	private JPanel dayPanel = new JPanel();
	private JCheckBox[] checkBoxs = new JCheckBox[7];
	private String dayName[] = { "Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday" };

	public OptionDialog() {
		setBounds(100, 100, 600, 300);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setPreferredSize(new Dimension(400, 200));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		defaultLocation = SystemDownloadManager.getInstance().getDefaultPath();
		locationButton = new JButton("Set Default Location");
		locationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileHidingEnabled(false);
				FileFilter fileFilter = new FileFilter() {

					@Override
					public String getDescription() {
						return null;
					}

					@Override
					public boolean accept(File file) {
						if (file.getName().startsWith(".")) {
							return false;
						} else if (file.isDirectory()) {
							return true;
						}
						return false;
					}
				};
				fileChooser.setCurrentDirectory(new File(defaultLocation));
				fileChooser.setFileFilter(fileFilter);
				int result = fileChooser.showOpenDialog(OptionDialog.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					defaultLocation = fileChooser.getSelectedFile().getPath();
				}
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, locationButton, 10,
				SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, locationButton, 10,
				SpringLayout.WEST, contentPanel);
		contentPanel.add(locationButton);

		numberFormat.setMaximumIntegerDigits(2);
		hourFormatter = new JFormattedTextField(numberFormat);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, hourFormatter, 10,
				SpringLayout.SOUTH, locationButton);
		sl_contentPanel.putConstraint(SpringLayout.WEST, hourFormatter, 0,
				SpringLayout.WEST, locationButton);
		hourFormatter.setColumns(5);
		contentPanel.add(hourFormatter);

		minuteFormatter = new JFormattedTextField(numberFormat);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, minuteFormatter, 0,
				SpringLayout.NORTH, hourFormatter);
		sl_contentPanel.putConstraint(SpringLayout.EAST, minuteFormatter, 0,
				SpringLayout.EAST, locationButton);
		minuteFormatter.setColumns(5);
		contentPanel.add(minuteFormatter);

		startDownloadDate = new JButton("Set Start Download Date");
		sl_contentPanel.putConstraint(SpringLayout.EAST, locationButton, 0,
				SpringLayout.EAST, startDownloadDate);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, startDownloadDate,
				10, SpringLayout.SOUTH, hourFormatter);
		sl_contentPanel.putConstraint(SpringLayout.WEST, startDownloadDate, 0,
				SpringLayout.WEST, hourFormatter);
		contentPanel.add(startDownloadDate);
		startDownloadDate.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent event) {
				startDownload = new Date();
				startDownload.setHours(Integer.parseInt(hourFormatter.getText()));
				startDownload.setMinutes(Integer.parseInt(minuteFormatter
						.getText()));
				hourFormatter.setText(String.valueOf(startDownload.getHours()));
				minuteFormatter.setText(String.valueOf(startDownload
						.getMinutes()));
			}
		});
		startDownloadDate.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseEntered(MouseEvent e) {
				if (startDownload != null) {
					hourFormatter.setText(String.valueOf(startDownload
							.getHours()));
					minuteFormatter.setText(String.valueOf(startDownload
							.getMinutes()));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hourFormatter.setText("");
				minuteFormatter.setText("");
			}
		});

		endDownloadDate = new JButton("Set End Download Date");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, endDownloadDate, 10,
				SpringLayout.SOUTH, startDownloadDate);
		sl_contentPanel.putConstraint(SpringLayout.WEST, endDownloadDate, 0,
				SpringLayout.WEST, startDownloadDate);
		sl_contentPanel.putConstraint(SpringLayout.EAST, endDownloadDate, 0,
				SpringLayout.EAST, startDownloadDate);
		contentPanel.add(endDownloadDate);
		endDownloadDate.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				endDownload = new Date();
				endDownload.setHours(Integer.parseInt(hourFormatter.getText()));
				endDownload.setMinutes(Integer.parseInt(minuteFormatter
						.getText()));
				hourFormatter.setText(String.valueOf(endDownload.getHours()));
				minuteFormatter.setText(String.valueOf(endDownload.getMinutes()));
			}
		});
		endDownloadDate.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseEntered(MouseEvent e) {
				if (endDownload != null) {
					hourFormatter.setText(String.valueOf(endDownload.getHours()));
					minuteFormatter.setText(String.valueOf(endDownload
							.getMinutes()));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hourFormatter.setText("");
				minuteFormatter.setText("");
			}
		});

		runtimeDownloadFormatter = new JFormattedTextField(numberFormat);
		sl_contentPanel.putConstraint(SpringLayout.NORTH,
				runtimeDownloadFormatter, 10, SpringLayout.SOUTH,
				endDownloadDate);
		sl_contentPanel
				.putConstraint(SpringLayout.WEST, runtimeDownloadFormatter, 0,
						SpringLayout.WEST, endDownloadDate);
		runtimeDownloadFormatter.setColumns(5);
		contentPanel.add(runtimeDownloadFormatter);
		runtimeDownloadFormatter.setText(String.valueOf(runtimeDownload));

		runtimeDownloadButton = new JButton("Set Runtime Download");
		sl_contentPanel.putConstraint(SpringLayout.NORTH,
				runtimeDownloadButton, 10, SpringLayout.SOUTH,
				runtimeDownloadFormatter);
		sl_contentPanel.putConstraint(SpringLayout.WEST, runtimeDownloadButton,
				0, SpringLayout.WEST, locationButton);
		sl_contentPanel.putConstraint(SpringLayout.EAST, runtimeDownloadButton,
				0, SpringLayout.EAST, locationButton);
		contentPanel.add(runtimeDownloadButton);
		runtimeDownloadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				runtimeDownload = Integer.parseInt(runtimeDownloadFormatter
						.getText());
				runtimeDownloadFormatter.setText(String
						.valueOf(runtimeDownload));
			}
		});

		dayPanel.setPreferredSize(new Dimension(200, 200));
		SpringLayout sl_dayPanel = new SpringLayout();
		getContentPane().add(dayPanel, BorderLayout.EAST);
		dayPanel.setLayout(sl_dayPanel);
		for (int i = 0; i < 7; i++) {
			checkBoxs[i] = new JCheckBox();
			checkBoxs[i].setText(dayName[i]);
			checkBoxs[i].setSelected(SystemDownloadManager.getInstance()
					.getDownloadDay(i));
			downloadDays[i] = checkBoxs[i].isSelected();
			checkBoxs[i].setName(String.valueOf(i));
			if (i == 0) {
				sl_dayPanel.putConstraint(SpringLayout.NORTH, checkBoxs[i], 0,
						SpringLayout.NORTH, dayPanel);
				sl_dayPanel.putConstraint(SpringLayout.WEST, checkBoxs[i], 0,
						SpringLayout.WEST, dayPanel);
				sl_dayPanel.putConstraint(SpringLayout.EAST, checkBoxs[i], 0,
						SpringLayout.EAST, dayPanel);
			} else if (0 < i && i < 6) {
				sl_dayPanel.putConstraint(SpringLayout.NORTH, checkBoxs[i], 0,
						SpringLayout.SOUTH, checkBoxs[i - 1]);
				sl_dayPanel.putConstraint(SpringLayout.WEST, checkBoxs[i], 0,
						SpringLayout.WEST, checkBoxs[i - 1]);
				sl_dayPanel.putConstraint(SpringLayout.EAST, checkBoxs[i], 0,
						SpringLayout.EAST, checkBoxs[i - 1]);
			} else {
				sl_dayPanel.putConstraint(SpringLayout.NORTH, checkBoxs[i], 0,
						SpringLayout.SOUTH, checkBoxs[i - 1]);
				sl_dayPanel.putConstraint(SpringLayout.WEST, checkBoxs[i], 0,
						SpringLayout.WEST, checkBoxs[i - 1]);
				sl_dayPanel.putConstraint(SpringLayout.EAST, checkBoxs[i], 0,
						SpringLayout.EAST, checkBoxs[i - 1]);
			}
			dayPanel.add(checkBoxs[i]);
		}
		for (int i = 0; i < 7; i++) {
			checkBoxs[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					System.err.println(((JCheckBox) event.getSource())
							.getName());
					if (((JCheckBox) event.getSource()).isSelected()) {
						downloadDays[Integer.parseInt(((JCheckBox) event
								.getSource()).getName())] = true;
					} else {
						downloadDays[Integer.parseInt(((JCheckBox) event
								.getSource()).getName())] = false;
					}
				}
			});
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						SystemDownloadManager.getInstance().setDefaultPath(
								defaultLocation);
						SystemDownloadManager.getInstance().setStartDownload(
								startDownload);
						SystemDownloadManager.getInstance().setRuntimeDownload(
								runtimeDownload);
						SystemDownloadManager.getInstance().setEndDownload(
								endDownload);
						SystemDownloadManager.getInstance().setDownloadDay(
								downloadDays);
						OptionDialog.this.dispose();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						OptionDialog.this.dispose();
					}
				});
			}
		}
	}
}
